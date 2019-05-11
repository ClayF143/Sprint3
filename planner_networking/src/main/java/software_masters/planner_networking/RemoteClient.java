package software_masters.planner_networking;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteClient extends Remote {
	
	void update(String a) throws RemoteException;

	Server getServer() throws RemoteException;
	
	String getCookie() throws RemoteException;

	void setObserverID(int id) throws RemoteException;
	
	int getObserverID() throws RemoteException;

}
