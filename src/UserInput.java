

import java.util.Hashtable;
import graphCode.GraphInput;
import graphCode.Vertex;
import graphCode.SimpleGraph;
import graphCode.FordFulkerson;
import graphCode.ScalingFordFulkerson;

public class UserInput {


    public static void main(String[] args) {

        //Call GraphBuilder.RandomGraph to generate graph
        BuildGraph build = new BuildGraph();


        // Combine directory and filename for full path
        String fullPath = "/workspaces/EmpiricalProject/TestCases/RandomGraph-200-4-5-10.txt";



        SimpleGraph newGraph = new SimpleGraph();

        Hashtable<String, Vertex> vertices;
        vertices = GraphInput.LoadSimpleGraph(newGraph, fullPath);

        Vertex source = vertices.get("s");
        Vertex sink = vertices.get("t");
        // Get source and sink vertices from the hashtable
        if (source != null && sink != null) {
            long startTime = System.nanoTime();
            FordFulkerson fordFulkerson = new FordFulkerson(newGraph, source, sink);
            double regularMaxFlow = fordFulkerson.calculateMaxFlow(0);
            long endTime = System.nanoTime();
            long regularDuration = (endTime - startTime) / 1_000_000;

            // Create new graph for scaling version
            SimpleGraph scalingGraph = new SimpleGraph();
            Hashtable<String, Vertex> scalingVertices = GraphInput.LoadSimpleGraph(scalingGraph, fullPath);
            Vertex scalingSource = scalingVertices.get("s");
            Vertex scalingSink = scalingVertices.get("t");

            // Run Scaling Ford-Fulkerson
            startTime = System.nanoTime();
            ScalingFordFulkerson scalingFF = new ScalingFordFulkerson(scalingGraph, scalingSource, scalingSink);
            double scaledMaxFlow = scalingFF.calculateMaxFlow(0);
            endTime = System.nanoTime();
            long scaledDuration = (endTime - startTime) / 1_000_000;


            // Print results
            System.out.println("\nResults for graph: " + fullPath);
            System.out.println("----------------------------------------");
            System.out.println("Regular Ford-Fulkerson:");
            System.out.println("  Max Flow: " + regularMaxFlow);
            System.out.println("  Time: " + regularDuration + "ms");
            System.out.println("\nScaling Ford-Fulkerson:");
            System.out.println("  Max Flow: " + scaledMaxFlow);
            System.out.println("  Time: " + scaledDuration + "ms");
        }
    }

    static String getUserInput(String prompt) {
        System.out.print(prompt);
        return System.console().readLine();
    }
}
