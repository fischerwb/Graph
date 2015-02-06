package Graph;

import java.util.*;

public class ShortestPathImpl implements ShortestPath {

  private final List<VertexIfc> unsettled;
  private final List<VertexIfc> settled;
  private final Map<String, PathAndDistance> pathsTo;


  private class PathAndDistance {

    Double distance;
    EdgeIfc edge;

    PathAndDistance () {

      distance = 0d;
      edge = null;
    }

    PathAndDistance (double d, EdgeIfc e) {

      distance = d;
      edge = e;
    }
  }

  /**
   * Used to update the distance and edge to a vertex
   *
   * @param key      - The vertex name
   * @param distance - The new distance from the starting vertex to this vertex
   * @param edge     - The first edge in the path from this vertex to the starting vertex
   */
  void setPathAndDistance (String key, double distance, EdgeIfc edge) {

    PathAndDistance pd = pathsTo.get(key);
    if (pd != null) {
      pd.distance = distance;
      pd.edge = edge;
    } else {
      pd = new PathAndDistance(distance, edge);
      pathsTo.put(key, pd);
    }
  }

  /**
   * Traverse the graph to set distance and path information from the "from" vertex to all the vertices
   * that it has a path to.
   *
   * @param graph - The graph
   * @param from  - The vertext to gather path information for
   */
  public ShortestPathImpl (Graph graph, VertexIfc from) {

    unsettled = new ArrayList<VertexIfc>(10);
    settled = new ArrayList<VertexIfc>(graph.getSize());
    pathsTo = new HashMap<String, PathAndDistance>(graph.getSize());

    for (Vertex v : graph.getVertices()) {
      setPathAndDistance(v.getName(), Double.POSITIVE_INFINITY, null);
    }

    unsettled.add(from);
    setPathAndDistance(from.getName(), 0d, null);

    while (!unsettled.isEmpty()) {
      VertexIfc evalVertex = getVertexWithShortestDistance();
      unsettled.remove(evalVertex);
      settled.add(evalVertex);
      evaluateAdjacentVertices(evalVertex);
    }
  }

  /**
   * Used to find the next vertex to evaluate in the list of unsettled vertices.
   *
   * @return The vertex that the shortest distance from the starting vertex
   */
  private VertexIfc getVertexWithShortestDistance () {

    VertexIfc ret = null;
    if (unsettled.isEmpty()) {
      return ret;
    }

    Double shortest = Double.POSITIVE_INFINITY;
    for (VertexIfc v : unsettled) {
      PathAndDistance pd = pathsTo.get(v.getName());
      Double thisDistance = pd.distance;
      if (thisDistance < shortest) {
        shortest = thisDistance;
        ret = v;
      }
    }

    return ret;
  }

  /**
   * Looks at all the edges of the vertex to find the shortest distance from this vertex to its adjacent vertices.
   *
   * @param evalVertex - The vertex being evaluated
   */
  private void evaluateAdjacentVertices (VertexIfc evalVertex) {

    for (EdgeIfc e : evalVertex.outgoingEdges()) {
      VertexIfc to = e.to();
      if (!settled.contains(to)) {
        PathAndDistance pdEval = pathsTo.get(evalVertex.getName());
        PathAndDistance pdTo = pathsTo.get(to.getName());
        Double edgeDistance = Double.valueOf(e.getWeight());
        Double newDistance = pdEval.distance + edgeDistance;
        if (pdTo.distance > newDistance) {
          pdTo.distance = newDistance;
          pdTo.edge = e;
          unsettled.add(to);
        }
      }
    }
  }

  /**
   * @param to - The destination vertex
   * @return The distance between the vertices
   */
  @Override
  public double distanceTo (Vertex to) {

    PathAndDistance pd = pathsTo.get(to.getName());
    return pd.distance;
  }

  /**
   * @param to - The destination vertex
   * @return True if there is a path between the vertices, false otherwise
   */
  @Override
  public boolean hasPathTo (Vertex to) {

    return distanceTo(to) < Double.POSITIVE_INFINITY;
  }

  /**
   * @param to - The destination vertex
   * @return The path between the vertices
   */
  @Override
  public Stack<Edge> pathTo (Vertex to) {

    if (!hasPathTo(to)) {
      return null;
    }

    Stack<Edge> path = new Stack<>();
    PathAndDistance pd = pathsTo.get(to.getName());
    EdgeIfc e = pd.edge;
    while (e != null) {
      path.push(e);
      pd = pathsTo.get(e.from().getName());
      e = pd.edge;
    }

    return path;
  }

}
