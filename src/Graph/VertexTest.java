package Graph;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;

public class VertexTest extends SimpleBaseTestCase {

  public void setUp () throws Exception {

    super.setUp();
  }

  public void tearDown () throws Exception {

    super.tearDown();
  }


  public void testJSON () throws Exception {

    String json = from.toJSONString();
    assertFalse(json.isEmpty());

    JSONParser parser = new JSONParser();
    JSONObject obj;

    try {
      obj = (JSONObject) parser.parse(json);
    }
    catch (ParseException e) {
      return;
    }

    JSONObject l = (JSONObject) obj.get("location");
    Point location = new Point(((Long) l.get("x")).intValue(), ((Long) l.get("y")).intValue());

    assertEquals(from.getName(), (String) obj.get("name"));
    assertEquals(from.getLocation(), location);
    assertEquals(from.getData(), (String) obj.get("data"));
  }

  public void testGetName () throws Exception {

    assertEquals(FROM_NAME, from.getName());
    assertEquals(TO_NAME, to.getName());
  }

  public void testGetLocation () throws Exception {

    assertEquals(FROM_LOCATION, from.getLocation());
    assertEquals(TO_LOCATION, to.getLocation());
  }

  public void testSetLocation () throws Exception {

    from.setLocation(CHANGED_FROM_LOCATION);
    assertEquals(CHANGED_FROM_LOCATION, from.getLocation());
    from.setLocation(FROM_LOCATION);
    assertEquals(FROM_LOCATION, from.getLocation());

    to.setLocation(CHANGED_TO_LOCATION);
    assertEquals(CHANGED_TO_LOCATION, to.getLocation());
    to.setLocation(TO_LOCATION);
    assertEquals(TO_LOCATION, to.getLocation());
  }

  public void testHasEdge () throws Exception {

    assertTrue(from.hasEdge(to));
    assertFalse(to.hasEdge(from));
  }

  public void testFindEdge () throws Exception {

    Edge e = from.findEdge(to);
    assertNotNull(e);
    assertEquals(LABEL, e.getLabel());

    e = to.findEdge(from);
    assertNull(e);
  }

  public void testFind () throws Exception {

    EdgeImpl e = from.find(to);
    assertNotNull(e);
    assertEquals(LABEL, e.getLabel());

    e = to.find(from);
    assertNull(e);
  }

  public void testAddRemoveEdge () throws Exception {

    EdgeImpl newEdge = new EdgeImpl(to, from, CHANGED_LABEL, CHANGED_WEIGHT, DIRECTED);
    from.addEdge(newEdge);
    to.addEdge(newEdge);

    assertTrue(from.hasEdge(to));
    assertTrue(to.hasEdge(from));

    from.removeEdge(newEdge);
    to.removeEdge(newEdge);

    assertTrue(from.hasEdge(to));
    assertFalse(to.hasEdge(from));
  }

  public void testGetEdges () throws Exception {

    assertEquals(0, from.incomingEdges().size());
    assertEquals(1, from.outgoingEdges().size());

    assertEquals(1, to.incomingEdges().size());
    assertEquals(0, to.outgoingEdges().size());
  }

}
