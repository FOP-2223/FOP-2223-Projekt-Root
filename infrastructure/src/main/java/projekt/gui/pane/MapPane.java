package projekt.gui.pane;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import org.jetbrains.annotations.Nullable;
import projekt.base.Location;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;
import projekt.gui.TUColors;
import projekt.gui.Utils;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static projekt.gui.TUColors.*;
import static projekt.gui.Utils.getDifference;
import static projekt.gui.Utils.midPoint;

public class MapPane extends Pane {

    public static final float OUTER_TICKS_WIDTH = .5f;
    public static final float FIVE_TICKS_WIDTH = .125f;
    public static final float ONE_TICKS_WIDTH = 1f / 32f;
    public static final float TEN_TICKS_WIDTH = .25f;

    private static final java.awt.Color NODE_COLOR = COLOR_0D;
    private static final java.awt.Color EDGE_COLOR = TUColors.COLOR_0C;
    private static final double NODE_DIAMETER = 15;
    private static final double IMAGE_SIZE = 0.1;
    private static final java.awt.Color CAR_COLOR = TUColors.COLOR_6C;
    private static final Image CAR_SELECTED = Utils.loadImage("car.png", CAR_COLOR);
    private static final double SCALE_IN = 1.1;
    private static final double SCALE_OUT = 1 / SCALE_IN;

    private final AtomicReference<Point2D> lastPoint = new AtomicReference<>();
    private final AffineTransform transformation = new AffineTransform();

    private final Text positionText = new Text();

    private final Map<Region.Node, LabeledNode> nodes = new HashMap<>();
    private final Map<Region.Edge, LabeledEdge> edges = new HashMap<>();
    private final Map<Vehicle, ImageView> vehicles = new HashMap<>();
    private final List<Shape> grid = new ArrayList<>();

    private Region.Node selectedNode;
    private Consumer<Region.Node> nodeSelectionHandler;
    private Consumer<Region.Node> nodeRemoveSelectionHandler;

    private Region.Edge selectedEdge;
    private Consumer<Region.Edge> edgeSelectionHandler;
    private Consumer<Region.Edge> edgeRemoveSelectionHandler;

    private Vehicle selectedVehicle;
    private Consumer<Vehicle> vehicleSelectionHandler;
    private Consumer<Vehicle> vehicleRemoveSelectionHandler;


    public MapPane() {
        this(List.of(), List.of(), List.of());
    }

    public MapPane(Collection<Region.Node> nodes, Collection<Region.Edge> edges, Collection<Vehicle> vehicles) {
        initListeners();

        //TODO make configurable
        transformation.translate(350, 350);
        transformation.scale(20, 20);

        for (Region.Edge edge : edges) {
            addEdge(edge);
        }

        for (Region.Node node : nodes) {
            addNode(node);
        }

        for (Vehicle vehicle : vehicles) {
            addVehicle(vehicle);
        }

        drawGrid();
        drawPositionText();
        positionText.setFill(Color.WHITE);
    }

    private void initListeners() {

        setOnMouseDragged(actionEvent -> {
                Point2D point = new Point2D.Double(actionEvent.getX(), actionEvent.getY());
                Point2D diff = getDifference(point, lastPoint.get());

                transformation.translate(diff.getX() / transformation.getScaleX(), diff.getY() / transformation.getScaleY());

                redrawMap();
                redrawGrid();
                updatePositionText(point);

                lastPoint.set(point);
            }
        );

        setOnScroll(event -> {
            double scale = event.getDeltaY() > 0 ? SCALE_IN : SCALE_OUT;
            transformation.scale(scale, scale);

            redrawMap();
            redrawGrid();
        });


        setOnMouseMoved(actionEvent -> {
            Point2D point = new Point2D.Double(actionEvent.getX(), actionEvent.getY());
            lastPoint.set(point);
            updatePositionText(point);
        });

        widthProperty().addListener((obs, oldValue, newValue) -> {
            redrawGrid();
            redrawMap();
            drawPositionText();
        });
        heightProperty().addListener((obs, oldValue, newValue) -> {
            redrawGrid();
            redrawMap();
            drawPositionText();
        });
    }

    public void addEdge(Region.Edge edge) {
        edges.put(edge, drawEdge(edge));
    }

    public void addAllEdges(Collection<Region.Edge> edges) {
        for (Region.Edge edge : edges) {
            addEdge(edge);
        }
    }

    public void removeEdge(Region.Edge edge) {
        LabeledEdge labeledEdge = edges.remove(edge);

        if (labeledEdge != null) {
            getChildren().removeAll(labeledEdge.line(), labeledEdge.text());
        }
    }

