package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Appointments;
import model.Report;
import utils.Dao;

/**
 * @author : Gentillo_Curescu 
 *	This is controller class for appointment reports view
 */
public class AppointmentReportsController implements Initializable { 
	@FXML
	private AnchorPane anchorPgApptmnt;

	@FXML
	private Label appointmentLabel;

	@FXML
	private TableView<Report> appointmentTableView;

	@FXML
	private TableColumn<Report, String> countCol;

	@FXML
	private Button goBackButton;

	@FXML
	private Label labelMain;

	@FXML
	private AnchorPane mainPgAnchr;

	@FXML
	private TableColumn<Report, String> monthCol;

	@FXML
	private TableColumn<Report, String> typeCol;

	/**
	 * @param event
	 * Action Event method which is called on Main menu button pressed
	 * Redirects to the Reports View
	 */
	@FXML
	void goToMainMenu(ActionEvent event) {

		try {
			Main.setRooot("/view/ReportsView");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 *This method initializes the appointment reports table through database
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		Dao dao = new Dao();
		Connection con = dao.getConnection();
		ObservableList<Appointments> appointmentData = FXCollections.observableArrayList();
		PreparedStatement ps1;
		ResultSet rs1;
		
		String sql1= "SELECT * FROM appointments";
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

				Timestamp start = rs1.getTimestamp("Start");
				Timestamp end = rs1.getTimestamp("End");

				LocalDateTime localLocalDateTimeStart = start.toLocalDateTime();
				LocalDateTime localLocalDateTimeEnd = end.toLocalDateTime();

				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

				String startF = dateTimeFormatter.format(localLocalDateTimeStart);
				String endF = dateTimeFormatter.format(localLocalDateTimeEnd);

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
				
				appointmentData.add(appointmnt);

			}

			ps1.close();
			rs1.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		ObservableList<Report> appointmentReportData = generateAppointmentReportData(appointmentData);

		appointmentTableView.setPlaceholder(new Label("The table is empty or no search results found!"));

		monthCol.setCellValueFactory(new PropertyValueFactory<Report, String>("month"));
		typeCol.setCellValueFactory(new PropertyValueFactory<Report, String>("type"));
		countCol.setCellValueFactory(new PropertyValueFactory<Report, String>("count"));
		appointmentTableView.setItems(appointmentReportData);

	}

	/**
	 * @param appointmentData
	 * @return observable list of data type Report
	 * This method generates appointment reports data 
	 */
	private ObservableList<Report> generateAppointmentReportData(ObservableList<Appointments> appointmentData) {


        ObservableList<Report> appntmntRprtDt = FXCollections.observableArrayList();
        Map appointmentReportMap = new HashMap();

        for (Appointments appointment: appointmentData){
            Map typeAndCountMap = new HashMap();
            Map blankMap = new HashMap();
            
            String startString = appointment.getStart();
            Timestamp startTimeStamp = Timestamp.valueOf(startString);
            LocalDateTime startLocalDateTime = startTimeStamp.toLocalDateTime();
            String month = String.valueOf(startLocalDateTime.getMonth());
            
           
            String type = appointment.getType();

            if(!appointmentReportMap.containsKey(month)){
                appointmentReportMap.put(month, blankMap);
            }

            if(appointmentReportMap.containsKey(month)){
                typeAndCountMap = (Map) appointmentReportMap.get(month);
            }
            if (typeAndCountMap.containsKey(type)) {
                int currentCount = (int) typeAndCountMap.get(type);
                typeAndCountMap.put(type, currentCount+1);
            } else {
                typeAndCountMap.put(type, 1);
            }

            appointmentReportMap.put(month, typeAndCountMap);

        }


        for (Object month : appointmentReportMap.keySet()){
            for (Object type :((Map<?, ?>) appointmentReportMap.get(month)).keySet()){
                Object count = ((Map<?, ?>) appointmentReportMap.get(month)).get(type);
                Report appointmentReport = new Report(month.toString(), type.toString(), count.toString());
                appntmntRprtDt.add(appointmentReport);
            }

        }
        return appntmntRprtDt;
    }
}
