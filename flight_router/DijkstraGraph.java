// --== CS400 File Header Information ==--
// Name: Chance Howarth
// Email: crhowarth@wisc.edu
// Group and Team: G17
// Group TA: Robert Nagel
// Lecturer: Florian
// Notes to Grader: N/A


import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referenced by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     * @param map the map that the graph uses to map a data object to the node
     *        object it is stored in
     */
    public DijkstraGraph(MapADT<NodeType, Node> map) {
        super(map);
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */

    protected SearchNode computeShortestPath(NodeType start, NodeType end) throws NoSuchElementException {
        if (!nodes.containsKey(start) || !nodes.containsKey(end)) {
            throw new NoSuchElementException("Start or end node not found");
        }

        PriorityQueue<SearchNode> pQueue = new PriorityQueue<>();
        PlaceholderMap<NodeType, SearchNode> searchedNodes = new PlaceholderMap<>();
        SearchNode startNode = new SearchNode(nodes.get(start), 0, null);
        pQueue.add(startNode);

        while (!pQueue.isEmpty()) {
            SearchNode currentNode = pQueue.poll();

            // Check if we reached the end node
            if (currentNode.node.data.equals(end)) {
                return currentNode;
            }

            // Avoid reprocessing nodes
            if (searchedNodes.containsKey(currentNode.node.data) &&
                    searchedNodes.get(currentNode.node.data).cost <= currentNode.cost) {
                continue;
            }
            searchedNodes.put(currentNode.node.data, currentNode);

            // Process each edge using a regular for loop
            for (int i = 0; i < currentNode.node.edgesLeaving.size(); i++) {
                Edge edge = currentNode.node.edgesLeaving.get(i);
                Node nextNode = edge.successor;
                double totalCost = currentNode.cost + edge.data.doubleValue();

                if (!searchedNodes.containsKey(nextNode.data) ||
                        searchedNodes.get(nextNode.data).cost > totalCost) {
                    SearchNode nextSearchNode = new SearchNode(nextNode, totalCost, currentNode);
                    pQueue.add(nextSearchNode);
                }
            }
        }

        throw new NoSuchElementException("No path available");
    }


    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shorteset path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        SearchNode endNode = computeShortestPath(start, end);
        LinkedList<NodeType> nodePath = new LinkedList<>();
        for (SearchNode temporaryNode = endNode; temporaryNode != null; temporaryNode = temporaryNode.predecessor) {
            nodePath.addFirst(temporaryNode.node.data);
        }
        return nodePath;
    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        SearchNode endNode = computeShortestPath(start, end);
        return endNode.cost;
    }

    //@Test
    //void testShortestPathData(){

        // create tree
        //DijkstraGraph<String, Integer> testGraph = new DijkstraGraph<>(new PlaceholderMap<>());

        //DijkstraGraph<String, Integer> testtree = new DijkstraGraph<>(new PlaceholderMap<>());

        // add Nodes
        //testtree.insertNode("A");
        //testtree.insertNode("B");
        //testtree.insertNode("C");
        //testtree.insertNode("D");
        //testtree.insertNode("E");

        // insert edge values
        //testtree.insertEdge("A", "B", 3);
        //testtree.insertEdge("A", "C", 6);
        //testtree.insertEdge("B", "D", 4);
        //testtree.insertEdge("C", "D", 2);
        //testtree.insertEdge("C", "E", 5);
        //testtree.insertEdge("D", "E", 1);

        //List<String> expectedPath = List.of("A", "B", "D", "E");

        // test shortestPathData method
        //List<String> actualPath = testtree.shortestPathData("A", "E");

        //assertEquals(expectedPath, actualPath);
    //}

    //@Test
    //void testShortestPathCost(){

        // create tree
        //DijkstraGraph<String, Integer> testtree = new DijkstraGraph<>(new PlaceholderMap<>());

        // add Nodes
        //testtree.insertNode("A");
        //testtree.insertNode("B");
        //testtree.insertNode("C");
        //testtree.insertNode("D");
        //testtree.insertNode("E");

        // insert edge values
        //testtree.insertEdge("A", "B", 3);
        //testtree.insertEdge("A", "C", 6);
        //testtree.insertEdge("B", "D", 4);
        //testtree.insertEdge("C", "D", 2);
        //testtree.insertEdge("C", "E", 5);
        //testtree.insertEdge("D", "E", 1);

        //double expectedCost = 8;

        // test shortestPathCost method
        //double actualCost = testtree.shortestPathCost("A", "E");

        //assertEquals(expectedCost, actualCost);
    //}

    //@Test
    //void testComputeShortestPath(){

        // create tree
        //DijkstraGraph<String, Integer> testtree = new DijkstraGraph<>(new PlaceholderMap<>());

        // add Nodes
        //testtree.insertNode("A");
        //testtree.insertNode("B");
        //testtree.insertNode("C");
        //testtree.insertNode("D");
        //testtree.insertNode("E");

        // insert edge values
        //testtree.insertEdge("A", "B", 3);
        //testtree.insertEdge("A", "C", 6);
        //testtree.insertEdge("B", "D", 4);
        //testtree.insertEdge("C", "D", 2);
        //testtree.insertEdge("C", "E", 5);
        //testtree.insertEdge("D", "E", 1);

        //double expectedCost = 8;

        //DijkstraGraph<String, Integer>.SearchNode endNode = testtree.computeShortestPath("A", "E");

        //assertEquals(expectedCost, endNode.cost);
    //}

}
