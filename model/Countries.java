package model;

/**This Class handles the getters, setters, and constructors for Countries
 * by: @Gentillo_Curescu */

public class Countries {

	private int id;
	private String countryName;

	public Countries() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**Constructors for Countries.
	 * @param id The Countries id.
	 * @param countryName The Countries name.
	 */
	
	public Countries(int id, String countryName) {
		super();
		this.id = id;
		this.countryName = countryName;
	}

	/** This section Is where all the Getters and Setters are placed.
	 */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

}
