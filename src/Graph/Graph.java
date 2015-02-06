package Graph;

import java.awt.*;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

public interface Graph {

  /**
   * @return The number of vertices in the graph
   */
  public int getSize ();

  /**
   * Are there any vertices in the graph
   *
   * @return true if there are no vertices in the graph, false otherwise
   */
  public boolean isEmpty ();

  /**
   * @param name - The name of the vertex to retrieve
   * @return The vertex with the matching name, null if the vertex isn't in the graph
   */
  public Vertex getVertex (String name);

  /**
   * @param location - The location to determine if a vertex resides at
   * @return The vertex at the location, null if there isn't a vertex at that location
   */
  public Vertex getVertexAtLocation (Point location);

  /**
   * @return A list of the vertices in the graph
   */
  public List<Vertex> getVertices ();

  /**
   * Adds a vertex to the graph, if the given name is valid, and a vertex with that name isn't present in the graph
   *
   * @param name     - The name for the new vertex
   * @param location - The location of the new vertex
   * @param data     - The data for the vertex
   * @return True if the vertex is added to the graph, false if it already existed or there was an problem
   * @throws IllegalArgumentException If the name is null or empty
   */
  public boolean addVertex (String name, Point location, String data) throws IllegalArgumentException;

  /**
   * Removes the vertex from the graph, and removes all the edges adjacent to the vertex
   *
   * @param name - The name of the vertex to remove
   * @return True if the edge was removed, false if it doesn't exist or there was a problem
   */
  public boolean removeVertex (String name);

  /**
   * Removes all the vertices and edges from the graph
   */
  public void clear ();

  /**
   * @return The list of the edges in the graph
   */
  public List<Edge> getEdges ();

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
  public boolean addEdge (Vertex from, Vertex to, String label, int weight, boolean directed)
      throws IllegalArgumentException;

  /**
   * Remove an Edge from the graph
   *
   * @param from - The Edge starting vertex
   * @param to   -The Edge ending vertex
   * @return true if the Edge exists, false otherwise
   */
  public boolean removeEdge (Vertex from, Vertex to);

  /**
   * Writes the JSON string representation of this graph to a Writer.
   *
   * @param writer - The writer to be used to store the JSON string
   * @return True if the JSON string is successfully written to the writer, false otherwise
   */
  public boolean save (Writer writer);

  /**
   * Reads a JSON string from a Reader, then populates the graph with the vertices and
   * edges contained in the JSON string.
   *
   * @param reader - The reader to be used to access the JSON string
   * @return True if the graph is successfully populated with the vertices and edges in the JSON string, false otherwise
   */
  public boolean load (Reader reader);

  /**
   * Does a breadth first search to determine if all the vertices can be reached from the starting point
   *
   * @param startingPoint - The vertex at which to start the search, may be null. If null, just use the
   *                      first vertex found in the vertices map
   * @return True if all the vertices can be reached, false otherwise
   */
  public boolean isGraphConnected (Vertex startingPoint);

  /**
   * Does a breadth first search to determine if there is a path from the from vertex
   * to the to vertex
   *
   * @param from - The vertex where the search will start
   * @param to   - The destination vertex
   * @return True if there is a path from "from" to "to"
   */
  public boolean areVerticesConnected (Vertex from, Vertex to);

  /**
   * Return an interface that can be used to get path information
   * for the from vertex.
   *
   * @param from - The vertex to get path information for
   * @return The interface to use to find distances and paths to other vertices
   */
  public ShortestPath getPathInformation (Vertex from);

}
