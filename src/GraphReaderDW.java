import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GraphReaderDW implements GraphReaderInterface {

    public GraphReaderDW() {}

    public AirportInterface[] readAirportsFromGraph(String filename) throws FileNotFoundException {


        AirportInterface[] airports = new AirportInterface[10];
        File file = new File(filename);
        int numAirport = 0;

        try {

            Scanner scan = new Scanner(file);
            scan.nextLine();

            while (scan.hasNextLine()) {

                String line = scan.nextLine();
                if (!line.isEmpty()) {
                    if (line.indexOf("label")>=0) {

                        //grab the label in quotation marks
                        int index = line.indexOf("\"");
                        String secondQ = line.substring(index+1);
                        String info = secondQ.substring(0,secondQ.indexOf("\""));
                        //split the string btwn the comma to separate the airportName and country
                        String[] splitInfo = info.split(",");
                        String airport = splitInfo[0].trim();
                        String country = splitInfo[1].trim();

                        //add the airport read from the graph to the array
                        airports[numAirport] = new AirportDW(airport, country);
                        numAirport++;

                    }


                }


            }

            return airports;


        } catch (FileNotFoundException e) {

            throw new FileNotFoundException("Error: No file was found with this filename!");

        }


    }


    public CostInterface[][] readCostsFromGraph(String filename) throws FileNotFoundException {

        try {
            AirportInterface[] airports = readAirportsFromGraph(filename);
            int size = airports.length;
            CostInterface[][] edges = new CostInterface[size][size];
            //	AirportInterface x;
            //	AirportInterface y;
            int indexX = 0;
            int indexY = 0;

            File file = new File(filename);
            Scanner scan = new Scanner(file);
            scan.nextLine();

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (!line.isEmpty()) {
                    if (line.indexOf("--")>0) {

                        //find the substring containing the airport names
                        String airportInfo = line.substring(0,line.indexOf("["));
                        //split the string btwn the two airports
                        String[] splitInfo = airportInfo.split("--");
                        String airport1 = splitInfo[0].trim();
                        String airport2 = splitInfo[1].trim();

                        //find both airports in the airport array
                        for (int i=0; i<airports.length; i++) {

                            String airportCompare = airports[i].getAirportName();
                            //if the airport name is not already in camelCase make it camelCase to compare with the graphNodes
                            if (airportCompare.contains(" ")) {

                                String[] splitBySpace = airportCompare.split(" ");
                                splitBySpace[0] = splitBySpace[0].toLowerCase();
                                airportCompare = splitBySpace[0]+splitBySpace[1];
                            } else {
                                airportCompare = airportCompare.toLowerCase();
                            }
                            //find a match with airportA in the array
                            if (airport1.equals(airportCompare)) {

                                indexX = i;
                                break;

                            }

                        }
                        for (int i=0; i<airports.length; i++) {

                            String airportCompare = airports[i].getAirportName();
                            //put airport name into camelCase to compare with graphNodes
                            if (airportCompare.contains(" ")) {

                                String[] splitBySpace = airportCompare.split(" ");
                                splitBySpace[0] = splitBySpace[0].toLowerCase();
                                airportCompare = splitBySpace[0]+splitBySpace[1];
                            } else {
                                airportCompare = airportCompare.toLowerCase();
                            }

                            //find a match with airportB in the array
                            if (airport2.equals(airportCompare)) {

                                indexY = i;
                                break;

                            }
                        }

                        //create new airports
                        AirportInterface x = new AirportDW(airports[indexX].getAirportName(), airports[indexX].getCountry());
                        AirportInterface y = new AirportDW(airports[indexY].getAirportName(), airports[indexY].getCountry());

                        //get the cost from inside the brackets
                        String costString = line.substring(line.indexOf("=")+1,line.indexOf("]"));
                        double cost = Double.parseDouble(costString);
                        //add the CostInteface into the array, in both directions since the graph is undirected
                        edges[indexX][indexY] = new CostDW(x, y, cost);
                        edges[indexY][indexX] = new CostDW(y, x, cost);
                    }
                }
            }
            return edges;

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Error: No file was found with this filename!");
        }
    }

}