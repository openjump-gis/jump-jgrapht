package fr.michaelm.jump.feature.jgrapht;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.graph.*;

import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.index.strtree.STRtree;


/**
 * This utility class offers static methods to build graphs from feature
 * collections.
 * @author Michael Michaud
 * @version 1.0 (2021-03-30) for OpenJUMP 2
 * @version 0.7.1 (2017-03-14)
 */
public class GraphFactory {

    private static long id = 0L;

    /**
     * Relation used to connect features.
     */
    protected enum Relation
    {
        /**
         * Two FeatureAsNode are connected if they intersect .
         */
        INTERSECTS,
        /**
         * Two FeatureAsNode are connected if they touch.
         */
        TOUCHES,
        /**
         * Two FeatureAsNode are near from each other.
         */
        ISWITHIN
    }


    /**
     * Create a WeightedPseudograph from a collection of features.
     * A Pseudograph is an undirected Graph where both multiple edges between two vertices
     * and loops are permitted.
     * Features are connected if their extremities touch each others in the 2D plan.
     * @param features a collection of features.
     * @return a WeightedPseudograph
     */
    public static WeightedPseudograph<INode,FeatureAsEdge>
            createGraph(Collection<Feature> features) {
        return createUndirectedGraph(features, false);
    }


   /**
    * Create a WeightedPseudograph from a collection of features.
    * A Pseudograph is an undirected Graph where both multiple edges between two vertices
    * and loops are permitted.
    * @param features a collection of features.
    * @param dim3 true means that nodes are evaluated equals when x,y,z are equals
    * @return a WeightedPseudograph
    */
    public static WeightedPseudograph<INode,FeatureAsEdge>
        createUndirectedGraph(Collection<Feature> features, boolean dim3) {
        WeightedPseudograph<INode,FeatureAsEdge> graph =
                new WeightedPseudograph<>(FeatureAsEdge.class);
        return (WeightedPseudograph<INode,FeatureAsEdge>)add(graph, features, dim3);
    }


    /**
     * Create a WeightedDirectedPseudograph from a collection of features.
     * A Pseudograph is a Graph where both multiple edges between two vertices
     * and loops are permitted.
     * Features are connected if their extremities touch each others in the 2D plan.
     * @param features a collection of features.
     * @return a WeightedPseudograph
     */
    public static DirectedWeightedPseudograph<INode,FeatureAsEdge>
            createDirectedGraph(Collection<Feature> features) {
        return createDirectedGraph(features, false);
    }


    /**
     * Create a DirectedWeightedPseudograph from a collection of features
     * Creates a non-simple directed graph in which both graph loops and multiple edges are permitted.
     * @param features a collection of features.
     * @param dim3 true means that nodes are evaluated equals when x,y,z are equals
     * @return a DirectedWeightedPseudograph or a DirectedWeightedMultigraph
     */
    public static DirectedWeightedPseudograph<INode,FeatureAsEdge>
            createDirectedGraph(Collection<Feature> features, boolean dim3) {
        DirectedWeightedPseudograph<INode,FeatureAsEdge> graph =
                new DirectedWeightedPseudograph(FeatureAsEdge.class);
        return (DirectedWeightedPseudograph<INode,FeatureAsEdge>)add(graph, features, dim3);
    }


   /**
    * Create a WeightedPseudograph (undirected) from a collection of features
    * and a spatial Relation.
    * @param features a collection of features.
    * @param relation the relation defining edges
    * @return a WeightedPseudograph with Features as nodes and relation as edges
    */
    public static WeightedPseudograph<FeatureAsNode,Long> createGraph(
            Collection<Feature> features,
            Relation relation) {
        WeightedPseudograph<FeatureAsNode,Long> graph =
                new WeightedPseudograph<FeatureAsNode,Long>(Long.class);
        return add(graph, features, relation, 0);
    }


   /**
    * Create a WeightedPseudograph (undirected) from a collection of features.
    * Features are connected if their distance is <= maxdist
    * @param features a collection of features
    * @param maxDist the maximum distance to consider two features as connected
    * @return a WeightedPseudograph with Features as nodes and relation as edges
    */
    public static WeightedPseudograph<FeatureAsNode,Long> createGraph(
            Collection<Feature> features,
            double maxDist) {
        WeightedPseudograph<FeatureAsNode,Long> graph =
                new WeightedPseudograph<>(Long.class);
        return add(graph, features, Relation.ISWITHIN, maxDist);
    }


    private static Graph<INode,FeatureAsEdge> add(
            Graph<INode,FeatureAsEdge> graph,
            Collection<Feature> features,
            boolean dim3) {
        Coordinate[] cc;
        for (Feature f : features) {
            Geometry g = f.getGeometry();
            if (g.isEmpty()) continue;
            cc = f.getGeometry().getCoordinates();
            INode node1 = dim3? new Node3D(cc[0]) : new Node2D(cc[0]);
            graph.addVertex(node1);
            if (g.getDimension() == 0) continue;
            INode node2 = dim3? new Node3D(cc[cc.length-1]) : new Node2D(cc[cc.length-1]);
            graph.addVertex(node2);
            FeatureAsEdge edge = new FeatureAsEdge(f);
            graph.addEdge(node1, node2, edge);
            graph.setEdgeWeight(edge, g.getLength());
        }
        return graph;
    }


