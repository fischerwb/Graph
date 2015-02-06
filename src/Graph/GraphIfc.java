package Graph;

import org.json.simple.JSONAware;

import java.util.List;
import java.util.Set;

public interface GraphIfc extends Graph, JSONAware {

  public List<Vertex> vertices ();

  public Set<EdgeIfc> edges ();

}
