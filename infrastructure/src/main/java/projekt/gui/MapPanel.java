package projekt.gui;

import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.jetbrains.annotations.Nullable;
import projekt.base.Location;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

import java.awt.*;
import java.awt.geom.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static projekt.gui.TUColors.COLOR_0A;
import static projekt.gui.TUColors.COLOR_0D;
import static projekt.gui.Utils.getDifference;
import static projekt.gui.Utils.midPoint;

public class MapPanel extends TitledPane {

    public static final float OUTER_TICKS_WIDTH = .5f;
    public static final float FIVE_TICKS_WIDTH = .125f;
    public static final float ONE_TICKS_WIDTH = 1f / 32f;
    public static final float TEN_TICKS_WIDTH = .25f;
    private static final java.awt.Color NODE_COLOR = TUColors.COLOR_0D;
    //private static final Color NODE_COLOR = Color.rgb(nc.getRed(), nc.getGreen(), nc.getBlue(), nc.getAlpha()/255.0);
    private static final java.awt.Color EDGE_COLOR = TUColors.COLOR_0C;
    //private static final Color EDGE_COLOR = Color.rgb(ec.getRed(), ec.getGreen(), ec.getBlue(), ec.getAlpha()/255.0);

    private static final double NODE_DIAMETER = .95;
    private static final double IMAGE_SIZE = .5;
    private static final java.awt.Color CAR_COLOR = TUColors.COLOR_6C;
    //private static final Color CAR_COLOR = Color.rgb(cc.getRed(), cc.getGreen(), cc.getBlue(), cc.getAlpha()/255.0);
    private static final Image CAR = Utils.loadImage2("car.png", new java.awt.Color(CAR_COLOR.getRed(), CAR_COLOR.getGreen(), CAR_COLOR.getBlue(), 255 / 4));
    private static final Image CAR_SELECTED = Utils.loadImage2("car.png", CAR_COLOR);
    private static final double SCALE_IN = 1.1;
    private static final double SCALE_OUT = 1 / SCALE_IN;
    final AtomicReference<Point> lastPoint = new AtomicReference<>();
    private final SimulationScene scene;
    private final AffineTransform transformation = new AffineTransform();
    private boolean alreadyCentered = false;

    public MapPanel(SimulationScene simulationScene) {
        this.scene = simulationScene;
        initComponents();
    }

    private void initComponents() {
        //final Label map = new Label("Map");
        setText("Map");
        setCollapsible(false);
        var bgf = new BackgroundFill(Color.BLACK, new CornerRadii(0), new javafx.geometry.Insets(0));
        setBackground(new Background(bgf));

        //setBorder(new TitledBorder("Map"));

        this.setOnMouseDragged(actionEvent -> {
            try {
                var lastTransformed = transformation.inverseTransform(lastPoint.get(), null);

                var point = new Point((int) actionEvent.getX(), (int) actionEvent.getY());
                var currentTransformed = transformation.inverseTransform(point, null);
                var difference = getDifference(currentTransformed, lastTransformed);
                transformation.translate(difference.getX(), difference.getY());
                //repaint();
                lastPoint.set(point);
                updateLocation();
            } catch (NoninvertibleTransformException e) {
                throw new IllegalStateException();
            }
        }
        );

        this.setOnMouseMoved(actionEvent -> {
            var point = new Point((int) actionEvent.getX(), (int) actionEvent.getY());
            lastPoint.set(point);
            updateLocation();
        });

        this.setOnScroll(event -> {
            try {

                var before = transformation.inverseTransform(lastPoint.get(), null);
                var scale = event.getDeltaX() > 0 ? SCALE_OUT : SCALE_IN;
                transformation.scale(scale, scale);
                var after = transformation.inverseTransform(lastPoint.get(), null);
                var difference = getDifference(after, before);
                transformation.translate(difference.getX(), difference.getY());
                //repaint();
                updateLocation();
            } catch (NoninvertibleTransformException e) {
                throw new IllegalStateException("Cannot inverse location after scroll");
            }
        });

        // mouse pressed
        this.setOnMouseClicked(e -> updateVehicleSelection());
    }

