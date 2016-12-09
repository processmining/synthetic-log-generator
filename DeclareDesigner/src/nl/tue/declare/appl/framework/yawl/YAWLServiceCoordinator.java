package nl.tue.declare.appl.framework.yawl;

import java.util.*;

import nl.tue.declare.execution.*;
import yawlservice.*;

/**
 * <p>
 * Title: DECLARE
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: TU/e
 * </p>
 * 
 * @author Maja Pesic
 * @version 1.0
 */
public abstract class YAWLServiceCoordinator {

	private YAWLServiceServer server;
	private int clientPort;
	private String clientHost;

	private HashMap<ExternalCase, WorkItem> launched;

	public YAWLServiceCoordinator(int serverPort, int clientPort,
			String clientHost) {
		super();
		this.clientPort = clientPort;
		this.clientHost = clientHost;
		launched = new HashMap<ExternalCase, WorkItem>();

		server = new YAWLServiceServer(serverPort) {
			public void received(YAWLMessage request) {
				YAWLServiceCoordinator.this.received(request);
			}
		};
		server.start();
	}

	private YAWLServiceClient getYAWLServiceClient() {
		try {
			return new YAWLServiceClient(clientHost, clientPort);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private boolean send(YAWLMessage request) {
		YAWLServiceClient client = getYAWLServiceClient();
		if (client != null) {
			return client.send(request);
		}
		;
		return false;
	}

	private String request(YAWLMessage request) {
		YAWLServiceClient client = getYAWLServiceClient();
		if (client != null) {
			return client.request(request);
		}
		;
		return null;
	}

	private void received(YAWLMessage request) {
		if (request instanceof WorkItemMessage) { // it's a WorkItem
			ExternalWorkItem wi = ((WorkItemMessage) request).getWorkItem();
			if (request instanceof StartWorkItemMessage) {
				workItemArrived(wi);
			}
		} else {
			if (request instanceof CompleteCaseMessage) {
				ExternalCase cs = ((CompleteCaseMessage) request).getCase();
				WorkItem wi = launched.get(cs);				
				if (cs != null && wi != null){
					caseCompleted(wi,cs);
				}
			}
		}

	}

	public abstract void workItemArrived(ExternalWorkItem wi);
	public abstract void caseCompleted(WorkItem wi,ExternalCase cs);

	public boolean returnWorkItem(ExternalWorkItem wi) {
		return send(new CompleteWorkItemMessage(wi));
	}

	public boolean launchInstance(WorkItem wi, ExternalCase ec) {
		this.createData(wi, ec);
		String id = request(new LaunchCaseMessage(ec));
		if (id != null) {
			ec.setCaseID(id);
			launched.put(ec, wi);
			return true;
		}
		return false;
	}

	private void createData(WorkItem wi, ExternalCase ec) {
		for (int i = 0; i < wi.dataCount(); i++) {
			WorkItemData element = wi.getDataAt(i);
			String data = element.getData().getDataElement().getName();
			String value = element.getValue().toString();
			if (value == null)
				value = "UNKNOWN";
			ec.addData(new CaseDataElement(data, value));
		}
	}
}
