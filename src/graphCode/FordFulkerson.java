package graphCode;

import javax.swing.plaf.IconUIResource;
import java.io.*;
import java.util.*;



// Define class and provide 'types' for containers
public class FordFulkerson {
    private SimpleGraph graph;
    private SimpleGraph residualGraph;
    private Vertex source;
    private Vertex sink;
    private Map<Edge, Double> flow;
    private Map<Edge, Edge> bindings;
    private Map<String, Edge> fedge_index;
    private Map<String, Edge> bedge_index;

    //Initialize the containers
    public FordFulkerson(SimpleGraph graph, Vertex source, Vertex sink) {
        this.graph = graph;
        this.residualGraph = new SimpleGraph();
        this.source = source;
        this.sink = sink;
        this.flow = new HashMap<>();
        this.bindings = new HashMap<>();
        this.fedge_index = new HashMap<>();
        this.bedge_index = new HashMap<>();

        List<Edge> originalEdges = new ArrayList<>();
        //Copy edges
        Iterator<Edge> edgeIterator = graph.edges();
        while (edgeIterator.hasNext()) {
            Edge e = edgeIterator.next();
            String v_pair = getVerticesPair(e);
            originalEdges.add(e);
            flow.put(e, 0.0);
            fedge_index.put(v_pair,e);

        }
        for (Edge currentEdge : originalEdges) {
            Vertex v1 = currentEdge.getFirstEndpoint();
            Vertex v2 = currentEdge.getSecondEndpoint();

            //Create backwards edge
            Edge backwardEdge = graph.insertEdge(v2, v1, 0, "back_" + currentEdge.getName());
            flow.put(backwardEdge, 0.0);
            //bind forwardEdge and backward together
            bindings.put(currentEdge,backwardEdge);
            bindings.put(backwardEdge,currentEdge);

            String v_pair = getVerticesPair(backwardEdge);
            bedge_index.put(v_pair,backwardEdge);
        }
    }

    private String getVerticesPair(Edge e){
        Vertex v1 = e.getFirstEndpoint();
        Vertex v2 = e.getSecondEndpoint();
        String v1_str = (String) v1.getName(); 
        String v2_str = (String) v2.getName(); 
        String v_pair = v1_str + "#" + v2_str;
        return v_pair;
    }

    private Edge findSameDirectionEdge(Edge e){
        String v_pair = getVerticesPair(e);
        if (isForwardEdge(e)){
            if (bedge_index.containsKey(v_pair)){
                Edge sameDirectionEdge = bedge_index.get(v_pair);
                return sameDirectionEdge;
            }
            else{
                return null;
            }
        }
        else{
            if (fedge_index.containsKey(v_pair)){
                Edge sameDirectionEdge = fedge_index.get(v_pair);
                return sameDirectionEdge;
            }
            else{
                return null;
            } 
        }
    }

    public double calculateMaxFlow(double delta) {
        //Main Method of algorithm that calculates the maximum flow of a
        //flow network
        //Input: None
        //Output: Maximum flow of the flow network

        //Store augmented path in path variable
        List<Edge> path = findAugmentingPath(delta);

        //While findAugmentingPath is not null
        while (path != null) {
            //Get bottleneck value
            double bottleneck = findBottleneck(path);
            //Augment flow of path
            augmentFlow(path, bottleneck);
            //Update path to become next path
            path = findAugmentingPath(delta);
        }


        //When no more paths are found the maximum flow will be
        //equal to the sum of all flows coming out of the source

        //Variable to store max flow
        double maxFlow = 0;

        //Accesses the specific LinkedList of edges belonging to the source
        Iterator<Edge> edgeIterator = graph.incidentEdges(source);

        while (edgeIterator.hasNext()) {
            //Store the next edge object
            Edge currentEdge = edgeIterator.next();
            //Check if the stored edge object is a forward edge
            if (isForwardEdge(currentEdge)) {
                maxFlow += flow.get(currentEdge);
            }
        }
        return maxFlow;
    }