    /**
     * Performs scale and translation of the view so that the given map fills this view.
     */
    private void center() {
        if (alreadyCentered)
            return;
        var width = getWidth();
        var height = getHeight();
        var reverse = getReverseTransform();
        // scale view
        var min = scene.region.getNodes().stream().map(Utils::midPoint).collect(Utils.Collectors.POINT_MIN);
        var max = scene.region.getNodes().stream().map(Utils::midPoint).collect(Utils.Collectors.POINT_MAX);
        reverse.transform(min, min);
        reverse.transform(max, max);
        var currentWidth = max.getY() - min.getY();
        var currentHeight = max.getY() - min.getY();
        var scale = Math.min(getWidth() / currentWidth, getHeight() / currentHeight);
        scale *= 0.75;
        getTransform().scale(scale, scale);
        // translate
        reverse = getReverseTransform();
        var center = new Point2D.Double(width / 2d, height / 2d);
        reverse.transform(center, center);
        var nodeCenter = scene.region.getNodes().stream().map(Utils::midPoint).collect(Utils.Collectors.center());
        transformation.translate(center.getX() - nodeCenter.getX(), center.getY() - nodeCenter.getY());
        alreadyCentered = true;
    }

    /**
     * Returns the last point hovered by the mouse or {@code null} if no point was hovered by the mouse.
     *
     * @return the point
     * @see MapPanel#getCurrentLocation()
     */
    private Point2D getCurrentPoint() {
        if (lastPoint.get() == null)
            return null;
        var reverse = getReverseTransform();
        return reverse.transform(lastPoint.get(), null);
    }

    /**
     * Returns the last location hovered by the mouse or {@code null} if no location was hovered by the mouse.
     *
     * @return the location
     * @see MapPanel#getCurrentPoint()
     */
    public Location getCurrentLocation() {
        var currentPoint = getCurrentPoint();
        if (currentPoint == null)
            return null;
        return new Location((int) Math.round(currentPoint.getX()), (int) Math.round(currentPoint.getY()));
    }

    /**
     * Returns a list of vehicles positioned at the given position.
     *
     * @param position the position to look for
     * @return the list of vehicles
     */
    private List<Vehicle> getVehicles(Point2D position) {
        return scene.vehicleManager.getVehicles()
            .stream()
            .filter(v -> Utils.midPoint(v).distance(position) < 1).toList();
    }

    /**
     * Updates the location information in the main frame.
     */
    private void updateLocation() {
        var location = getCurrentLocation();
        scene.controlsPanel.getMousePositionLabel().setText(
            String.format("(x: %d, y: %d)", location.getX(), location.getY()));
    }

    /**
     * Updates the vehicle selection.
     */
    public void updateVehicleSelection() {
        var vehicles = getVehicles(getCurrentPoint());
        if (!vehicles.isEmpty()) {
            var firstVehicle = vehicles.get(0);
            scene.selectedVehicle = (scene.selectedVehicle != firstVehicle ? firstVehicle : null);
        }
    }

    /**
     * Returns the affine transformation used to map model locations to view locations.
     *
     * @return the affine transformation
     * @see MapPanel#getReverseTransform()
     */
    private AffineTransform getTransform() {
        return transformation;
    }

    /**
     * Returns the reverse affine transformation used to map view locations to model locations.
     *
     * @return the reverse affine transformation
     * @see MapPanel#getReverseTransform()
     */
    private AffineTransform getReverseTransform() {
        try {
            return getTransform().createInverse();
        } catch (NoninvertibleTransformException e) {
            throw new IllegalStateException("transformation is not invertible");
        }
    }

    /**
     * Paints the given vehicle.
     *
     * @param vehicle the vehicle to paint
     */
    private void paintVehicle(Vehicle vehicle) {
        var img = CAR_SELECTED;
        if (scene.selectedVehicle != null && scene.selectedVehicle != vehicle) {
            img = CAR;
        }
        paintImage(midPoint(vehicle), img);
    }

    private void paintImage(Point2D midPoint, javafx.scene.image.Image img) {
        var x = midPoint.getX();
        var y = midPoint.getY();
        /*var old = g.getTransform();
        var transformation = new AffineTransform(old);
        transformation.translate(x - IMAGE_SIZE / 2, y - IMAGE_SIZE / 2);
        transformation.scale(IMAGE_SIZE, IMAGE_SIZE);*/

        var imageView = new ImageView();
        imageView.setImage(img);
        imageView.setTranslateX(IMAGE_SIZE);
        imageView.setTranslateY(IMAGE_SIZE);
        imageView.scaleXProperty();
        imageView.scaleYProperty();
        getChildren().add(imageView);
    }

