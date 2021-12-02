package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Countries;
import model.Customers;
import model.FirstLevelDivision;
import utils.Dao;

/**
 * @author : Gentillo_Curescu 
 * Controller class for add customer view
 *
 */
public class CustomerAddController implements Initializable{

    @FXML
    private Label IDLbl;

    @FXML
    private TextField addressTextField;

    @FXML
    private Label addrssLbl;

    @FXML
    private Button cancelBtn;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private TextArea errorTxtBx;

    @FXML
    private ComboBox<String> firstLevelDivisionComboBox;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private Label nmLbl;

    @FXML
    private Label phnNmbrLbl;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private TextField postCodeTextField;

    @FXML
    private Label pstlCdLbl;

    @FXML
    private Button saveCustomerBtn;

    @FXML
    private Label ttlLbl;

    private static int divisionIdSelected;
   
    private static Customers editCustomer;
    
    /**
     * @return Customers object
     * edit customer getter method
     */
    public static Customers getEditCustomer() {
		return editCustomer;
	}

	/**
	 * @param editCustomer
	 * edit customer setter method
	 */
	public static void setEditCustomer(Customers editCustomer) {
		CustomerAddController.editCustomer = editCustomer;
	}

	/**
	 * @param event
	 * Action event method called on cancel button pressed
	 * Redirects to customer view
	 */
	@FXML
    void cancelAddCustomer(ActionEvent event) {
    	
    	try {
			Main.setRooot("/view/CustomerView");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * @param event
     * @throws IOException
     * Action event method called on save button pressed
     * This method saves the customer details to database
     */
    @FXML
    void saveCustomer(ActionEvent event) throws IOException {

    	String name = nameTextField.getText().toString();
    	String address = addressTextField.getText().toString();
    	String phone = phoneNumberTextField.getText().toString();
    	String postCode = postCodeTextField.getText().toString();
    	int divisionId = divisionIdSelected;
    	
    
    	Dao dao = new Dao();
		Connection con = dao.getConnection();
		PreparedStatement ps ;
		
		String sql=null;
		
		try {
			
			if(CustomerAddController.getEditCustomer()!=null) {
		    	int id = CustomerAddController.getEditCustomer().getId();
				sql = "UPDATE customers SET Customer_Name=?, Address=?, Postal_Code=?, Phone=?, Division_ID=? Where Customer_ID ="+id;
			}else {
				
				 sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?,?,?,?,?)";
			}
			 ps = con.prepareStatement(sql);
			 ps.setString(1, name);
			 ps.setString(2, address);
			 ps.setString(3, postCode);
			 ps.setString(4, phone);
			 ps.setInt(5, divisionId);
			
			 ps.executeUpdate();
			 
			 Alert alert = new Alert(AlertType.INFORMATION);
			 alert.setContentText("Customer Saved Successfully");
			 alert.show();
			 CustomerAddController.setEditCustomer(null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed to insert Customer");
		}
		
		Main.setRooot("/view/CustomerView");
		
    }

    /**
     * @param event
     * Action event method for country combo box 
     * populates the country box
     */
    @FXML
    void selectCountry(ActionEvent event) {

    	
    	String countryName = countryComboBox.getSelectionModel().getSelectedItem();
    	populateFirstLevelDivisionCombobox(countryName);
    	
    
    }

    /**
     * @param countryName
     * This method populates the first level division combo box
     */
    private void populateFirstLevelDivisionCombobox(String countryName) {
		
    	ObservableList<String> divisionNames = FXCollections.observableArrayList();
		Dao dao = new Dao();
		Connection con = dao.getConnection();
		PreparedStatement ps ;
		ResultSet rs;
		
		String sqlCountries = "SELECT Country_ID FROM countries where Country=?";
		int countryId=0;
		
		try {
			 ps = con.prepareStatement(sqlCountries);
			 ps.setString(1, countryName);
			 rs = ps.executeQuery();
			 
			if(rs.next()) {
				
				countryId = rs.getInt("Country_ID");
				
			}
			 
			 ps.close();
			 rs.close();
			 
			 PreparedStatement ps1 ;
				ResultSet rs1;
				
				String sqlDivisions = "SELECT Division FROM first_level_divisions where Country_ID=?";
				
				 ps1 = con.prepareStatement(sqlDivisions);
				 ps1.setInt(1, countryId);
				 rs1 = ps1.executeQuery();
				 
				 while(rs1.next()) {
					 
					 divisionNames.add(rs1.getString("Division"));
				 }
				 
				 firstLevelDivisionComboBox.setItems(divisionNames);
				 ps1.close();
				 rs1.close();
				 con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @param event
	 * This method populates the first level division box values according to the country selected
	 */
	@FXML
    void selectFirstLevelDivision(ActionEvent event) {

		String divisionNameSelected = firstLevelDivisionComboBox.getSelectionModel().getSelectedItem();
		
		Dao dao = new Dao();
		Connection con = dao.getConnection();
		PreparedStatement ps ;
		ResultSet rs;
		
		String sqlCountries = "SELECT Division_ID FROM first_level_divisions where Division=?";
		
		try {
			 ps = con.prepareStatement(sqlCountries);
			 ps.setString(1, divisionNameSelected);
			 rs = ps.executeQuery();
			 
			 if(rs.next()) {
				 
				 divisionIdSelected = rs.getInt("Division_ID");
			 }
			 
			 ps.close();
			 rs.close();
			 
			
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	/**
	 *Initializes the combo boxes and table
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		populateCountryBox();
		
		if(CustomerAddController.getEditCustomer() != null) {
			
			
			int divisionId=CustomerAddController.editCustomer.getDivisionId();
			int countryId=0;
			
			String divisionName=null;
			String countryName=null;
			
	    	Dao dao = new Dao();
			Connection con = dao.getConnection();
			PreparedStatement ps ;
			ResultSet rs;
			String sqlDivision = "SELECT Division,Country_ID FROM first_level_divisions WHERE Division_ID=?";
			String sqlCountries = "SELECT Country FROM countries WHERE Country_ID=?";
			try {
				 ps = con.prepareStatement(sqlDivision);
				 ps.setInt(1, divisionId);
				 rs = ps.executeQuery();
				 
				if(rs.next()) {
					 
					 divisionName = rs.getString("Division");
					 countryId = rs.getInt("Country_ID");
					 
				 }
				 
				 ps.close();
				 rs.close();
				 
				 firstLevelDivisionComboBox.getSelectionModel().select(divisionName);
				 PreparedStatement ps1 ;
					ResultSet rs1;
					 ps1 = con.prepareStatement(sqlCountries);
					 ps1.setInt(1, countryId);
					 rs1 = ps1.executeQuery();
					 
					if(rs1.next()) {
						 
						countryName = rs1.getString("Country");
						 
					 }
				 
					countryComboBox.getSelectionModel().select(countryName);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
			
			nameTextField.setText(CustomerAddController.editCustomer.getName());
			phoneNumberTextField.setText(CustomerAddController.editCustomer.getPhone());
			addressTextField.setText(CustomerAddController.editCustomer.getAddress());
			postCodeTextField.setText(CustomerAddController.getEditCustomer().getPostCode());
		}		
		
	}
	
	/**
	 * Method to populate the country combo box
	 */
	public void populateCountryBox() {
		

		ObservableList<String> countryNames = FXCollections.observableArrayList();
		Dao dao = new Dao();
		Connection con = dao.getConnection();
		PreparedStatement ps ;
		ResultSet rs;
		
		String sqlCountries = "SELECT Country FROM countries";
		try {
			 ps = con.prepareStatement(sqlCountries);
			 rs = ps.executeQuery();
			 
			 while(rs.next()) {
				 
				 countryNames.add(rs.getString("Country"));
				 
			 }
			 
			 ps.close();
			 rs.close();
			 
			 countryComboBox.setItems(countryNames);
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
