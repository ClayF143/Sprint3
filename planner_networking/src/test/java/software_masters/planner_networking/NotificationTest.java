package software_masters.planner_networking;

import static org.junit.Assert.assertTrue;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.junit.*;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class NotificationTest extends ApplicationTest {

	
	//A better test would be to actually get two computers and
	//login with different users on both and change something on each.
	//This does the same thing but does it all locally.
	private Stage stage;
	private Scene scene;
	
	@BeforeClass
	public static void setUpClass() throws Exception
	{
		ApplicationTest.launch(Driver.class);
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		this.stage = stage;
		stage.show();
	}
	
	@Test
	public void notificationTest() throws IllegalArgumentException, RemoteException, NotBoundException, InterruptedException
	{
		//login
		scene = stage.getScene();
		clickOn("#userText");
		write("user");
		clickOn("#passText");
		write("user");
		clickOn("#loginButton");
		
		//editing something and saving shouldn't bring the popup on your screen
		scene=stage.getScene();
		clickOn("#yearDropdown");
		type(KeyCode.DOWN);
		type(KeyCode.ENTER);
		clickOn("#yearSelectButton");
		clickOn("#editButton");
		clickOn("#tree");
		type(KeyCode.DOWN);
		clickOn("#contentField");
		clickOn("#contentField");
		clickOn("#contentField");
		write("ducks");
		clickOn("#saveButton");
		clickOn("#logout");
		scene = stage.getScene();
		clickOn("#no");
		scene=stage.getScene();
		TextField t=(TextField)scene.lookup("#userText");
		assert(t!=null);
		
		//login
		clickOn("#userText");
		write("user");
		clickOn("#passText");
		write("user");
		clickOn("#loginButton");
		scene = stage.getScene();
		
		//someone else changing things should bring a popup on the screen
		changeSomething();
		clickOn("#logout");
		try
		{
			clickOn("#no");
		}
		catch(Exception e){}
		t=(TextField)scene.lookup("#userText");
		assertTrue(t==null);
	}
	
	private void changeSomething() throws IllegalArgumentException, RemoteException, NotBoundException
	{
		Registry registry = LocateRegistry.getRegistry(1060);
		Server stub = (Server) registry.lookup("PlannerServer");
		Client client=new Client(stub);
		client.login("admin", "admin");
		client.getPlan1("2019");
		client.setCurrNode(client.getCurrNode1().getChildren().get(0));
		client.addBranch();
		client.pushPlan(client.getCurrPlanFile());
	}

}
