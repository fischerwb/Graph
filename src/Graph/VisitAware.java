package Graph;

public interface VisitAware {

  /**
   * @return True is this object has been visited during the search
   */
  public boolean visited ();

  /**
   * Set the flag to indicate that the object has been visited
   */
  public void visit ();

  /**
   * Clear the flag to indicate that the object has not been visited
   */
  public void leave ();

}
