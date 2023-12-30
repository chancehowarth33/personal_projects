// Author: Chance Howarth
// Email: howarthchance@gmail.com

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Backend implements BackendInterface {

    private DijkstraGraph<String, Double> routeGraph;
    private double totalDistance;
    private FrontendInterface frontend;

    public Backend(FrontendInterface frontendInterface) {
        this.totalDistance = 0.0;
        this.frontend = frontendInterface;
        this.routeGraph = new DijkstraGraph<>(new PlaceholderMap<>());
    }

    public void readData(String filePath) throws FileNotFoundException {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = fileReader.readLine()) != null) {
                Matcher routeMatcher = Pattern.compile("\"(\\w+)\"\\s*--\\s*\"(\\w+)\"\\s*\\[miles=(\\d+)\\]")
                        .matcher(currentLine);

                if (routeMatcher.find()) {
                    String origin = routeMatcher.group(1);
                    String destination = routeMatcher.group(2);
                    double distance = Double.parseDouble(routeMatcher.group(3));

                    if (!routeGraph.containsNode(origin)) {
                        routeGraph.insertNode(origin);
                    }
                    if (!routeGraph.containsNode(destination)) {
                        routeGraph.insertNode(destination);
                    }
                    routeGraph.insertEdge(origin, destination, distance);
                    totalDistance += distance;
                }
            }
        } catch (IOException e) {
            throw new FileNotFoundException("Unable to find file: " + filePath);
        }
    }

    public String getShortestRoute(String start, String end) {
        if (!routeGraph.containsNode(start) || !routeGraph.containsNode(end)) {
            throw new IllegalArgumentException("Invalid airport code.");
        }

        List<String> shortestRoute = routeGraph.shortestPathData(start, end);
        double routeDistance = routeGraph.shortestPathCost(start, end);

        StringBuilder routeStrBuilder = new StringBuilder();
        for (int i = 0; i < shortestRoute.size() - 1; i++) {
            String fromAirport = shortestRoute.get(i);
            String toAirport = shortestRoute.get(i + 1);

            double segmentDistance = routeGraph.getEdge(fromAirport, toAirport);
            routeStrBuilder.append(fromAirport).append(" (")
                           .append(segmentDistance).append(" miles) -> ");
        }
        routeStrBuilder.append(shortestRoute.get(shortestRoute.size() - 1))
                       .append(" (Total Miles: ").append(routeDistance).append(")");

        return routeStrBuilder.toString();
    }

    public String getSetStats() {
        int airports = routeGraph.getNodeCount();
        int flights = routeGraph.getEdgeCount();

        return "Total airports: " + airports + "\nTotal flights: " + flights +
               "\nTotal distance: " + this.totalDistance + " miles";
    }

    public void setFrontendInstance(FrontendInterface frontendInterface) {
        this.frontend = frontendInterface;
    }

    public DijkstraGraph<String, Double> getGraph() {
        return routeGraph;
    }
}
