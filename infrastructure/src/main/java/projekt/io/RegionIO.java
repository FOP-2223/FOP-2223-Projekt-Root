package projekt.io;

import projekt.base.*;
import projekt.delivery.routing.CachedPathCalculator;
import projekt.delivery.routing.Region;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;

public class RegionIO {

    private static final Map<String, Class<? extends DistanceCalculator>> DESERIALIZED_DISTANCE_CALCULATOR = Map.of(
        CachedPathCalculator.class.getSimpleName(), ChessboardDistanceCalculator.class,
        EuclideanDistanceCalculator.class.getName(), EuclideanDistanceCalculator.class,
        ManhattanDistanceCalculator.class.getName(), ManhattanDistanceCalculator.class
    );

    public static Region readRegion(BufferedReader reader) {
        Region.Builder builder = Region.builder();

        try {

            String line;
            while (!Objects.equals(line = reader.readLine(), "END REGION")) {

                if (line.startsWith("N ")) {
                    String[] serializedNode = line.substring(2).split(",", 3);
                    builder.addNode(serializedNode[0], parseLocation(serializedNode[1], serializedNode[2]));

                } else if (line.startsWith("E ")) {
                    String[] serializedEdge = line.substring(2).split(",", 5);
                    builder.addEdge(serializedEdge[0],
                        parseLocation(serializedEdge[1], serializedEdge[2]),
                        parseLocation(serializedEdge[3], serializedEdge[4]));

                } else if (line.startsWith("D ")) {
                    builder.distanceCalculator(parseDistanceCalculator(line.substring(2)));
                } else {
                    throw new RuntimeException("Illegal line read: %s".formatted(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return builder.build();
    }

    public static void writeRegion(BufferedWriter writer, Region region) {
        try {
            for (Region.Node node : region.getNodes()) {
                writer.write("N %s\n".formatted(serializeNode(node)));
            }

            for (Region.Edge edge : region.getEdges()) {
                writer.write("E %s\n".formatted(serializeEdge(edge)));
            }

            writer.write("D %s".formatted(region.getDistanceCalculator().getClass().getSimpleName()));

            writer.write("END REGION");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String serializeNode(Region.Node node) {
        return "%s,%d,%d".formatted(node.getName(), node.getLocation().getX(), node.getLocation().getY());
    }

    private static String serializeEdge(Region.Edge edge) {
        return "%s,%d,%d,%d,%d".formatted(edge.getName(),
            edge.getNodeA().getLocation().getX(), edge.getNodeA().getLocation().getY(),
            edge.getNodeB().getLocation().getX(), edge.getNodeB().getLocation().getY());
    }

    private static Location parseLocation(String x, String y) {
        return new Location(Integer.parseInt(x), Integer.parseInt(y));
    }

    private static DistanceCalculator parseDistanceCalculator(String serializedDistanceCalculator) {
        Class<? extends DistanceCalculator> distanceCalculatorClass =
            DESERIALIZED_DISTANCE_CALCULATOR.get(serializedDistanceCalculator);
        try {
            return distanceCalculatorClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
