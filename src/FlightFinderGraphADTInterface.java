import java.util.List;
import java.util.NoSuchElementException;

public interface FlightFinderGraphADTInterface {
    public boolean insertNode(AirportInterface data);
    public boolean removeNode(AirportInterface data);
    public boolean containsNode(AirportInterface data);
    public int getNodeCount();
    public boolean insertEdge(AirportInterface pred, AirportInterface succ, CostInterface weight);
    public boolean removeEdge(AirportInterface pred, AirportInterface succ);
    public boolean containsEdge(AirportInterface pred, AirportInterface succ);
    public CostInterface getEdge(AirportInterface pred, AirportInterface succ);
    public int getEdgeCount();
    public List<AirportInterface> shortestPathData(AirportInterface start, AirportInterface end);
    public AirportInterface getAirportByPlace(AirportInterface start, String country) throws NoSuchElementException;
    public double shortestPathCost(AirportInterface start, AirportInterface end);
    public List<AirportInterface> shortestPathWithMaxNodes(AirportInterface start, AirportInterface end, int nodeNum);
    public double shortestCostWithMaxNodes(AirportInterface start, AirportInterface end, int nodeNum);

}
