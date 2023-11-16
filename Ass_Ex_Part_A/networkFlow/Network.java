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
        // complete this method as part of Task 2
    }

    /**
     * Returns true if and only if the assignment of integers to the flow fields
     * of each edge in the network is a valid flow.
     *
     * @return true, if the assignment is a valid flow
     */
    public boolean isFlow() {
        // complete this method as part of Task 1
        int n = getNumVertices()-1;
        Vertex s = getSink();
        Vertex t = getSource();

        System.out.println(s);
        System.out.println(t+"\n");

        // for each vertex, check if it has an adjacent vertex
        // if it does they make an edge, get the flow of the edge
        // and if edge flow <= edge capacity

        for (int idx=0; idx<=n; idx++){
            Vertex vv = getVertexByIndex(idx);
            if (vv != s && vv != t) {
                LinkedList adj = getAdjList(vv));
                Edge e = new Edge(vv, adj.toArray()[0])
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
        return 0;
    }

    /**
     * Prints the flow. Display the flow through the network in the following
     * format: (u,v) c(u,v)/f(u,v) where (u,v) is an edge, c(u,v) is the
     * capacity of that edge and f(u,v) is the flow through that edge - one line
     * for each edge in the network
     */
    public void printFlow() {
        // complete this method as part of Task 1
    }
}
