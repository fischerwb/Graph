package Graph;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Stack;

public class GraphTest extends TestCase {

  private static final int NUM_VERTICES = 4;
  private static final String VERTEX_NAME_PREFIX = "Node ";

  /*
    4 Vertices where there is a directional link from 1 to 2, 2 to 3, and 3 to 4, and a bidirectional link
    between 1 and 4.
   */

  private GraphImpl graph = null;

  public void setUp () throws Exception {

    super.setUp();

    graph = new GraphImpl();

    int i;
    Vertex first = null;
    Vertex last = null;
    for (i = 1; i <= NUM_VERTICES; i++) {
      Vertex v = addVertex(i);
      if (last != null) {
        addEdge(last, v, i, true);
      } else if (first == null) {
        first = v;
      }
      last = v;
    }

    addEdge(first, last, 10, false);
  }

  public void tearDown () throws Exception {

  }

  public void testJSON () throws Exception {

    String json = graph.toJSONString();
    assertNotNull(json);
    assertFalse(json.isEmpty());

    JSONParser parser = new JSONParser();
    JSONObject obj = (JSONObject) parser.parse(json);

    JSONArray varray = (JSONArray) obj.get("vertices");
    assertEquals(NUM_VERTICES, varray.size());

    JSONArray earray = (JSONArray) obj.get("edges");
    assertEquals(NUM_VERTICES + 1, earray.size());


    FileWriter out = new FileWriter("json/Graph.json");
    assertTrue(graph.save(out));
    out.close();

    graph.clear();

    assertTrue(graph.isEmpty());

    FileReader in = new FileReader("json/Graph.json");
    assertTrue(graph.load(in));
    in.close();

    File file = new File("Graph.json");
    file.delete();

    Assert.assertEquals(NUM_VERTICES, graph.getSize());
    Assert.assertEquals(NUM_VERTICES + 1, graph.getEdges().size());

    // Verify that all the vertices exist
    // And that they all have the correct edges
    Vertex one = graph.getVertex(VERTEX_NAME_PREFIX + "1");
    assertNotNull(one);

    Vertex two = graph.getVertex(VERTEX_NAME_PREFIX + "2");
    assertNotNull(two);

    Vertex three = graph.getVertex(VERTEX_NAME_PREFIX + "3");
    assertNotNull(three);

    Vertex four = graph.getVertex(VERTEX_NAME_PREFIX + "4");
    assertNotNull(four);

    assertTrue(one.hasEdge(two));
    assertFalse(one.hasEdge(three));
    assertTrue(one.hasEdge(four));

    assertFalse(two.hasEdge(one));
    assertTrue(two.hasEdge(three));
    assertFalse(two.hasEdge(four));

    assertFalse(three.hasEdge(one));
    assertFalse(three.hasEdge(two));
    assertTrue(three.hasEdge(four));

    assertTrue(four.hasEdge(one));
    assertFalse(four.hasEdge(two));
    assertFalse(four.hasEdge(three));
  }

  public void testClear () throws Exception {

    graph.clear();
    assertTrue(graph.isEmpty());
    assertTrue(graph.getVertices().isEmpty());
    assertTrue(graph.getEdges().isEmpty());
  }

  public void testGetSize () throws Exception {

    Assert.assertEquals(NUM_VERTICES, graph.getSize());
  }

  public void testIsEmpty () throws Exception {

    assertFalse(graph.isEmpty());
  }

  public void testGetVertex () throws Exception {

    int i = 3;
    String name = VERTEX_NAME_PREFIX + String.valueOf(i);
    Vertex v = graph.getVertex(name);

    assertNotNull(v);
    Assert.assertEquals(name, v.getName());
    Assert.assertEquals(new Point(i, i), v.getLocation());
  }

  public void testGetVertexAtLocation () throws Exception {

    int i = 2;
    Point location = new Point(i, i);
    String name = VERTEX_NAME_PREFIX + String.valueOf(i);
    Vertex v = graph.getVertexAtLocation(location);

    assertNotNull(v);
    Assert.assertEquals(name, v.getName());
    Assert.assertEquals(location, v.getLocation());
  }

  public void testGetVertices () throws Exception {

    Assert.assertEquals(NUM_VERTICES, graph.getVertices().size());
  }

  private Vertex addVertex (int i) {

    String name = VERTEX_NAME_PREFIX + String.valueOf(i);
    Point location = new Point(i, i);
    assertTrue(graph.addVertex(name, location, ""));
    return graph.getVertex(name);
  }

