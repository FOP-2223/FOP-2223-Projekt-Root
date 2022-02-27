package projekt.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
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
    private double scale = 1d;
    private double scaleModifierFactor = 0.1;

    private static final double NODE_DIAMETER = 10;

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
     * Create A shape with the desired Text and the desired width
     *
     * @param g2d             the specified Graphics context to draw the font with
     * @param borderThickness the border thickness to account for
     * @param text            the string to display
     * @param f               the font used for drawing the string
     * @return The Shape of the outline
     */
    public Shape fitTextInBounds(Graphics2D g2d, Rectangle bounds, float borderThickness, String text, Font f) {
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

        region
            .getNodes()
            .stream()
            .map(Region.Node::getLocation)
            .forEach(location -> g2d.fill(new Ellipse2D.Double(location.getX(), location.getY(), NODE_DIAMETER, NODE_DIAMETER)));
        region
            .getEdges()
            .stream()
            .map(edge -> new Location[] {edge.getNodeA().getLocation(), edge.getNodeB().getLocation()})
            .forEach(locations -> {
                Line2D.Double line = new Line2D.Double(
                    locations[0].getX() + NODE_DIAMETER / 2,
                    locations[0].getY() + NODE_DIAMETER / 2,
                    locations[1].getX() + NODE_DIAMETER / 2,
                    locations[1].getY() + NODE_DIAMETER / 2
                );
                g2d.draw(line);
            });

        // Mark Center
        g2d.setColor(Color.GREEN);
        g2d.fill(new Ellipse2D.Double(0, 0, NODE_DIAMETER, NODE_DIAMETER));
        g2d.drawString("Center", 0, 0);
//        var centerLabelBounds = new Rectangle(-100, -75, 200, 50);
//        var centerString = fitTextInBounds(g2d,
//                centerLabelBounds, 0, "Center", g2d.getFont());
//        g2d.fill(centerString);

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
