package projekt.io;

import projekt.base.Location;
import projekt.delivery.routing.Region;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.time.Duration;

public class RegionIO {

    public static Region readRegion(Reader reader) {
        Region.Builder builder = Region.builder();
        // TODO: Read used distance function
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            bufferedReader.lines().forEach(line -> {
                if (line.startsWith("N ")) {
                    String[] serializedNode = line.substring(2).split("#", 2);
                    builder.addNode(serializedNode[0], parseLocation(serializedNode[1]));
                } else if (line.startsWith("E ")) {
                    String[] serializedEdge = line.substring(2).split(",", 4);
                    builder.addEdge(serializedEdge[0],
                        parseLocation(serializedEdge[1]),
                        parseLocation(serializedEdge[2]));
                } else {
                    // TODO: throw exception or ignore?
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO: builder.distanceCalculator(distanceCalculator);
        return builder.build();
    }

    public static void writeRegion(Writer writer, Region region) {
        // TODO: Write used distance function
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            for (Region.Node node : region.getNodes()) {
                bufferedWriter.write("N %s,%s\n".formatted(node.getName(), node.getLocation()));
            }

            for (Region.Edge edge : region.getEdges()) {
                bufferedWriter.write("E %s,%s,%s\n".formatted(
                    edge.getName(),
                    serializeNode(edge.getNodeA()),
                    serializeNode(edge.getNodeB())
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String serializeNode(Region.Node node) {
        return "%s#%s".formatted(node.getName(), node.getLocation());
    }

    private static Location parseLocation(String serializedLocation) {
        String[] splitSerializedLocation = serializedLocation.split("\\|");
        return new Location(Integer.parseInt(splitSerializedLocation[0]), Integer.parseInt(splitSerializedLocation[1]));
    }
}
