
import java.io.*;

public class Log {
		
	private static PrintWriter log;
	
	public static void open(String filename) throws FileNotFoundException {
		close();
		log = new PrintWriter(filename);
	}
	
	public static void write(String line) {
		if (log == null) return;
		line = line.replace("\r", " ");
		line = line.replace("\n", " ");
		log.println(line);
		log.flush();
	}
	
	public static void close() {
		if (log != null) log.close();
		log = null;
	}
}
