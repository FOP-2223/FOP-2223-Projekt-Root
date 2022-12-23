package projekt.gui;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import projekt.base.Location;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.Collector;

import static projekt.delivery.routing.VehicleManager.Occupied;

public interface Utils {

    static Point2D getDifference(Point2D p1, Point2D p2) {
        return new Point2D.Double(p1.getX() - p2.getX(), p1.getY() - p2.getY());
    }

    static Image loadImage(String name, Color color) {

        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(Utils.class.getClassLoader().getResource(name)));
            for (int x = 0; x < image.getWidth(); x++)
                for (int y = 0; y < image.getHeight(); y++)
                    if (image.getRGB(x, y) == Color.BLACK.getRGB())
                        image.setRGB(x, y, color.getRGB());
            return SwingFXUtils.toFXImage(image, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static Image loadImage2(String s, Color carColor) {
        try {
            return new Image(Objects.requireNonNull(Utils.class.getClassLoader().getResource(s)).openStream());
        } catch (IOException e) {
            System.err.println("Could not load Image: "+s);
            e.printStackTrace();
        }
        return null;
    }

    static Point2D midPoint(Occupied<?> occupied) {
        if (occupied.getComponent() instanceof Region.Node) {
            return midPoint(((Region.Node) occupied.getComponent()).getLocation());
        } else if (occupied.getComponent() instanceof Region.Edge) {
            return midPoint((Region.Edge) occupied.getComponent());
        }
        throw new UnsupportedOperationException("unsupported type of component");
    }

    static Point2D midPoint(Location location) {
        return new Point2D.Double(location.getX(), location.getY());
    }

    static Point2D midPoint(Vehicle vehicle) {
        return midPoint(vehicle.getOccupied());
    }

    static Point2D midPoint(Region.Node node) {
        return midPoint(node.getLocation());
    }

    static Point2D midPoint(Region.Edge edge) {
        var l1 = edge.getNodeA().getLocation();
        var l2 = edge.getNodeB().getLocation();
        return new Point2D.Double((l1.getX() + l2.getX()) / 2d, (l1.getY() + l2.getY()) / 2d);
    }



    interface Collectors {

        Collector<Point2D, Point2D, Point2D> POINT_MIN = point((a, e) -> a < e);
        Collector<Point2D, Point2D, Point2D> POINT_MAX = point((a, e) -> a > e);

        static Collector<Point2D, Point2D, Point2D> point(BiPredicate<Double, Double> pred) {

            return Collector.of(Point2D.Double::new,
                (a, e) -> a.setLocation(
                    pred.test(a.getX(), e.getX()) ? a.getX() : e.getX(),
                    pred.test(a.getY(), e.getY()) ? a.getY() : e.getY()),
                (a, e) -> {
                    throw new UnsupportedOperationException();
                });
        }

        static Collector<Point2D, Point2D, Point2D> center() {

            return new Collector<>() {

                final AtomicInteger counter = new AtomicInteger();

                @Override
                public Supplier<Point2D> supplier() {
                    return Point2D.Double::new;
                }

                @Override
                public BiConsumer<Point2D, Point2D> accumulator() {
                    return (acc, elem) -> {
                        counter.incrementAndGet();
                        acc.setLocation(acc.getX() + elem.getX(), acc.getY() + elem.getY());
                    };
                }

                @Override
                public BinaryOperator<Point2D> combiner() {
                    return (a, b) -> new Point2D.Double((a.getX() + b.getX()) / 2d, (a.getY() + b.getY()) / 2d);
                }

                @Override
                public Function<Point2D, Point2D> finisher() {
                    return acc -> {
                        System.out.println("---> " + counter);
                        acc.setLocation(acc.getX() / counter.get(), acc.getY() / counter.get());
                        return acc;
                    };
                }

                @Override
                public Set<Characteristics> characteristics() {
                    return Set.of();
                }
            };
        }
    }
}
