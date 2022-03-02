package projekt.gui;

import projekt.base.Location;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static projekt.delivery.routing.VehicleManager.*;

public interface Utils {

    static Point2D getDifference(Point2D p1, Point2D p2) {
        return new Point2D.Double(p1.getX() - p2.getX(), p1.getY() - p2.getY());
    }

    static BufferedImage loadImage(String name, Color color) {

        try {
            var image = ImageIO.read(Objects.requireNonNull(Utils.class.getClassLoader().getResource(name)));
            for (int x = 0; x < image.getWidth(); x++)
                for (int y = 0; y < image.getHeight(); y++)
                    if (image.getRGB(x, y) == Color.BLACK.getRGB())
                        image.setRGB(x, y, color.getRGB());
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static Point2D toPoint(Occupied<?> occupied) {
        if (occupied.getComponent() instanceof Region.Node) {
            return toPoint(((Region.Node) occupied.getComponent()).getLocation());
        } else if (occupied.getComponent() instanceof Region.Edge) {
            //noinspection PatternVariableCanBeUsed
            var edge = ((Region.Edge) occupied.getComponent());
            var l1 = edge.getNodeA().getLocation();
            var l2 = edge.getNodeB().getLocation();
            return new Point2D.Double((l1.getX() + l2.getX()) / 2d, (l1.getY() + l2.getY())/2d) ;
        }
        throw new UnsupportedOperationException("unsupported type of component");
    }

    static Point2D toPoint(Location location) {
        return new Point2D.Double(location.getX(), location.getY());
    }

    static Point2D toPoint(Vehicle vehicle) {
        return toPoint(vehicle.getOccupied());
    }


}
