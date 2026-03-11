
import java.util.*;

class problem8 {

    String licensePlate;
    long entryTime;
    boolean occupied;

    public ParkingSpot() {
        this.licensePlate = null;
        this.entryTime = 0;
        this.occupied = false;
    }
}

public class ParkingLot {

    private ParkingSpot[] table;
    private int capacity;
    private int occupiedCount = 0;
    private int totalProbes = 0;
    private int operations = 0;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        table = new ParkingSpot[capacity];

        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Hash function
    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }

    // Park vehicle using linear probing
    public void parkVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (table[index].occupied) {
            index = (index + 1) % capacity;
            probes++;
        }

        table[index].licensePlate = licensePlate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;

        occupiedCount++;
        totalProbes += probes;
        operations++;

        System.out.println("parkVehicle(\"" + licensePlate + "\") → Assigned spot #"
                + index + " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String licensePlate) {

        int index = hash(licensePlate);

        while (table[index].occupied) {

            if (licensePlate.equals(table[index].licensePlate)) {

                long durationMillis =
                        System.currentTimeMillis() - table[index].entryTime;

                double hours = durationMillis / (1000.0 * 60 * 60);

                double fee = hours * 5.5; // example rate

                table[index].occupied = false;
                table[index].licensePlate = null;

                occupiedCount--;

                System.out.printf(
                        "exitVehicle(\"%s\") → Spot #%d freed, Duration: %.2f h, Fee: $%.2f%n",
                        licensePlate, index, hours, fee
                );

                return;
            }

            index = (index + 1) % capacity;
        }

        System.out.println("Vehicle not found.");
    }

    // Find nearest available spot
    public int findNearestSpot() {

        for (int i = 0; i < capacity; i++) {
            if (!table[i].occupied) {
                return i;
            }
        }

        return -1;
    }

    // Parking statistics
    public void getStatistics() {

        double occupancy =
                (occupiedCount * 100.0) / capacity;

        double avgProbes =
                operations == 0 ? 0 : (double) totalProbes / operations;

        System.out.println("\nParking Statistics:");
        System.out.println("Occupancy: " + String.format("%.2f", occupancy) + "%");
        System.out.println("Avg Probes: " + String.format("%.2f", avgProbes));
    }

    public static void main(String[] args) throws InterruptedException {

        problem8 = new ParkingLot(500);

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        Thread.sleep(2000);

        lot.exitVehicle("ABC-1234");

        System.out.println("Nearest spot: #" + lot.findNearestSpot());

        lot.getStatistics();
    }
}
