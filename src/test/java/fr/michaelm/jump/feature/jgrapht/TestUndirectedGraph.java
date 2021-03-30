package fr.michaelm.jump.feature.jgrapht;

import org.jgrapht.alg.cycle.CycleDetector;
import org.junit.Test;
import org.locationtech.jts.util.Assert;

public class TestUndirectedGraph extends JumpJGraphTTest {

  @Test
  public void TestUndirectedGraph1() throws Exception {
    JumpJGraphTTest.XGraph g = createGraph("/graphs/undirected/graph1");
    Assert.isTrue(g.graph.getType().isUndirected());
    Assert.isTrue(g.graph.getType().isAllowingCycles());
    Assert.isTrue(g.graph.getType().isAllowingMultipleEdges());
    Assert.isTrue(g.graph.getType().isAllowingSelfLoops());
    Assert.equals(4, g.graph.edgeSet().size());
    Assert.equals(4, g.graph.vertexSet().size());
    Assert.equals(2, g.graph.degreeOf(new Node2D(g.nodes.get("A"))));
    Assert.equals(2, g.graph.degreeOf(new Node2D(g.nodes.get("D"))));
    Assert.equals(2, g.graph.degreeOf(new Node2D(g.nodes.get("C"))));
  }

}
