package controller;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * @author : Gentillo_Curescu 
 * Controller class for main view
 *
 */
public class MainController {

    @FXML
    private Button logoutBtn;

    @FXML
    private Button viewAppointmentsBtn;

    @FXML
    private Button viewCustomersBtn;

    @FXML
    private Button viewReportsBtn;

    /**
     * @param event
     * Action event method called on logout button pressed
     * 
     */
    @FXML
    void Logout(ActionEvent event) {
    	try {
			Main.setRooot("/view/LoginView");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * @param event
     * Action event method called on view appointment button pressed
     * Redirects to the appointment view
     */
    @FXML
    void viewAppointments(ActionEvent event) {

    	try {
			Main.setRooot("/view/AppointmentView");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * @param event
     * Action event method called on view customer button pressed
     * Redirects to customer view
     */
    @FXML
    void viewCustomers(ActionEvent event) {

    	try {
			Main.setRooot("/view/CustomerView");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * @param event
     * Action event method called on view reports button pressed 
     * Redirects to the  reports view
     */
    @FXML
    void viewReports(ActionEvent event) {

    	try {
			Main.setRooot("/view/ReportsView");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