    /**
     * Paints the given node.
     *
     * @param node the node to paint
     */
    private void drawNode(Region.Node node) {
        var n = node.getLocation();
        var circle = new Ellipse(n.getX() - NODE_DIAMETER / 2f, n.getY() - NODE_DIAMETER / 2f,
            NODE_DIAMETER, NODE_DIAMETER);
        circle.setFill(convert(NODE_COLOR));
        circle.setStrokeWidth(0.1);
        circle.setStroke(convert(EDGE_COLOR));
        getChildren().add(circle);

        var text = new Text(n.getX() + .5, n.getY() - .5, node.getName());
        text.setStroke(convert(COLOR_0A));
        getChildren().add(text);
    }

    private static Color convert(java.awt.Color color) {
        return Color.rgb(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()/255.0);
    }

    private void drawEdge(Region.Edge edge) {
        var a = edge.getNodeA().getLocation();
        var b = edge.getNodeB().getLocation();
        var line = new Line(a.getX(), a.getY(), b.getX(), b.getY());

        line.setStroke(convert(EDGE_COLOR));
        line.setStrokeWidth(0.1); // TODO: verify width
        getChildren().add(line);

        var mid = midPoint(edge);
        var text = new Text(mid.getX(), mid.getY(), edge.getName());
        text.setStroke(convert(COLOR_0A));
        getChildren().add(text);
    }

    /**
     * Draws a Grid to help With Positioning
     *
     * @param width total width of grid
     * @param height total height of grid
     * @param drawMinors should all lines be drawn?
     */
    private void drawGrid(int width, int height, boolean drawMinors) {
        var color = convert(COLOR_0D);

        // Vertical Lines
        for (int i = 0, x = -width / 2; x < width / 2; i++, x += 1) {
            Float strokeWidth = getStrokeWidth(drawMinors, i);
            if (strokeWidth == null) continue;
            var line = new Line(x, -height / 2f, x, height / 2f);
            line.setStrokeWidth(strokeWidth);
            getChildren().add(line);
        }

        // Horizontal Lines
        for (int i = 0, y = -height / 2; y < height / 2; i++, y += 1) {
            Float strokeWidth = getStrokeWidth(drawMinors, i);
            if (strokeWidth == null) continue;

            var line = new Line(-width / 2f, y, width / 2f, y);
            line.setStrokeWidth(strokeWidth);
            getChildren().add(line);
        }

        var border = new Rectangle(
            (int) (-width / 2 - OUTER_TICKS_WIDTH / 2),
            (int) (-height / 2 - OUTER_TICKS_WIDTH / 2),
            (int) (width + OUTER_TICKS_WIDTH),
            (int) (height + OUTER_TICKS_WIDTH));
        border.setStrokeWidth(OUTER_TICKS_WIDTH);
        getChildren().add(border);
    }

    @Nullable
    private static Float getStrokeWidth(boolean drawMinors, int i) {
        float strokeWidth;
        if (i % 10 == 0) {
            strokeWidth = TEN_TICKS_WIDTH;
        } else if (i % 5 == 0) {
            strokeWidth = FIVE_TICKS_WIDTH;
        } else {
            strokeWidth = ONE_TICKS_WIDTH;
            if (!drawMinors) {
                return null;
            }
        }
        return strokeWidth;
    }

    protected void paintComponent() {
        //super.paintComponent(g);
        if (lastPoint.get() == null)
            center();
        paintMap();
    }

    /**
     * Paints the Map using the given g2d. This method assumes that (0,0) paints
     * centered
     */
    private void paintMap() {
        // Background
        drawGrid(50, 50, true);
        scene.region.getEdges().forEach(this::drawEdge);
        scene.region.getNodes().forEach(this::drawNode);
        scene.vehicleManager.getVehicles().forEach(this::paintVehicle);
    }

    public void resetCenterLocation() {
        // TODO:
    }

    public void resetScale() {
        // TODO:
    }
}
