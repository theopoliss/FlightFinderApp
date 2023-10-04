import java.util.*;

/**
 * FlightFinderGraph contains a set of nodes of airports, along with a set of
 * directed and weighted edges connecting these nodes.
 */
public class FlightFinderGraphADTAE implements FlightFinderGraphADTInterface {
    protected class Node {
        public AirportInterface data;
        public List<FlightFinderGraphADTAE.Edge> edgesLeaving = new LinkedList<>();
        public List<FlightFinderGraphADTAE.Edge> edgesEntering = new LinkedList<>();
        public Node(AirportInterface data) { this.data = data; }
    }
    // Nodes can be retrieved from this hashtable by their unique data
    protected Hashtable<AirportInterface, FlightFinderGraphADTAE.Node> nodes = new Hashtable();

    // Each edge contains data/weight, and two nodes that it connects
    protected class Edge {
        public CostInterface data; // the weight or cost of this edge
        public FlightFinderGraphADTAE.Node predecessor;
        public FlightFinderGraphADTAE.Node successor;
        public Edge(CostInterface data, FlightFinderGraphADTAE.Node pred, FlightFinderGraphADTAE.Node succ) {
            this.data = data;
            this.predecessor = pred;
            this.successor = succ;
        }
    }
    protected int edgeCount = 0;

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph.  The final node in this path is stored in its node
     * field.  The total cost of this path is stored in its cost field.  And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<FlightFinderGraphADTAE.SearchNode> {
        public FlightFinderGraphADTAE.Node node;
        public double cost;
        public FlightFinderGraphADTAE.SearchNode predecessor;
        public SearchNode(FlightFinderGraphADTAE.Node node, double cost, FlightFinderGraphADTAE.SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }
        @Override
        public String toString() {
            if (node != null && predecessor != null) {
                return predecessor.node.data + "-->(" + cost + ") " + node.data;
            }
            return "null";
        }
        @Override
        public int compareTo(SearchNode other) {
            if( cost > other.cost ) return +1;
            if( cost < other.cost ) return -1;
            return 0;
        }
    }


    /**
     * Insert a new node into the graph.
     *
     * @param data is the data item stored in the new node
     * @return true if the data is unique and can be inserted into a new node,
     *         or false if this data is already in the graph
     * @throws NullPointerException if data is null
     */
    @Override
    public boolean insertNode(AirportInterface data) {
        if(nodes.containsKey(data)) return false; // throws NPE when data's null
        nodes.put(data, new FlightFinderGraphADTAE.Node(data));
        return true;
    }

    /**
     * Remove a node from the graph.
     * And also remove all edges adjacent to that node.
     *
     * @param data is the data item stored in the node to be removed
     * @return true if a vertex with data is found and removed, or
     *         false if that data value is not found in the graph
     * @throws NullPointerException if data is null
     */
    @Override
    public boolean removeNode(AirportInterface data) {
        // remove this node from nodes collection
        if(!nodes.containsKey(data)) return false; // throws NPE when data==null
        FlightFinderGraphADTAE.Node oldNode = nodes.remove(data);
        // remove all edges entering neighboring nodes from this one
        for(FlightFinderGraphADTAE.Edge edge : oldNode.edgesLeaving)
            edge.successor.edgesEntering.remove(edge);
        // remove all edges leaving neighboring nodes toward this one
        for(FlightFinderGraphADTAE.Edge edge : oldNode.edgesEntering)
            edge.predecessor.edgesLeaving.remove(edge);
        return true;
    }

    /**
     * Check whether the graph contains a node with the provided data.
     *
     * @param data the node contents to check for
     * @return true if data item is stored in a node within the graph, or
    false otherwise
     */
    @Override
    public boolean containsNode(AirportInterface data) {
        return nodes.containsKey(data);
    }

    /**
     * Return the number of nodes in the graph
     *
     * @return the number of nodes in the graph
     */
    @Override
    public int getNodeCount() {
        return nodes.size();
    }

