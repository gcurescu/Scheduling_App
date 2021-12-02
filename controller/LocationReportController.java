package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.Appointments;
import utils.Dao;

/**
 * @author : Gentillo_Curescu 
 * Controller class for the location reports view
 *
 */
public class LocationReportController implements Initializable {

	@FXML
	private AnchorPane anchorPgApptmnt;

	@FXML
	private TableColumn<Appointments, String> appointmentIdCol;

	@FXML
	private TableView<Appointments> appointmentTable;

	@FXML
	private TableColumn<Appointments, String> contactIdCol;

	@FXML
	private TableColumn<Appointments, String> customerIdCol;

	@FXML
	private TableColumn<Appointments, String> descriptionCol;

	@FXML
	private TableColumn<Appointments, String> endCol;

	@FXML
	private Button goBackBtn;

	@FXML
	private Label labelApptmnt;

	@FXML
	private Label labelMain;

	@FXML
	private TableColumn<Appointments, String> locationCol;

	@FXML
	private AnchorPane mainPgAnchr;

	@FXML
	private TextField searchLocationTextField;

	@FXML
	private TableColumn<Appointments, String> startCol;

	@FXML
	private TableColumn<Appointments, String> titleCol;

	@FXML
	private TableColumn<Appointments, String> typeCol;
	
	ObservableList<Appointments> appointmentData = FXCollections.observableArrayList();

	/**
	 * @param event
	 *Action event method called main menu button pressed
	 *Redirects to the reports view
	 */
	@FXML
	void goToMainMenu(ActionEvent event) {
		try {
			Main.setRooot("/view/ReportsView");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param event
	 * Key Event method called on search field key released 
	 * searches the appointment
	 */
	@FXML
	void onKyRlsdAppntmntSrchLctnTxt(KeyEvent event) {

		String searchLocation = searchLocationTextField.getText();
		ObservableList<Appointments> filteredAppointments = FXCollections.observableArrayList();

		
		for (Appointments appointment : appointmentData){
            if(appointment.getLocation().contains(searchLocation)){
            	filteredAppointments.add(appointment);
            }
        }

		 if(!(filteredAppointments.isEmpty())) {
	            appointmentTable.setItems(filteredAppointments);
	        } else{
	            
	        	appointmentTable.setItems(appointmentData);
	        }
	}

	/**
	 *Method for initializing the location reports table
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		

		Dao dao = new Dao();
		Connection con = dao.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		String sql = "SELECT * FROM appointments";

		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {

				int appointmentId = rs.getInt("Appointment_ID");
				int customerId = rs.getInt("Customer_ID");
				String title = rs.getString("Title");
				String description = rs.getString("Description");
				String location = rs.getString("Location");
				int contact_ID = rs.getInt("Contact_ID");
				String type = rs.getString("Type");
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

				String start = rs.getString("Start");
//				LocalDateTime localDateTime = start.toLocalDateTime();
//				String startF = dateTimeFormatter.format(localDateTime);

				String end = rs.getString("End");
//				LocalDateTime localDateTimeE = end.toLocalDateTime();
//				String endF = dateTimeFormatter.format(localDateTimeE);

				String startF = AppointmentsController.toLocal(start);
				String endF = AppointmentsController.toLocal(end);

				Appointments appointment = new Appointments(appointmentId, title, description, location, type, startF,
						endF, customerId, contact_ID);
				appointmentData.add(appointment);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		appointmentTable.setPlaceholder(new Label("The table is empty or no search results found."));
		appointmentIdCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("id"));
		customerIdCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("customer_id"));
		titleCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("title"));
		descriptionCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("description"));
		locationCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("location"));
		contactIdCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("contact_id"));
		typeCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("type"));
		startCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("start"));
		endCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("end"));

		appointmentTable.setItems(appointmentData);

	}

}
