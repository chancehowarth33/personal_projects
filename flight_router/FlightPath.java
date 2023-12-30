// Author: Chance Howarth
// Email: howarthchance@gmail.com

import java.util.LinkedList;
import java.util.List;


public class FlightPath implements FlightPathInterface {

    LinkedList<String> route;
    LinkedList<Double> segments;
    double totMiles;

    public FlightPath(GraphADT<String, Double> graph, String start, String end){

        this.totMiles = graph.shortestPathCost(start, end);
        this.route = (LinkedList<String>) graph.shortestPathData(start, end);
        this.segments = new LinkedList<Double>();
        for(int i=1;i<this.route.size();i++){
            this.segments.add(graph.getEdge(this.route.get(i-1), this.route.get(i)));
        }

    }

    /**
     * Getter method for a linked list of the Flight Path
     * @return LinkedList<String> containing the flight path
     */
    @Override
    public List<String> getRoute() {
        return this.route;
    }

    /**
     * Getter method for the numerical segments dividing each leg of the flightpath
     * @return LinkedList<Double> containing the segments dividing each leg
     */
    @Override
    public List<Double> getSegments() {
        return this.segments;
    }

    /**
     * Getter method for the total miles in a flightpath
     * @return Double for number of total miles
     */
    @Override
    public double getTotalMiles() {
        return this.totMiles;
    }

}

