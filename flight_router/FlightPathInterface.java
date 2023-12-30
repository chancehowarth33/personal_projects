import java.util.List;

public interface FlightPathInterface {
    
    
    // List<String> route;
    // List<Double> segments;
    // double totMiles;

    // public FlightPath(GraphADT<String, Double> graph, String start, String end){
        
    //     this.totMiles = graph.shortestPathCost(start, end);
    //     this.route = graph.shortestPathData(start, end);
    //     this.segments = new ArrayList<>();
    //     for(int i=1;i<this.route.size();i++){
    //         this.segments.add(graph.getEdge(this.route.get(i-1), this.route.get(i)));
    //     }

    // }


    /**
     * getter method to get the list of airports alone the route
     * @return list of string referred as airports
     */
    public List<String> getRoute();

    /**
     * getter method to get a list of miles to travel for each segments of the route
     * @return list of double referred as miles
     */
    public List<Double> getSegments();

    /**
     * getter method to get the total miles to travel
     * @return double that referred as miles
     */
    public double getTotalMiles();
}

