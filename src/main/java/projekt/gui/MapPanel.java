package projekt.gui;

import projekt.base.Location;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static projekt.gui.Utils.getDifference;
import static projekt.gui.Utils.toPoint;

public class MapPanel extends JPanel {

    private static final Stroke STROKE = new BasicStroke(.1f);
    private static final Color NODE_COLOR = TUColors.COLOR_0D;
    private static final Color EDGE_COLOR = TUColors.COLOR_0C;

    private static final double NODE_DIAMETER = .95;
    private static final double IMAGE_SIZE = .5;
    private static final Color CAR_COLOR = TUColors.COLOR_6C;
    private static final Image CAR = Utils.loadImage("car.png", new Color(CAR_COLOR.getRed(), CAR_COLOR.getGreen(), CAR_COLOR.getBlue(), 255 / 4));
    private static final Image CAR_SELECTED = Utils.loadImage("car.png", CAR_COLOR);
    private static final double SCALE_IN = 1.1;
    private static final double SCALE_OUT = 1 / SCALE_IN;
    final AtomicReference<Point> lastPoint = new AtomicReference<>();
    private final MainFrame mainFrame;
    private final AffineTransform transformation = new AffineTransform();
    private boolean alreadyCentered = false;

    public MapPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setBackground(Color.BLACK);
        setBorder(new TitledBorder("Map"));
        this.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent event) {
                try {
                    var lastTransformed = transformation.inverseTransform(lastPoint.get(), null);
                    var currentTransformed = transformation.inverseTransform(event.getPoint(), null);
                    var difference = getDifference(currentTransformed, lastTransformed);
                    transformation.translate(difference.getX(), difference.getY());
                    repaint();
                    lastPoint.set(event.getPoint());
                    updateLocation();
                } catch (NoninvertibleTransformException e) {
                    throw new IllegalStateException();
                }
            }

            @Override
            public void mouseMoved(MouseEvent event) {
                lastPoint.set(event.getPoint());
                updateLocation();
            }
        });
        this.addMouseWheelListener(event -> {
            try {
                var before = transformation.inverseTransform(lastPoint.get(), null);
                var scale = event.getWheelRotation() > 0 ? SCALE_OUT : SCALE_IN;
                transformation.scale(scale, scale);
                var after = transformation.inverseTransform(lastPoint.get(), null);
                var difference = getDifference(after, before);
                transformation.translate(difference.getX(), difference.getY());
                repaint();
                updateLocation();
            } catch (NoninvertibleTransformException e) {
                throw new IllegalStateException("");
            }
        });
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                updateVehicleSelection();
            }
        });
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
        var min = mainFrame.region.getNodes().stream().map(Utils::toPoint).collect(Utils.Collectors.POINT_MIN);
        var max = mainFrame.region.getNodes().stream().map(Utils::toPoint).collect(Utils.Collectors.POINT_MAX);
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
        var nodeCenter = mainFrame.region.getNodes().stream().map(Utils::toPoint).collect(Utils.Collectors.center());
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
        return mainFrame.vehicleManager.getVehicles()
            .stream()
            .filter(v -> toPoint(v).distance(position) < 1).toList();
    }

    /**
     * Updates the location information in the main frame.
     */
    private void updateLocation() {
        var location = getCurrentLocation();
        mainFrame.getControlsPanel().getMousePositionLabel().setText(
            String.format("(x: %d, y: %d)", location.getX(), location.getY()));
    }

    /**
     * Updates the vehicle selection.
     */
    public void updateVehicleSelection() {
        var vehicles = getVehicles(getCurrentPoint());
        if (!vehicles.isEmpty()) {
            var firstVehicle = vehicles.get(0);
            mainFrame.setSelectedVehicle(mainFrame.getSelectedVehicle() != firstVehicle ? firstVehicle : null);
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
     * Paints the given image at the given location with a size of {@link #IMAGE_SIZE}.
     *
     * @param g     a graphics object to paint the image on
     * @param x     the x-coordinate
     * @param y     the y-coordinate
     * @param image the image to paint
     */
    private void paintImage(Graphics2D g, double x, double y, Image image) {
        var old = g.getTransform();
        var transformation = new AffineTransform(old);
        transformation.translate(x - IMAGE_SIZE / 2, y - IMAGE_SIZE / 2);
        transformation.scale(IMAGE_SIZE, IMAGE_SIZE);
        g.setTransform(transformation);
        g.drawImage(image, 0, 0, 1, 1, null);
        g.setTransform(old);
    }

    /**
     * Paints the given image at the given location with a size of {@link #IMAGE_SIZE}.
     *
     * @param g        a graphics object to paint the image on
     * @param location the location
     * @param image    the image to paint
     */
    private void paintImage(Graphics2D g, Point2D location, Image image) {
        paintImage(g, location.getX(), location.getY(), image);
    }

    /**
     * Paints the given vehicle.
     *
     * @param g       g a graphics object to paint the image on
     * @param vehicle the vehicle to paint
     */
    private void paintVehicle(Graphics2D g, Vehicle vehicle) {
        var image = CAR_SELECTED;
        if (mainFrame.getSelectedVehicle() != null && mainFrame.getSelectedVehicle() != vehicle)
            image = CAR;
        paintImage(g, toPoint(vehicle), image);
    }

    /**
     * Paints the given node.
     *
     * @param g    a graphics object to paint the image on
     * @param node the node to paint
     */
    private void drawNode(Graphics2D g, Region.Node node) {
        var oldColor = g.getColor();
        var oldStroke = g.getStroke();
        g.setStroke(STROKE);
        g.setColor(NODE_COLOR);
        var location = node.getLocation();
        var circle = new Ellipse2D.Double(location.getX() - NODE_DIAMETER / 2f, location.getY() - NODE_DIAMETER / 2f,
            NODE_DIAMETER,
            NODE_DIAMETER);
        var textPoint = Utils.toPoint(node);
        textPoint.setLocation(textPoint.getX() + .5, textPoint.getY() - .5);
        text(g, textPoint, TUColors.COLOR_0A, node.getName());
        g.fill(circle);
        g.setColor(EDGE_COLOR);
        g.draw(circle);
        g.setStroke(oldStroke);
        g.setColor(oldColor);
    }

    /**
     * Paints the given edge.
     *
     * @param g    the graphics object to paint the image on
     * @param edge the edge to paint
     */
    private void paintEdge(Graphics2D g, Region.Edge edge) {
        var oldColor = g.getColor();
        var oldStroke = g.getStroke();
        g.setColor(EDGE_COLOR);
        g.setStroke(STROKE);
        var l = Utils.toPoint(edge);
        var l1 = edge.getNodeA().getLocation();
        var l2 = edge.getNodeB().getLocation();
        Line2D.Double line = new Line2D.Double(
            l1.getX(),
            l1.getY(),
            l2.getX(),
            l2.getY());
        g.draw(line);
        text(g, l, TUColors.COLOR_0A, String.format("%s", edge.getName()));
        g.setStroke(oldStroke);
        g.setColor(oldColor);
    }

    /**
     * Paints the given text at the given position using the given position.
     *
     * @param g        the graphics object to paint the image on
     * @param position the position to start
     * @param color    the color of the text
     * @param text     the text
     */
    private void text(Graphics2D g, Point2D position, Color color, String text) {
        var oldTransformation = g.getTransform();
        var oldColor = g.getColor();
        g.setColor(color);
        position = oldTransformation.transform(position, null);
        g.setTransform(new AffineTransform());
        g.setFont(g.getFont().deriveFont(Font.BOLD));
        g.drawString(text, (float) position.getX(), (float) position.getY());
        g.setColor(oldColor);
        g.setTransform(oldTransformation);
    }

    /**
     * Draws a Grid to help With Positioning
     *
     * @param g2d the specified graphics context
     */
    public void drawGrid(Graphics2D g2d, int width, int height, boolean drawMinors) {
        // save g2d configuration
        Color oldColor = g2d.getColor();
        Stroke oldStroke = g2d.getStroke();
        // G2d Configuration
        g2d.setColor(TUColors.COLOR_0D);
        float outerTicksWidth = .5f;
        float tenTicksWidth = .25f;
        float fiveTicksWidth = .125f;
        float oneTicksWidth = 1f / 32f;
        // Vertical Lines
        for (int i = 0, x = -width / 2; x < width / 2; i++, x += 1) {
            float strokeWidth;
            if (i % 10 == 0) {
                strokeWidth = tenTicksWidth;
            } else if (i % 5 == 0) {
                strokeWidth = fiveTicksWidth;
            } else {
                strokeWidth = oneTicksWidth;
                if (!drawMinors) {
                    continue;
                }
            }
            g2d.setStroke(new BasicStroke(strokeWidth));
            g2d.drawLine(x, -height / 2, x, height / 2);
        }
        // Horizontal Lines
        for (int i = 0, y = -height / 2; y < height / 2; i++, y += 1) {
            float strokeWidth;
            if (i % 10 == 0) {
                strokeWidth = tenTicksWidth;
            } else if (i % 5 == 0) {
                strokeWidth = fiveTicksWidth;
            } else {
                strokeWidth = oneTicksWidth;
                if (!drawMinors) {
                    continue;
                }
            }
            g2d.setStroke(new BasicStroke(strokeWidth));
            g2d.drawLine(-width / 2, y, width / 2, y);
        }

        // Border
        g2d.setStroke(new BasicStroke(outerTicksWidth));
        g2d.drawRect(
            (int) (-width / 2 - outerTicksWidth / 2),
            (int) (-height / 2 - outerTicksWidth / 2),
            (int) (width + outerTicksWidth),
            (int) (height + outerTicksWidth));

        // Restore g2d Configuration
        g2d.setColor(oldColor);
        g2d.setStroke(oldStroke);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (lastPoint.get() == null)
            center();
        var g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        g2d.setTransform(getTransform());
        paintMap(g2d);
        g2d.setTransform(old);
    }

    /**
     * Paints the Map using the given g2d. This method assumes that (0,0) paints
     * centered
     *
     * @param g The specified Graphics Context, transformed so (0,0) is
     *          centered.
     */
    private void paintMap(Graphics2D g) {
        // Background
        drawGrid(g, 50, 50, true);
        mainFrame.region.getEdges().forEach(e -> paintEdge(g, e));
        mainFrame.getRegion().getNodes().forEach(n -> drawNode(g, n));
        mainFrame.vehicleManager.getVehicles().forEach(v -> paintVehicle(g, v));
    }
}