    /**
     * Add features to a weighted pseudograph (undirected).
     * Features are connected according to the Relation parameter.
     * Weights are initialized with their default value
     * @param graph the graph to populate
     * @param features features to add to be added to the graph
     * @param relation relation determining if two features are connected or not
     * @param maxDist max distance to connect features in the case of Relation.ISWITHIN
     * @return a WeightedPseudograph
     */
    private static WeightedPseudograph<FeatureAsNode,Long> add(
            WeightedPseudograph<FeatureAsNode,Long> graph,
            Collection<Feature> features,
            Relation relation,
            double maxDist) {

        Collection<FeatureAsNode> featureAsNodes = new ArrayList<>();

        STRtree index = new STRtree();
        for (Feature feature : features) {
            FeatureAsNode f = new FeatureAsNode(feature);
            if (f.getGeometry().isEmpty()) continue;
            index.insert(f.getGeometry().getEnvelopeInternal(), f);
            featureAsNodes.add(f);
        }

        for (FeatureAsNode f : featureAsNodes) {
            if (f.getGeometry().isEmpty()) continue;
            Envelope env = f.getGeometry().getEnvelopeInternal();
            env.expandBy(maxDist);
            List<FeatureAsNode> list = (List<FeatureAsNode>)index.query(env);
            boolean isolated = true;
            for (FeatureAsNode candidate : list) {
                if (candidate == f) continue;
                if (graph.containsEdge(candidate, f)) continue;
                if (relation==Relation.INTERSECTS &&
                    f.getGeometry().intersects(candidate.getGeometry())) {
                    graph.addVertex(f);
                    graph.addVertex(candidate);
                    graph.addEdge(f, candidate, ++id);
                    isolated = false;
                }
                else if (relation==Relation.TOUCHES &&
                    f.getGeometry().touches(candidate.getGeometry())) {
                    graph.addVertex(f);
                    graph.addVertex(candidate);
                    graph.addEdge(f, candidate, ++id);
                    isolated = false;
                }
                else if (relation==Relation.ISWITHIN &&
                    f.getGeometry().distance(candidate.getGeometry())<=maxDist) {
                    graph.addVertex(f);
                    graph.addVertex(candidate);
                    graph.addEdge(f, candidate, ++id);
                    graph.setEdgeWeight(f, candidate, f.getGeometry().distance(candidate.getGeometry()));
                    isolated = false;
                }
            }
            if (isolated) graph.addVertex(f);
        }
        return graph;
    }


    /**
     * Add features to a directed weighted pseudograph
     * @param graph the DirectedWeightedPseudograph to add Features to
     * @param features Features to add to the DirectedWeightedPseudograph
     * @param direct_weight weight for the direct orientation (linestring orientation)
     *                      (0 to mean that the edge cannot be traversed in this direction)
     * @param inverse_weight weight for the inverse orientation (inverse linestring orientation)
     *                      (0 to mean that the edge cannot be traversed in this direction)
     * @param dim3 true if edges must be 3d connected (x, y and z)
     * @return a DirectedWeightedPseudograph
     */
    private static DirectedWeightedPseudograph<INode,FeatureAsEdge> add(
            DirectedWeightedPseudograph<INode,FeatureAsEdge> graph,
            Collection<Feature> features,
            String direct_weight,
            String inverse_weight,
            boolean dim3) {
        Coordinate[] cc;
        for (Feature f : features) {
            Geometry g = f.getGeometry();
            if (g.isEmpty()) continue;
            cc = f.getGeometry().getCoordinates();
            INode node1 = dim3? new Node3D(cc[0]) : new Node2D(cc[0]);
            graph.addVertex(node1);
            if (g.getDimension() == 0) continue;
            INode node2 = dim3? new Node3D(cc[cc.length-1]) : new Node2D(cc[cc.length-1]);
            graph.addVertex(node2);
            FeatureAsEdge edge = new FeatureAsEdge(f);
            if (f.getSchema().hasAttribute(direct_weight) &&
                f.getSchema().getAttributeType(direct_weight) == AttributeType.DOUBLE) {
                double weight = f.getDouble(f.getSchema().getAttributeIndex(direct_weight));
                if (weight >= 0) {
                    graph.addEdge(node1, node2, edge);
                    graph.setEdgeWeight(edge, weight);
                }
            }
            if (f.getSchema().hasAttribute(inverse_weight) &&
                f.getSchema().getAttributeType(inverse_weight) == AttributeType.DOUBLE) {
                double weight = f.getDouble(f.getSchema().getAttributeIndex(inverse_weight));
                if (weight >= 0) {
                    graph.addEdge(node2, node1, edge);
                    graph.setEdgeWeight(edge, weight);
                }
            }
        }
        return graph;
    }

}

