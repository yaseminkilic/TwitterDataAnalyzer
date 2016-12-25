import java.util.Date;

public class Twitter {
	
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
