package model;

/**This Class handles the getters, setters, and constructors for Contacts
 * by: @Gentillo_Curescu */

public class Contacts {

	private int id;
	private String name;
	private String email;

	public Contacts() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**Constructors for Contact.
	 * @param id The Contact id.
	 * @param name The Contact name.
	 * @param email The Contact email.
	 */
	
	public Contacts(int id, String name, String email) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
