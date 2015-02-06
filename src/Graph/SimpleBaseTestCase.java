package Graph;

import Graph.EdgeImpl;
import Graph.VertexImpl;
import junit.framework.TestCase;

import java.awt.*;

public class SimpleBaseTestCase extends TestCase {

  static final String LABEL = "Test Edge";
  static final int WEIGHT = 100;
  static final boolean DIRECTED = false;

  static final String CHANGED_LABEL = "Edge Test";
  static final int CHANGED_WEIGHT = 10;

  static final String FROM_NAME = "From";
  static final Point FROM_LOCATION = new Point(10, 1);
  static final Point CHANGED_FROM_LOCATION = new Point(1, 10);

  static final String TO_NAME = "To";
  static final Point TO_LOCATION = new Point(20, 1);
  static final Point CHANGED_TO_LOCATION = new Point(1, 20);

  EdgeImpl edge;
  VertexImpl from;
  VertexImpl to;

  public void setUp () throws Exception {

    super.setUp();

    from = new VertexImpl(FROM_NAME, FROM_LOCATION, "");
    to = new VertexImpl(TO_NAME, TO_LOCATION, "");
    edge = new EdgeImpl(from, to, LABEL, WEIGHT, DIRECTED);

    from.addEdge(edge);
    to.addEdge(edge);
  }

  public void tearDown () throws Exception {

    edge = null;
    from = null;
    to = null;
  }


}
