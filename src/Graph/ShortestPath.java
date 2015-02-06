package Graph;

import java.util.Stack;

public interface ShortestPath {

  /**
   *
   * @param to - The destination vertex
   *
   * @return The distance between the vertices
   */
  public double distanceTo(Vertex to);

  /**
   *
   * @param to - The destination vertex
   *
   * @return True if there is a path between the vertices, false otherwise
   */
  public boolean hasPathTo(Vertex to);

  /**
   *
   * @param to - The destination vertex
   *
   * @return The path between the vertices
   */
  public Stack<Edge> pathTo(Vertex to);

}
