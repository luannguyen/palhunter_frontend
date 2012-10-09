/**
 * 
 */
package edu.usc.cs587.examples;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Ling
 *
 */
public class Constants {
	public static void main(String[] args) {
		String userDir = System.getProperty("user.dir");
		System.out.println("userDir:"+userDir);
		String userHome = System.getProperty("user.home");
		System.out.println("userDir:"+userHome);
		String fileSeparator = System.getProperty("file.separator");
		System.out.println("file.separator:"+fileSeparator);
		System.out.println("os.name:"+getOS());
		System.out.println("current time:"+getCurrentTime());
		System.out.println("Testing importing jar file...");
	}
	public static String getOS() {
		return "changed os "+System.getProperty("os.name");
	}
	public static String getUserDir() {
		return System.getProperty("user.dir");
	}
	public static String getUserHome() {
		return System.getProperty("user.home");
	}
	public static String getPath() {
		File f = new File("");
		return f.getAbsolutePath();
	}
	public static String getCurrentTime() {
		long current = System.currentTimeMillis();
		Date dcurrent = new Date(current);
		return dcurrent.toString();
	}
	public static String getTimeAsString(long a) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return "["+df.format(new Date(a))+"]";
	}
}

