package nl.tue.declare.appl.framework.engine;

import java.util.*;

import nl.tue.declare.domain.organization.*;
import nl.tue.declare.execution.*;
import nl.tue.declare.execution.msg.*;

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
public class RemoteWorklist
    implements IWorklistRemoteProxyListener {

  private int id;
  private User user;
  private ResponseWorklistProxy responseChannel;
  private InformationWorklistProxy informationChannel;
  private IRemoteWorklistListener listener;

  public RemoteWorklist(int id, User user, IRemoteWorklistListener listener) {
    super();
    this.id = id;
    this.user = user;
    this.responseChannel = null;
    this.informationChannel = null;
    this.listener = listener;
  }

  public boolean forUser(User user) {
    return this.user.equals(user);
  }

  public User getUser() {
    return this.user;
  }

  public int getId() {
    return id;
  }

  public void addInformationChannel(InformationWorklistProxy proxy) {
    if (informationChannel == null) {
      informationChannel = proxy;
      informationChannel.addListener(this);
    }
  }

  public void addResponseChannel(ResponseWorklistProxy proxy) {
    if (responseChannel == null) {
      responseChannel = proxy;
      responseChannel.addListener(this);
    }
  }

  private void removeChannel(IWorklistRemoteProxy proxy) {
    if (proxy == responseChannel) {
      responseChannel = null;
    }
    if (proxy == informationChannel) {
      informationChannel = null;
    }
  }

  private boolean disconnected() {
    return ( (responseChannel == null) && (informationChannel == null));
  }

  public boolean connected() {
    return ( (responseChannel != null) && (informationChannel != null));
  }

  /*  public void removeChannel() {
      informationChannel = null;
    }*/

  /**
   *
   * @param state AssignmentState
   * @return AssignmentState
   */
  private AssignmentState authorize(AssignmentState state) {
    AssignmentState authorized = (AssignmentState) state.clone();
    for (int i = 0; i < authorized.eventCount(); i++) {
      AssignmentState.EventState eventState = authorized.eventAt(i);
      if (!authorized.getAssignment().authorized(eventState.event().getActivity(),
                                                 user)) {
        authorized.removeEvent(eventState);
      }
    }
    return authorized;
  }

  public void stateChanged(AssignmentState state) {
    AssignmentState authorizatiton = this.authorize(state);
    IMessage msg = MessageFactory.createStateAssignmentMassage(authorizatiton);
    sendInformation(msg);
  }

  public void sendInformation(IMessage msg) {
    if (informationChannel != null && msg != null) {
      informationChannel.send(msg);
    }
  }

  public void sendChainInformation(List<IMessage> msgs) {
    if (informationChannel != null && msgs != null) {
      Iterator<IMessage> iterator = msgs.iterator();
      while (iterator.hasNext()) {
        informationChannel.send(iterator.next());
      }
      this.informationChannel.requestConfirm();
    }
  }

  public void start() {
    if (responseChannel != null) {
      responseChannel.initiate();
    }
  }

  /* public void received(String msg) {
   }*/

  public void disconnected(IWorklistRemoteProxy proxy) {
    removeChannel(proxy);
    if (disconnected()) {
      if (listener != null) {
        listener.disconnected(this);
      }
    }
  }

  public RemoteWorklist getWorklist() {
    return this;
  }

  public boolean isConnected() {
    return informationChannel.isConnected();
  }

  /* public IMessage request(IMessage msg, IWorklistRemoteProxy proxy) {
     if (listener != null)
       return listener.request(msg,proxy);
     return null;
   }*/
}
