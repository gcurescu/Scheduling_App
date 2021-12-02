package model;

/**This Class handles the getters, setters, and constructors for Report
 * by: @Gentillo_Curescu */

public class Report {

	private String month;
	private String type;
	private String count;

	public Report() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**Constructors for Report.
	 * @param month The Report month.
	 * @param type The Report type.
	 * @param count The Report count.
	 */

	public Report(String month, String type, String count) {
		super();
		this.month = month;
		this.type = type;
		this.count = count;
	}

	/** This section Is where all the Getters and Setters are placed.
	 */

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

}
