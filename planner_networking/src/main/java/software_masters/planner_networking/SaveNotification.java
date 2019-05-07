package software_masters.planner_networking;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SaveNotification {

	static Stage stage;
	static boolean btnYesClicked;

	/**
	 * Creates a confirmation box to be implemented in the application
	 * 
	 * @param message
	 * @param title
	 * @param textYes
	 * @param textNo
	 * @return
	 */
	public static void show()
	{
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Notification");
		stage.setMinWidth(250);

		Label lbl = new Label("Another user saved a plan within your department");
		Button ok = new Button();
		ok.setText("Ok");
		ok.setOnAction(e -> okbtnClicked(stage));
		ok.setId("ok");

		VBox pane=new VBox(50);
		pane.getChildren().addAll(lbl,ok);
		pane.setAlignment(Pos.CENTER);
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.showAndWait();
	}
	
	private static void okbtnClicked(Stage stage)
	{
		stage.close();
	}
}
