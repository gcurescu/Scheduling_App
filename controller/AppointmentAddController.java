package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import model.Appointments;
import utils.Dao;

/**
 * @author : Gentillo_Curescu 
 * This is the controller class for appointment add view 
 */
public class AppointmentAddController implements Initializable {

	@FXML
	private DatePicker EndAppointmentDatePicker;

	@FXML
	private Label appntmntIDLbl;

	@FXML
	private TextField appointmentIdTextField;

	@FXML
	private Button cancelAppointmentBtn;

	@FXML
	private ComboBox<String> contactComboBox;

	@FXML
	private TextField contactIdTextField;

	@FXML
	private Label cstmrIDLbl;

	@FXML
	private ComboBox<String> cutomerComboBox;

	@FXML
	private TextField cutomerIdTextField;

	@FXML
	private TextArea descriptionTextArea;

	@FXML
	private Label dscrptnLbl;

	@FXML
	private TextField endHoursTextField;

	@FXML
	private Label endLabel;

	@FXML
	private TextField endMinutesTextField;

	@FXML
	private TextArea errorTxtBx;

	@FXML
	private Label guiLabel;

	@FXML
	private TextField locationTextField;

	@FXML
	private Label loctnLbl;

	@FXML
	private Button saveAppointmentBtn;

	@FXML
	private DatePicker startAppointmentDatePicker;

	@FXML
	private TextField startHourTextField;

	@FXML
	private Label startLabel;

	@FXML
	private TextField startMinutesTextField;

	@FXML
	private Label titleLabel1;

	@FXML
	private TextField titleTextField;

	@FXML
	private Label ttlLbl;

	@FXML
	private Label typeLabel;

	@FXML
	private TextField typeTextField;

	private static Appointments editAppointments = new Appointments();

	/**
	 * @return Appointments object
	 * appointments getter method
	 */
	public static Appointments getEditAppointments() {
		return editAppointments;
	}

	/**
	 * @param editAppointments
	 * appointments setter method
	 */
	public static void setEditAppointments(Appointments editAppointments) {
		AppointmentAddController.editAppointments = editAppointments;
	}

