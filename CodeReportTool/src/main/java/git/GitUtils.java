package git;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;
import java.util.Locale;


public class GitUtils {

	public static String execShell(String command) {
		try {
			Process p = Runtime.getRuntime().exec(new String[]{"bash","-c",command});
			BufferedReader reader = 
				  new BufferedReader(new InputStreamReader(p.getInputStream()));

			StringBuilder sb = new StringBuilder();
			String line = "";           
			while ((line = reader.readLine())!= null) {
				    sb.append(line + "\n");
			}
			return sb.toString();
		} catch (Exception e) {
			return null;
		}

	}

	public static String[] getHashes(String path) {
		String result = execShell("cd " + path + "; git log --pretty=oneline --abbrev-commit");
		return result.split("\n");
	}

	public static Date getDate(String path, String hash) {
		try {
			String dateString = execShell("cd " + path + "; git show " + hash + " --pretty=format:\"%ad\" --date=iso").split("\n")[0];

			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.ENGLISH);
			Date date = format.parse(dateString);

			return date;
		} catch (Exception e) {
		}

		return null;
	}

	public static double charsPerMinute(String path) {
		String[] hashes = getHashes(path);

		String lastHash = hashes[0];
		String firstHash = hashes[hashes.length - 1];

		Date last = getDate(path, lastHash);
		Date first = getDate(path, firstHash);

		int lastChars = getJava(path, lastHash).length();
		int firstChars = getJava(path, firstHash).length();

		int charDelta = lastChars = firstChars;

		long timeDelta = (last.getTime()-first.getTime())/1000;

		double charPerSecond = (double) charDelta / (double) timeDelta;

		return charPerSecond * 60;
	}

	public static String getJava(String path, String hash) {
		execShell("cd " + path + "; git checkout " + hash);

		String java = execShell("find . -name '*.java' -exec cat {} \\;");

		execShell("cd " + path + "; git checkout master");

		return java;
	}

	public static Date startTime(String path) {
		String[] hashes = getHashes(path);
		String startHash = hashes[hashes.length - 1];

		return getDate(path, startHash);
	}
	public static Date endTime(String path) {
		String[] hashes = getHashes(path);
		String startHash = hashes[0];

		return getDate(path, startHash);
	}
}
