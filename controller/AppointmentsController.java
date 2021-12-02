package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Appointments;
import utils.Dao;

/**
 * @author : Gentillo_Curescu
 *This is controller class for appointments view 
 */
public class AppointmentsController implements Initializable {

	@FXML
	private Button addAppointmentButton;

	@FXML
	private AnchorPane anchorPgApptmnt;

	@FXML
	private TableColumn<Appointments, String> appointmentIdCol;

	@FXML
	private TableView<Appointments> appointmentTableView;

	@FXML
	private ToggleGroup apptmntTgglView;

	@FXML
	private TableColumn<Appointments, String> contactIdCol;

	@FXML
	private TableColumn<Appointments, String> customerIdCol;

	@FXML
	private Button deleteAppointmentBtn;

	@FXML
	private TableColumn<Appointments, String> descriptionCol;

	@FXML
	private Button editAppointmentBtn;

	@FXML
	private TableColumn<Appointments, String> endCol;

	@FXML
	private TextArea errorTxtBx;

	@FXML
	private Label labelApptmnt;

	@FXML
	private TableColumn<Appointments, String> locationCol;

	@FXML
	private Button mainPageButton;

	@FXML
	private AnchorPane mainPgAnchr;

	@FXML
	private RadioButton monthlyViewRadioBtn;

	@FXML
	private TableColumn<Appointments, String> startCol;

	@FXML
	private TableColumn<Appointments, String> titleCol;

	@FXML
	private TableColumn<Appointments, String> typeCol;

	@FXML
	private RadioButton weeklyViewRadioBtn;

	@FXML
	private RadioButton yearlyViewRadioBtn;

	static ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();
	Integer dateRange = 0;