    public Region.Edge getSelectedEdge() {
        return selectedEdge;
    }

    public void onEdgeSelection(Consumer<Region.Edge> edgeSelectionHandler) {
        this.edgeSelectionHandler = edgeSelectionHandler;
    }

    public void onEdgeRemoveSelection(Consumer<Region.Edge> edgeRemoveSelectionHandler) {
        this.edgeRemoveSelectionHandler = edgeRemoveSelectionHandler;
    }

    public void addNode(Region.Node node) {
        nodes.put(node, drawNode(node));
    }

    public void addAllNodes(Collection<Region.Node> nodes) {
        for (Region.Node node : nodes) {
            addNode(node);
        }
    }

    public void removeNode(Region.Node node) {
        LabeledNode labeledNode = nodes.remove(node);


        if (labeledNode != null) {
            getChildren().removeAll(labeledNode.ellipse(), labeledNode.text());
        }
    }

    public Region.Node getSelectedNode() {
        return selectedNode;
    }

    public void onNodeSelection(Consumer<Region.Node> nodeSelectionHandler) {
        this.nodeSelectionHandler = nodeSelectionHandler;
    }

    public void onNodeRemoveSelection(Consumer<Region.Node> nodeRemoveSelectionHandler) {
        this.nodeRemoveSelectionHandler = nodeRemoveSelectionHandler;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.put(vehicle, drawVehicle(vehicle));
    }

    public void addAllVehicles(Collection<Vehicle> vehicles) {
        for (Vehicle vehicle : vehicles) {
            addVehicle(vehicle);
        }
    }

    public void removeVehicle(Vehicle vehicle) {
        ImageView imageView = vehicles.remove(vehicle);

        if (imageView != null) {
            getChildren().remove(imageView);
        }
    }

    public Vehicle getSelectedVehicle() {
        return selectedVehicle;
    }

    public void onVehicleSelection(Consumer<Vehicle> vehicleSelectionHandler) {
        this.vehicleSelectionHandler = vehicleSelectionHandler;
    }

    public void onVehicleRemoveSelection(Consumer<Vehicle> vehicleRemoveSelectionHandler) {
        this.vehicleRemoveSelectionHandler = vehicleRemoveSelectionHandler;
    }

    public void clear() {
        for (Region.Node node : new HashSet<>(nodes.keySet())) {
            removeNode(node);
        }

        for (Region.Edge edge : new HashSet<>(edges.keySet())) {
            removeEdge(edge);
        }

        for (Vehicle vehicle : new HashSet<>(vehicles.keySet())) {
            removeVehicle(vehicle);
        }
    }

    public void redrawMap() {
        redrawNodes();
        redrawEdges();
        redrawVehicles();
    }

    public void redrawNodes() {
        for (Map.Entry<Region.Node, LabeledNode> entry : nodes.entrySet()) {
            Region.Node node = entry.getKey();
            Ellipse ellipse = entry.getValue().ellipse();
            Text text = entry.getValue().text();

            getChildren().removeAll(ellipse, text);
            nodes.put(node, drawNode(node));
        }
    }

    public void redrawEdges() {
        for (Map.Entry<Region.Edge, LabeledEdge> entry : edges.entrySet()) {
            Region.Edge edge = entry.getKey();
            Line line = entry.getValue().line();
            Text text = entry.getValue().text();

            getChildren().removeAll(line, text);
            edges.put(edge, drawEdge(edge));
        }
    }

    public void redrawVehicles() {
        for (Map.Entry<Vehicle, ImageView> entry : vehicles.entrySet()) {
            Vehicle vehicle = entry.getKey();
            ImageView imageView = entry.getValue();

            getChildren().remove(imageView);
            vehicles.put(vehicle, drawVehicle(vehicle));
        }
    }

    public void redrawVehicle(Vehicle vehicle) {
        Point2D midPoint = midPoint(vehicle);
        Point2D transformedPoint = transformation.transform(midPoint, null);

        ImageView imageView = vehicles.get(vehicle);
        imageView.setX(transformedPoint.getX() - imageView.getImage().getWidth() / 2);
        imageView.setY(transformedPoint.getY() - imageView.getImage().getHeight() / 2);
    }

