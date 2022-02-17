package projekt.io;

import projekt.base.*;
import projekt.delivery.ConfirmedOrder;
import projekt.delivery.ProblemInstance;
import projekt.delivery.vehicle.Region;
import projekt.delivery.vehicle.VehicleManager;
import projekt.delivery.vehicle.VehicleOverloadedException;
import projekt.food.*;
import projekt.delivery.vehicle.Vehicle;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * Format:
 * ! {Euclidean | Manhattan | Chessboard}Distance2D
 * V <vehicle ID>: <vehicle capacity>
 * ...
 * D <x>, <y>, <demand>, <start time>, <end time>
 * ...
 *
 * First line is the dynamic type's simple class name.
 * <start time> and <end time> are ISO 8601-formatted: YYYY-MM-DDThh:mm:ss[.fffffffff]
 * See https://de.wikipedia.org/wiki/ISO_8601#Darstellung
 */

// FIXME: fix serialization of vehicle information (id, location, compatible foods, etc.)
public class ProblemInstanceIO {

    private static final Map<String, DistanceCalculator> DISTANCE_CALCULATOR_LOOKUP = Map.of(
        "Chessboard", new ChessboardDistanceCalculator(),
        "Euclidean", new EuclideanDistanceCalculator(),
        "Manhattan", new ManhattanDistanceCalculator()
    );

    public ProblemInstance readProblemInstance(Reader reader) {
        String distanceType = "";
        List<Vehicle> vehicleList = new ArrayList<>();
        List<ConfirmedOrder> orderList = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            distanceType = bufferedReader.readLine().split(" ", 2)[1];

            bufferedReader
                .lines()
                .forEach(s -> {
                    if (s.charAt(0) == 'O') {
                        String[] lineData = s.substring(2).split(", ", 6);

                        orderList.add(new ConfirmedOrder(
                            Integer.parseInt(lineData[0]),
                            Integer.parseInt(lineData[1]),
                            Integer.parseInt(lineData[2]),
                            new TimeInterval(
                                LocalDateTime.parse(lineData[3]),
                                LocalDateTime.parse(lineData[4])
                            ),
                            Arrays
                                .stream(lineData[5].split(", "))
                                .map(ProblemInstanceIO::parseFood)
                                .collect(Collectors.toList())
                        ));
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ProblemInstance(DISTANCE_CALCULATOR_LOOKUP.get(distanceType), vehicleList, orderList);
    }

    public void writeProblemInstances(Writer writer, ProblemInstance problemInstance) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(String.format("! %s\n", problemInstance.getDistance2D()));

            problemInstance
                .getVehicles()
                .stream()
                .map(vehicle -> String.format("V %d: %f\n", vehicle.getId(), vehicle.getCapacity()))
                .forEach(vehicleData -> {
                    try {
                        bufferedWriter.write(vehicleData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            problemInstance
                .getOrders()
                .stream()
                .map(order -> String.format("O %s\n", order.toString()))
                .forEach(orderData -> {
                    try {
                        bufferedWriter.write(orderData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Food parseFood(String foodString) {
        /*
         * [0]: food type
         * [1]: food variant
         * [2]: extras (seperated by "|")
         */
        String[] foodData = foodString.split(" ");

        return FoodType
            .parse(foodData[0])
            .getFoodVariants()
            .stream()
            .filter(currentFoodVariant -> currentFoodVariant.getName().equals(foodData[1]))
            .findAny()
            .orElseThrow()
            .create(
                Arrays
                    .stream(foodData[2].split("\\|"))
                    .map(s -> (Extra<? super Food.Config>) Extras.ALL.get(s))
                    .collect(Collectors.toList())
            );
    }
}
