public class CostDW implements CostInterface {

    private AirportInterface airportA;
    private AirportInterface airportB;
    private double cost;


    public CostDW(AirportInterface a, AirportInterface b, double cost) {

        airportA = a;
        airportB = b;
        this.cost = cost;

    }

    public Double getCost() {

        return cost;

    }

    public AirportInterface getAirportA() {
        return airportA;
    }

    public AirportInterface getAirportB() {
        return airportB;
    }


    /**
     * Overriding compareTo to compare the cost and return a positive integer if current cost value is greater than the cost compared to,
     * return a negative if the cost compared to is greater, and zero if equal.
     */
    @Override
    public int compareTo(CostInterface o) {

        return (int)(this.getCost() - o.getCost());

    }



}