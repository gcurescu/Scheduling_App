package model;

/**This Class handles the getters, setters, and constructors for FirstLevelDivision
 * by: @Gentillo_Curescu */

public class FirstLevelDivision {

	private int id;
	private String division;
	private int countryId;

	public FirstLevelDivision() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**Constructors for FirstLevelDivisions.
	 * @param id The FirstLevelDivision id.
	 * @param division The FirstLevelDivision name.
	 * @param countryId The FirstLevelDivision password.
	 */

	public FirstLevelDivision(int id, String division, int countryId) {
		super();
		this.id = id;
		this.division = division;
		this.countryId = countryId;
	}

	/** This section Is where all the Getters and Setters are placed.
	 */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

}
