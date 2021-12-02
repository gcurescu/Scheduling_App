package model;

/**This Class handles the getters, setters, and constructors for Appointments
 * by: @Gentillo_Curescu */

public class Appointments {

	private int id;
	private String title;
	private String description;
	private String location;
	private String type;
	private String start;
	private String end;
	private int user_id;
	private int customer_id;
	private int contact_id;

	public Appointments() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**Constructors for Appointment.
	 * @param id The Appointment id.
	 * @param title The Appointment title.
	 * @param description The Appointment description.
	 * @param location The Appointment location.
	 * @param type The Appointment type.
	 * @param start The Appointment start time.
	 * @param end  The Appointment end time.
	 * @param customer_id The Appointment customer id.
	 * @param contact_id The Appointment contact is.
	 */
	
	public Appointments(int id, String title, String description, String location, String type, String start,
			String end, int customer_id, int contact_id) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.location = location;
		this.type = type;
		this.start = start;
		this.end = end;

		this.customer_id = customer_id;
		this.contact_id = contact_id;
	}

	public Appointments(int id2, String title2, String description2, String location2, String type2, String startF,
			String endF, int userId, int customerId, int contactId) {
		// TODO Auto-generated constructor stub
	}

	/** This section Is where all the Getters and Setters are placed.
	 */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	public int getContact_id() {
		return contact_id;
	}

	public void setContact_id(int contact_id) {
		this.contact_id = contact_id;
	}

	@Override
	public String toString() {
		return "Appointments [id=" + id + ", title=" + title + ", description=" + description + ", location=" + location
				+ ", type=" + type + ", start=" + start + ", end=" + end + ", user_id=" + user_id + ", customer_id="
				+ customer_id + ", contact_id=" + contact_id + "]";
	}

}
