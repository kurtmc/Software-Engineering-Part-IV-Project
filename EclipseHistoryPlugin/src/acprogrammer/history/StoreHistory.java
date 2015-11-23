package acprogrammer.history;

import java.io.IOException;

public class StoreHistory {
	
	public static void execShell(String cmd) {
		Process p = null;
		try {
			p = Runtime.getRuntime().exec(new String[]{"bash", "-c", cmd});
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void commitFile(String repoPath, String filePath) {
		execShell("cd " + repoPath + "; git add " + filePath + "; git commit --allow-empty-message -m ''");
	}
	
	public static void commitAll(String path) {
		execShell("cd " + path + "; git add .; git commit -m \"message\"");
	}
	
	public static void gitInit(String path) {
		execShell("cd " + path + "; git init");
	}

}
