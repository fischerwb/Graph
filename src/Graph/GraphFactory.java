package Graph;

import java.awt.*;

public class GraphFactory {

  public static Graph createGraph() {

    return new GraphImpl();
  }

  // Only used within the package, not allowed by general public
  static VertexIfc createVertex (String n, Point l, String d) {

    return new VertexImpl(n, l, d);
  }

  // Only used within the package, not allowed by general public
  static EdgeIfc createEdge (VertexIfc f, VertexIfc t, String l, int w, boolean d) {

    return new EdgeImpl(f, t, l, w, d);
  }

  // Just used by tests within the package
  static GraphIfc createTestGraph() {

    return new GraphImpl();
  }

}
