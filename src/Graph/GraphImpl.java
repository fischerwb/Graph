package Graph;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.Writer;
import java.util.*;
import java.util.List;

public class GraphImpl implements Graph, JSONAware {

  /**
   * Vector<Vertex> of graph vertices
   */
  private final Map<String, VertexImpl> vertices;

  /**
   * Vector<Edge> of edges in the graph
   */
  private final Set<EdgeImpl> edges;


  /**
   * Construct a new graph without any vertices or edges
   */
  public GraphImpl () {

    vertices = new HashMap<>();
    edges = new HashSet<>();
  }

  /**
   * @return A string form of the graph using the JSON string representation
   */
  @Override
  public String toString () {

    StringBuffer tmp = new StringBuffer("Graph.Graph ");
    tmp.append(toJSONString());
    return tmp.toString();
  }

  /**
   * @param o - Object, possibly a graph, to compare to this graph
   * @return True if the object matches this graph, false otherwise
   */
  @Override
  public boolean equals (Object o) {

    if (this == o) {
      return true;
    }

    if ((o == null) || (this.getClass() != o.getClass())) {
      return false;
    }

    GraphImpl graph = (GraphImpl) o;
    return vertices.equals(graph.vertices) && edges.equals(graph.edges);
  }

  /**
   * @return The hashcode for this graph
   */
  @Override
  public int hashCode () {

    int hash = 7;
    hash = 31 * hash + vertices.hashCode();
    hash = 31 * hash + edges.hashCode();
    return hash;
  }

  /**
   * @return A string containing the JSON representation of this graph
   */
  @Override
  public String toJSONString () {

    return getJSON().toString();
  }

  /**
   * Writes the JSON string representation of this graph to a Writer.
   *
   * @param writer - The writer to be used to store the JSON string
   * @return True if the JSON string is successfully written to the writer, false otherwise
   */
  @Override
  public boolean save (Writer writer) {

    JSONObject json = getJSON();

    try {
      JSONObject.writeJSONString(json, writer);
    }
    catch (Exception e) {
      return false;
    }

    return true;
  }

  /**
   * Reads a JSON string from a Reader, then populates the graph with the vertices and
   * edges contained in the JSON string.
   *
   * @param reader - The reader to be used to access the JSON string
   * @return True if the graph is successfully populated with the vertices and edges in the JSON string, false otherwise
   */
  @Override
  public boolean load (Reader reader) {

    BufferedReader bufferReader = new BufferedReader(reader);
    StringBuffer json = new StringBuffer();
    String line;

    try {
      while ((line = bufferReader.readLine()) != null) {
        json.append(line);
      }

      bufferReader.close();
    }
    catch (Exception ex) {
      return false;
    }

    JSONParser parser = new JSONParser();
    JSONObject obj;

    try {
      obj = (JSONObject) parser.parse(json.toString());
    }
    catch (ParseException e) {
      return false;
    }

    /*
    Walk through object adding vertices and edges.
     */
    JSONArray varray = (JSONArray) obj.get("vertices");
    for (Object vobj : varray) {
      JSONObject v = (JSONObject) vobj;
      JSONObject l = (JSONObject) v.get("location");
      Point location = new Point(((Long) l.get("x")).intValue(), ((Long) l.get("y")).intValue());
      addVertex((String) v.get("name"), location, (String) v.get("data"));
    }

    JSONArray earray = (JSONArray) obj.get("edges");
    for (Object eobj : earray) {
      JSONObject e = (JSONObject) eobj;
      String fromName = (String) e.get("from");
      String toName = (String) e.get("to");
      Vertex from = getVertex(fromName);
      Vertex to = getVertex(toName);

      addEdge(from, to, (String) e.get("label"), ((Long) e.get("weight")).intValue(), (boolean) e.get("directed"));
    }

    return true;
  }

  /**
   * @return The number of vertices in the graph
   */
  @Override
  public int getSize () {

    return vertices.size();
  }

  /**
   * Are there any vertices in the graph
   *
   * @return true if there are no vertices in the graph, false otherwise
   */
  @Override
  public boolean isEmpty () {

    return vertices.isEmpty();
  }

  /**
   * @param name - The name of the vertex to retrieve
   * @return The vertex with the matching name, null if the vertex isn't in the graph
   */
  @Override
  public Vertex getVertex (String name) {

    return findVertex(name);
  }

  /**
   * @param location - The location to determine if a vertex resides at
   * @return The vertex at the location, null if there isn't a vertex at that location
   */
  @Override
  public Vertex getVertexAtLocation (Point location) {

    for (VertexImpl v : vertices.values()) {
      if (v.getLocation().equals(location)) {
        return v;
      }
    }

    return null;
  }

  /**
   * @return A list of the vertices in the graph
   */
  @Override
  public List<Vertex> getVertices () {

    return new ArrayList<Vertex>(this.vertices.values());
  }

