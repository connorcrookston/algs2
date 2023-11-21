package networkFlow;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.awt.desktop.SystemEventListener;
import java.util.*;

/**
 * The Class ResidualGraph. Represents the residual graph corresponding to a
 * given network.
 */
public class ResidualGraph extends Network {

    /**
     * Instantiates a new ResidualGraph object. Builds the residual graph
     * corresponding to the given network net. Residual graph has the same
     * number of vertices as net.
     *
     * @param net the network
     */
    public ResidualGraph(Network net) {
        super(net.numVertices);
        // complete this constructor as part of Task 2

        for (int i = 0; i < net.numVertices; i++) {
            this.vertices[i] = net.getVertexByIndex(i);
        }

        // iterate over all vertices in network
        for (int u = 0; u < net.numVertices; u++) {
            // iterate over all adjacent vertices
            for (Vertex v : net.getAdjList((net.getVertexByIndex(u)))) {
                Edge edge = net.getAdjMatrixEntry(net.getVertexByIndex(u), v);
                int resCap = edge.getCap() - edge.getFlow();

                // add forward edge if resCap > 0
                if (resCap > 0) {
                    this.addEdge(net.getVertexByIndex(u), v, resCap);
//                    System.out.println("Adding forward edge: " + net.getVertexByIndex(u) + " - > " + v + " resCap= " + resCap);
                }

                // add reverse edge if flow > 0
                if (edge.getFlow() > 0) {
                    this.addEdge(v, net.getVertexByIndex(u), edge.getFlow());
//                    System.out.println("Adding reverse edge: " + v + " - > " + net.getVertexByIndex(u) + " resCap= " + edge.getFlow());
                }
            }
        }
    }

    /**
     * Find an augmenting path if one exists. Determines whether there is a
     * directed path from the source to the sink in the residual graph -- if so,
     * return a linked list containing the edges in the augmenting path in the
     * form (s,v_1), (v_1,v_2), ..., (v_{k-1},v_k), (v_k,t); if not, return an
     * empty linked list.
     *
     * @return the linked list
     */
    public LinkedList<Edge> findAugmentingPath() {
        LinkedList<Edge> path = new LinkedList<>();
        boolean[] visited = new boolean[numVertices];
        Queue<Vertex> queue = new ArrayDeque<>();
        Map<Vertex, Edge> parent = new HashMap<>();

        Vertex source = getVertexByIndex(0);
        Vertex sink = getVertexByIndex(numVertices - 1);

        queue.add(source);
        visited[source.getLabel()] = true;

        while (!queue.isEmpty()) {
            Vertex u = queue.poll();

            if (u.equals(sink)) {
                for (Vertex current = sink; current != source; current = parent.get(current).getSourceVertex()) {
                    Edge e = parent.get(current);
                    if (e == null) {
                        return new LinkedList<>(); // Early exit if path is incomplete
                    }
                    path.addFirst(e);
                }
                return path;
            }

            for (Vertex v : getAdjList(u)) {
                Edge edge = getAdjMatrixEntry(u, v);
                if (!visited[v.getLabel()] && edge != null && edge.getCap() > 0) {
                    parent.put(v, edge);
                    queue.add(v);
                    visited[v.getLabel()] = true;
                }
            }
        }
        return new LinkedList<>();
    }
}