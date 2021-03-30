package fr.michaelm.jump.feature.jgrapht;

import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.jgrapht.graph.WeightedPseudograph;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class JumpJGraphTTest {

  static final Random RDM = new Random();
  static final GeometryFactory FACTORY = new GeometryFactory();
  static final Pattern PATTERN = Pattern.compile("^\\s*([A-Za-z0-9+_.-]+)\\s+(<?-+>?)\\s+([A-Za-z0-9+_.-]+)\\s*$");
  static final FeatureSchema SCHEMA = new FeatureSchema();
  static final String GEOMETRY = "geometry";
  static final String NAME = "name";
  static final String WEIGHT = "weight";
  static {
    SCHEMA.addAttribute(GEOMETRY, AttributeType.GEOMETRY);
    SCHEMA.addAttribute(NAME, AttributeType.STRING);
    SCHEMA.addAttribute(WEIGHT, AttributeType.STRING);
  }

  static class XGraph {
    public Graph<INode,FeatureAsEdge> graph;
    public Map<String,Coordinate> nodes;
    public XGraph(Graph<INode,FeatureAsEdge> graph, Map<String,Coordinate> nodes) {
      this.graph = graph;
      this.nodes = nodes;
    }
  }

  static public XGraph createGraph(String name) throws IOException {
    Graph<INode,FeatureAsEdge> graph = null;
    Map<String,Coordinate> nodes = new HashMap<>();
    try (InputStream is = JumpJGraphTTest.class.getResourceAsStream(name);
         InputStreamReader isr = new InputStreamReader(is);
         BufferedReader reader = new BufferedReader(isr)) {
      int dim = 2;
      boolean set = false;
      for (String line : reader.lines().collect(Collectors.toList())) {
        String lower = line.trim().toLowerCase();
        if (!set && lower.startsWith("directed")) {
          graph = new DirectedWeightedPseudograph<>(FeatureAsEdge.class);
          if (lower.endsWith("3d")) dim = 3;
          set = true;
        } else if (!set && lower.startsWith("undirected")) {
          graph = new WeightedPseudograph<>(FeatureAsEdge.class);
          if (lower.endsWith("3d")) dim = 3;
          set = true;
        } else {
          addEdge(graph, nodes, line, dim);
        }
      }
    }
    return new XGraph(graph, nodes);
  }

  static LineString createLineString(double...coords) {

    if (coords.length == 4) {
      return FACTORY.createLineString(new Coordinate[]{
          new Coordinate(coords[0], coords[1]),
          new Coordinate(coords[2], coords[3])
      });
    } else if (coords.length == 6) {
      return FACTORY.createLineString(new Coordinate[]{
          new Coordinate(coords[0], coords[1], coords[2]),
          new Coordinate(coords[3], coords[4], coords[5])
      });
    } else throw new RuntimeException("Only 4 or 6 double arguments are accepted");
  }

  static Feature createFeature(String name, double...coords) {
    Feature f = new BasicFeature(SCHEMA);
    f.setAttribute(NAME, name);
    f.setGeometry(createLineString(coords));
    return f;
  }

  private static LineString createLineString(int...coords) {

    if (coords.length == 4) {
      return FACTORY.createLineString(new Coordinate[]{
          new Coordinate(coords[0], coords[1]),
          new Coordinate(coords[2], coords[3])
      });
    } else if (coords.length == 6) {
      return FACTORY.createLineString(new Coordinate[]{
          new Coordinate(coords[0], coords[1], coords[2]),
          new Coordinate(coords[3], coords[4], coords[5])
      });
    } else throw new RuntimeException("Only 4 or 6 double arguments are accepted");
  }

  static Feature createFeature(String name, int...coords) {
    Feature f = new BasicFeature(SCHEMA);
    f.setAttribute(NAME, name);
    f.setGeometry(createLineString(coords));
    return f;
  }

  static Feature createFeature(String name, double x, double y) {
    Feature f = new BasicFeature(SCHEMA);
    f.setAttribute(NAME, name);
    f.setGeometry(FACTORY.createPoint(new Coordinate(x,y)));
    return f;
  }

  private static void addEdge(Graph<INode,FeatureAsEdge> graph,
                         Map<String,Coordinate> map, String string, int dim) {
    Matcher matcher = PATTERN.matcher(string);
    if (matcher.matches()) {
      String node1 = matcher.group(1);
      String relation = matcher.group(2);
      String node2 = matcher.group(3);
      if (!map.containsKey(node1)) map.put(node1, rdmCoord(dim));
      if (!map.containsKey(node2)) map.put(node2, rdmCoord(dim));
      if (relation.startsWith("<")) {
        Feature f = new BasicFeature(SCHEMA);
        f.setGeometry(FACTORY.createLineString(new Coordinate[]{map.get(node2), map.get(node1)}));
        System.out.println("Add edge " + f.getGeometry());
        INode n1 = iNode(map.get(node1));
        INode n2 = iNode(map.get(node2));
        graph.addEdge(n2, n1, new FeatureAsEdge(f));
      }
      if (relation.endsWith(">")) {
        Feature f = new BasicFeature(SCHEMA);
        f.setGeometry(FACTORY.createLineString(new Coordinate[]{map.get(node1), map.get(node2)}));
        System.out.println("Add edge " + f.getGeometry());
        INode n1 = iNode(map.get(node1));
        INode n2 = iNode(map.get(node2));
        graph.addVertex(n1);
        graph.addVertex(n2);
        graph.addEdge(n1, n2, new FeatureAsEdge(f));
      }
    }
  }

  private static Coordinate rdmCoord(int dim) {
    if (dim == 2) return new Coordinate(RDM.nextDouble(), RDM.nextDouble());
    else if (dim == 3) return new Coordinate(RDM.nextDouble(), RDM.nextDouble(), RDM.nextDouble());
    else throw new RuntimeException("Coordinate must have 2 or 3 dimension");
  }

  private static INode iNode(Coordinate c) {
    return Double.isNaN(c.z) ? new Node2D(c) : new Node3D(c);
  }

  private static INode rdmNode(int dim) {
    return dim == 2 ? new Node2D(rdmCoord(dim)) : new Node3D(rdmCoord(3));
  }

}