  public void testAddVertex () throws Exception {

    int numVertices = graph.getSize();
    int i = 10;

    // Simple add/remove of vertex with no edges
    addVertex(i);
    Assert.assertEquals(numVertices + 1, graph.getSize());

    assertTrue(graph.removeVertex(VERTEX_NAME_PREFIX + String.valueOf(i)));
    Assert.assertEquals(numVertices, graph.getSize());
    Assert.assertEquals(graph.getSize(), graph.getVertices().size());

    // Add/Remove vertex with directional edge to another vertex
    Vertex v = addVertex(i);
    Assert.assertEquals(numVertices + 1, graph.getSize());
    assertNotNull(v);

    String fromName = VERTEX_NAME_PREFIX + "1";
    Vertex from = graph.getVertex(fromName);
    assertNotNull(from);

    int numEdges = graph.getEdges().size();

    addEdge(from, v, i, true);
    assertTrue(from.hasEdge(v));
    assertFalse(v.hasEdge(from));
    Assert.assertEquals(numEdges + 1, graph.getEdges().size());

    assertTrue(graph.removeVertex(v.getName()));
    Assert.assertEquals(numVertices, graph.getSize());
    assertFalse(from.hasEdge(v));
    Assert.assertEquals(numEdges, graph.getEdges().size());

    // Add/Remove vertex with directional edge to another vertex
    v = addVertex(i);
    Assert.assertEquals(numVertices + 1, graph.getSize());
    assertNotNull(v);

    fromName = VERTEX_NAME_PREFIX + "1";
    from = graph.getVertex(fromName);
    assertNotNull(from);

    numEdges = graph.getEdges().size();

    addEdge(from, v, i, false);
    assertTrue(from.hasEdge(v));
    assertTrue(v.hasEdge(from));
    Assert.assertEquals(numEdges + 2, graph.getEdges().size());

    assertTrue(graph.removeVertex(v.getName()));
    Assert.assertEquals(numVertices, graph.getSize());
    assertFalse(from.hasEdge(v));
    Assert.assertEquals(numEdges, graph.getEdges().size());
  }

  private void addEdge (Vertex from, Vertex to, int i, boolean directed) {

    String EDGE_LABEL_PREFIX = "Edge ";
    assertTrue(graph.addEdge(from, to, EDGE_LABEL_PREFIX + String.valueOf(i * i), i * i, directed));
  }

  public void testAddEdge () throws Exception {

    int numEdges = graph.getEdges().size();
    String fromName = VERTEX_NAME_PREFIX + "1";
    String toName = VERTEX_NAME_PREFIX + String.valueOf(graph.getSize() - 1);
    Vertex from = graph.getVertex(fromName);
    Vertex to = graph.getVertex(toName);

    assertNotNull(from);
    assertNotNull(to);

    // Add bidirectional
    addEdge(from, to, numEdges + 1, false);
    assertTrue(from.hasEdge(to));
    assertTrue(to.hasEdge(from));

    assertTrue(graph.removeEdge(from, to));
    assertFalse(from.hasEdge(to));
    assertFalse(to.hasEdge(from));

    // Add edge from "from" to "to"
    addEdge(from, to, numEdges + 1, true);
    assertTrue(from.hasEdge(to));
    assertFalse(to.hasEdge(from));

    assertTrue(graph.removeEdge(from, to));
    assertFalse(from.hasEdge(to));
    assertFalse(to.hasEdge(from));

    // Add edge from "to" to "from"
    addEdge(to, from, numEdges + 1, true);
    assertFalse(from.hasEdge(to));
    assertTrue(to.hasEdge(from));

    assertTrue(graph.removeEdge(to, from));
    assertFalse(from.hasEdge(to));
    assertFalse(to.hasEdge(from));
  }

  public void testGetEdges () throws Exception {
    // NUM_VERTICES + 1 because have one bidirectional edge.
    Assert.assertEquals("Incorrect number of edges", NUM_VERTICES + 1, graph.getEdges().size());
  }

  public void testIsGraphConnected () throws Exception {

    assertTrue(graph.isGraphConnected(null));

    Vertex from = graph.getVertex(VERTEX_NAME_PREFIX + "3");
    Vertex to = graph.getVertex(VERTEX_NAME_PREFIX + "4");
    assertTrue(graph.removeEdge(from, to));

    // Shows the problem that, depending upon which is the root,
    // it may or may not say the graph is connected
    Vertex root = graph.getVertex(VERTEX_NAME_PREFIX + "1");
    assertTrue(graph.isGraphConnected(root));

    root = graph.getVertex(VERTEX_NAME_PREFIX + "2");
    assertFalse(graph.isGraphConnected(root));

    root = graph.getVertex(VERTEX_NAME_PREFIX + "3");
    assertFalse(graph.isGraphConnected(root));

    root = graph.getVertex(VERTEX_NAME_PREFIX + "4");
    assertTrue(graph.isGraphConnected(root));
  }

