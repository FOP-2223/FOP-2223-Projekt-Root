package projekt.io;

import projekt.base.DistanceCalculator;
import projekt.base.Location;
import projekt.delivery.routing.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;

public class VehicleManagerIO {

    private static final Map<String, Class<? extends PathCalculator>> DESERIALIZED_PATH_CALCULATOR = Map.of(
        CachedPathCalculator.class.getSimpleName(), CachedPathCalculator.class,
        DijkstraPathCalculator.class.getSimpleName(), DijkstraPathCalculator.class
    );

    public static VehicleManager readVehicleManager(BufferedReader reader) {

        VehicleManager.Builder builder = VehicleManager.builder();

        try {

            String line;
            while (!Objects.equals(line = reader.readLine(), "END VEHICLE MANAGER")) {

                if (line.startsWith("R ")) {
                    builder.region(RegionIO.readRegion(reader));
                } else if (line.startsWith("V ")) {
                    String[] splitSerializedVehicle = line.substring(2).split(",", 3);
                    builder.addVehicle(parseLocation(splitSerializedVehicle[0], splitSerializedVehicle[1]),
                        Double.parseDouble(splitSerializedVehicle[2]));
                } else if (line.startsWith("P ")) {
                    builder.pathCalculator(parsePathCalculator(line.substring(2)));
                } else {
                    throw new RuntimeException("Illegal line read: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.build();
    }

    public static void writeVehicleManager(BufferedWriter writer, VehicleManager vehicleManager) {

        try {
            writer.write("R ");
            RegionIO.writeRegion(writer, vehicleManager.getRegion());

            for (Vehicle vehicle : vehicleManager.getAllVehicles()) {
                writer.write("V %s".formatted(serializeVehicle(vehicle)));
            }

            writer.write("P %s".formatted(vehicleManager.getPathCalculator().getClass().getSimpleName()));

            writer.write("END VEHICLE MANAGER");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String serializeVehicle(Vehicle vehicle) {
        return "%d,%d,%f".formatted(
            vehicle.getStartingNode().getComponent().getLocation().getX(),
            vehicle.getStartingNode().getComponent().getLocation().getX(),
            vehicle.getCapacity());

    }

    private static Location parseLocation(String x, String y) {
        return new Location(Integer.parseInt(x), Integer.parseInt(y));
    }

    private static String serializePathCalculator(PathCalculator pathCalculator) {
        StringBuilder sb = new StringBuilder();
        sb.append(pathCalculator.getClass().getSimpleName());

        while (pathCalculator instanceof CachedPathCalculator cachedPathCalculator) {
            pathCalculator = cachedPathCalculator.getDelegate();
            sb.append(",%s".formatted(pathCalculator.getClass().getSimpleName()));
        }

        return sb.toString();
    }

    private static PathCalculator parsePathCalculator(String serializedPathCalculator) {
        String[] split = serializedPathCalculator.split(",");
        PathCalculator currentPC;
        try {
            currentPC = DESERIALIZED_PATH_CALCULATOR.get(split[split.length - 1]).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        PathCalculator returnPC = currentPC;

        //handle cached Path Calculators
        for (int i = split.length - 2; i >= 0; i--) {
            PathCalculator PC;
            try {
                PC = DESERIALIZED_PATH_CALCULATOR.get(split[i]).getDeclaredConstructor(PathCalculator.class).newInstance(currentPC);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            currentPC = PC;
        }

        return returnPC;
    }
}