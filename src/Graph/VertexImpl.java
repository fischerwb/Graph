package Graph;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VertexImpl implements Vertex, JSONAware, VisitAware {

  private final List<EdgeImpl> incomingEdges;

  private final List<EdgeImpl> outgoingEdges;

  private final String name;

  private Point location;

  private String data;

  private boolean visited;

  /**
   * Creates a vertex with name n and location l
   *
   * @param n - name of vertex
   * @param l - location of vertex
   */
  public VertexImpl (String n, Point l) {

    this(n, l, null);
  }

  /**
   * Create a Vertex with name n, location l, and given data
   *
   * @param n - name of vertex
   * @param l - location of vertex
   * @param d - data associated with vertex
   */
  public VertexImpl (String n, Point l, String d) {

    incomingEdges = new ArrayList<>();
    outgoingEdges = new ArrayList<>();
    name = n;
    location = l;
    data = d;
  }

  /**
   * @return a string form of the vertex with in and out edges.
   */
  @Override
  public String toString () {

    // Use the JSON string for the vertex
    StringBuffer tmp = new StringBuffer("Graph.Vertex ");
    tmp.append(toJSONString());
    return tmp.toString();
  }

  /**
   * @param o - Object, possibly a vertex, to compare to this vertex
   * @return True if the object matches this vertex, false otherwise
   */
  @Override
  public boolean equals (Object o) {

    if (this == o) {
      return true;
    }

    if ((o == null) || (this.getClass() != o.getClass())) {
      return false;
    }

    VertexImpl vertex = (VertexImpl) o;
    return this.name.equals(vertex.name) &&
        this.location.equals(location) &&
        this.data.equals(vertex.data) &&
        this.outgoingEdges.equals(vertex.outgoingEdges) &&
        this.incomingEdges.equals(vertex.incomingEdges);
  }

  /**
   * @return The hashcode for this vertex
   */
  @Override
  public int hashCode () {

    int hash = 7;
    hash = 31 * hash + name.hashCode();
    hash = 31 * hash + location.hashCode();
    hash = 31 * hash + data.hashCode();
    hash = 31 * hash + outgoingEdges.hashCode();
    hash = 31 * hash + incomingEdges.hashCode();
    return hash;
  }

  /**
   * @return A string containing the JSON representation of this vertex
   */
  @Override
  public String toJSONString () {
    //Create a JSON object for this vertex, then get the string representation
    return getJSON().toString();
  }

  /**
   * @return The name of the vertex, should never be null
   */
  @Override
  public String getName () {

    return name;
  }

  /**
   * @return The location of this vertex on the graph
   */
  @Override
  public Point getLocation () {

    return location;
  }

  /**
   * Used to set the new location of the vertex in the case where the vertext is moved within the graph
   *
   * @param l The location of the vertex on the graph
   */
  @Override
  public void setLocation (Point l) {

    location = l;
  }

  /**
   * @return The possibly null data of the vertex
   */
  @Override
  public String getData () {

    return data;
  }

  /**
   * Used to set an extra data string for the vertex
   *
   * @param d The data for the vertex.
   */
  @Override
  public void setData (String d) {

    data = d;
  }

  /**
   * Is there an outgoing edge ending at v.
   *
   * @param v - The vertex to check
   * @return true if there is an outgoing edge ending at vertex, false otherwise.
   */
  @Override
  public boolean hasEdge (Vertex v) {

    return (findEdge(v) != null);
  }

  /**
   * Search the outgoing edges looking for an edge whose edge.to == v.
   *
   * @param v The destination
   * @return The outgoing edge going to v if one exists, null otherwise.
   */
  @Override
  public Edge findEdge (Vertex v) {

    return find(v);
  }

  /**
   *
   * @return True is this object has been visited during the search
   */
  @Override
  public boolean visited () {

    return visited;
  }

  /**
   * Set the flag to indicate that the object has been visited
   */
  @Override
  public void visit () {

    visited = true;
  }

  /**
   * Clear the flag to indicate that the object has not been visited
   */
  @Override
  public void leave () {

    visited = false;
  }

  /**
   * Search the outgoing edges looking for an edge whose edge.to == v.
   *
   * @param v - The vertex that we are looking for a outgoing edge that connects to
   * @return The outgoing edge going to v if one exists, null otherwise.
   */
  EdgeImpl find (Vertex v) {

    for (EdgeImpl e : outgoingEdges) {
      if (e.getTo().equals(v)) {
        return e;
      }
    }

    return null;
  }

  /**
   * Add an edge to the vertex. If edge.from is this vertex, its an outgoing
   * edge. If edge.to is this vertex, its an incoming edge. If neither from or
   * to is this vertex, the edge is not added.
   *
   * @param e - The edge to add
   * @return true if the edge was added, false otherwise
   */
  boolean addEdge (EdgeImpl e) {

    if (e.getFrom().equals(this)) {
      outgoingEdges.add(e);
    } else if (e.getTo().equals(this)) {
      incomingEdges.add(e);
    } else {
      return false;
    }

    return true;
  }

  /**
   * Remove an edge from this vertex
   *
   * @param e - The edge to removeEdge
   * @return true if the edge was removed, false if the edge was not connected
   *         to this vertex
   */

  boolean removeEdge (EdgeImpl e) {

    if (e.getFrom().equals(this)) {
      outgoingEdges.remove(e);
    } else if (e.getTo().equals(this)) {
      incomingEdges.remove(e);
    } else {
      return false;
    }

    return true;
  }

  /**
   * Removes all the edges in the vertex from this vertex, and from the graph
   *
   * @param graph The graph this vertex is contained in
   * @return true if all the edges are successfully remove, false otherwise
   */
  boolean removeEdges (GraphImpl graph) {

    removeEdges(graph, outgoingEdges.iterator(), false);
    removeEdges(graph, incomingEdges.iterator(), true);

    return outgoingEdges.isEmpty() && incomingEdges.isEmpty();
  }

  /**
   * Get the incoming edges
   *
   * @return Incoming edge list
   */
  List<EdgeImpl> incomingEdges () {

    return incomingEdges;
  }

  /**
   * Get the outgoing edges
   *
   * @return Outgoing edge list
   */
  List<EdgeImpl> outgoingEdges () {

    return outgoingEdges;
  }

  /**
   * @return The JSON object that represents this vertex
   */
  JSONObject getJSON () {

    JSONObject obj = new JSONObject();
    JSONObject locationObj = new JSONObject();

    locationObj.put("x", location.x);
    locationObj.put("y", location.y);

    obj.put("name", name);
    obj.put("location", locationObj);
    obj.put("data", data);
    obj.put("incomingEdges", edgesToJSON(incomingEdges));
    obj.put("outgoingEdges", edgesToJSON(outgoingEdges));

    return obj;
  }

  /**
   * @param edges - The edge list to create JSON for, can be the outgoing or incoming edges
   * @return The JSONArray representing the edges
   */
  private JSONArray edgesToJSON (List<EdgeImpl> edges) {

    JSONArray arr = new JSONArray();
    JSONParser parser = new JSONParser();

    for (EdgeImpl e : edges) {
      try {
        JSONObject jsonVertex = (JSONObject) parser.parse(e.toJSONString());
        arr.add(jsonVertex);
      }
      catch (ParseException ex) {
      }
    }

    return arr;
  }

  /**
   * Remove all the edges in the iterator from the vertex and the graph
   *
   * @param graph - The graph the vertex is contained within
   * @param it    - Iterator to use to loop through the edges
   * @param from  - True if removing the incoming edges, false for the outgoing edges
   */
  private void removeEdges (GraphImpl graph, Iterator<EdgeImpl> it, boolean from) {

    while (it.hasNext()) {
      EdgeImpl e = it.next();

      VertexImpl v = from ? e.from() : e.to();
      v.removeEdge(e);
      graph.edges().remove(e);

      it.remove();
    }
  }

}
