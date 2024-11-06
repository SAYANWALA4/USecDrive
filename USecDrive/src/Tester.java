import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;

public class Tester {
	public static void main(String[] args) {
		LocalDate date = LocalDate.now(); 
		LocalTime time = LocalTime.now();
	    System.out.println(date+" Time:"+time); // Display the current date
	}
}
