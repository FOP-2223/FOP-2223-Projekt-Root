package projekt.io;

import projekt.base.Location;
import projekt.food.Food;
import projekt.delivery.vehicle.Vehicle;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * Format:
 * # <vehicle ID>: <destination (ID?)>...
 * ...
 *
 * TODO: assume at least one location on each line?
 */

public class RoutePlanningIO {

    public static Map<Vehicle, List<Location>> readRoutePlanner(Reader reader) {
        Map<Vehicle, List<Location>> routeMapping = null;

        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            routeMapping = bufferedReader
                .lines()
                .map(s -> s.substring(2).split(", ", 2))
                .collect(Collectors.toMap(
                    strings -> new Vehicle() {
                        private final String[] vehicleData = strings[0].split(": ");
                        private final List<Food> foods = new ArrayList<>();

                        @Override
                        public int getId() {
                            return Integer.parseInt(vehicleData[0]);
                        }

                        @Override
                        public double getCapacity() {
                            return Double.parseDouble(vehicleData[1]);
                        }

                        @Override
                        public Collection<Food> getFood() {
                            return foods;
                        }

                        @Override
                        public void addFood(Food food) {
                            foods.add(food);
                        }
                    },
                    strings -> Arrays
                        .stream(strings[1].split(", "))
                        .map(s -> {
                            String[] locationData = s.split("\\|");

                            return new Location(Integer.parseInt(locationData[0]), Integer.parseInt(locationData[1]));
                        })
                        .collect(Collectors.toList())
                ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return routeMapping;
    }

    public static void writeRoutePlanner(Writer writer, Map<Vehicle, List<Location>> routeMapping) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            routeMapping.forEach((key, value) -> {
                try {
                    bufferedWriter.write(String.format(
                        "# %d: %f, %s\n",
                        key.getId(),
                        key.getCapacity(),
                        value
                            .stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(", "))
                    ));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
