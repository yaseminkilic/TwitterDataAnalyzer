
public class Main {

	public static void main(String[] args) {
		
		try {
			ApplicationExecution app = new ApplicationExecution();
			app.execution(args);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("HATA !!! Exception : " + e.getMessage());
		}
	}
}