    /**
     * Insert a new directed edge with positive edges weight into the graph.
     * Or if an edge between pred and succ already exists, update the data
     * stored in that edge to be weight.
     *
     * @param pred is the data item contained in the new edge's predecesor node
     * @param succ is the data item contained in the new edge's successor node
     * @param weight is the non-negative data item stored in the new edge
     * @return true if the edge could be inserted or updated, or
     *         false if the pred or succ data are not found in any graph nodes
     */
    @Override
    public boolean insertEdge(AirportInterface pred, AirportInterface succ, CostInterface weight) {
        // find nodes associated with node data, and return false when not found
        FlightFinderGraphADTAE.Node predNode = nodes.get(pred);
        FlightFinderGraphADTAE.Node succNode = nodes.get(succ);
        if(predNode == null || succNode == null) return false;
        try {
            // when an edge already exists within the graph, update its weight
            FlightFinderGraphADTAE.Edge existingEdge = getEdgeHelper(pred,succ);
            existingEdge.data = weight;
        } catch(NoSuchElementException e) {
            // otherwise create a new edges
            FlightFinderGraphADTAE.Edge newEdge = new FlightFinderGraphADTAE.Edge(weight,predNode,succNode);
            this.edgeCount++;
            // and insert it into each of its adjacent nodes' respective lists
            predNode.edgesLeaving.add(newEdge);
            succNode.edgesEntering.add(newEdge);
        }
        return true;
    }

    /**
     * Remove an edge from the graph.
     *
     * @param pred the data item contained in the source node for the edge
     * @param succ the data item contained in the target node for the edge
     * @return true if the edge could be removed, or
     *         false if such an edge is not found in the graph
     */
    @Override
    public boolean removeEdge(AirportInterface pred, AirportInterface succ) {
        try {
            // when an edge exists
            FlightFinderGraphADTAE.Edge oldEdge = getEdgeHelper(pred,succ);
            // remove it from the edge lists of each adjacent node
            oldEdge.predecessor.edgesLeaving.remove(oldEdge);
            oldEdge.successor.edgesEntering.remove(oldEdge);
            // and decrement the edge count before removing
            this.edgeCount--;
            return true;
        } catch(NoSuchElementException e) {
            // when no such edge exists, return false instead
            return false;
        }
    }

    /**
     * Check if edge is in the graph.
     *
     * @param pred the data item contained in the source node for the edge
     * @param succ the data item contained in the target node for the edge
     * @return true if the edge is found in the graph, or false other
     */
    @Override
    public boolean containsEdge(AirportInterface pred, AirportInterface succ) {
        try { getEdgeHelper(pred,succ); return true; }
        catch(NoSuchElementException e) { return false; }
    }

    /**
     * Return the data associated with a specific edge.
     *
     * @param pred the data item contained in the source node for the edge
     * @param succ the data item contained in the target node for the edge
     * @return the non-negative data from the edge between those nodes
     * @throws NoSuchElementException if either node or the edge between them
     *         are not found within this graph
     */
    @Override
    public CostInterface getEdge(AirportInterface pred, AirportInterface succ) {
        return getEdgeHelper(pred,succ).data;
    }

    protected FlightFinderGraphADTAE.Edge getEdgeHelper(AirportInterface pred, AirportInterface succ) {
        FlightFinderGraphADTAE.Node predNode = nodes.get(pred);
        // search for edge through the predecessor's list of leaving edges
        for(FlightFinderGraphADTAE.Edge edge : predNode.edgesLeaving)
            // compare succ to the data in each leaving edge's successor
            if(edge.successor.data.equals(succ))
                return edge;
        // when no such edge can be found, throw NSE
        throw new NoSuchElementException("No edge from "+pred.toString()+" to "+
                succ.toString());
    }

