package application;
	
import static javafx.fxml.FXMLLoader.load;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;


/**
 * @author : Gentillo_Curescu 
 *	Main Class of the application that loads and shows the main view
 */
public class Main extends Application {
	
	private static Scene scene;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			 scene = new Scene(loadFXML("/view/LoginView"));
			 primaryStage.setScene(scene);
			 primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void setRooot(String fxml) throws IOException {
	
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

	 
	public static void main(String[] args) {
		launch(args);
	}
}
