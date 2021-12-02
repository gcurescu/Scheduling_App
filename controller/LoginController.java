package controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.Dao;

/**
 * @author : Gentillo_Curescu 
 * Controller for login view
 *
 */
public class LoginController implements Initializable {

	@FXML
	private Label errorLabel;

	@FXML
	private Label locationLabel;

	@FXML
	private Button loginBtn;

	@FXML
	private Label loginLabel;

	@FXML
	private TextField passwordTextField;

	@FXML
	private TextField userNameTextField;

	@FXML
	private Label welcomeLabel;
	private ResourceBundle resourceBundle;

	/**
	 * @param event
	 * Action event method called on login button pressed
	 * Validates the login details and allow the access to main view
	 */
	@FXML
	void login(ActionEvent event) {

		String userName = userNameTextField.getText().toString();
		String password = passwordTextField.getText().toString();

		Dao dao = new Dao();
		Connection con = dao.getConnection();
		String sql = "SELECT USER_Name, Password FROM users WHERE User_Name = ? AND Password = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = con.prepareStatement(sql);
			ps.setString(1, userName);
			ps.setString(2, password);
			rs = ps.executeQuery();
			if (rs.next()) {

				System.out.println("Login Successful!");
				this.saveLoginStatus("UserName: " + userName + " Logged In Successfully!");
				Main.setRooot("/view/MainView");

			} else {

				System.out.println("Login Un-Successful!");
				this.saveLoginStatus("UserName: " + userName + " Login attempt failed!");
				this.errorLabel.setText("Invalid User Name or Password!");

			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param status
	 * This method saves the login status in a text field
	 */
	public void saveLoginStatus(String status) {

		Path path = FileSystems.getDefault().getPath("src/Login-Activity.txt");
		if (!Files.exists(path)) {

			
			try {
				Files.createFile(path);
				

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			System.out.println("File Exists Already!");
		}
		
		LocalDateTime localDateTime = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		String data = dateTimeFormatter.format(localDateTime) + " " + status + System.lineSeparator();
		System.out.println(dateTimeFormatter.format(localDateTime));
		BufferedWriter writer;
		try {
			writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND);
			writer.append(data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Initializes the login view fields and labels according to the locale 
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		resourceBundle = ResourceBundle.getBundle("rb", Locale.getDefault());
		 Locale locale = Locale.getDefault();
	        if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
	            // this part translates text to French if locale is changed
	            userNameTextField.setText(resourceBundle.getString("usernameField"));
	            passwordTextField.setText(resourceBundle.getString("passwordField"));
	            welcomeLabel.setText(resourceBundle.getString("welcomeLabel"));
	            loginBtn.setText(resourceBundle.getString("loginButton"));
	            locationLabel.setText(resourceBundle.getString("plzLoginLabel"));
	            locationLabel.setText(resourceBundle.getString("locationLabel") + locale);
	        }
	}

}
