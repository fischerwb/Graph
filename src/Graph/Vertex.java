package Graph;

import java.awt.*;

public interface Vertex {

  /**
   * @return The name of the vertex, should never be null
   */
  public String getName ();

  /**
   * @return The location of this vertex on the graph
   */
  public Point getLocation ();

  /**
   * Used to set the new location of the vertex in the case where the vertext is moved within the graph
   *
   * @param l The location of the vertex on the graph
   */
  public void setLocation (Point l);

  /**
   * @return The possibly null data of the vertex
   */
  public String getData ();

  /**
   * Used to set an extra data string for the vertex
   *
   * @param d The data for the vertex.
   */
  public void setData (String d);

  /**
   * Is there an outgoing edge ending at v.
   *
   * @param v - The vertex to check
   * @return true if there is an outgoing edge ending at vertex, false otherwise.
   */
  public boolean hasEdge (Vertex v);

  /**
   * Search the outgoing edges looking for an edge whose edge.to == v.
   *
   * @param v The destination
   * @return The outgoing edge going to v if one exists, null otherwise.
   */
  public Edge findEdge (Vertex v);
}
