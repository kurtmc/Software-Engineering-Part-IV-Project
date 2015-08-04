import java.util.Date;

public class Main {

    private static void print(Object o) {
        System.out.println(o);
    }
    /**
     * Handles the command line interface to generating reports based on git repos
     * @param args
     */
    public static void main(String[] args) {

        if (args.length < 1) {
            print("Not enough arguments. You need to specify the git repo");
	    System.exit(1);
        }

	String repo = args[0];


	Date startTime = GitWrapper.startTime(repo);
	System.out.println("Test started at " + startTime);

	Date endTime = GitWrapper.endTime(repo);
	System.out.println("Test ended at " + endTime);

	System.out.println("The test lasted " + (endTime.getTime() - startTime.getTime())/ (1000 * 60) + " minutes");

	double chars = GitWrapper.charsPerMinute(repo);
	System.out.println("Characters per minute " + chars);
    }
}
