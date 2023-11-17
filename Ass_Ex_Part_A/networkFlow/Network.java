package networkFlow;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.beans.VetoableChangeListener;
import java.util.*;

/**
 * The Class Network. Represents a network - inherits from DirectedGraph class.
 */
public class Network extends DirectedGraph {

    /**
     * The source vertex of the network.
     */
    protected Vertex source;

    /**
     * The label of the source vertex.
     */
    protected int sourceLabel;

    /**
     * The sink vertex of the network.
     */
    protected Vertex sink;

    /**
     * The label of the sink vertex.
     */
    protected int sinkLabel;

    /**
     * Instantiates a new network.
     *
     * @param n the number of vertices
     */
    public Network(int n) {
        super(n);

        // add the source vertex - assumed to have label 0
        sourceLabel = 0;
        source = addVertex(sourceLabel);
        // add the sink vertex - assumed to have label numVertices - 1
        sinkLabel = numVertices - 1;
        sink = addVertex(sinkLabel);

        // add the remaining vertices
        for (int i = 1; i <= numVertices - 2; i++) {
            addVertex(i);
        }
    }

    /**
     * Gets the source vertex.
     *
     * @return the source vertex
     */
    public Vertex getSource() {
        return source;
    }

    /**
     * Gets the sink vertex.
     *
     * @return the sink vertex
     */
    public Vertex getSink() {
        return sink;
    }

    /**
     * Adds the edge with specified source and target vertices and capacity.
     *
     * @param sourceEndpoint the source endpoint vertex
     * @param targetEndpoint the target endpoint vertex
     * @param capacity the capacity of the edge
     */
    public void addEdge(Vertex sourceEndpoint, Vertex targetEndpoint, int capacity) {
        Edge e = new Edge(sourceEndpoint, targetEndpoint, capacity);
        adjLists.get(sourceEndpoint.getLabel()).addLast(targetEndpoint);
        adjMatrix[sourceEndpoint.getLabel()][targetEndpoint.getLabel()] = e;
    }

    /**
     * Set the flow on a given edge. This does not, and should not, do any
     * checking for validity of the input flow.
     *
     * @param sourceEndpoint the source endpoint vertex
     * @param targetEndpoint the target endpoint vertex
     * @param flow the flow of the edge
     */
    public void setFlow(Vertex sourceEndpoint, Vertex targetEndpoint, int flow) {
        adjMatrix[sourceEndpoint.getLabel()][targetEndpoint.getLabel()].setFlow(flow);
//        Edge e = getAdjMatrixEntry(sourceEndpoint, targetEndpoint);
//        System.out.println(e.getFlow());
    }

    /**
     * Get the capacity along a given edge.
     *
     * @param sourceEndpoint the source endpoint vertex
     * @param targetEndpoint the target endpoint vertex
     * @return the capacity of the given edge
     */
    public int getEdgeCapacity(Vertex sourceEndpoint, Vertex targetEndpoint) {
        return adjMatrix[sourceEndpoint.getLabel()][targetEndpoint.getLabel()].getCap();
    }

    /**
     * Calculates by how much the flow along the given path can be increased,
     * and then augments the network along this path by this amount.
     *
     * @param path a list of edges along which the flow should be augmented
     */
    public void augmentPath(List<Edge> path) {
        // find min residual capacity value along the path
        int minResidualCap = Integer.MAX_VALUE;
        for (Edge edge : path) {
            minResidualCap = Math.min(minResidualCap, edge.getCap() - edge.getFlow());
        }

        // augment the flow along the path
        for (Edge edge : path) {
            Vertex u = edge.getSourceVertex();
            Vertex v = edge.getTargetVertex();
            Edge forwardEdge = this.getAdjMatrixEntry(u, v);

            if (forwardEdge != null) {
                // increase flow in forward direction
                setFlow(u, v, forwardEdge.getFlow() + minResidualCap);
            } else {
                // decrease flow in reverse if reverse edge exists
                Edge reverseEdge = this.getAdjMatrixEntry(v, u);
                if (reverseEdge != null) {
                    setFlow(v, u, reverseEdge.getFlow() - minResidualCap);
                }
            }
        }
    }

    /**
     * Returns true if and only if the assignment of integers to the flow fields
     * of each edge in the network is a valid flow.
     *
     * @return true, if the assignment is a valid flow
     */
    public boolean isFlow() {
        // complete this method as part of Task 1
        int n = getNumVertices();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Edge edge = getAdjMatrixEntry(getVertexByIndex(i), getVertexByIndex(j));
                if (edge != null && edge.getFlow() > edge.getCap()) {
                    return false;   // flow exceeds capacity
                }
            }
        }

        return true;
    }

    /**
     * Gets the value of the flow.
     *
     * @return the value of the flow
     */
    public int getValue() {
        // complete this method as part of Task
        Vertex source = getVertexByIndex(0);
        Vertex sink = getVertexByIndex(getNumVertices()-1);

        boolean[] visited = new boolean[getNumVertices()];
        int maxFlow = dfs(source, sink, visited, 0);

        return maxFlow;
    }

    private int dfs(Vertex current, Vertex sink, boolean[] visited, int maxFlow) {

        // set the current vertex's visited value to true
        visited[current.getLabel()] = true;

        for (Vertex v : getAdjList(current)) {
           if (!visited[v.getLabel()]) {
               Edge edge = getAdjMatrixEntry(current, v);
               if (edge != null) {
                   int edgeFlow = edge.getFlow();
//                   System.out.println(edge.getCap());
//                   System.out.println(edge.getFlow());
                   maxFlow += edgeFlow + dfs(v, sink, visited, maxFlow);
               }
           }
        }

        // reset current visited to false for backtracking
        visited[current.getLabel()] = false;
        return maxFlow;
    }

    /**
     * Prints the flow. Display the flow through the network in the following
     * format: (u,v) c(u,v)/f(u,v) where (u,v) is an edge, c(u,v) is the
     * capacity of that edge and f(u,v) is the flow through that edge - one line
     * for each edge in the network
     */
    public void printFlow() {
        // complete this method as part of Task 1
        int n = getNumVertices();

        // iterate over all vertices in network
        for (int u = 0; u < n; u++) {
            // iterate over all target vertices in network
            for (int v = 0; v < n; v++) {
                Edge edge = getAdjMatrixEntry(getVertexByIndex(u), getVertexByIndex(v));
                // check if there is an edge, print details
                if (edge != null) {
                    System.out.println("(" + u + "," + v + ") " + edge.getCap() + "/" + edge.getFlow());
                }
            }
        }
    }
}
