package model;

/**This Class handles the getters, setters, and constructors for CUstomers
 * by: @Gentillo_Curescu */

public class Customers {

	private int id;
	private String name;
	private String address;
	private String postCode;
	private String phone;

	private String divisionName;
	private int divisionId;


	public Customers() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**These are the Constructors of the Customers.
	 * @param id
	 * @param name
	 * @param address
	 * @param postCode
	 * @param phone
	 * @param divisionName
	 * @param divisionId
	 */

	public Customers(int id, String name, String address, String postCode, String phone, String divisionName,
			int divisionId) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.postCode = postCode;
		this.phone = phone;
		this.divisionName = divisionName;
		this.divisionId = divisionId;
	}

	/** This section Is where all the Getters and Setters are placed.
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public int getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(int divisionId) {
		this.divisionId = divisionId;
	}

}
