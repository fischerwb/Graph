package Graph;

import org.json.simple.JSONAware;

public interface EdgeIfc extends Edge, JSONAware {

  public VertexIfc from ();

  public VertexIfc to ();

}
