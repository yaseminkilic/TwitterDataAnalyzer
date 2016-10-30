import java.util.Date;

public class Twitter {
	
	Date date;
	long id;
	String text;
	String name;
	
	
	public Twitter(long id,String name,String text,Date date){
		this.id = id;
		this.name = name;
		this.date = date;
		this.text = text;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
