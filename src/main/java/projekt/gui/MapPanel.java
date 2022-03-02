package projekt.gui;

import projekt.base.Location;
import projekt.delivery.DeliveryService;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;
import projekt.pizzeria.Pizzeria;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextLayout;
import java.awt.geom.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static projekt.gui.Utils.getDifference;
import static projekt.gui.Utils.toPoint;

public class MapPanel extends JPanel {

    private static final double NODE_DIAMETER = 0.8;
    private static final double IMAGE_DIAMETER = 0.7;
    //private static final Image IMAGE_CAR = getDefaultToolkit().getImage(Resources.getResource("car.png"));
    private static final Image IMAGE_CAR = Utils.loadImage("car.png", TUColors.COLOR_0D);
    private static final double SCALE_IN = 1.1;
    private static final double SCALE_OUT = 1 / SCALE_IN;

    private final Region region;
    private final VehicleManager vehicleManager;
    private final DeliveryService deliveryService;
    private final Pizzeria pizzeria;

    private final MainFrame mainFrame;
    private final Map<Location, String> nodes;


    private final AffineTransform t = new AffineTransform();
    //private final AffineTransform t = new AffineTransform();

    private Vehicle selectedVehicle = null;

    public MapPanel(Region region,
                    VehicleManager vehicleManager,
                    DeliveryService deliveryService,
                    Pizzeria pizzeria,
                    MainFrame mainFrame) {
        this.region = region;
        this.vehicleManager = vehicleManager;
        this.deliveryService = deliveryService;
        this.pizzeria = pizzeria;
        this.mainFrame = mainFrame;

        nodes = region.getNodes().stream().collect(Collectors.toMap(Region.Node::getLocation, Region.Component::getName));
        initComponents();
    }

    final AtomicReference<Point> lastPoint = new AtomicReference<>();

