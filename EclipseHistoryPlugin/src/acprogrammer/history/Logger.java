package acprogrammer.history;

public class Logger {
	
	public void log(String message) {
		log(System.currentTimeMillis(), message);
	}
	
	public void log(long timestamp, String message) {
		System.out.println("[" + timestamp + "] " + message);
	}

}
