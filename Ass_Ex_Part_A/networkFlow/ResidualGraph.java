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
        return null;
    }
}
