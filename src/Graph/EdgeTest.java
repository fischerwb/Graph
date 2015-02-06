package Graph;

import junit.framework.Assert;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EdgeTest extends SimpleBaseTestCase {

  public void setUp () throws Exception {

    super.setUp();
  }

  public void tearDown () throws Exception {

    super.tearDown();
  }

  private boolean isValidJSON (String json) {

    JSONParser parser = new JSONParser();
    JSONObject obj;

    try {
      obj = (JSONObject) parser.parse(json);
    }
    catch (ParseException e) {
      return false;
    }

    try {
      Assert.assertEquals(edge.getLabel(), (String) obj.get("label"));
      Assert.assertEquals(edge.getFrom().getName(), (String) obj.get("from"));
      Assert.assertEquals(edge.getTo().getName(), (String) obj.get("to"));
      Assert.assertEquals(edge.getWeight(), ((Long) obj.get("weight")).intValue());
      Assert.assertEquals(edge.getDirected(), (boolean) obj.get("directed"));
    }
    catch (Exception ex) {
      return false;
    }

    return true;
  }

  public void testJSON () throws Exception {

    JSONParser parser = new JSONParser();
    JSONObject obj;

    try {
      obj = (JSONObject) parser.parse(edge.toJSONString());
    }
    catch (ParseException e) {
      return;
    }

    Assert.assertEquals(edge.getLabel(), (String) obj.get("label"));
    Assert.assertEquals(edge.getFrom().getName(), (String) obj.get("from"));
    Assert.assertEquals(edge.getTo().getName(), (String) obj.get("to"));
    Assert.assertEquals(edge.getWeight(), ((Long) obj.get("weight")).intValue());
    Assert.assertEquals(edge.getDirected(), (boolean) obj.get("directed"));
  }

  public void testGetLabel () throws Exception {

    Assert.assertEquals("Incorrect edge label", LABEL, edge.getLabel());
  }

  public void testSetLabel () throws Exception {

    edge.setLabel(CHANGED_LABEL);
    Assert.assertEquals(CHANGED_LABEL, edge.getLabel());

    // null label isn't allowed, so label should stay the same
    edge.setLabel(null);
    Assert.assertEquals(CHANGED_LABEL, edge.getLabel());
  }

  public void testGetWeight () throws Exception {

    Assert.assertEquals(WEIGHT, edge.getWeight());
  }

  public void testSetWeight () throws Exception {

    edge.setWeight(CHANGED_WEIGHT);
    Assert.assertEquals(CHANGED_WEIGHT, edge.getWeight());

    // weight must be 0 or greater
    edge.setWeight(-1);
    Assert.assertEquals(CHANGED_WEIGHT, edge.getWeight());
  }

  public void testGetDirected () throws Exception {

    Assert.assertEquals(DIRECTED, edge.getDirected());
  }

  public void testGetTo () throws Exception {

    Assert.assertEquals(TO_NAME, edge.getTo().getName());
  }

  public void testGetFrom () throws Exception {

    Assert.assertEquals(FROM_NAME, edge.getFrom().getName());
  }

}
