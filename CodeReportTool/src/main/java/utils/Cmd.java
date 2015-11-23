package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Cmd {

	public static String execShell(String command) {
		try {
			Process p = Runtime.getRuntime().exec(new String[]{"bash","-c",command});
			BufferedReader reader = 
				  new BufferedReader(new InputStreamReader(p.getInputStream()));

			StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine())!= null) {
                sb.append(line);
                sb.append("\n");
            }
			return sb.toString();
		} catch (Exception e) {
			return null;
		}

	}

}
