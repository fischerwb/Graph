package Graph;

import org.json.simple.JSONAware;

import java.util.List;

public interface VertexIfc extends Vertex, VisitAware, JSONAware {

  public boolean addEdge (EdgeIfc e);

  public boolean removeEdge (EdgeIfc e);

  public boolean removeEdges (GraphIfc graph);

  public EdgeIfc find (Vertex v);

  public List<EdgeIfc> incomingEdges ();

  public List<EdgeIfc> outgoingEdges ();

}
