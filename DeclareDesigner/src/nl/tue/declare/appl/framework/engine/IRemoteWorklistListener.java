package nl.tue.declare.appl.framework.engine;


/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author Maja Pesic
 * @version 1.0
 */
public interface IRemoteWorklistListener {
  public void disconnected(RemoteWorklist worklist);
  //public IMessage request(IMessage msg, IWorklistRemoteProxy proxy);
}
