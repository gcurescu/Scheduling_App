package controller;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * @author : Gentillo_Curescu 
 *Controller class for the reports view
 */
public class ReportsController {

    @FXML
    private Button appointmentReportBtn;

    @FXML
    private Button contactReportBtn;

    @FXML
    private Button locationReportBtn;

    @FXML
    private Button mainMenuBtn;

    /**
     * @param event
     * Action event method called on the appointment report button pressed
     * Redirects to the appointment reports view
     */
    @FXML
    void appointmentReport(ActionEvent event) {

    	try {
			Main.setRooot("/view/AppointmentReportView");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * @param event
     * Action event for the contact reports button
     * Redirects to the contact report view
     */
    @FXML
    void contactReport(ActionEvent event) {

    	try {
			Main.setRooot("/view/ContactReportView");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * @param event
     * Action event method called on main menu button pressed
     * Redirects to the main view
     */
    @FXML
    void goToMainMenu(ActionEvent event) {
    	try {
			Main.setRooot("/view/MainView");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * @param event
     * Action event method called on location reports button pressed
     * Redirects to location reports view
     */
    @FXML
    void locationReport(ActionEvent event) {

    	try {
			Main.setRooot("/view/LocationReportView");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
