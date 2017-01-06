import java.util.Date;

/* 
 * This class is directly related to use efficiently Twitter data such as date, id, text and name.
 */
public class Twitter {
	
	/* Some useful variables to control class' operations and interaction with other classes. */ 
	private Date date;
	private long id;
	private String text;
	private String name;
	
	public Twitter(long id,String name,String text,Date date){
		this.id = id;
		this.name = name;
		this.date = date;
		this.text = text;
	}
	
	public Date getDate() {
		return date;
	}
	
	void setDate(Date date) {
		this.date = date;
	}
	
	public long getId() {
		return id;
	}
	
	void setId(long id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}
	
	void setText(String text) {
		this.text = text;
	}
	
	public String getName() {
		return name;
	}
	
	void setName(String name) {
		this.name = name;
	}
}