    /**
     * Return the number of edges in the graph.
     *
     * @return the number of edges in the graph
     */
    @Override
    public int getEdgeCount() {
        return this.edgeCount;
    }
    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations.  The
     * SearchNode that is returned by this method represents the end of the
     * shortest path that is found: its cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *         or when either start or end data do not correspond to a graph node
     */
    protected FlightFinderGraphADTAE.SearchNode computeShortestPath(AirportInterface start, AirportInterface end) throws NoSuchElementException {
        if (!containsNode(start) || !containsNode(end)) {
            throw new NoSuchElementException("Start or end data does not correspond to a graph node");
        }
        double cost = 0;
        FlightFinderGraphADTAE.SearchNode predecessor;

        Hashtable<AirportInterface, FlightFinderGraphADTAE.Node> visitedNodes = new Hashtable();
        PriorityQueue<FlightFinderGraphADTAE.SearchNode> pq = new PriorityQueue<FlightFinderGraphADTAE.SearchNode>();
        FlightFinderGraphADTAE.SearchNode startSearchNode = new FlightFinderGraphADTAE.SearchNode(nodes.get(start), 0, null);
        pq.add(startSearchNode);

        while (!pq.isEmpty()) {
            FlightFinderGraphADTAE.SearchNode currentNode = pq.remove();
            if (!visitedNodes.containsKey(currentNode.node.data)) {
                visitedNodes.put(currentNode.node.data, currentNode.node); // mark node as visited
                predecessor = currentNode.predecessor;
                cost = currentNode.cost;
                for (FlightFinderGraphADTAE.Edge edge : currentNode.node.edgesLeaving) {
                    FlightFinderGraphADTAE.SearchNode unvisitedSuccessor = new FlightFinderGraphADTAE.SearchNode(edge.successor, cost+edge.data.getCost(), currentNode);
                    pq.add(unvisitedSuccessor);
                }
                if (currentNode.node.data.equals(end)) {
                    return currentNode;
                }
            }
        }
        throw new NoSuchElementException("No path from start to end was found");
    }
    @Override
    public List<AirportInterface> shortestPathData(AirportInterface start, AirportInterface end) {
        List<AirportInterface> shortestPath = new ArrayList<AirportInterface>();
        List<AirportInterface> toReturn = new ArrayList<AirportInterface>();
        FlightFinderGraphADTAE.SearchNode endNode = computeShortestPath(start, end);
        addNodeToList(shortestPath, endNode);
        for (int i = shortestPath.size() - 1; i >= 0; i--) {
            toReturn.add(shortestPath.get(i));
        }
        return toReturn;
    }
    @Override
    public double shortestPathCost(AirportInterface start, AirportInterface end) {
        return computeShortestPath(start, end).cost;
    }
    /**
     * Helper method that recursively adds nodes to the List of nodes representing the path
     * @param path the List of nodes that represents the path
     * @param node the SearchNode that is at the end of the path (and will be added to the path)
     */
    private void addNodeToList(List<AirportInterface> path, FlightFinderGraphADTAE.SearchNode node) {
        if (node == null) {
            return;
        }
        path.add(node.node.data);
        addNodeToList(path, node.predecessor);
    }

    /**
     * Finds the airport in the given country that has the shortest path from the start airport
     * @param start the airport to start at
     * @param country the country to end up in
     * @return the airport in the given country with the shortest path
     */
    @Override
    public AirportInterface getAirportByPlace(AirportInterface start, String country) {
        // Create a Set of airports and remove the ones that are not in the given country
        List<AirportInterface> airportsInCountry = new ArrayList<>();
        Set<AirportInterface> airports = nodes.keySet();
        for (AirportInterface airport : airports) {
            if (airport.getCountry().equals(country)) {
                airportsInCountry.add(airport);
            }
        }
        if (airportsInCountry.size() == 0) {
            throw new NoSuchElementException("No airports exist in the country: " + country);
        }
        // Find which airport in the given country has the shortest path from the start
        AirportInterface mostEfficient = null;
        Double min = Double.MAX_VALUE;
        Double currentCost;
        for (AirportInterface airport : airportsInCountry) {
            currentCost = shortestPathCost(start, airport);
            if (currentCost < min) {
                min = currentCost;
                mostEfficient = airport;
            }
        }
        return mostEfficient;
    }

    /**
     * Private helper method that returns all the possible paths from one airport to another in a Hashtable
     * that uses Lists of airports as keys and doubles as values by calling findAllPathsHelper
     * @param start the airport to start at
     * @param end the destination airport
     * @return a Hashtable of all the paths from one airport to another with its cost
     */
    private Hashtable<List<AirportInterface>, Double> findAllPaths(AirportInterface start, AirportInterface end) {
        if (!containsNode(start) || !containsNode(end)) {
            throw new NoSuchElementException("Start or end data does not correspond to a graph node");
        }
        Double cost = 0.0;
        List<Double> costs = new ArrayList<>();
        Hashtable<AirportInterface, FlightFinderGraphADTAE.Node> visitedNodes = new Hashtable<>();
        List<AirportInterface> localPathList = new ArrayList<>();
        localPathList.add(start);
        Hashtable<List<AirportInterface>, Double> allPaths = new Hashtable<>();
        // call helper method
        findAllPathsHelper(start, end, cost, visitedNodes, localPathList, allPaths);
        return allPaths;
    }

