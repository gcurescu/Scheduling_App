package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Observable;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import model.Customers;
import utils.Dao;


/**
 * @author : Gentillo_Curescu
 * Controller class for customers view
 *
 */
public class CustomersController implements Initializable{

	@FXML
	private Label custLbl;

	@FXML
	private AnchorPane custPgAnchr;

	@FXML
	private Button customerAddBtn;

	@FXML
	private TableColumn<Customers, String> customerAddressCol;

	@FXML
	private Button customerDeleteBtn;

	@FXML
	private TableColumn<Customers, String> customerDivisionCol;

	@FXML
	private Button customerEditBtn;

	@FXML
	private TableColumn<Customers, String> customerIdCol;

	@FXML
	private TableColumn<Customers, String> customerNameCol;

	@FXML
	private TableColumn<Customers, String> customerPhoneCol;

	@FXML
	private TableView<Customers> customerTableView;

	@FXML
	private TextArea errorTextBox;

	@FXML
	private Label labelMain;

	@FXML
	private Button mainMenuButton;

	@FXML
	private AnchorPane mainPgAnchr;
	
	private ObservableList<Customers> customersList;
	

	/**
	 * @param event
	 *Action event method called on add customer button pressed
	 *Redirects to the add customer view 
	 */
	@FXML
	void addCustomer(ActionEvent event) {

		try {
			Main.setRooot("/view/CustomerAddView");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param event
	 *Action event method called on delete customer button pressed
	 *Delete the selected customer 
	 */
	@FXML
	void deleteCustomer(ActionEvent event) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("Customer has been deleted.");
		alert.show();

		Customers customer = customerTableView.getSelectionModel().getSelectedItem();

		Dao dao = new Dao();
		Connection con = dao.getConnection();
		String sql = "DELETE FROM customers WHERE Customer_ID = ?";
		PreparedStatement ps = null;

		try {

			ps = con.prepareStatement(sql);
			ps.setInt(1, customer.getId());
			ps.executeUpdate();
			
			System.out.println("Customer Deleted Successfully!");
			errorTextBox.clear();
			ps.close();
			con.close();
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			errorTextBox.appendText("All customers appoitments must be deleted before deleting the customer!");
		}
		
		try {
			Main.setRooot("/view/CustomerView");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param event
	 *Action event method called on edit customer button pressed
	 *Redirects to the add customer view 
	 */
	@FXML
	void editCustomer(ActionEvent event) {
		
		
		Customers customer = customerTableView.getSelectionModel().getSelectedItem();
		
		if(customer !=null) {
			CustomerAddController.setEditCustomer(customer);
			try {
				
				Main.setRooot("/view/CustomerAddView");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else {
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Please Select a Customer To Edit");
			
		}
		

	}

	/**
	 * @param event
	 *Action event method called on main menu button pressed
	 *Redirects to the main view 
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
	 *Method initializing the customer table
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		customersList = FXCollections.observableArrayList();
		Dao dao = new Dao();
		String sql = "SELECT * FROM customers";
		
		Connection con = dao.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		
		try {

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				 int id = Integer.parseInt(rs.getString("Customer_ID"));
		            String name = rs.getString("Customer_Name");
		            String address = rs.getString("Address");
		            String postCode = rs.getString("Postal_Code");
		            String phone = rs.getString("Phone");
		            int divisionId =  Integer.parseInt(rs.getString("Division_ID"));
		            
		            String divisionName = null;
		            
		            PreparedStatement ps1;
		            ResultSet rs1;
		            String sql1="SELECT Division FROM first_level_divisions where Division_ID=?";
		            
		            
		            ps1=con.prepareStatement(sql1);
		            ps1.setInt(1, divisionId);
		            rs1=ps1.executeQuery();
		            
		            if (rs1.next()){
		                divisionName = rs1.getString("Division");
		            }
		            Customers customer = new Customers(id, name, address,postCode, phone, divisionName, divisionId);
		            customersList.add(customer);
			}
			
			ps.close();
			con.close();
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		customerTableView.setPlaceholder(new Label("No Results Found!"));
      
        customerIdCol.setCellValueFactory(new PropertyValueFactory<Customers,String>("id"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<Customers,String>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<Customers,String>("address"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<Customers,String>("phone"));
        customerDivisionCol.setCellValueFactory(new PropertyValueFactory<Customers, String>("divisionName"));
        customerTableView.setItems(customersList);
		
		
	}

}