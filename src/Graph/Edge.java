package Graph;

public interface Edge {

  /**
   * @return The possibly null label of the edge
   */
  public String getLabel ();

  /**
   * Sets the label for the edge
   *
   * @param l - The label for this edge
   */
  public void setLabel (String l);

  /**
   * @return The weight for this edge
   */
  public int getWeight ();

  /**
   * Sets the weight for this edge
   *
   * @param w - The weight for the edge
   */
  public void setWeight (int w);

  /**
   * @return True if this edge has a direction, false if bedirectional
   */
  public boolean getDirected ();

  /**
   * @return The starting vertex for this edge
   */
  public Vertex getFrom ();

  /**
   * @return The ending vertex for this edge
   */
  public Vertex getTo ();

}
