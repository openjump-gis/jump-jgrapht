package fr.michaelm.jump.feature.jgrapht;

import java.util.*;

import com.vividsolutions.jump.feature.Feature;
import org.jgrapht.*;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.locationtech.jts.geom.Geometry;

/**
 * Utility class to work with graphs built from feature collections.
 * @author Michael Michaud
 * @version 1.0 (2021-03-19) for OpenJUMP 2
 * @version 0.1 (2007-05-28)
 */
public class GraphUtil {
    
    
   /**
    * Returns true if the graph formed by features is connected.
    * @param features the collection of features
    * @param directed wether the graph to build is directed or not
    * @param dim3 true if c(x,y,z) and c(x,y,z') are considered as different nodes
    */
    public static boolean isGraphConnected(Collection<Feature> features, boolean directed, boolean dim3) {
        Graph<INode,FeatureAsEdge> g = directed ?
                GraphFactory.createDirectedGraph(features, dim3)
                :GraphFactory.createUndirectedGraph(features, dim3);
        return new ConnectivityInspector<>(g).isConnected();
    }
    
   /**
    * Returns a list of connected Set s of vertices.
    * @param features the collection of features
    * @param directed wether the graph to build is directed or not
    * @param dim3 true if c(x,y,z) and c(x,y,z') are considered as different nodes
    */
    public static List<Set<INode>> createConnectedNodeSets(Collection<Feature> features, boolean directed, boolean dim3) {
        Graph<INode,FeatureAsEdge> g = directed ?
                GraphFactory.createDirectedGraph(features, dim3)
                :GraphFactory.createUndirectedGraph(features, dim3);
        return new ConnectivityInspector<>(g).connectedSets();
    }
    
   /**
    * Returns vertices having a degree higher than min and lower than max as a list of
    * geometries.
    * @param features the collection of features
    * @param degree the degree of nodes to return (inclusive)
    * @param directed wether the graph to build is directed or not
    * @param dim3 true if c(x,y,z) and c(x,y,z') are considered as different nodes
    */
    public static List<Geometry> getVertices(Collection<Feature> features,
                                          int degree, boolean directed, boolean dim3) {
        return getVertices(features, degree, degree, directed, dim3);
    }
    
    /**
    * Returns vertices having a degree higher than min and lower than max as a list of
    * geometries.
    * @param features the collection of features
    * @param minDegree the minimum degree of nodes to return (inclusive)
    * @param maxDegree the maximum degree of nodes to return (inclusive)
    * @param directed wether the graph to build is directed or not
    * @param dim3 true if c(x,y,z) and c(x,y,z') are considered as different nodes
    */
    public static List<Geometry> getVertices(Collection<Feature> features,
                                          int minDegree, int maxDegree,
                                          boolean directed, boolean dim3) {
        assert minDegree >= 0 : "" + minDegree + " : minDegree must be positive or null";
        assert maxDegree >= minDegree : "" + maxDegree + " : maxDegree must more or equals to minDegree";
        Graph<INode,FeatureAsEdge> g = directed ?
                GraphFactory.createDirectedGraph(features, dim3)
                :GraphFactory.createUndirectedGraph(features, dim3);
        List<Geometry> geometries = new ArrayList<>();
        for (INode node : g.vertexSet()) {
            int degree = g.degreeOf(node);
            if (degree >= minDegree && degree <= maxDegree) {
                geometries.add(node.getGeometry());
            }
        }
        return geometries;
    }
    
}