	/**
	 * @param event
	 * Action event method called on add appointment button pressed 
	 * Redirects to add appointment view
	 */
	@FXML
	void addAppointment(ActionEvent event) {

		try {
			Main.setRooot("/view/AppointmentAddView");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param event
	 * @throws IOException
	 * Action event method called on delete button pressed
	 * This method deletes the appointment
	 */
	@FXML
	void deleteAppointment(ActionEvent event) throws IOException {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("Appointment has been deleted");
		alert.show();

		Appointments appointment = appointmentTableView.getSelectionModel().getSelectedItem();

		Dao dao = new Dao();
		Connection con = dao.getConnection();
		String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
		PreparedStatement ps = null;

		try {

			ps = con.prepareStatement(sql);
			ps.setInt(1, appointment.getId());
			ps.executeUpdate();
			ps.close();
			con.close();
			Main.setRooot("/view/AppointmentView");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	/**
	 * @param event
	 * Action event method called on editAppointment button pressed
	 * This method sets the editAppointment object and redirects to the appointment edit view
	 */
	@FXML
	void editAppointment(ActionEvent event) {

		Appointments appointment = appointmentTableView.getSelectionModel().getSelectedItem();
		AppointmentAddController.setEditAppointments(appointment);
		try {
			Main.setRooot("/view/AppointmentAddView");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param event
	 * Action event method called on main button pressed
	 * Redirects to the Main view
	 */
	@FXML
	void goToMainPage(ActionEvent event) {

		try {
			Main.setRooot("/view/MainView");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param event
	 * Action event method called on radio button selection
	 */
	@FXML
	void radioButtonHandler(ActionEvent event) {

		initialize(null, null);
	}


//	public static String toUTC(String dateTime) {
//		Timestamp timestamp = Timestamp.valueOf(dateTime);
//
//
//		LocalDateTime ldt = timestamp.toLocalDateTime();
//		ZonedDateTime zdt = ldt.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
//		ZonedDateTime utczdt = zdt.withZoneSameInstant(ZoneId.of("UTC"));
//		LocalDateTime ldtIn = utczdt.toLocalDateTime();
//		String finishUTC =ldtIn.format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss"));
//		return finishUTC;
//	}
	/** Converts time to Local.
	 * @param dateTime
	 * */
	public static String toLocal(String dateTime) {
		Timestamp timestamp = Timestamp.valueOf(dateTime);
		LocalDateTime ldt = timestamp.toLocalDateTime();
		ZonedDateTime zdtOut = ldt.atZone(ZoneId.of("UTC"));
		ZonedDateTime zdtOutToLocalTZ = zdtOut.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
		LocalDateTime ldOutFinal = zdtOutToLocalTZ.toLocalDateTime();
		String finishLocal =ldOutFinal.format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss"));
		return finishLocal;
	}


	/**
	 *This method is called when a constructor class is loaded 
	 *Initializes the starting conditions and appointments controller
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		this.checkFifteenMinutesNotification();
		Dao dao = new Dao();
		Connection con = dao.getConnection();

		if (weeklyViewRadioBtn.isSelected()) {
			dateRange = 7;
		}
		if (monthlyViewRadioBtn.isSelected()) {
			dateRange = 30;
		}
		if (yearlyViewRadioBtn.isSelected()) {

			dateRange = 365;

		}
		if (dateRange > 0) {

			appointmentList.clear();
			LocalDate before = LocalDate.now().minusDays(dateRange);
			LocalDate after = LocalDate.now().plusDays(dateRange);
			System.out.println(before + "before");

			String sql1 = "SELECT * FROM appointments WHERE  " + "Start >= '" + before + "' and Start <= '" + after
					+ "'";
			PreparedStatement ps1;
			ResultSet rs1;

			try {

				ps1 = con.prepareStatement(sql1);
				rs1 = ps1.executeQuery();

				while (rs1.next()) {

					int id = Integer.parseInt(rs1.getString("Appointment_ID"));
					String title = rs1.getString("Title");
					String description = rs1.getString("Description");
					String location = rs1.getString("Location");
					String type = rs1.getString("Type");
					int customerId = rs1.getInt("Customer_ID");
					int userId = rs1.getInt("User_ID");
					int contactId = rs1.getInt("Contact_ID");

					String start = rs1.getString("Start");
					String end = rs1.getString("End");

//					LocalDateTime localLocalDateTimeStart = start.toLocalDateTime();
//					LocalDateTime localLocalDateTimeEnd = end.toLocalDateTime();
//
//					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//					String startF = dateTimeFormatter.format(localLocalDateTimeStart);
//					String endF = dateTimeFormatter.format(localLocalDateTimeEnd);

					String startF = toLocal(start);
					String endF = toLocal(end);

					Appointments appointmnt = new Appointments();
					appointmnt.setId(id);
					appointmnt.setTitle(title);
					appointmnt.setDescription(description);
					appointmnt.setLocation(location);
					appointmnt.setType(type);
					appointmnt.setCustomer_id(customerId);
					appointmnt.setUser_id(userId);
					appointmnt.setContact_id(contactId);
					appointmnt.setStart(startF);
					appointmnt.setEnd(endF);

					appointmentList.add(appointmnt);

				}

				ps1.close();
				rs1.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			appointmentTableView.setPlaceholder(new Label("The table is empty or no search results found."));

			appointmentIdCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("id"));
			customerIdCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("customer_id"));
			titleCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("title"));
			descriptionCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("description"));
			locationCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("location"));
			contactIdCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("contact_id"));
			typeCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("type"));
			startCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("start"));
			endCol.setCellValueFactory(new PropertyValueFactory<Appointments, String>("end"));

			appointmentTableView.setItems(appointmentList);

		}

	}

	/**
	 * This method checks the fifteen minutes alert for the appointments.
	 * This method also includes the first Lambda Expression required for the project.
	 * LAMBDA EXPLANATION
	 * This simplifies the process and saves space in the code by creating a short hand way to
	 * write the method and no need for method naming when calling or using a lambda expression
	 * The Lambda expression's purpose is used here to efficiently check each appointment and compare them to the
	 * current time triggering an alert if an appointment is within 15 minutes of logging in
	 * Lambda expressions are beneficial in reducing the number of lines of code.
	 * Functional programming with lambda expressions is more efficient.
	 *
	 */
	public void checkFifteenMinutesNotification() {

		ObservableList<Appointments> listOfAppointments = FXCollections.observableArrayList();
		errorTxtBx.clear();
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime fifteenMinutesTime = currentTime.plusMinutes(15);

		Dao dao = new Dao();
		String sql = "SELECT * FROM appointments";

		Connection con = dao.getConnection();
		PreparedStatement ps;
		ResultSet rs;

		try {

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {

				int id = Integer.parseInt(rs.getString("Appointment_ID"));
				String title = rs.getString("Title");
				String description = rs.getString("Description");
				String location = rs.getString("Location");
				String type = rs.getString("Type");
				int customerId = rs.getInt("Customer_ID");
				int userId = rs.getInt("User_ID");
				int contactId = rs.getInt("Contact_ID");

				String start = rs.getString("Start");
				String end = rs.getString("End");

//				LocalDateTime localDateTimeStart = start.toLocalDateTime();
//				LocalDateTime localDateTimeEnd = end.toLocalDateTime();
//
//				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
////				String startF = dateTimeFormatter.format(localDateTimeStart);
//				String endF = dateTimeFormatter.format(localDateTimeEnd);

				String startF = toLocal(start);
				String endF = toLocal(end);

				Appointments appointmnt = new Appointments();
				appointmnt.setId(id);
				appointmnt.setTitle(title);
				appointmnt.setDescription(description);
				appointmnt.setLocation(location);
				appointmnt.setType(type);
				appointmnt.setCustomer_id(customerId);
				appointmnt.setUser_id(userId);
				appointmnt.setContact_id(contactId);
				appointmnt.setStart(startF);
				appointmnt.setEnd(endF);

				System.out.println(appointmnt.toString());
				listOfAppointments.add(appointmnt);

			}
			System.out.println("Number Of Appointments : " + listOfAppointments.size());

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (listOfAppointments != null) {

			AtomicBoolean ifAppointmentExist = new AtomicBoolean(true);

			// Lambda Expression
			listOfAppointments.forEach((appointment) -> {

				String startF = appointment.getStart();
				Timestamp startTimestamp = Timestamp.valueOf(startF);
				LocalDateTime startLocalDateTime = startTimestamp.toLocalDateTime();
				LocalDateTime fifteenMinutesStartLocalDateTime = startLocalDateTime.plusMinutes(15);

				if (startLocalDateTime.isAfter(currentTime)
						&& startLocalDateTime.isBefore(currentTime.plusMinutes(15))) {

					ifAppointmentExist.set(false);

					errorTxtBx.appendText(
							"Appointment ID: " + appointment.getId() + " Is starting at: " + appointment.getStart());

				}

			});

			if (ifAppointmentExist.get()) {
				errorTxtBx.appendText("There are no appointments within the next 15 minutes. ");
			}
		}
		else {
			errorTxtBx.appendText("There are no appointments within the next 15 minutes. ");
		}
	}

}
