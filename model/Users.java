package model;

/**This Class handles the getters, setters, and constructors for CUstomers
 * by: @Gentillo_Curescu */

public class Users {

	private int id;
	private String name;
	private String password;

	public Users() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**Constructors for Users.
	 * @param id The User id.
	 * @param name The User name.
	 * @param password The User password.
	 */

	public Users(int id, String name, String password) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
