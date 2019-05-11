package software_masters.planner_networking;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author lee kendall and wesley murray
 */

public class Client extends UnicastRemoteObject implements RemoteClient
{

	/**
	 * This class represents the client which users interact with. It includes
	 * methods for retrieving and editing business plans, keeping track of the
	 * user's cookie after login.
	 * 
	 */
	private String cookie;
	private PlanFile currPlanFile;
	private PlanFile currPlanFile2;
	private Node currNode;
	private Node currNode2;
	private Server server;
	private String username;
	private int observerID;
	
	private static final long serialVersionUID = 1L;

	/**
	 * Sets the client's server.
	 * 
	 * @param server
	 */
	public Client(Server server) throws RemoteException
	{
		this.server = server;
	}
	
	public int getObserverID() throws RemoteException{
		return observerID;
	}

	public void setObserverID(int observerID) throws RemoteException{
		this.observerID = observerID;
	}

	public String getUsername()
	{
		return username;
	}
	

	/**
	 * Logs in, returns cookie
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws IllegalArgumentException
	 */
	public void login(String username, String password) throws IllegalArgumentException, RemoteException
	{
		currPlanFile = null;
		currNode = null;
		cookie = server.login(username, password);
		System.out.println("logged in");
		this.username=username;
	}

	/**
	 * Returns planFile object from the user's department given a year. Throws
	 * exception if that planFile doesn't exist.
	 * 
	 * @param year
	 * @return
	 * @throws IllegalArgumentException
	 */
	public void getPlan1(String year) throws IllegalArgumentException, RemoteException
	{
		currPlanFile = server.getPlan(year, cookie);
		currNode = currPlanFile.getPlan().getRoot();
	}
	
	public void getPlan2(String year) throws IllegalArgumentException, RemoteException
	{
		currPlanFile2 = server.getPlan(year, cookie);
		currNode2 = currPlanFile2.getPlan().getRoot();
	}

	/**
	 * Returns the collection of a department's planFiles
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public Collection<PlanFile> getPlans() throws RemoteException
	{
		return server.getPlans(cookie);

	}

	/**
	 * 
	 * Returns a blank plan outline given a name. Throws exception if the plan
	 * outline doesn't exist.
	 * 
	 * @param name
	 * @return
	 * @throws IllegalArgumentException
	 */
	public void getPlanOutline(String name) throws IllegalArgumentException, RemoteException
	{
		currPlanFile = server.getPlanOutline(name, cookie);
		currNode = currPlanFile.getPlan().getRoot();
	}

	/**
	 * Saves planFile to the user's department if that planFile is marked as
	 * editable. If not editable, an exception is thrown. An exception is also
	 * thrown if a newly created planFile is not assigned a year.
	 * 
	 * @param plan
	 * @throws IllegalArgumentException
	 */
	public void pushPlan(PlanFile plan) throws IllegalArgumentException, RemoteException
	{
		server.savePlan(plan, cookie);
	}

	/**
	 * Adds new user to loginMap, generates new cookie for user and adds to
	 * cookieMap. Throws exception if user isn't an admin or the department doesn't
	 * exist.
	 * 
	 * @param username
	 * @param password
	 * @param departmentName
	 * @param isAdmin
	 * @throws IllegalArgumentException
	 */
	public void addUser(String username, String password, String departmentName, boolean isAdmin)
			throws IllegalArgumentException, RemoteException
	{
		server.addUser(username, password, departmentName, isAdmin, cookie);
	}

	/**
	 * Sets whether or not a planFile is editable
	 * 
	 * @param departmentName
	 * @param year
	 * @param canEdit
	 * @throws IllegalArgumentException
	 */
	public void flagPlan(String departmentName, String year, boolean canEdit)
			throws IllegalArgumentException, RemoteException
	{
		server.flagPlan(departmentName, year, canEdit, cookie);

	}

	/**
	 * Adds a new department
	 * 
	 * @param departmentName
	 * @throws IllegalArgumentException
	 */
	public void addDepartment(String departmentName) throws IllegalArgumentException, RemoteException
	{
		server.addDepartment(departmentName, cookie);

	}

	/**
	 * Adds a new branch to the business plan tree if allowed
	 * 
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	public void addBranch() throws IllegalArgumentException, RemoteException
	{
		ArrayList<String> names = currPlanFile.getPlan().getList();
		int index = names.indexOf(currNode.getName());
		Node temp = currNode.getParent();

		for (int i = index; i < names.size(); i++)
		{
			Node temp1 = new Node(temp, names.get(i), "Insert Content", null);
			temp.getChildren().add(temp1);
			temp = temp1;
		}

	}

	/**
	 * Removes a branch from the business plan tree if at least one duplicate exists
	 * 
	 * @throws IllegalArgumentException
	 */
	public void removeBranch() throws IllegalArgumentException
	{
		Node temp = currNode.getParent();
		currPlanFile.getPlan().removeNode(currNode);
		currNode = temp.getChildren().get(0);
	}

	/**
	 * Sets the data held in the currently accessed node
	 * 
	 * @param data
	 */
	public void editData(String data)
	{
		currNode.setData(data);
	}

	/**
	 * @return the data associated with a node
	 */
	public String getData()
	{
		return currNode.getData();
	}

	/**
	 * @param year
	 */
	public void setYear(String year)
	{
		currPlanFile.setYear(year);
	}

	/**
	 * @return the cookie
	 */
	public String getCookie()
	{
		return cookie;
	}

	/**
	 * @param cookie the cookie to set
	 */
	public void setCookie(String cookie)
	{
		this.cookie = cookie;
	}

	/**
	 * @return the currPlanFile
	 */
	public PlanFile getCurrPlanFile()
	{
		return currPlanFile;
	}
	
	public PlanFile getCurrPlanFile2()
	{
		return currPlanFile2;
	}

	/**
	 * @param currPlanFile the currPlanFile to set
	 */
	public void setCurrPlanFile(PlanFile currPlanFile)
	{
		this.currPlanFile = currPlanFile;
	}

	/**
	 * @return the currNode
	 */
	public Node getCurrNode1()
	{
		return currNode;
	}
	
	public Node getCurrNode2()
	{
		return currNode2;
	}

	/**
	 * @param currNode the currNode to set
	 */
	public void setCurrNode(Node currNode)
	{
		this.currNode = currNode;
	}
	
	public void setCurrNode2(Node currNode)
	{
		this.currNode2=currNode;
	}

	/**
	 * @return the server
	 */
	public Server getServer()
	{
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(Server server)
	{
		this.server = server;
	}

	public void update(String a)
	{
		if(!a.equals(cookie))
		{
			System.out.println(a+" "+cookie);
			SaveNotification.show();
		}
	}

}
