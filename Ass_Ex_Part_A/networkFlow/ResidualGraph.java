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
        // complete this method as part of Task 2
        LinkedList<Edge> path = new LinkedList<>();
        boolean[] visited = new boolean[numVertices];
        Queue<Vertex> queue = new LinkedList<>();
        Map<Vertex, Edge> parent = new HashMap<>();


        Vertex source = getVertexByIndex(0);
        Vertex sink = getVertexByIndex(numVertices-1);

//        System.out.println("source: " + source);
//
//        System.out.println("sink: " + sink + "\n");

        queue.add(source);
        visited[source.getLabel()] = true;

        while (!queue.isEmpty()) {
//            System.out.println(queue);
            Vertex u = queue.poll();
            visited[u.getLabel()] = true;
//            System.out.println("visited " + u.getLabel() + " " + visited[u.getLabel()]);
//            System.out.println("Visiting: " + u.getLabel());
            if (u.equals(sink)) {
                Vertex current = u;
                while (current != source) {
                    Edge e = parent.get(current);
                    if (e == null) {
                        System.out.println("Missing edge in path for vertex: " + current.getLabel()); // Debugging
                        return new LinkedList<>(); // Early exit if path is incomplete
                    }
                    path.addFirst(e);
                    current = e.getSourceVertex();
                }
                return path;
            }
            for (Vertex v : getAdjList(u)) {
                Edge edge = getAdjMatrixEntry(u, v);
//                System.out.println("Checking edge from " + u.getLabel() + " to " + v.getLabel());
//                System.out.println("Edge cap: " + edge.getCap());
                if (!visited[v.getLabel()] && edge != null && edge.getCap() > 0) {
//                    System.out.println("Adding edge to path: " + u.getLabel() + " -> " + v.getLabel());
                    parent.put(v, edge);
                    if (!queue.contains(v)) {
                        queue.add(v);
                    }
                }
            }
        }
        return new LinkedList<>();
    }
}
