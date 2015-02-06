package Graph;

interface VisitorEX<E extends Exception> {

  /**
   * Called by the graph traversal methods when a vertex is first visited.
   *
   * @param g - The graph
   * @param v - The vertex being visited.
   * @throws E exception for any error
   */
  public void visiting (Graph g, VertexImpl v) throws E;

}
