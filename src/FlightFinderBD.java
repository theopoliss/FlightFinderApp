// --== CS400 Project p3w3 ==--
// Name: Bruno Inzunza
// CSL Username: bruno
// Email: binzunza@wisc.edu
// Lecture #: florian 3:30 MWF
// Notes to Grader: <any optional extra notes to your grader>
import java.io.FileNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;


public class FlightFinderBD implements FlightFinderInterface {

    //AE object which will contain the graph of all the AirportDW
    public FlightFinderGraphADTAE AirportDW = new FlightFinderGraphADTAE();
//    public FlightFinderGraphADTBD AirportBDs = new FlightFinderGraphADTBD();
    public AirportInterface[] nodes;
    public CostInterface[][] edges;

    /**
     * method that calls ae to find the shortest path and returns the list of AirportBD objects in the path order
     * @param start the starting AirportBD
     * @param end the ending AirportBD
     * @param layovers the amount of layovers the user wants
     * @return the list of AirportBD objects in the shortest path
     */
    public List<AirportInterface> searchByCost(String start, String end, int layovers){
        AirportInterface first = getAirportObject(start);
        AirportInterface second = getAirportObject(end);
        if(layovers != -1){
            return(AirportDW.shortestPathWithMaxNodes(first, second, layovers));
        }
        return(AirportDW.shortestPathData(first, second));
    }

    //For Original Testers
//    public List<AirportBD> searchByCost(AirportBD start, AirportBD end, int layovers){
//        return(AirportBDs.shortestPathData(start, end));
//    }


    /**
     * method that calls ae to find the closest airport that gets you from a starting airport to a certain country
     * @param airport starting airport
     * @param country ending location country
     * @return the closest airport in that specific country
     */
    public AirportInterface searchAirportCountry(String airport, String country){
        AirportInterface ap = getAirportObject(airport);
        return AirportDW.getAirportByPlace(ap, country);
    }

    /**
     * method that calls ae to find the shortest path and returns the total cost
     * @param start the starting AirportBD
     * @param end the ending AirportBD
     * @param layovers the amount of layovers the user wants
     * @return a double representing the total cost of the shortest path
     */
    public double searchByCostDouble(String start, String end, int layovers){
        AirportInterface first = getAirportObject(start);
        AirportInterface second = getAirportObject(end);
        if(layovers < 0){
            return AirportDW.shortestPathCost(first,second);
        }
        else{
            return AirportDW.shortestCostWithMaxNodes(first, second, layovers);
        }
    }

    //For original testers
//    public double searchByCostDouble(AirportBD start, AirportBD end, int layovers){
//        if(layovers < 0){
//            return AirportBDs.shortestPathCost(start,end);
//        }
//        else{
//            return AirportBDs.shortestCostWithMaxNodes(start, end, layovers);
//        }
//    }

    /**
     * method that calls the dw to get load the data from a file and inputs the nodes and edges onto the graph
     * @param fileName the file with the data
     * @throws FileNotFoundException thrown if the file inputted does not exist
     */
    public void loadData(String fileName) throws FileNotFoundException{
        GraphReaderDW temp = new GraphReaderDW();
        try {
            nodes = temp.readAirportsFromGraph(fileName);
            edges = temp.readCostsFromGraph(fileName);
        }
        catch(FileNotFoundException e){
            throw new FileNotFoundException("File does not exist");
        }
        for(int i = 0; i < 7; i++){
            //System.out.println(nodes[i].getAirportName());
            AirportDW.insertNode(nodes[i]);
            //System.out.println(AirportDW.getNodeCount());
        }
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 7; j++) {
                if(i != j){
//                    System.out.println(AirportDW.containsNode(edges[i][j].getAirportA()));
//                    System.out.println(AirportDW.containsNode(edges[i][j].getAirportB()));
//                    System.out.println(edges[i][j].getAirportA().getAirportName());
//                    System.out.println(edges[i][j].getAirportB().getAirportName());
                    AirportDW.insertEdge(getAirportObject(edges[i][j].getAirportA().getAirportName()), getAirportObject(edges[i][j].getAirportB().getAirportName()), edges[i][j]);
                    // System.out.println(AirportDW.getEdgeCount());
                }
            }
        }

    }

    @Override
    public boolean contains(String location) {
        AirportInterface temp = getAirportObject("location");
        return AirportDW.containsNode(temp);
    }

    /**
     * method that checks whether an AirportBD exists in the graph
     * @param location the AirportBD being searched for
     * @return true if the AirportBD does exist, and false otherwise
     */
    public boolean contains(AirportInterface location) {

        return AirportDW.containsNode(location);
    }

    //For original testers
//    public boolean contains(AirportBD location) {
//
//        return AirportBDs.containsNode(location);
//    }

    /**
     * method that gets an AirportBD object from its name
     * @param name the name of the AirportBD
     * @return the AirportBD object correlating to its inputted name
     * @throws NoSuchElementException thrown if an AirportBD does not exist in the graph
     */
    public AirportInterface getAirportObject(String name) throws NoSuchElementException {
        Set<AirportInterface> keys = AirportDW.nodes.keySet();
        for (AirportInterface key : keys) {
            String value = key.getAirportName();
            if(value.equalsIgnoreCase(name)){
                return key;
            }
        }
        throw new NoSuchElementException("Airport does not exist");
    }


    //For original testers
//    public AirportInterface getAirportBDObject(String name) throws NoSuchElementException {
//        Set<String> keys = AirportBDs.AirportBDHash.keySet();
//        for (String key : keys) {
//            String value = AirportBDs.AirportBDHash.get(key).getAirportName();
//            if(value.equals(name)){
//                return AirportBDs.AirportBDHash.get(key);
//            }
//        }
//        throw new NoSuchElementException("Airport does not exist");
//    }
}