	/**
	 * @param event
	 * Event method called on the cancel appointment button pressed
	 * Redirects to the Appointment View
	 */
	@FXML
	void cancelAppointment(ActionEvent event) {

		try {
			Main.setRooot("/view/AppointmentView");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param event
	 * @throws ParseException
	 * @throws SQLException
	 * @throws IOException
	 * Action event method called on saveAppointment button pressed
	 * Saves the appointment details in the database
	 */
	@FXML
	void saveAppointment(ActionEvent event) throws ParseException, SQLException, IOException {

		String title = titleTextField.getText();
		String type = typeTextField.getText();
		String location = locationTextField.getText();
		String startDate = startAppointmentDatePicker.getEditor().getText();
		String startMinutes = startMinutesTextField.getText();
		String startHours = startHourTextField.getText();
		String description = descriptionTextArea.getText();
		String customerID = cutomerIdTextField.getText();
		String contactID = contactIdTextField.getText();

		if (startMinutes.length() == 1) {
			startMinutes = "0" + startMinutes;
		}

		if (startHours.length() == 1) {
			startHours = "0" + startHours;
		}

		String startTime = startHours + ":" + startMinutes;

		String endDate = EndAppointmentDatePicker.getEditor().getText();

		String endMin = endMinutesTextField.getText();

		if (endMin.length() == 1) {
			endMin = "0" + endMin;
		}

		String endHours = endHoursTextField.getText();

		if (endHours.length() == 1) {
			endHours = "0" + endHours;
		}

		String endTime = endHours + ":" + endMin;

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime startLocalDateTime = LocalDateTime.parse(startDate + " " + startTime, formatter);
		Timestamp startTimeStamp = Timestamp.valueOf(startLocalDateTime);

		LocalDateTime endLocalDateTime = LocalDateTime.parse(endDate + " " + endTime, formatter);
		Timestamp endTimeStamp = Timestamp.valueOf(endLocalDateTime);

		
		/**
		 * Scheduling an appointment outside of business hours including weekends 
		 * which are defined as 8:00 am to 10:00 pm EST are set in the following parameters. 
		 * 
		 * 
		 */
		
		/*The parameters for scheduling an appointment outside of business hours defined as
        8:00 a.m. to 10:00 p.m. EST, including weekends are set below.
       If a time is set for outside of business hours, an error message is displayed
       */

      //boolean anycheckAppointmentTimeBusinessHours = checkAppointmentTimeBusinessHours(startHours);
		
		if ((Integer.parseInt(startHours) < 8 || Integer.parseInt(startHours) > 22) ||
				(Integer.parseInt(endHours) < 8 || Integer.parseInt(endHours) > 22)) {
			errorTxtBx.clear();
			errorTxtBx.setVisible(true);
			errorTxtBx.appendText(" Start time or End time entered is not within business hours. Please enter hour between 8-21 ");
		} else {

			String appointmentID = appointmentIdTextField.getText();

			boolean anyOverlappingAppointments = checkOverlappingAppointments(startTimeStamp, endTimeStamp,
					appointmentID);
			String sql=null;
			if (anyOverlappingAppointments) {
				Dao dao = new Dao();
				Connection con = dao.getConnection();
				PreparedStatement ps;
				if (AppointmentAddController.getEditAppointments() != null) {

					 sql = "UPDATE appointments SET " + "Title = '" + title + "', Description = '" + description
							+ "', Location = '" + location + "', Type = '" + type + "', Start = ?" + ", End = ?"
							+ ", Customer_ID = '" + customerID + "', User_ID = '" + "1" + "', Contact_ID = '"
							+ contactID + "'" + " WHERE " + "Appointment_ID = " + AppointmentAddController.getEditAppointments().getId();

				} else {

					 sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES("
							+ "'" + title + "', '" + description + "', '" + location + "', '" + type + "', " + "?"
							+ ", " + "?" + ", '" + customerID + "', '" + "1" + "', '" + contactID + "')";

				}
					try {
						ps = con.prepareStatement(sql);
						ps.setTimestamp(1, startTimeStamp);
						ps.setTimestamp(2, endTimeStamp);
						ps.executeUpdate();

						Main.setRooot("/view/AppointmentView");

						ps.close();
						con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		

		AppointmentAddController.setEditAppointments(null);
	}

	/**
	 * @param event
	 * Action event method called on selecting contact combo box 
	 * Populates the contact Id Text Field by getting the selected contacts name from the combo box and querying through database
	 */
	@FXML
	void selectContact(ActionEvent event) {

		String conName = String.valueOf(contactComboBox.getValue());
		Dao dao = new Dao();
		Connection con = dao.getConnection();
		String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name=?";
		PreparedStatement ps;
		ResultSet rs;
		int contactId = 0;

		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, conName);
			rs = ps.executeQuery();

			if (rs.next()) {

				contactId = rs.getInt("Contact_ID");

			}
			contactIdTextField.setText(String.valueOf(contactId));
			ps.close();
			rs.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param event
	 * This method is called when a customer combobox is selected
	 */
	@FXML
	void selectCustomer(ActionEvent event) {

		String custName = String.valueOf(cutomerComboBox.getValue());
		Dao dao = new Dao();
		Connection con = dao.getConnection();
		String sql = "SELECT Customer_ID FROM customers WHERE Customer_Name=?";
		PreparedStatement ps;
		ResultSet rs;
		int custId = 0;

		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, custName);
			rs = ps.executeQuery();

			if (rs.next()) {

				custId = rs.getInt("Customer_ID");

			}
			cutomerIdTextField.setText(String.valueOf(custId));
			ps.close();
			rs.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 *This method initializes the appointments table 
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		if (AppointmentAddController.getEditAppointments() != null) {

			guiLabel.setText("Edit Appointment");

			Dao dao = new Dao();
			Connection con = dao.getConnection();
			PreparedStatement ps;
			ResultSet rs = null;
			String query = "SELECT * FROM appointments WHERE Appointment_ID=?";
			try {
				ps = con.prepareStatement(query);
				ps.setInt(1, AppointmentAddController.getEditAppointments().getId());

				rs = ps.executeQuery();

				if (rs.next()) {

					appointmentIdTextField.setText(rs.getString("Appointment_ID"));

					titleTextField.setText(rs.getString("Title"));

					locationTextField.setText(rs.getString("Location"));

					typeTextField.setText(rs.getString("Type"));

					LocalDateTime startLocalDateTime = rs.getTimestamp("Start").toLocalDateTime();

					String minutesFromLocalDateTime = String.valueOf(startLocalDateTime.getMinute());
					startMinutesTextField.setText(minutesFromLocalDateTime);

					String hoursFromLocalDateTime = String.valueOf(startLocalDateTime.getHour());
					startHourTextField.setText(hoursFromLocalDateTime);

					LocalDate localDate = startLocalDateTime.toLocalDate();
					startAppointmentDatePicker.getEditor().setText(String.valueOf(localDate));

					startAppointmentDatePicker.setValue(LocalDate.parse(String.valueOf(localDate)));

					LocalDateTime endLocalDateTime = rs.getTimestamp("End").toLocalDateTime();

					LocalDate endLocalDate = endLocalDateTime.toLocalDate();
					String minutesFromEndLocalDateTime = String.valueOf(endLocalDateTime.getMinute());
					endMinutesTextField.setText(minutesFromEndLocalDateTime);

					String hoursFromEndLocalDateTime = String.valueOf(endLocalDateTime.getHour());
					endHoursTextField.setText(hoursFromEndLocalDateTime);

					EndAppointmentDatePicker.getEditor().setText(String.valueOf(endLocalDate));

					EndAppointmentDatePicker.setValue(LocalDate.parse(String.valueOf(endLocalDate)));

					descriptionTextArea.appendText(rs.getString("Description"));

				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			String getCustomerNamesSql = "SELECT Customer_Name FROM customers WHERE Customer_ID=?";
			String getContactNamesSql = "SELECT Contact_Name FROM contacts WHERE Contact_Id=?";

			PreparedStatement ps0, ps1;
			ResultSet rs0, rs1;

			try {
				ps0 = con.prepareStatement(getCustomerNamesSql);
				ps0.setInt(1, Integer.parseInt(rs.getString("Customer_ID")));
				rs0 = ps0.executeQuery();

				if (rs0.next()) {

					cutomerComboBox.getSelectionModel().select(rs0.getString("Customer_Name"));
					cutomerIdTextField.setText(rs.getString("Customer_ID"));
					cutomerComboBox.getOnAction();
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {

				ps1 = con.prepareStatement(getContactNamesSql);
				ps1.setInt(1, Integer.parseInt(rs.getString("Contact_ID")));
				rs1 = ps1.executeQuery();

				if (rs1.next()) {

					contactComboBox.getSelectionModel().select(rs1.getString("Contact_Name"));
					contactIdTextField.setText(rs.getString("Customer_ID"));

					contactComboBox.getOnAction();
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		

			ObservableList<String> customerNames = FXCollections.observableArrayList();
			ObservableList<String> contactNames = FXCollections.observableArrayList();

			Dao dao = new Dao();
			Connection con = dao.getConnection();
			String getCustomerNamesSql = "SELECT Customer_Name FROM customers";
			String getContactNamesSql = "SELECT Contact_Name FROM contacts";

			PreparedStatement ps, ps1;
			ResultSet rs, rs1;

			try {
				ps = con.prepareStatement(getContactNamesSql);
				rs = ps.executeQuery();

				while (rs.next()) {

					contactNames.add(rs.getString("Contact_Name"));

				}

				contactComboBox.setItems(contactNames);
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				ps1 = con.prepareStatement(getCustomerNamesSql);
				rs1 = ps1.executeQuery();

			
				while (rs1.next()) {

					customerNames.add(rs1.getString("Customer_Name"));

				}

				cutomerComboBox.setItems(customerNames);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

				@Override
				public String toString(LocalDate localDate) {
					return localDate != null ? dateTimeFormatter.format(localDate) : "";
				}

				@Override
				public LocalDate fromString(String s) {
					return s != null && !s.isEmpty() ? LocalDate.parse(s, dateTimeFormatter) : null;
				}
			};

			EndAppointmentDatePicker.setConverter(converter);
			EndAppointmentDatePicker.setPromptText("yyyy-MM-dd");
			startAppointmentDatePicker.setConverter(converter);
			startAppointmentDatePicker.setPromptText("yyyy-MM-dd");

		
			

	}

	/**
	 * This method checks overlapping appointments.
	 * This method also includes the second of the required Lambda Expressions for this project.
	 * LAMBDA EXPLANATION
	 * This simplifies the process and saves space in the code by creating a short hand way to
	 * write the method and no need for method naming when calling or using a lambda
	 * The Lambda expression's purpose is to efficiently retrieve the current time, convert it to UTC,
	 * then compare it to the existing appointments when checking if any of the appointments overlap.
	 * Lambda expressions are beneficial in reducing the number of lines of code.
	 * Functional programming with lambda expressions is more efficient.
	 *
	 * @param end
	 * @param appointment_ID
	 * @return boolean value true or false
	 * @throws ParseException
	 * @throws SQLException
	 */
	public boolean checkOverlappingAppointments(Timestamp start, Timestamp end, String appointment_ID)
			throws ParseException, SQLException {

		errorTxtBx.clear();

		ObservableList<Appointments> appointments = FXCollections.observableArrayList();

		Dao dao = new Dao();
		Connection con = dao.getConnection();
		PreparedStatement ps;
		ResultSet rs;

		if (appointment_ID.isEmpty()) {

			String sql = "SELECT * FROM appointments";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

		} else {

			String sql = "SELECT * FROM appointments WHERE Appointment_ID !=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, appointment_ID);
			rs = ps.executeQuery();

		}

		LocalDateTime startTimeToCheckLocalDateTime = start.toLocalDateTime();

		LocalDateTime endTimeToCheckLocalDateTime = end.toLocalDateTime();

		while (rs.next()) {

			Timestamp startTime = rs.getTimestamp("Start");
			Timestamp endTime = rs.getTimestamp("End");

			//Lambda Expression used to efficiently convert time to UTC
			Supplier<ZonedDateTime> nowUTC = () -> ZonedDateTime.now(ZoneOffset.UTC);
			LocalDateTime startLocalDateTime = startTime.toLocalDateTime();
			LocalDateTime endLocalDateTime = endTime.toLocalDateTime();

			int appointmentId = rs.getInt("Appointment_ID");
			int customerId = rs.getInt("Customer_ID");
			String title = rs.getString("Title");
			String description = rs.getString("Description");
			String location = rs.getString("Location");
			int contactId = rs.getInt("Contact_ID");
			String type = rs.getString("Type");

			LocalDateTime localDateTime = startTime.toLocalDateTime();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
			String startF = dateTimeFormatter.format(localDateTime);

			LocalDateTime localDateTime1 = endTime.toLocalDateTime();
			String endF = dateTimeFormatter.format(localDateTime1);

			if (startTimeToCheckLocalDateTime.isAfter(startLocalDateTime)
					&& startTimeToCheckLocalDateTime.isBefore(endLocalDateTime)
					|| (endTimeToCheckLocalDateTime.isAfter(startLocalDateTime)
							&& endTimeToCheckLocalDateTime.isBefore(endLocalDateTime))
					|| (startLocalDateTime.isAfter(startTimeToCheckLocalDateTime)
							&& startLocalDateTime.isBefore(endTimeToCheckLocalDateTime))
					|| (startLocalDateTime.equals(startTimeToCheckLocalDateTime)
							|| startLocalDateTime.equals(endTimeToCheckLocalDateTime))
					|| (endLocalDateTime.equals(endTimeToCheckLocalDateTime)
							|| endLocalDateTime.equals(startTimeToCheckLocalDateTime))) {
				Appointments appointment = new Appointments(appointmentId, title, description, location, type, startF,
						endF, customerId, contactId);
				appointments.add(appointment);

			}

		}

		if (appointments.isEmpty()) {

			return true;

		} else {

			for (Appointments appointment : appointments) {
				errorTxtBx.setVisible(true);
				errorTxtBx.appendText("Appointment ID: " + appointment.getId() + " Starting: " + appointment.getStart()
						+ " Ending: " + appointment.getEnd() + " is overlapping." + System.lineSeparator()
						+ "Please choose a time that does not conflict.");
			}
			return false;
		}
	}

}
