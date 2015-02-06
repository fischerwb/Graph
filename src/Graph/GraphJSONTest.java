package Graph;

import Graph.GraphImpl;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.io.FileReader;
import java.util.Stack;

public class GraphJSONTest extends TestCase {

  private GraphImpl graph = null;

  public void setUp () throws Exception {

    super.setUp();

    graph = new GraphImpl();
  }

  public void tearDown () throws Exception {

  }

  private void printPath(Vertex from, Vertex to, Stack<Edge> path) {

    System.out.print("Path from ");
    System.out.print(from.getName());
    System.out.print(" to ");
    System.out.print(to.getName());
    System.out.print("\n");

    Edge e;
    do {
      e = path.pop();
      System.out.print("    ");
      System.out.print(e.getLabel());
      System.out.print("\n");
    } while (!path.empty());
    System.out.print("\n");
  }

  public void testGraphA () throws Exception {
    FileReader in = new FileReader("json/GraphA.json");
    assertTrue(graph.load(in));
    in.close();

    Assert.assertEquals(8, graph.getSize());
    Assert.assertEquals(14, graph.getEdges().size());

    Vertex a = graph.getVertex("A");
    ShortestPath sp = graph.getPathInformation(a);

    Vertex b = graph.getVertex("B");
    printPath(a, b, sp.pathTo(b));
    Assert.assertEquals(2d, sp.distanceTo(b));

    Vertex c = graph.getVertex("C");
    printPath(a, c, sp.pathTo(c));
    Assert.assertEquals(1d, sp.distanceTo(c));

    Vertex d = graph.getVertex("D");
    printPath(a, d, sp.pathTo(d));
    Assert.assertEquals(4d, sp.distanceTo(d));

    Vertex e = graph.getVertex("E");
    printPath(a, e, sp.pathTo(e));
    Assert.assertEquals(7d, sp.distanceTo(e));

    Vertex f = graph.getVertex("F");
    printPath(a, f, sp.pathTo(f));
    Assert.assertEquals(5d, sp.distanceTo(f));

    Vertex g = graph.getVertex("G");
    printPath(a, g, sp.pathTo(g));
    Assert.assertEquals(7d, sp.distanceTo(g));

    Vertex h = graph.getVertex("H");
    printPath(a, h, sp.pathTo(h));
    Assert.assertEquals(8d, sp.distanceTo(h));

  }

  public void testBadJSON () throws Exception {
    FileReader in = new FileReader("json/BadGraph.json");
    in.close();
    assertFalse(graph.load(in));
  }

}
