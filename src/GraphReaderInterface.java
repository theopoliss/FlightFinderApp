import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public interface GraphReaderInterface {
    // public GraphReaderInterface();
    public AirportInterface[] readAirportsFromGraph(String filename) throws FileNotFoundException;
    public CostInterface[][] readCostsFromGraph(String filename) throws FileNotFoundException;
}