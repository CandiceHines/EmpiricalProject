//package graphCode;
//
//import graphCode.Vertex;
//import javax.swing.plaf.IconUIResource;
//import java.io.*;
//import java.lang.reflect.Type;
//import java.util.*;
//import java.util.Stack;
//
//public class PreFlowPush {
//    private SimpleGraph graph;
//    private Vertex source;
//    private Vertex sink;
//    private Map<Vertex, Integer> potentialLabel = new HashMap<>();
//    private Map<Vertex, Integer> flowMap = new HashMap<>();
//
//    public PreFlowPush () {
//        this.graph = graph;
//        this.source = source;
//        this.sink = sink;
//        this.potentialLabel = new HashMap<>();
//        this.flowMap = new HashMap<>();
//    }
//
//    public int preFlowPushAlgo() {
//        //Label all nodes
//        int height = graph.numVertices();
//        for (Object vertex: graph.vertexList) {
//            if (vertex.equals(source)) {
//                //Source height = the num of vertices
//                potentialLabel.put(vertex, height);
//            } else {
//                potentialLabel.put(vertex, 0);
//            }
//        }
//
//        //Start with preflow
//        //Iterate through the incidentEdges and update the flowMap
//        //with each edges preflow
//
//        for (Object vertex: graph.vertexList) {
//            Iterator<Edge> edgeIterator = graph.incidentEdges(vertex);
//
//
//        }
//
//
//        v = edge.getFirstEndpoint();
//        w = edge.getSecondEndpout();
//    }
//
//    public void Push(Vertex v, Vertex w) {
//
//    }
//
//    public void Relabel(Vertex v) {
//
//    }

//}