  /**
   * Adds a vertex to the graph, if the given name is valid, and a vertex with that name isn't present in the graph
   *
   * @param name     - The name for the new vertex
   * @param location - The location of the new vertex
   * @param data     - The data for the vertex
   * @return True if the vertex is added to the graph, false if it already existed or there was an problem
   * @throws IllegalArgumentException If the name is null or empty
   */
  @Override
  public boolean addVertex (String name, Point location, String data) throws IllegalArgumentException {

    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Vertex name cannot be empty");
    }

    if (!vertices.containsKey(name)) {
      VertexImpl v = new VertexImpl(name, location, data);
      vertices.put(name, v);
      return vertices.containsKey(name);
    }

    return false;
  }

  /**
   * Removes the vertex from the graph, and removes all the edges adjacent to the vertex
   *
   * @param name - The name of the vertex to remove
   * @return True if the edge was removed, false if it doesn't exist or there was a problem
   */
  @Override
  public boolean removeVertex (String name) {

    VertexImpl v = vertices.get(name);

    if (v == null) {
      return false;
    }

    vertices.remove(name);

    // Remove the edges associated with v
    return v.removeEdges(this);
  }

  /**
   * Removes all the vertices and edges from the graph
   */
  @Override
  public void clear () {

    ArrayList<String> toRemove = new ArrayList<>(vertices.size());

    for (String name : vertices.keySet()) {
      toRemove.add(name);
    }

    for (String name : toRemove) {
      removeVertex(name);
    }
  }

  /**
   * Insert a directed, weighted Edge into the graph.
   *
   * @param from     - The Edge starting vertex
   * @param to       - The Edge ending vertex
   * @param label    - The Edge label
   * @param weight   - The Edge weight
   * @param directed - True if the edge has a direction, false if it is bidirectional
   * @return true if the Edge was added, false if from already has this Edge
   * @throws IllegalArgumentException If from/to are not vertices in the graph
   */
  @Override
  public boolean addEdge (Vertex from, Vertex to, String label, int weight, boolean directed)
      throws IllegalArgumentException {

    if (!vertices.containsKey(from.getName())) {
      throw new IllegalArgumentException("from is not in graph");
    }

    if (!vertices.containsKey(to.getName())) {
      throw new IllegalArgumentException("to is not in graph");
    }

    boolean added = addDirectedEdge(from, to, label, weight, directed);

    if (!directed) {
      added = added && addDirectedEdge(to, from, label, weight, directed);
    }

    return added;
  }

  /**
   * @return The list of the edges in the graph
   */
  @Override
  public List<Edge> getEdges () {

    return new ArrayList<Edge>(this.edges);
  }

  /**
   * Remove an Edge from the graph
   *
   * @param from - The Edge starting vertex
   * @param to   -The Edge ending vertex
   * @return true if the Edge exists, false otherwise
   */
  @Override
  public boolean removeEdge (Vertex from, Vertex to) {

    boolean removed = false;
    VertexImpl actualFrom = findVertex(from.getName());
    VertexImpl actualTo = findVertex(to.getName());
    EdgeImpl e = actualFrom.find(actualTo);
    if (e != null) {
      actualFrom.removeEdge(e);
      actualTo.removeEdge(e);
      removed = edges.remove(e);

      if (!e.getDirected()) {
        e = actualTo.find(actualFrom);
        if (e != null) {
          actualFrom.removeEdge(e);
          actualTo.removeEdge(e);
          removed = removed && edges.remove(e);
        }
      }
    }

    return removed;
  }

  /**
   * Does a breadth first search to determine if all the vertices can be reached from the starting point
   *
   * @param startingPoint - The vertex at which to start the search, may be null. If null, just use the
   *                      first vertex found in the vertices map
   * @return True if all the vertices can be reached, false otherwise
   */
  @Override
  public boolean isGraphConnected (Vertex startingPoint) {

    // if 0 or 1 vertices, return true
    int numVertices = getSize();
    if (numVertices <= 1) {
      return true;
    }

    VertexImpl v = null;
    if (startingPoint == null) {
      Iterator<VertexImpl> it = vertices.values().iterator();
      if (it.hasNext()) {
        v = it.next();
      }
    } else {
      v = findVertex(startingPoint.getName());
    }

    if (v != null) {
      // Do breadth-first or depth-first search, counting all vertices reached
      // If number of vertices counted equals the number of vertices in the graph, return true
      try {
        this.breadthFirstSearch(v, null);
      }
      catch (Exception ex) {
      }
    }

    int visitCount = 0;
    for (VertexImpl vtx : vertices.values()) {
      if (vtx.visited()) {
        visitCount++;
      }
    }

    return numVertices == visitCount;
  }

  /**
   * Does a breadth first search to determine if there is a path from the from vertex
   * to the to vertex
   *
   * @param from - The vertex where the search will start
   * @param to   - The destination vertex
   * @return True if there is a path from "from" to "to"
   */
  @Override
  public boolean areVerticesConnected (Vertex from, Vertex to) {

    if (from == null || to == null) {
      return false;
    }

    final VertexImpl f = findVertex(from.getName());
    final VertexImpl t = findVertex(to.getName());

    if (f == null || t == null) {
      return false;
    }

    final VisitorEX<RuntimeException> visitor = new VisitorEX<RuntimeException>() {

      public void visiting (Graph g, VertexImpl v) throws RuntimeException {

        if (v != null && v.equals(t)) {
          // throw an exception once we find the destination vertex
          throw new RuntimeException("Found destination vertex");
        }
      }
    };

    try {
      this.breadthFirstSearch(f, visitor);
    }
    catch (Exception ex) {
    }

    // If we visited both the from and to vertices, then they are connected
    return f.visited() && t.visited();
  }

  /**
   * Used internally to get the edges in the graph
   *
   * @return The set of the edges in the graph
   */
  Set<EdgeImpl> edges () {

    return edges;
  }

  /**
   * @return The JSON object that represents this graph
   */
  JSONObject getJSON () {

    JSONObject obj = new JSONObject();
    JSONParser parser = new JSONParser();

    JSONArray jsonVertices = new JSONArray();
    for (VertexImpl v : vertices.values()) {
      try {
        JSONObject jsonVertex = (JSONObject) parser.parse(v.toJSONString());
        jsonVertices.add(jsonVertex);
      }
      catch (ParseException e) {
      }
    }
    obj.put("vertices", jsonVertices);

    JSONArray jsonEdges = new JSONArray();
    for (EdgeImpl e : edges.toArray(new EdgeImpl[edges.size()])) {
      try {
        JSONObject jsonEdge = (JSONObject) parser.parse(e.toJSONString());
        jsonEdges.add(jsonEdge);
      }
      catch (ParseException ex) {
      }
    }
    obj.put("edges", jsonEdges);

    return obj;
  }

  /**
   * @param name - The name of the vertex to retrieve
   * @return The vertex with the matching name, null if the vertex isn't in the graph
   */
  VertexImpl findVertex (String name) {

    return vertices.get(name);
  }

  /**
   * Adds a directed edge to a vertex. For bidirectional edges, will be call twice when the edge is added.
   *
   * @param from     - The Edge starting vertex
   * @param to       - The Edge ending vertex
   * @param label    - The Edge label
   * @param weight   - The Edge weight
   * @param directed - True if the edge has a direction, false if it is bidirectional
   * @return True if the edge was added to the vertex and the graph
   */
  private boolean addDirectedEdge (Vertex from, Vertex to, String label, int weight, boolean directed) {

    boolean added = false;
    if (!from.hasEdge(to)) {
      VertexImpl actualFrom = findVertex(from.getName());
      VertexImpl actualTo = findVertex(to.getName());
      EdgeImpl e = new EdgeImpl(actualFrom, actualTo, label, weight, directed);
      actualFrom.addEdge(e);
      actualTo.addEdge(e);
      edges.add(e);
      added = true;
    }

    return added;
  }

  /**
   * Initializes the vertices by clearing the visited flag
   */
  private void initializeBreadthFirstSearch () {

    for (VisitAware v : vertices.values()) {
      v.leave();
    }
  }

  /**
   * Perform a breadth first search of this graph, starting at v. The visit may
   * be cut short if visitor throws an exception during a visit callback.
   *
   * @param v       - The search starting point
   * @param visitor - The visitor whose visiting method is called after visiting a vertex.
   * @throws E if visitor.visiting throws an exception
   */
  private <E extends Exception> void breadthFirstSearch (VertexImpl v, VisitorEX<E> visitor) throws E {

    LinkedList<VertexImpl> q = new LinkedList<VertexImpl>();

    initializeBreadthFirstSearch();

    q.add(v);
    v.visit();
    if (visitor != null) {
      visitor.visiting(this, v);
    }
    while (!q.isEmpty()) {
      v = q.removeFirst();
      for (EdgeImpl e : v.outgoingEdges()) {
        VertexImpl to = e.to();
        if (!to.visited()) {
          q.add(to);
          to.visit();
          if (visitor != null) {
            visitor.visiting(this, to);
          }
        }
      }
    }
  }

  /**
   * Return an interface that can be used to get path information
   * for the from vertex.
   *
   * @param from - The vertex to get path information for
   * @return The interface to use to find distances and paths to other vertices
   */
  @Override
  public ShortestPath getPathInformation(Vertex from) {
    return new ShortestPathImpl(this, findVertex(from.getName()));
  }

}
