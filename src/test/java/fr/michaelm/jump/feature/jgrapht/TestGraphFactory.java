package fr.michaelm.jump.feature.jgrapht;

import com.vividsolutions.jump.feature.Feature;
import org.jgrapht.Graph;
import org.jgrapht.alg.cycle.CycleDetector;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.util.Assert;

import java.util.Arrays;

public class TestGraphFactory extends JumpJGraphTTest {

  @Test
  public void testCreateGraph() {
    Feature f1 = createFeature("A", 0, 0, 10, 0);
    Feature f2 = createFeature("B", 10, 0, 20, 10);
    Feature f3 = createFeature("C", 20, 10, 0, 0);
    Graph<INode,FeatureAsEdge> graph = GraphFactory.createGraph(
        Arrays.asList(f1, f2, f3)
    );
    Assert.equals(3, graph.edgeSet().size());
    Assert.equals(3, graph.vertexSet().size());
    Assert.isTrue(graph.getType().isUndirected());
    Assert.isTrue(graph.vertexSet().stream().noneMatch(n->graph.degreeOf(n)==1));
  }

  @Test
  public void testCreateDirectedGraph() {
    Feature f1 = createFeature("A", 0, 0, 10, 0);
    Feature f2 = createFeature("B", 10, 0, 20, 10);
    Feature f3 = createFeature("C", 20, 10, 0, 0);
    Graph<INode,FeatureAsEdge> graph = GraphFactory.createDirectedGraph(
        Arrays.asList(f1, f2, f3), false
    );
    Assert.equals(3, graph.edgeSet().size());
    Assert.equals(3, graph.vertexSet().size());
    Assert.isTrue(graph.getType().isDirected());
    Assert.isTrue(graph.vertexSet().stream().noneMatch(n->graph.degreeOf(n)==1));
    Assert.isTrue(new CycleDetector<>(graph).detectCycles());
  }

  @Test
  public void testFeatureAsNodeGraph() {
    Feature f1 = createFeature("A", 0, 0);
    Feature f2 = createFeature("B", 1, 1);
    Feature f3 = createFeature("C", 0, 2);
    Feature f4 = createFeature("D", 10, 10);
    Graph<FeatureAsNode, Long> graph = GraphFactory.createGraph(
        Arrays.asList(f1, f2, f3, f4), 5.0
    );
    System.out.println(graph);
    Assert.equals(3, graph.edgeSet().size());
    System.out.println(graph.vertexSet());
    Assert.equals(4, graph.vertexSet().size());
    Assert.isTrue(graph.getType().isUndirected());
    Assert.equals(1L, graph.vertexSet().stream().filter(n->graph.degreeOf(n)==0).count());
    Assert.equals(0L, graph.vertexSet().stream().filter(n->graph.degreeOf(n)==1).count());
    Assert.equals(3L, graph.vertexSet().stream().filter(n->graph.degreeOf(n)==2).count());
  }

}