    private void initComponents() {
        setBackground(Color.BLACK);
        setBorder(new TitledBorder("Map"));

        this.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent event) {
                try {
                    var lastTransformed = t.inverseTransform(lastPoint.get(), null);
                    var currentTransformed = t.inverseTransform(event.getPoint(), null);
                    var difference = getDifference(currentTransformed, lastTransformed);
                    t.translate(difference.getX(), difference.getY());
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
                var before = t.inverseTransform(lastPoint.get(), null);
                var scale = event.getWheelRotation() > 0 ? SCALE_OUT : SCALE_IN;
                t.scale(scale, scale);
                var after = t.inverseTransform(lastPoint.get(), null);
                var difference = getDifference(after, before);
                t.translate(difference.getX(), difference.getY());
                repaint();
                updateLocation();
            } catch (NoninvertibleTransformException e) {
                throw new IllegalStateException();
            }
        });

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                updateVehicleSelection();
            }
        });

    }

    public Point2D getCurrentPoint() {
        if (lastPoint.get() == null)
            return null;
        var reverse = getReverseTransform();
        return reverse.transform(lastPoint.get(), null);
    }

    public Location getCurrentLocation() {
        var currentPoint = getCurrentPoint();
        if (currentPoint == null)
            return null;
        return new Location((int) Math.round(currentPoint.getX()), (int) Math.round(currentPoint.getY()));
    }

    public Set<Vehicle> getVehicles(Point2D position) {

        return vehicleManager.getVehicles()
            .stream()
            .filter(v -> toPoint(v).distance(position) < 1)
            .collect(Collectors.toUnmodifiableSet());

    }

    public void updateLocation() {
        var location = getCurrentLocation();
        mainFrame.getControlsPanel().getMousePositionLabel().setText(
            String.format("(x: %d, y: %d)", location.getX(), location.getY()));
    }

    public void updateVehicleSelection() {
        mainFrame.setVehicles(getVehicles(getCurrentPoint()));

    }

    private AffineTransform getTransform() {
        return t;
    }

    private AffineTransform getReverseTransform() {
        try {
            return t.createInverse();
        } catch (NoninvertibleTransformException e) {
            throw new IllegalStateException("transformation is non-invertible");
        }
    }

    /**
     * Fills a Given Shape and also draws a border with the given Colors saving and
     * restoring the original stoke and color of g2d.
     *
     * @param g2d           the specified Graphics context
     * @param interiorColor the Color of the filled Area
     * @param borderColor   the border Color
     * @param borderWidth   the Width of the Border
     * @param s             the Shape to draw
     */
    public void fillDraw(Graphics2D g2d, Color interiorColor, Color borderColor, float borderWidth, Shape s) {
        // Store current g2d Configuration
        Color oldColor = g2d.getColor();
        Stroke oldStroke = g2d.getStroke();

        // Fill the shape
        g2d.setColor(interiorColor);
        g2d.fill(s);

        if (borderWidth > 0) {
            // Draw a border on top
            g2d.setStroke(new BasicStroke(borderWidth));
            g2d.setColor(borderColor);
            g2d.draw(s);
        }

        // Restore g2d Configuration
        g2d.setStroke(oldStroke);
        g2d.setColor(oldColor);
    }

    public Shape centerShapeAtPos(double x, double y, Shape s) {
        return AffineTransform.getTranslateInstance(
                x - s.getBounds2D().getCenterX(),
                y - s.getBounds2D().getCenterY())
            .createTransformedShape(s);
    }

    public Shape centerShapeAtPos(Point center, Shape s) {
        return centerShapeAtPos(center.x, center.y, s);
    }

    public Rectangle2D r2dFromCenter(double x, double y, double w, double h) {
        return new Rectangle2D.Double(x - w / 2, y - w / 2, w, h);
    }

    public Rectangle2D r2dFromCenter(Point center, double w, double h) {
        return r2dFromCenter(center.x, center.y, w, h);
    }

    public void drawAt(Graphics2D g2d, double x, double y, Shape s) {
        g2d.draw(centerShapeAtPos(x, y, s));
    }

    public void fillAt(Graphics2D g2d, double x, double y, Shape s) {
        g2d.fill(centerShapeAtPos(x, y, s));
    }

    public void paintImage(Graphics2D g2d, double x, double y, Image image) {
        var old = g2d.getTransform();
        var transformation = new AffineTransform(old);
        transformation.translate(x - IMAGE_DIAMETER / 2, y - IMAGE_DIAMETER / 2);
        transformation.scale(IMAGE_DIAMETER, IMAGE_DIAMETER);
        g2d.setTransform(transformation);
        g2d.drawImage(image, 0, 0, 1, 1, null);
        g2d.setTransform(old);
    }

    public void paintImage(Graphics2D g2d, Point2D p, Image image) {
        paintImage(g2d, p.getX(), p.getY(), image);
    }

    public void paintVehicle(Graphics2D g2d, Vehicle vehicle) {
        paintImage(g2d, toPoint(vehicle), IMAGE_CAR);

    }

    public void paintImage(Graphics2D g2d, Location l1, Location l2, Image image) {
        paintImage(g2d, (l1.getX() + l2.getX()) / 2d, (l1.getY() + l2.getY()) / 2d, image);
    }

    /**
     * Create A shape with the desired Text and the desired width
     *
     * @param g2d             the specified Graphics context to draw the font with
     * @param borderThickness the border thickness to account for
     * @param text            the string to display
     * @param f               the font used for drawing the string
     * @return The Shape of the outline
     */
    public Shape fitTextInBounds(Graphics2D g2d, Rectangle2D bounds, float borderThickness, String text, Font f) {
        // Store current g2d Configuration
        Font oldFont = g2d.getFont();

        // graphics configuration
        g2d.setFont(f);

        // Prepare Shape creation
        TextLayout tl = new TextLayout(text, f, g2d.getFontRenderContext());
        Rectangle2D fontBounds = tl.getOutline(null).getBounds2D();

        // Calculate scale Factor
        double factorX = (bounds.getWidth() - borderThickness) / fontBounds.getWidth();
        double factorY = (bounds.getHeight() - borderThickness) / fontBounds.getHeight();
        double factor = Math.min(factorX, factorY);

        // Scale
        // TODO: Calculate scale accordingly when resizing window
        AffineTransform scaleTf = new AffineTransform();
        scaleTf.scale(factor, factor);

        // Move
        scaleTf.translate(bounds.getCenterX() / factor - fontBounds.getCenterX(),
            bounds.getCenterY() / factor - fontBounds.getCenterY());
        Shape outline = tl.getOutline(scaleTf);

        // Restore graphics configuration
        g2d.setFont(oldFont);
        return outline;
    }

    public Shape getLabel(Graphics2D g2d, double x, double y, double width, double offset, String text) {
        var bounds = r2dFromCenter(x, y - width / 2 - offset, width, width);
        var scaledText = fitTextInBounds(g2d, bounds, 0, text, g2d.getFont());
        return centerShapeAtPos(x, y - scaledText.getBounds2D().getHeight() / 2 - offset, scaledText);
    }

    /**
     * Draws a Grid to help With Positioning
     *
     * @param g2d the specified graphics context
     */
    @SuppressWarnings("unused")
    public void drawGrid(Graphics2D g2d, int width, int height, boolean drawMinors) {
        // save g2d configuration
        Color oldColor = g2d.getColor();
        Stroke oldStroke = g2d.getStroke();

        // G2d Configuration
        g2d.setColor(TUColors.COLOR_0D);

        float outerTicksWidth = .5f;
        float tenTicksWidth = .25f;
        float fiveTicksWidth = .125f;
        float oneTicksWidth = 1f/32f;

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

        var g2d = (Graphics2D) g;

        var insets = getInsets();
        var innerBounds = new Rectangle(getX() + insets.left,
            getY() + insets.top,
            getWidth() - insets.left - insets.right,
            getHeight() - insets.top - insets.bottom);

        AffineTransform old = g2d.getTransform();

        AffineTransform at = new AffineTransform();

        at.concatenate(t);
        g2d.setTransform(at);

//        var oldTranslation = g2d.getTransform();
//        g2d.scale(scale, scale);
//        g2d.translate((innerBounds.getCenterX() + centerLocation.getX()) / scale,
//            (innerBounds.getCenterY() + centerLocation.getY()) / scale);

        paintMap(g2d);

        g2d.setTransform(old);



    }

    /**
     * Paints the Map using the given g2d. This method assumes that (0,0) paints
     * centered
     *
     * @param g2d       The specified Graphics Context, transformed so (0,0) is
     *                  centered.
     */
    private void paintMap(Graphics2D g2d) {
        // Background
        // g2d.setColor(Color.BLACK);
        // g2d.fill(MapBounds);
        drawGrid(g2d, 40, 40, true);
        g2d.setStroke(new BasicStroke(0.125f));
        var actualNodeDiameter = NODE_DIAMETER;
        g2d.setColor(TUColors.COLOR_0B);
        region
            .getEdges()
            .stream()
            .map(edge -> new Location[]{edge.getNodeA().getLocation(), edge.getNodeB().getLocation()})
            .forEach(locations -> {
                Line2D.Double line = new Line2D.Double(
                    locations[0].getX(),
                    locations[0].getY(),
                    locations[1].getX(),
                    locations[1].getY());
                g2d.draw(line);
            });
        g2d.setColor(TUColors.COLOR_0C);
        region
            .getNodes()
            .stream()
            .map(Region.Node::getLocation)
            .forEach(location -> fillAt(g2d,
                location.getX(),
                location.getY(),
                new Ellipse2D.Double(location.getX(), location.getY(),
                    actualNodeDiameter,
                    actualNodeDiameter)));

        vehicleManager
            .getVehicles()
            .stream()
            .forEach(v -> paintVehicle(g2d, v));

//        // Mark Center
//        g2d.setColor(Color.GREEN);
//        fillAt(g2d, 0, 0, new Ellipse2D.Double(0, 0, actualNodeDiameter, actualNodeDiameter));
//        // g2d.drawString("Center", 0, 0);
//        var centerLabel = getLabel(g2d, 0, 0, Math.max(10,100 / scale), 1, "Center");
//        g2d.setStroke(new BasicStroke(0.5f));
//        g2d.fill(centerLabel);

        // TODO: Draw Actual Map
    }

}
