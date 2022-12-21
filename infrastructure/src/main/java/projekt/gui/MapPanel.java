package projekt.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.jetbrains.annotations.Nullable;
import projekt.base.Location;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;
import projekt.gui.scene.SimulationScene;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static projekt.gui.TUColors.COLOR_0A;
import static projekt.gui.TUColors.COLOR_0D;
import static projekt.gui.Utils.getDifference;
import static projekt.gui.Utils.midPoint;

public class MapPanel extends Pane {

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
    private static int factor = 50;
    private static final int offset = 200;

    public MapPanel(SimulationScene simulationScene) {
        this.scene = simulationScene;
        initComponents();
        paintComponent();
    }

    private void initComponents() {


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
                paintMap();
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
            var scale = event.getDeltaX() > 0 ? SCALE_OUT : SCALE_IN;
            factor *= scale;
            updateLocation();
            paintMap();
        });

        setMinSize(500, 500);

        // mouse pressed
        this.setOnMouseClicked(e -> updateVehicleSelection());
        paintMap();
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
        return scene.vehicleManager.getAllVehicles()
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
            return transformation;
//            throw new IllegalStateException("transformation is not invertible");
        }
    }

    /**
     * Paints the given vehicle.
     *
     * @param vehicle the vehicle to paint
     */
    private void paintVehicle(Vehicle vehicle) {
        if (vehicle == null)
            return;
        var img = CAR_SELECTED;
        if (scene.selectedVehicle != null && scene.selectedVehicle != vehicle) {
            img = CAR;
        }
        paintImage(midPoint(vehicle), img);
        System.out.println("Painting car: "+vehicle.getId());
    }

    private void paintImage(Point2D midPoint, javafx.scene.image.Image img) {
        var x = midPoint.getX() * factor + offset - IMAGE_SIZE/2;
        var y = midPoint.getY() * factor + offset - IMAGE_SIZE/2;

        var imageView = new ImageView();
        imageView.setImage(img);
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
        imageView.scaleXProperty().set(IMAGE_SIZE);
        imageView.scaleYProperty().set(IMAGE_SIZE);
        getChildren().add(imageView);
    }

    /**
     * Paints the given node.
     *
     * @param node the node to paint
     */
    private void drawNode(Region.Node node) {
        var n = node.getLocation();
        var circle = new Ellipse(n.getX() * factor + offset - NODE_DIAMETER / 2f, n.getY() * factor + offset - NODE_DIAMETER / 2f,
            NODE_DIAMETER, NODE_DIAMETER);
        circle.setFill(convert(NODE_COLOR));
        circle.setStrokeWidth(0.1);
        circle.setStroke(convert(EDGE_COLOR));
        getChildren().add(circle);

        var text = new Text(n.getX() * factor + offset + .5, n.getY() * factor + offset - .5, node.getName());
        text.setStroke(convert(COLOR_0A));
        getChildren().add(text);
    }

    private static Color convert(java.awt.Color color) {
        return Color.rgb(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()/255.0);
    }

    private void drawEdge(Region.Edge edge) {
        var a = edge.getNodeA().getLocation();
        var b = edge.getNodeB().getLocation();
        var line = new Line(a.getX() * factor + offset, a.getY() * factor + offset, b.getX() * factor + offset, b.getY() * factor + offset);

        line.setStroke(convert(EDGE_COLOR));
        line.setStrokeWidth(1); // TODO: verify width
        getChildren().add(line);

        var mid = midPoint(edge);
        var text = new Text(mid.getX() * factor + offset, mid.getY() * factor + offset, edge.getName());
        text.setStroke(convert(COLOR_0A));
        getChildren().add(text);
    }

    /**
     * Draws a Grid to help With Positioning
     *
     * @param drawMinors should all lines be drawn?
     */
    private void drawGrid(boolean drawMinors) {
        var color = convert(COLOR_0D);

        var step = 0.8;
        var xDim = 500;
        var yDim = 500;
        // Vertical Lines
        for (int i = 0, x = 0; x <= xDim; i++, x += (int) (step * factor)) {
            Float strokeWidth = getStrokeWidth(drawMinors, i);
            if (strokeWidth == null) continue;
            var line = new Line(x, 0, x, yDim);
            line.setStrokeWidth(strokeWidth);
            line.setStroke(color);
            getChildren().add(line);
        }

        // Horizontal Lines
        for (int i = 0, y = 0; y <= yDim; i++, y += (int) (step * factor)) {
            Float strokeWidth = getStrokeWidth(drawMinors, i);
            if (strokeWidth == null) continue;

            var line = new Line(0, y, xDim, y);
            line.setStrokeWidth(strokeWidth);
            line.setStroke(color);
            getChildren().add(line);
        }

        var border = new Rectangle(0, 0, (xDim + OUTER_TICKS_WIDTH), (yDim + OUTER_TICKS_WIDTH));
        border.setStrokeWidth(OUTER_TICKS_WIDTH);
        //getChildren().add(border);
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
        var bgf = new BackgroundFill(Color.valueOf("0c0e14"), new CornerRadii(0), new javafx.geometry.Insets(0));
        setBackground(new Background(bgf));
        getChildren().clear();
        drawGrid(true);
        scene.region.getEdges().forEach(this::drawEdge);
        scene.region.getNodes().forEach(this::drawNode);
        scene.vehicleManager.getAllVehicles().forEach(this::paintVehicle);
    }

    public void resetCenterLocation() {
        // TODO:
    }

    public void resetScale() {
        // TODO:
    }
}