    /**
     * Paints the given node.
     *
     * @param node the node to paint
     */
    private LabeledNode drawNode(Region.Node node) {
        Point2D transformedPoint = transformLocation(node.getLocation());

        Ellipse ellipse = new Ellipse(transformedPoint.getX(), transformedPoint.getY(), NODE_DIAMETER, NODE_DIAMETER);
        ellipse.setFill(convert(NODE_COLOR));
        ellipse.setStrokeWidth(1);
        ellipse.setStroke(node.equals(selectedNode) ? convert(COLOR_9B) : convert(COLOR_0A));
        setMouseTransparent(false);

        Text text = new Text(transformedPoint.getX(), transformedPoint.getY(), node.getName());
        text.setStroke(convert(COLOR_0A));

        if (checkBounds(transformedPoint)) {
            getChildren().add(ellipse);
            getChildren().add(text);
        }

        ellipse.setOnMouseClicked(e -> handleNodeClick(ellipse, node));
        text.setOnMouseClicked(e -> handleNodeClick(ellipse, node));

        return new LabeledNode(ellipse, text);
    }

    private void handleNodeClick(Ellipse ellipse, Region.Node node) {
        if (selectedNode != null) {
            nodes.get(selectedNode).ellipse().setStroke(convert(EDGE_COLOR));
        }

        if (node.equals(selectedNode)) {
            if (nodeRemoveSelectionHandler != null) {
                nodeRemoveSelectionHandler.accept(selectedNode);
            }

            if (vehicleRemoveSelectionHandler != null && selectedVehicle != null) {
                vehicleRemoveSelectionHandler.accept(selectedVehicle);
            }

            selectedNode = null;
            selectedVehicle = null;
        } else {
            ellipse.setStroke(convert(COLOR_9B));
            selectedNode = node;
            selectedVehicle = vehicles.keySet().stream()
                .filter(vehicle -> vehicle.getStartingNode().getComponent().equals(selectedNode))
                .findFirst().orElse(null);

            if (nodeSelectionHandler != null) {
                nodeSelectionHandler.accept(selectedNode);
            }

            if (vehicleSelectionHandler != null && selectedVehicle != null) {
                vehicleSelectionHandler.accept(selectedVehicle);
            }
        }
    }

    private LabeledEdge drawEdge(Region.Edge edge) {
        Location a = edge.getNodeA().getLocation();
        Location b = edge.getNodeB().getLocation();

        Point2D transformedA = transformLocation(a);
        Point2D transformedB = transformLocation(b);

        Line line = new Line(transformedA.getX(), transformedA.getY(), transformedB.getX(), transformedB.getY());

        line.setStroke(edge.equals(selectedEdge) ? convert(COLOR_9B) : convert(COLOR_0A));
        line.setStrokeWidth(1);

        Point2D mid = getTransform().transform(midPoint(edge), null);
        Text text = new Text(mid.getX(), mid.getY(), edge.getName());
        text.setStroke(convert(COLOR_0A));

        if (fitLine(line)) {
            getChildren().addAll(line, text);
        }

        line.setOnMouseClicked(e -> handleEdgeClick(line, edge));
        text.setOnMouseClicked(e -> handleEdgeClick(line, edge));


        return new LabeledEdge(line, text);
    }

    private void handleEdgeClick(Line line, Region.Edge edge) {
        if (selectedEdge != null) {
            edges.get(selectedEdge).line().setStroke(convert(EDGE_COLOR));
        }

        if (edge.equals(selectedEdge)) {
            if (edgeRemoveSelectionHandler != null) {
                edgeRemoveSelectionHandler.accept(selectedEdge);
            }
            selectedEdge = null;
        } else {
            line.setStroke(convert(COLOR_9B));
            selectedEdge = edge;
            if (edgeSelectionHandler != null) {
                edgeSelectionHandler.accept(selectedEdge);
            }
        }
    }

    private boolean fitLine(Line line) {
        Point2D A = new Point2D.Double(line.getStartX(), line.getEndX());
        Point2D B = new Point2D.Double(line.getStartY(), line.getEndY());


        if (checkBounds(A) && checkBounds(B)) {
            return true;
        } else if (!checkBounds(A) || !checkBounds(B)) {
            //TODO Don't hide whole line
            return false;
        }

        return false;
    }

    private Point2D fitPoint(Point2D point) {
        return new Point2D.Double(Math.max(0, Math.min(getWidth(), point.getX())), Math.max(0, Math.min(getHeight(), point.getY())));
    }

    /**
     * Paints the given vehicle.
     *
     * @param vehicle the vehicle to paint
     */
    private ImageView drawVehicle(Vehicle vehicle) {

        Point2D midPoint = midPoint(vehicle);
        Point2D transformedPoint = transformation.transform(midPoint, null);

        var imageView = new ImageView();
        imageView.setImage(CAR_SELECTED);
        imageView.scaleXProperty().set(IMAGE_SIZE);
        imageView.scaleYProperty().set(IMAGE_SIZE);
        imageView.setX(transformedPoint.getX() - imageView.getImage().getWidth() / 2);
        imageView.setY(transformedPoint.getY() - imageView.getImage().getHeight() / 2);
        getChildren().add(imageView);

        return imageView;
    }

