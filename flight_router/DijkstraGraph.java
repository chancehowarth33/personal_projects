// Author: Chance Howarth
// Email: howarthchance@gmail.com


import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;


public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {


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
     * shortest path between the provided start and end locations.
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
     * provided end value.
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
     * end data.
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
