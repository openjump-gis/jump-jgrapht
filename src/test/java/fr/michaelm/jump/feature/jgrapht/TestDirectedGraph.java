package fr.michaelm.jump.feature.jgrapht;

import org.jgrapht.alg.cycle.CycleDetector;
import org.junit.Test;
import org.locationtech.jts.util.Assert;

public class TestDirectedGraph extends JumpJGraphTTest {

  @Test
  public void TestDirectedGraph1() throws Exception {
    XGraph g = createGraph("/graphs/directed/digraph1");
    Assert.isTrue(g.graph.getType().isDirected());
    Assert.isTrue(g.graph.getType().isAllowingCycles());
    Assert.isTrue(g.graph.getType().isAllowingMultipleEdges());
    Assert.isTrue(g.graph.getType().isAllowingSelfLoops());
    Assert.equals(4, g.graph.edgeSet().size());
    Assert.equals(4, g.graph.vertexSet().size());
    Assert.equals(2, g.graph.outDegreeOf(new Node2D(g.nodes.get("A"))));
    Assert.equals(0, g.graph.inDegreeOf(new Node2D(g.nodes.get("A"))));
    Assert.equals(2, g.graph.inDegreeOf(new Node2D(g.nodes.get("D"))));
    Assert.equals(0, g.graph.outDegreeOf(new Node2D(g.nodes.get("D"))));
    Assert.equals(1, g.graph.inDegreeOf(new Node2D(g.nodes.get("C"))));
    Assert.equals(1, g.graph.outDegreeOf(new Node2D(g.nodes.get("C"))));
    Assert.isTrue(!new CycleDetector(g.graph).detectCycles());
  }

}