    private boolean checkBounds(Point2D point) {
        return point.getX() >= 0 && point.getX() < getWidth() && point.getY() >= 0 && point.getY() <= getHeight();
    }

    /**
     * Returns the last point hovered by the mouse or {@code null} if no point was hovered by the mouse.
     *
     * @return the point
     * @see MapPane#getCurrentLocation()
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
     * @see MapPane#getCurrentPoint()
     */
    public Location getCurrentLocation() {
        var currentPoint = getCurrentPoint();
        if (currentPoint == null)
            return null;
        return new Location((int) Math.round(currentPoint.getX()), (int) Math.round(currentPoint.getY()));
    }

    /**
     * Returns the affine transformation used to map model locations to view locations.
     *
     * @return the affine transformation
     * @see MapPane#getReverseTransform()
     */
    private AffineTransform getTransform() {
        return transformation;
    }

    /**
     * Returns the reverse affine transformation used to map view locations to model locations.
     *
     * @return the reverse affine transformation
     * @see MapPane#getReverseTransform()
     */
    private AffineTransform getReverseTransform() {
        try {
            return getTransform().createInverse();
        } catch (NoninvertibleTransformException e) {
            throw new IllegalStateException("transformation is not invertible");
        }
    }

    public void redrawGrid() {
        getChildren().removeAll(grid);
        grid.clear();
        drawGrid();
    }

    private static Color convert(java.awt.Color color) {
        return Color.rgb(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 255.0);
    }

    /**
     * Draws a Grid to help With Positioning
     */
    private void drawGrid() {
        Color color = convert(COLOR_0D);

        int stepX = (int) (transformation.getScaleX() / 2);
        int stepY = (int) (transformation.getScaleY() / 2);

        int offsetX = (int) transformation.getTranslateX();
        int offsetY = (int) transformation.getTranslateY();

        // Vertical Lines
        for (int i = 0, x = offsetX % (stepX * 5); x <= getWidth(); i++, x += stepX) {
            Float strokeWidth = getStrokeWidth(i, offsetX % (stepX * 10) > stepX * 5);
            if (strokeWidth == null) continue;
            Line line = new Line(x, 0, x, getHeight());
            line.setStrokeWidth(strokeWidth);
            line.setStroke(color);
            getChildren().add(line);
            grid.add(line);
        }

        // Horizontal Lines
        for (int i = 0, y = offsetY % (stepY * 5); y <= getHeight(); i++, y += stepY) {
            Float strokeWidth = getStrokeWidth(i, offsetY % (stepY * 10) > stepY * 5);
            if (strokeWidth == null) continue;

            var line = new Line(0, y, getWidth(), y);
            line.setStrokeWidth(strokeWidth);
            line.setStroke(color);
            getChildren().add(line);
            grid.add(line);
        }

        Rectangle border = new Rectangle(0, 0, (700 + OUTER_TICKS_WIDTH), (700 + OUTER_TICKS_WIDTH));
        border.setFill(null);
        border.setStrokeWidth(OUTER_TICKS_WIDTH);
        getChildren().add(border);
        grid.add(border);
    }

    @Nullable
    private static Float getStrokeWidth(int i, boolean inverted) {
        float strokeWidth;
        if (i % 10 == 0) {
            strokeWidth = inverted ? TEN_TICKS_WIDTH : FIVE_TICKS_WIDTH;
        } else if (i % 5 == 0) {
            strokeWidth = inverted ? FIVE_TICKS_WIDTH : TEN_TICKS_WIDTH;
        } else {
            return null;
        }
        return strokeWidth;
    }

    private void drawPositionText() {
        positionText.setX(getWidth() - positionText.getLayoutBounds().getWidth());
        positionText.setY(getHeight());
        positionText.setText("(-, -)");
        if (!getChildren().contains(positionText)) {
            getChildren().add(positionText);
        }
    }

    private void updatePositionText(Point2D point) {
        point = getReverseTransform().transform(point, null);
        positionText.setText("(%d, %d)".formatted((int) point.getX(), (int) point.getY()));
        positionText.setX(getWidth() - positionText.getLayoutBounds().getWidth());
        positionText.setY(getHeight());
    }

    private Point2D transformLocation(Location location) {
        AffineTransform t = new AffineTransform();
        t.translate(0, 5);
        return getTransform().transform(locationToPoint2D(location), null);
    }

    private Point2D locationToPoint2D(Location location) {
        return new Point2D.Double(location.getX(), location.getY());
    }

    private record LabeledEdge(Line line, Text text) {
    }

    private record LabeledNode(Ellipse ellipse, Text text) {
    }
}