    /**
     * Private helper method that finds all paths between two airports
     * @param start the airport to start at
     * @param end the destination airport
     * @param cost the cost of a path
     * @param visitedNodes a Hashtable that keeps track of visited nodes in the graph
     * @param localPathList a possible path in the graph
     * @param allPaths all paths from the start airport to the end airport
     */
    private void findAllPathsHelper(AirportInterface  start, AirportInterface end, Double cost, Hashtable<AirportInterface, FlightFinderGraphADTAE.Node> visitedNodes, List<AirportInterface> localPathList, Hashtable<List<AirportInterface>, Double> allPaths) {
        visitedNodes.put(start, nodes.get(start));
        if (start.equals(end)) {
            allPaths.put(new ArrayList<AirportInterface>(localPathList), cost);
        }
        else {
            for (Edge edge : nodes.get(start).edgesLeaving) {
                Node currNode = edge.successor;
                if (!visitedNodes.containsKey(currNode.data)) {
                    localPathList.add(currNode.data);
                    cost += edge.data.getCost();
                    findAllPathsHelper(currNode.data, end, cost, visitedNodes, localPathList, allPaths);
                    localPathList.remove(currNode.data);
                    cost -= edge.data.getCost();
                }
            }
        }
        visitedNodes.remove(start, nodes.get(start));
    }

    /**
     * Returns a List of the shortest path between two nodes that traverses through a specific number of nodes
     * @param start the airport to start at
     * @param end the airport to end at
     * @param nodeNum the number of nodes to traverse through
     * @return a List of the shortest path
     */
    @Override
    public List<AirportInterface> shortestPathWithMaxNodes(AirportInterface start, AirportInterface end, int nodeNum) {
        Hashtable<List<AirportInterface>, Double> allPaths = findAllPaths(start, end);
        Hashtable<List<AirportInterface>, Double> validPaths = new Hashtable<>();
        List<AirportInterface> shortestPath = null;
        Double minCost = Double.MAX_VALUE;

        Set<List<AirportInterface>> set = allPaths.keySet();

        for (List<AirportInterface> path : set) {
            if (path.size() <= nodeNum) {
                validPaths.put(path, allPaths.get(path));
            }
        }
        if (validPaths.isEmpty()) {
            throw new NoSuchElementException("manual exception");
        }

        Set<List<AirportInterface>> validSet = validPaths.keySet();

        for (List<AirportInterface> path : validSet) {
            if (validPaths.get(path) < minCost) {
                shortestPath = path;
                minCost = validPaths.get(path);
            }
        }
        return shortestPath;
    }

    /**
     * Returns the cost of the shortest path between two nodes that traverses through a specific number of nodes
     * @param start the airport to start at
     * @param end the airport to end at
     * @param nodeNum the number of nodes to traverse through
     * @return the cost of the shortest path
     */
    @Override
    public double shortestCostWithMaxNodes(AirportInterface start, AirportInterface end, int nodeNum) {
        Hashtable<List<AirportInterface>, Double> allPaths = findAllPaths(start, end);
        Hashtable<List<AirportInterface>, Double> validPaths = new Hashtable<>();
        Double minCost = Double.MAX_VALUE;

        Set<List<AirportInterface>> set = allPaths.keySet();

        for (List<AirportInterface> path : set) {
            if (path.size() == nodeNum) {
                validPaths.put(path, allPaths.get(path));
            }
        }
        if (validPaths.isEmpty()) {
            throw new NoSuchElementException("manual exception");
        }

        Set<List<AirportInterface>> validSet = validPaths.keySet();

        for (List<AirportInterface> path : validSet) {
            minCost = Math.min(minCost, validPaths.get(path));
        }
        return minCost;
    }
}
