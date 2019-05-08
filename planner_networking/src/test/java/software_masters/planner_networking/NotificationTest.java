package software_masters.planner_networking;

import static org.junit.Assert.assertTrue;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.junit.*;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
		scene = stage.getScene();
		clickOn("#userText");
		write("user");
		clickOn("#passText");
		write("user");
		clickOn("#loginButton");
		scene=stage.getScene();
		System.out.println("logged in fine apparently");
		changeSomething();
		System.out.println("changed something");
		scene=stage.getScene();
		Button ok=(Button) scene.lookup("#ok");
		assertTrue(ok.getText().equals("ok"));
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
