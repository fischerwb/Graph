package Graph;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

class EdgeImpl implements Edge, JSONAware {

  private final VertexImpl from;

  private final VertexImpl to;

  private String label;

  private int weight;

  private final boolean directed;

  public static EdgeImpl createEdge(VertexImpl f, VertexImpl t, String l, int w, boolean d) {
    return new EdgeImpl(f, t, l, w, d);
  }

  /**
   * Create an edge between f and t with label l, weight w, and directed flag set to d
   *
   * @param f - The starting vertex
   * @param t - The ending vertex
   * @param w - The weight of the edge
   * @param d - True if the edge has a direction, false if bidirectional
   */
  private EdgeImpl (VertexImpl f, VertexImpl t, String l, int w, boolean d) {

    from = f;
    to = t;
    label = (l != null) ? l : "";
    weight = (w >= 0) ? w : 0;
    directed = d;
  }

  /**
   * @return A string form of the edge using the JSON string representation
   */
  @Override
  public String toString () {

    StringBuffer tmp = new StringBuffer("Graph.Edge ");
    tmp.append(toJSONString());
    return tmp.toString();
  }

  /**
   * @param o - Object, possibly an edge, to compare to this edge
   * @return True if the object matches this edge, false otherwise
   */
  @Override
  public boolean equals (Object o) {

    if (this == o) {
      return true;
    }

    if ((o == null) || (this.getClass() != o.getClass())) {
      return false;
    }

    EdgeImpl edge = (EdgeImpl) o;
    return this.label.equals(edge.label) &&
        this.weight == edge.weight &&
        this.directed == edge.directed &&
        this.from.getName().equals(edge.from.getName()) &&
        this.to.getName().equals(edge.to.getName());
  }

  /**
   * @return The hashcode for this edge
   */
  @Override
  public int hashCode () {

    int hash = 7;
    hash = 31 * hash + label.hashCode();
    hash = 31 * hash + weight;
    hash = 31 * hash + Boolean.valueOf(directed).hashCode();
    hash = 31 * hash + from.getName().hashCode();
    hash = 31 * hash + to.getName().hashCode();
    return hash;
  }

  /**
   * @return A string containing the JSON representation of this edge
   */
  @Override
  public String toJSONString () {

    return getJSON().toString();
  }

  /**
   * @return The possibly null label of the edge
   */
  @Override
  public String getLabel () {

    return label;
  }

  /**
   * Sets the label for the edge
   *
   * @param l - The label for this edge
   */
  @Override
  public void setLabel (String l) {

    label = (l != null) ? l : label;
  }

  /**
   * @return The weight for this edge
   */
  @Override
  public int getWeight () {

    return weight;
  }

  /**
   * Sets the weight for this edge
   *
   * @param w - The weight for the edge
   */
  @Override
  public void setWeight (int w) {

    weight = (w >= 0) ? w : weight;
  }

  /**
   * @return True if this edge has a direction, false if bedirectional
   */
  @Override
  public boolean getDirected () {

    return directed;
  }

  /**
   * @return The starting vertex for this edge
   */
  @Override
  public Vertex getFrom () {

    return from();
  }

  /**
   * @return The ending vertex for this edge
   */
  @Override
  public Vertex getTo () {

    return to();
  }

  /**
   * Used internally by the Graph
   *
   * @return The ending vertex for this edge
   */
  VertexImpl to () {

    return to;
  }

  /**
   * Used internally by the Graph
   *
   * @return The starting vertex for this edge
   */
  VertexImpl from () {

    return from;
  }

  /**
   * @return The JSON object that represents this edge
   */
  JSONObject getJSON () {

    JSONObject obj = new JSONObject();

    obj.put("label", label);
    if (from != null) {
      obj.put("from", from.getName());
    }
    if (to != null) {
      obj.put("to", to.getName());
    }
    obj.put("weight", weight);
    obj.put("directed", directed);

    return obj;
  }

}

