package graphCode;

import javax.swing.plaf.IconUIResource;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Stack;


public class ScalingFordFulkerson {
    private SimpleGraph graph;
    private Vertex source;
    private Vertex sink;

    public ScalingFordFulkerson(SimpleGraph graph, Vertex source, Vertex sink) {
        this.graph = graph;
        this.source = source;
        this.sink = sink;
    }

    public double calculateMaxFlow(double unusedDelta) {  // parameter added to match FordFulkerson interface
        Iterator<Edge> edgeList = graph.edges();
        double maxCapacity = findMaxCapacity(edgeList);
        return scalingWithDelta(maxCapacity);
    }

    private double findMaxCapacity(Iterator edgeData) {
        double maxCapacity = 0;
        for (Iterator i = edgeData; i.hasNext(); ) {
            Edge edge = (Edge) i.next();
            double capacity = Double.parseDouble(edge.getData().toString());  // Changed to Double.parseDouble
            if (capacity > maxCapacity) {
                maxCapacity = capacity;
            }
        }
        return maxCapacity;
    }

    private double scalingWithDelta(double maxCapacity) {
        FordFulkerson instance = new FordFulkerson(graph, source, sink);
        double delta = 1;
        double scaledMaxFlow = 0;

        // Find the largest power of 2 not larger than maxCapacity
        while (delta <= maxCapacity) {
            delta *= 2;
        }
        delta /= 2;  // Go back one step since we went over

        // Scale down delta until it's less than 1
        while (delta >= 1) {
            scaledMaxFlow = instance.calculateMaxFlow(delta);  // Remove += as we want the final maxflow
            delta /= 2;
        }
        return scaledMaxFlow;
    }
}

