package networkFlow;

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

        int n = getNumVertices();

        // iterate over all vertices in network
        for (int u = 0; u < n; u++) {
            // iterate over all adjacent vertices
            for (Vertex v : net.getAdjList((net.getVertexByIndex(u)))) {
                Edge edge = net.getAdjMatrixEntry(net.getVertexByIndex(u), v);
                int resCap = edge.getCap() - edge.getFlow();

                // add forward edge if resCap > 0
                if (resCap > 0) {
                    this.addEdge(net.getVertexByIndex(u), v, resCap);
                }

                // add reverse edge if flow > 0
                if (edge.getFlow() > 0) {
                    this.addEdge(v, net.getVertexByIndex(u), edge.getFlow());
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


        Vertex source = getVertexByIndex(0);
        Vertex sink = getVertexByIndex(numVertices - 1);

        System.out.println("sink: " + sink + "\n");

        queue.add(source);
        visited[source.getLabel()] = true;

        Map<Vertex, Edge> parent = new HashMap<>();

        while (!queue.isEmpty()) {
            Vertex u = queue.poll();
            for (Vertex v : getAdjList(u)) {
                Edge edge = getAdjMatrixEntry(u, v);
                if (!visited[v.getLabel()] && edge != null && edge.getCap() > edge.getFlow()) {
                    parent.put(v, edge);
                    visited[v.getLabel()] = true;
                    queue.add(v);

                    System.out.println(v);

                    if (v.equals(getVertexByIndex(numVertices - 1))) {
                        System.out.println();
                        Vertex current = v;
                        while (current != source) {
                            Edge e = parent.get(current);
                            path.addFirst(e);
                            current = e.getSourceVertex();
                        }
                        return path;
                    }
                }
            }
        }
        return new LinkedList<>();
    }
}
