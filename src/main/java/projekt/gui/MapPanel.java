package projekt.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import projekt.base.Location;
import projekt.delivery.DeliveryService;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.VehicleManager;
import projekt.pizzeria.Pizzeria;

public class MapPanel extends JPanel {
    private Region region;
    private VehicleManager vehicleManager;
    private DeliveryService deliveryService;
    private Pizzeria pizzeria;

    private Point anchorPoint;
    private Point centerLocation = new Point(0, 0);
    private double scale = 10d;
    private double scaleModifierFactor = 0.1;

    private static final double NODE_DIAMETER = 1;

    public MapPanel(Region region, VehicleManager vehicleManager, DeliveryService deliveryService, Pizzeria pizzeria) {
        this.region = region;
        this.vehicleManager = vehicleManager;
        this.deliveryService = deliveryService;
        this.pizzeria = pizzeria;
        initComponents();
    }

    private void initComponents() {
        setBackground(Color.BLACK);
        setBorder(new TitledBorder("Map"));
        this.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent event) {
                int anchorX = anchorPoint.x;
                int anchorY = anchorPoint.y;

                Point parentOnScreen = getParent().getLocationOnScreen();
                Point mouseOnScreen = event.getLocationOnScreen();
                Point position = new Point(mouseOnScreen.x - parentOnScreen.x - anchorX,
                        mouseOnScreen.y - parentOnScreen.y - anchorY);
                // setLocation(position);
                // TODO: Fix Mouse Movent always calculating from center
                setCenterLocation(position);
                // setCenterLocation(new Point((int) (centerLocation.getX() + position.getX()),
                // (int) (centerLocation.getY() + centerLocation.getY())));
            }

            @Override
            public void mouseMoved(MouseEvent event) {
                Point p = event.getPoint();

                // save current cursor position, used by mouseDragged()
                anchorPoint = p;

                // TODO: Check if mouse makes contact with Node and display info Menu
            }
        });
        this.addMouseWheelListener(event -> {
            int notches = event.getWheelRotation();
            if (notches < 0) {
                increaseScale();
            } else {
                decreaseScale();
            }
        });
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
        g2d.setColor(Color.GRAY);

        float outerTicksWidth = .6f;
        float tenTicksWidth = .3f;
        float fiveTicksWidth = .2f;
        float oneTicksWidth = .1f;

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

        var oldTranslation = g2d.getTransform();
        g2d.scale(scale, scale);
        g2d.translate((innerBounds.getCenterX() + centerLocation.getX()) / scale,
                (innerBounds.getCenterY() + centerLocation.getY()) / scale);

        paintMap(g2d, new Rectangle2D.Double(
                -innerBounds.getWidth() / 2,
                -innerBounds.getHeight() / 2,
                innerBounds.getWidth(),
                innerBounds.getHeight()));

        g2d.setTransform(oldTranslation);
    }

    /**
     * Paints the Map using the given g2d. This method assumes that (0,0) paints
     * centered
     *
     * @param g2d       The specified Graphics Context, transformed so (0,0) is
     *                  centered.
     * @param MapBounds the visual Bounds of the Map
     */
    private void paintMap(Graphics2D g2d, Rectangle2D MapBounds) {
        // Background
        // g2d.setColor(Color.BLACK);
        // g2d.fill(MapBounds);
        drawGrid(g2d, 40, 40, scale > 30d);
        g2d.setStroke(new BasicStroke(0.3f));

        var actualNodeDiameter = Math.min(NODE_DIAMETER / (scale / 100), 2 * NODE_DIAMETER);
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
        region
                .getEdges()
                .stream()
                .map(edge -> new Location[] { edge.getNodeA().getLocation(), edge.getNodeB().getLocation() })
                .forEach(locations -> {
                    Line2D.Double line = new Line2D.Double(
                            locations[0].getX(),
                            locations[0].getY(),
                            locations[1].getX(),
                            locations[1].getY());
                    g2d.draw(line);
                });

        // Mark Center
        g2d.setColor(Color.GREEN);
        fillAt(g2d, 0, 0, new Ellipse2D.Double(0, 0, actualNodeDiameter, actualNodeDiameter));
        // g2d.drawString("Center", 0, 0);
        var centerLabel = getLabel(g2d, 0, 0, Math.max(10,100 / scale), 1, "Center");
        g2d.setStroke(new BasicStroke(0.5f));
        g2d.fill(centerLabel);

        // TODO: Draw Actual Map
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
        repaint();
    }

    public void increaseScale() {
        this.scale *= 1d + scaleModifierFactor;
        repaint();
    }

    public void decreaseScale() {
        this.scale *= 1d - scaleModifierFactor;
        repaint();
    }

    public void resetScale() {
        this.scale = 1d;
        repaint();
    }

    public Point getCenterLocation() {
        return centerLocation;
    }

    public void setCenterLocation(Point centerLocation) {
        this.centerLocation = centerLocation;
        repaint();
    }

    public void resetCenterLocation() {
        this.centerLocation = new Point();
        repaint();
    }
}