  private void assertVertexConnection (String f, String t, boolean compareWith) {

    Vertex from = graph.getVertex(VERTEX_NAME_PREFIX + f);
    Vertex to = graph.getVertex(VERTEX_NAME_PREFIX + t);
    Assert.assertEquals(compareWith, graph.areVerticesConnected(from, to));
  }

  public void testAreVerticesConnected () throws Exception {
    // Check the paths from 1 to all other vertices
    assertVertexConnection("1", "2", true);
    assertVertexConnection("1", "3", true);
    assertVertexConnection("1", "4", true);

    // Check the paths from 2 to all other vertices
    assertVertexConnection("2", "3", true);
    assertVertexConnection("2", "4", true);
    assertVertexConnection("2", "1", true);

    // Check the paths from 3 to all other vertices
    assertVertexConnection("3", "4", true);
    assertVertexConnection("3", "1", true);
    assertVertexConnection("3", "2", true);

    // Check the paths from 4 to all other vertices
    assertVertexConnection("4", "1", true);
    assertVertexConnection("4", "2", true);
    assertVertexConnection("4", "3", true);

    // If remove edge between 2 & 3, can get to 2 from 3, but not vice-versa
    Vertex from = graph.getVertex(VERTEX_NAME_PREFIX + "2");
    Vertex to = graph.getVertex(VERTEX_NAME_PREFIX + "3");
    assertTrue(graph.removeEdge(from, to));

    assertVertexConnection("2", "3", false);
    assertVertexConnection("3", "2", true);

    // If remove bidirectional edge between 1 & 4,
    // can only get from 1 to 2 and 3 to 4
    from = graph.getVertex(VERTEX_NAME_PREFIX + "1");
    to = graph.getVertex(VERTEX_NAME_PREFIX + "4");
    assertTrue(graph.removeEdge(from, to));

    // Check the paths from 1 to all other vertices
    assertVertexConnection("1", "2", true);
    assertVertexConnection("1", "3", false);
    assertVertexConnection("1", "4", false);

    // Check the paths from 2 to all other vertices
    assertVertexConnection("2", "3", false);
    assertVertexConnection("2", "4", false);
    assertVertexConnection("2", "1", false);

    // Check the paths from 3 to all other vertices
    assertVertexConnection("3", "4", true);
    assertVertexConnection("3", "1", false);
    assertVertexConnection("3", "2", false);

    // Check the paths from 4 to all other vertices
    assertVertexConnection("4", "1", false);
    assertVertexConnection("4", "2", false);
    assertVertexConnection("4", "3", false);

  }

  public void testPaths() throws Exception {

    Vertex one = graph.getVertex(VERTEX_NAME_PREFIX + "1");
    Vertex two = graph.getVertex(VERTEX_NAME_PREFIX + "2");
    Vertex three = graph.getVertex(VERTEX_NAME_PREFIX + "3");
    Vertex four = graph.getVertex(VERTEX_NAME_PREFIX + "4");

    ShortestPath sp = graph.getPathInformation(one);

    assertTrue(sp.hasPathTo(two));
    assertTrue(sp.hasPathTo(three));
    assertTrue(sp.hasPathTo(four));

    Assert.assertEquals(4.0, sp.distanceTo(two));
    Assert.assertEquals(13.0, sp.distanceTo(three));
    Assert.assertEquals(29.0, sp.distanceTo(four));

    sp = graph.getPathInformation(three);
    Assert.assertEquals(16.0, sp.distanceTo(four));
    Assert.assertEquals(116.0, sp.distanceTo(one));
    Assert.assertEquals(120.0, sp.distanceTo(two));

    Stack<Edge> path = sp.pathTo(two);
    assertEquals(3, path.size());

    Edge e;
    int i = 0;
    String[] expectedPath = new String[3];
    expectedPath[0] = VERTEX_NAME_PREFIX + "4";
    expectedPath[1] = VERTEX_NAME_PREFIX + "1";
    expectedPath[2] = VERTEX_NAME_PREFIX + "2";
    do {
      e = path.pop();
      Assert.assertEquals(expectedPath[i], e.getTo().getName());
      i++;
    } while (!path.empty());

    // If remove edge between 2 & 3, can get to 2 from 3, but not vice-versa
    assertTrue(graph.removeEdge(two, three));
    assertTrue(sp.hasPathTo(two));

    sp = graph.getPathInformation(two);
    assertFalse(sp.hasPathTo(three));
  }

}