    private List<Edge> findAugmentingPath(double delta) {
        Map<Vertex, Edge> parentEdges = new HashMap<>();  // Track which edge led to each vertex
        Set<Vertex> visited = new HashSet<>();
        Stack<Vertex> stack = new Stack<>();

        stack.push(source);
        visited.add(source);

        while (!stack.isEmpty()) {
            Vertex currentVertex = stack.pop();

            if (currentVertex.equals(sink)) {
                // Reconstruct the path from source to sink
                List<Edge> path = new ArrayList<>();
                Vertex current = sink;
                while (!current.equals(source)) {
                    Edge edge = parentEdges.get(current);
                    path.add(0, edge);  // Add to front of list to maintain order
                    current = graph.opposite(current, edge);
                }
                return path;
            }

            Iterator<Edge> currentVertexEdges = graph.incidentEdges(currentVertex);
            while (currentVertexEdges.hasNext()) {
                Edge currentEdge = currentVertexEdges.next();
                Vertex nextVertex = graph.opposite(currentVertex, currentEdge);
                // judge if path is in correct direction
                boolean validDirection = (currentVertex == currentEdge.getFirstEndpoint());
                // judge if path is a 0 backward
                boolean validPath = (delta == 0) ?
                        getResidualCapacity(currentEdge) > 0 :
                        getResidualCapacity(currentEdge) >= delta;
                if (!visited.contains(nextVertex) && validPath && validDirection) {
                    visited.add(nextVertex);
                    stack.push(nextVertex);
                    parentEdges.put(nextVertex, currentEdge);  // Remember which edge led to nextVertex
                }
            }
        }
        return null;
    }

    private double findBottleneck(List<Edge> path) {
        //Method to find the bottleneck
        //Input: Path found by findAugmentingPath method
        //Output: Bottleneck value of that path
        double bottleneck = Double.MAX_VALUE;

        for (Edge currentEdge : path) {
            double residualCapacity = getResidualCapacity(currentEdge);
            Edge sameDirectionEdge = findSameDirectionEdge(currentEdge);
            if (sameDirectionEdge!=null){
                residualCapacity += getResidualCapacity(sameDirectionEdge);
            }
            if (residualCapacity < bottleneck) {
                bottleneck = residualCapacity;
            }
        }

        return bottleneck;
    }

    private void augmentFlow(List<Edge> path, double bottleneck) {
        //Method to update the flow and capacities of path
        //Input: Path found by findAugmentingPath method, bottleneck value
        //Output: None

        for (int i = 0; i < path.size(); i++) {
            Edge currentEdge = path.get(i);
            double residualCapacity = getResidualCapacity(currentEdge);
            if (residualCapacity>=bottleneck){
                // same behavior for forward and backward edge in our definition background
                flow.put(currentEdge, flow.get(currentEdge) + bottleneck);
                Edge bindEdge = bindings.get(currentEdge);
                flow.put(bindEdge, flow.get(bindEdge) - bottleneck);
            }
            else{
                Edge sameDirectionEdge = findSameDirectionEdge(currentEdge);
                flow.put(currentEdge, flow.get(currentEdge) + residualCapacity);
                Edge bindEdge = bindings.get(currentEdge);
                flow.put(bindEdge, flow.get(bindEdge) - residualCapacity);

                flow.put(sameDirectionEdge, flow.get(sameDirectionEdge) + (bottleneck-residualCapacity));
                Edge bindEdge_sde = bindings.get(sameDirectionEdge);
                flow.put(bindEdge_sde, flow.get(bindEdge_sde) - (bottleneck-residualCapacity));
            }
        }

    }

    private double getResidualCapacity(Edge edge) {
        //Method to calculate the residual capacity of either
        //a forward edge or a backwards edge (remaining flow)
        //Input: A forward or backwards edge from an augmented path
        //Output: Residual capacity of the edge

        double capacity = Double.parseDouble(edge.getData().toString());
        double currentFlow = flow.get(edge);

        if (isForwardEdge(edge)) {
            return capacity - currentFlow;  // How much more can go forward
        } else {
            return -currentFlow;  // How much can go back
        }
    }

    private boolean isForwardEdge(Edge edge) {
        //Method to determine if the provided edge is the forward edge
        //Input: An edge from an augmented path
        //Output: True that edge is the forward edge, false otherwise

        return edge.getName() == null || !edge.getName().toString().startsWith("back");
    }
}
