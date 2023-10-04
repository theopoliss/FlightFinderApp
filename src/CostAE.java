public class CostAE implements CostInterface {
    Double cost;
    public CostAE(Double cost) {
        this.cost = cost;
    }
    @Override
    public Double getCost() {
        return this.cost;
    }
    @Override
    public AirportInterface getAirportA() { return null; }
    @Override
    public AirportInterface getAirportB() { return null; }

    @Override
    public int compareTo(CostInterface o) {

        return (int)(this.getCost() - o.getCost());

    }

}