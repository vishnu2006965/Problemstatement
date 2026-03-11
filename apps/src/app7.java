import java.util.*;

class ParkingSpot {

    String licensePlate;
    long entryTime;
    String status;

    public ParkingSpot() {
        status = "EMPTY";
    }
}

public class app7 {

    private ParkingSpot[] table;
    private int size;
    private int occupied = 0;
    private int totalProbes = 0;

    public app7(int size) {

        this.size = size;
        table = new ParkingSpot[size];

        for (int i = 0; i < size; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Hash function
    private int hash(String licensePlate) {

        return Math.abs(licensePlate.hashCode()) % size;
    }

    // Park vehicle
    public void parkVehicle(String licensePlate) {

        int index = hash(licensePlate);

        int probes = 0;

        while (table[index].status.equals("OCCUPIED")) {

            index = (index + 1) % size; // linear probing
            probes++;

            if (probes >= size) {
                System.out.println("Parking Full");
                return;
            }
        }

        table[index].licensePlate = licensePlate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = "OCCUPIED";

        occupied++;
        totalProbes += probes;

        System.out.println(
                "Assigned spot #" + index +
                        " (" + probes + " probes)"
        );
    }

    // Exit vehicle
    public void exitVehicle(String licensePlate) {

        int index = hash(licensePlate);

        int probes = 0;

        while (!table[index].status.equals("EMPTY")) {

            if (table[index].status.equals("OCCUPIED")
                    && table[index].licensePlate.equals(licensePlate)) {

                long exitTime = System.currentTimeMillis();

                long duration =
                        (exitTime - table[index].entryTime) / 1000;

                double hours = duration / 3600.0;

                double fee = Math.ceil(hours) * 5;

                table[index].status = "DELETED";

                occupied--;

                System.out.println(
                        "Spot #" + index + " freed, Duration: "
                                + hours + " hours, Fee: $" + fee
                );

                return;
            }

            index = (index + 1) % size;
            probes++;

            if (probes >= size) break;
        }

        System.out.println("Vehicle not found");
    }

    // Find nearest spot to entrance
    public int findNearestSpot() {

        for (int i = 0; i < size; i++) {

            if (!table[i].status.equals("OCCUPIED")) {
                return i;
            }
        }

        return -1;
    }

    // Parking statistics
    public void getStatistics() {

        double occupancyRate =
                (occupied * 100.0) / size;

        double avgProbes =
                occupied == 0 ? 0 : (double) totalProbes / occupied;

        System.out.println("\nParking Statistics:");
        System.out.println("Occupancy: " + occupancyRate + "%");
        System.out.println("Avg Probes: " + avgProbes);
    }

    public static void main(String[] args) {

        app7 lot = new app7(500);

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.exitVehicle("ABC-1234");

        System.out.println(
                "Nearest available spot: " +
                        lot.findNearestSpot()
        );

        lot.getStatistics();
    }
}