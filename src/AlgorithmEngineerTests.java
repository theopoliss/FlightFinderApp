import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmEngineerTests {
    // Individual testers
    FlightFinderGraphADTInterface graph;
    AirportInterface sfo;
    AirportInterface jfk;
    AirportInterface man;
    AirportInterface lhr;
    AirportInterface mrs;
    AirportInterface lil;
    AirportInterface pek;
    AirportInterface cia;

    // CodeReviewOfBackendDeveloper/Integration
    FlightFinderBD flightFinder;

    /**
     * Initializes a test graph and adds nodes and edges
     */
    @BeforeEach
    void setup() {
        // Individual
        graph = new FlightFinderGraphADTAE();
        sfo = new AirportAE("SFO", "United States");
        jfk = new AirportAE("JFK", "United States");

        man = new AirportAE("MAN", "England");
        lhr = new AirportAE("LHR", "England");

        mrs = new AirportAE("MRS", "France");
        lil = new AirportAE("LIL", "France");

        pek = new AirportAE("PEK", "China");

        cia = new AirportAE("CIA", "Italy");

        graph.insertNode(sfo);
        graph.insertNode(jfk);
        graph.insertNode(man);
        graph.insertNode(lhr);
        graph.insertNode(mrs);
        graph.insertNode(lil);
        graph.insertNode(pek);
        // cia is disconnected
        graph.insertNode(cia);

        CostInterface sfo1 = new CostAE(5000.0);
        graph.insertEdge(lhr, sfo, sfo1);
        CostInterface sfo2 = new CostAE(3000.0);
        graph.insertEdge(jfk, sfo, sfo2);
        CostInterface sfo3 = new CostAE(7000.0);
        graph.insertEdge(mrs, sfo, sfo3);

        CostInterface jfk1 = new CostAE(3000.0);
        graph.insertEdge(sfo, jfk, jfk1);
        CostInterface jfk2 = new CostAE(3500.0);
        graph.insertEdge(lhr, jfk, jfk2);
        CostInterface jfk3 = new CostAE(4000.0);
        graph.insertEdge(mrs, jfk, jfk3);

        CostInterface lhr1 = new CostAE(3500.0);
        graph.insertEdge(jfk, lhr, lhr1);
        CostInterface lhr2 = new CostAE(700.0);
        graph.insertEdge(lil, lhr, lhr2);
        CostInterface lhr3 = new CostAE(200.0);
        graph.insertEdge(man, lhr, lhr3);
        CostInterface lhr4 = new CostAE(5000.0);
        graph.insertEdge(sfo, lhr, lhr4);

        CostInterface man1 = new CostAE(200.0);
        graph.insertEdge(lhr, man, man1);
        CostInterface man2 = new CostAE(380.0);
        graph.insertEdge(lil, man, man2);
        CostInterface man3 = new CostAE(900.0);
        graph.insertEdge(mrs, man, man3);

        CostInterface lil1 = new CostAE(700.0);
        graph.insertEdge(lhr, lil, lil1);
        CostInterface lil2 = new CostAE(380.0);
        graph.insertEdge(man, lil, lil2);
        CostInterface lil3 = new CostAE(600.0);
        graph.insertEdge(mrs, lil, lil3);

        CostInterface mrs1 = new CostAE(600.0);
        graph.insertEdge(lil, mrs, mrs1);
        CostInterface mrs2 = new CostAE(900.0);
        graph.insertEdge(man , mrs, mrs2);
        CostInterface mrs3 = new CostAE(7000.0);
        graph.insertEdge(sfo, mrs, mrs3);
        CostInterface mrs4 = new CostAE(4000.0);
        graph.insertEdge(jfk, mrs, mrs4);

        CostInterface pek1 = new CostAE(8000.0);
        graph.insertEdge(pek, sfo, pek1);

        // CodeReviewOfBackendDeveloper
        flightFinder = new FlightFinderBD();
    }

    /**
     * Tests that shortestPathCost returns the cost of the shortest path from one airport to another
     * Tests that shortestPathData returns a list containing the airports along the shortest from one airport to another
     * Tests that shortestPathCost and shortestPathData throw NoSuchElementException when expected to
     */
    @Test
    void algorithmEngineerTest1() {
        AirportInterface invalid = new AirportAE("LAX", "United States");
        try {
            graph.shortestPathCost(invalid, sfo);
            fail();
        }
        catch (NoSuchElementException e) {}
        try {
            graph.shortestPathData(sfo, cia);
            fail();
        }
        catch (NoSuchElementException e) {}
        try {
            graph.shortestPathCost(sfo, pek);
            fail();
        }
        catch (NoSuchElementException e) {}

        assertEquals(6100.0, graph.shortestPathCost(sfo, mrs));
        List<AirportInterface> expected1 = new ArrayList<>(Arrays.asList(sfo, lhr, man, mrs));
        assertEquals(expected1, graph.shortestPathData(sfo, mrs));

        assertEquals(4080.0, graph.shortestPathCost(lil, jfk));
        List<AirportInterface> expected2 = new ArrayList<>(Arrays.asList(lil, man, lhr, jfk));
        assertEquals(expected2, graph.shortestPathData(lil, jfk));
    }

    /**
     * Tests that getAirportByPlace returns the airport in a given country that is the closest
     * Tests that getAirportByPlace throws NoSuchElementException when expected to
     */
    @Test
    void algorithmEngineerTest2() {
        try {
            graph.getAirportByPlace(lil, "Italy");
            fail();
        }
        catch (NoSuchElementException e) {}
        try {
            graph.getAirportByPlace(cia, "United States");
            fail();
        }
        catch (NoSuchElementException e) {}

        assertEquals(jfk, graph.getAirportByPlace(lil, "United States"));
        assertEquals(lil, graph.getAirportByPlace(sfo, "France"));
    }

    /**
     * Tests that shortestCostWithMaxNodes returns the cost of the shortest path from one airport to another
     * while passing through a specific number of nodes.
     * Tests that shortestPathWithMaxNodes returns a list containing the airports along the shortest path
     * from one airport to another while passing through a specific number of nodes.
     * Tests that shortestPathWithMaxNodes throws a NoSuchElementException when expected to
     */
    @Test
    void algorithmEngineerTest3() {
        try {
            graph.shortestPathWithMaxNodes(sfo, pek, 3);
            fail();
        }
        catch (NoSuchElementException e) {}
        try {
            graph.shortestCostWithMaxNodes(sfo, pek, 3);
            fail();
        }
        catch (NoSuchElementException e) {}

        assertEquals(5000.0, graph.shortestCostWithMaxNodes(lhr, sfo, 4));
        List<AirportInterface> expected1 = new ArrayList<>(Arrays.asList(lhr, sfo));
        assertEquals(expected1, graph.shortestPathWithMaxNodes(lhr, sfo, 4));

        assertEquals(1100.0, graph.shortestCostWithMaxNodes(lhr, mrs, 3));
        List<AirportInterface> expected2 = new ArrayList<>(Arrays.asList(lhr, man, mrs));
        assertEquals(expected2, graph.shortestPathWithMaxNodes(lhr, mrs, 3));
    }

    /**
     * Tests that insertNode and insertEdge successfully adds new airports and edges between airports
     * Tests the functionality of containsNode and containsEdge
     */
    @Test
    void algorithmEngineerTest4() {
        AirportInterface msn = new AirportAE("MSN", "United States");
        graph.insertNode(msn);
        CostInterface costMsn = new CostAE(500.0);
        graph.insertEdge(sfo, msn, costMsn);
        assertTrue(graph.containsNode(msn));
        assertTrue(graph.containsEdge(sfo, msn));
        assertFalse(graph.containsEdge(msn, sfo));
        assertEquals(500.0, graph.shortestPathCost(sfo, msn));
    }

    /**
     * Tests that removeEdge and removeNode successfully removes airports and edges between airports
     */
    @Test
    void algorithmEngineerTest5() {
        graph.removeEdge(sfo, lhr);
        assertFalse(graph.containsEdge(sfo, lhr));

        assertEquals(7000.0, graph.shortestPathCost(sfo, mrs));
        List<AirportInterface> expected1 = new ArrayList<>(Arrays.asList(sfo, mrs));
        assertEquals(expected1, graph.shortestPathData(sfo, mrs));

        graph.removeNode(man);
        assertFalse(graph.containsNode(man));
    }

    /**
     * Tests the functionality of the methods loadData, contains, and getAirportObject in the BD class
     */
    @Test
    void CodeReviewOfBackendDeveloper1() {
        try {
            flightFinder.loadData("invalid.dot");
            fail();
        }
        catch (FileNotFoundException e) {}
        try {
            flightFinder.loadData("/Users/theoluo/IdeaProjects/P3W3/src/airportGraph.dot");
        }
        catch (FileNotFoundException e) {
            fail();
        }
        assertTrue(flightFinder.contains(flightFinder.getAirportObject("Chicago")));
        assertTrue(flightFinder.contains(flightFinder.getAirportObject("Liberia")));
        assertTrue(flightFinder.contains(flightFinder.getAirportObject("New York")));
    }

    /**
     * Tests the functionality of searchByCost and searchByCostDouble in the BD class
     */
    @Test
    void CodeReviewOfBackendDeveloper2() {
        try {
            flightFinder.loadData("/Users/theoluo/IdeaProjects/P3W3/src/airportGraph.dot");
        }
        catch (FileNotFoundException e) {
            fail();
        }
        assertEquals(500, flightFinder.searchByCostDouble("Chicago", "New York", -1));
        List<AirportInterface> expected = new ArrayList<>(Arrays.asList(flightFinder.getAirportObject("Chicago"), flightFinder.getAirportObject("New York")));
        assertEquals(expected, flightFinder.searchByCost("Chicago", "New York", -1));

        assertEquals(2010, flightFinder.searchByCostDouble("San Francisco", "Tokyo", -1));
        List<AirportInterface> expected2 = new ArrayList<>(Arrays.asList(flightFinder.getAirportObject("San Francisco"), flightFinder.getAirportObject("Amsterdam"), flightFinder.getAirportObject("Tokyo")));
        assertEquals(expected2, flightFinder.searchByCost("San Francisco", "Tokyo", -1));
    }

    /**
     * Tests integration by testing loading a file and searching for the cheapest flight between two airports with a max number of nodes
     */
    @Test
    void testIntegration1() {
        try {
            flightFinder.loadData("/Users/theoluo/IdeaProjects/P3W3/src/airportGraph.dot");
        }
        catch (FileNotFoundException e) {
            fail();
        }
        try {
            flightFinder.searchByCost("San Francisco", "Los Angeles", -1);
            fail();
        }
        catch (NoSuchElementException e) {}
        try {
            flightFinder.searchByCost("San Francisco", "Chicago", -2);
            fail();
        }
        catch (NoSuchElementException e) {}

        assertEquals(1230, flightFinder.searchByCostDouble("San Francisco", "Amsterdam", 2));
        List<AirportInterface> expected = new ArrayList<>(Arrays.asList(flightFinder.getAirportObject("San Francisco"), flightFinder.getAirportObject("Amsterdam")));
        assertEquals(expected, flightFinder.searchByCost("San Francisco", "Amsterdam", 2));

        assertEquals(2180, flightFinder.searchByCostDouble("Tokyo", "Liberia", 3));
        List<AirportInterface> expected2 = new ArrayList<>(Arrays.asList(flightFinder.getAirportObject("Tokyo"), flightFinder.getAirportObject("Amsterdam"), flightFinder.getAirportObject("Liberia")));
        assertEquals(expected2, flightFinder.searchByCost("Tokyo", "Liberia", 3));
    }

    /**
     * Tests integration by testing loading a file and searching for the cheapest airport to fly to in a given country
     */
    @Test
    void testIntegration2() {
        try {
            flightFinder.loadData("/Users/theoluo/IdeaProjects/P3W3/src/airportGraph.dot");
        }
        catch (FileNotFoundException e) {
            fail();
        }
        try {
            flightFinder.searchAirportCountry("Chicago", "China");
            fail();
        }
        catch (NoSuchElementException e) {}
        try {
            flightFinder.searchAirportCountry("Los Angeles", "Japan");
            fail();
        }
        catch (NoSuchElementException e) {}

        assertEquals(flightFinder.getAirportObject("Chicago"), flightFinder.searchAirportCountry("Tokyo", "USA"));
        assertEquals(flightFinder.getAirportObject("San Francisco"), flightFinder.searchAirportCountry("San Francisco", "USA"));
        assertEquals(flightFinder.getAirportObject("Amsterdam"), flightFinder.searchAirportCountry("San Francisco", "Netherlands"));
    }
}