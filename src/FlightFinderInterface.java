import java.util.List;
import java.util.NoSuchElementException;
import java.io.FileNotFoundException;


public interface FlightFinderInterface {


    //user inputs -1 if no specific amount of layovers desired
    public List<AirportInterface> searchByCost(String start, String end, int layovers);

    public double searchByCostDouble(String start, String end, int layovers);

    public void loadData(String fileName) throws FileNotFoundException;

    public boolean contains(String location);

    public AirportInterface getAirportObject(String name) throws NoSuchElementException;

    public AirportInterface searchAirportCountry(String airport, String country);
}