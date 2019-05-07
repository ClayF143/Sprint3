package software_masters.planner_networking;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Observable;

public interface RemoteObserver extends Remote {
	void update(Observable o, Object arg) throws RemoteException;
}
