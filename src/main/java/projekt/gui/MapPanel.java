package projekt.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class MapPanel extends JPanel {
    public MapPanel() {
        setBorder(new TitledBorder("Map"));
    }

    /**
     * Create A shape with the desired Text and the desired width
     *
     * @param g2d             the specified Graphics context to draw the font with
     * @param width           the desired text width
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
        g2d.translate(innerBounds.getCenterX(), innerBounds.getCenterY());

        paintMap(g2d, new Rectangle2D.Double(-innerBounds.getWidth() / 2,
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
        g2d.setColor(Color.BLACK);
        g2d.fill(MapBounds);

        g2d.setColor(Color.GREEN);
        g2d.fill(new Ellipse2D.Double(0, 0, 10, 10));
        // g2d.drawString("Center", 0, 0);
        var fontBounds = new Rectangle(-100, -75, 200, 50);
        // g2d.draw(fontBounds);
        var centerString = fitTextInBounds(g2d,
                fontBounds, 0, "Center", g2d.getFont());
        g2d.fill(centerString);
    }
}
