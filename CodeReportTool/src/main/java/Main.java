import git.Commit;
import git.Repository;
import git.SourceFile;

import java.util.Date;

public class Main {

    private static void print(Object o) {
        System.out.println(o);
    }
    /**
     * Handles the command line interface to generating reports based on git repos
     * @param args should contain one string of the repo to be analysed
     */
    public static void main(String[] args) {

        if (args.length < 1) {
            print("Not enough arguments. You need to specify the git repo");
	        System.exit(1);
        }

        Repository repository = new Repository(args[0]);

        Commit first = repository.getCommits().get(0);
        Commit last = repository.getCommits().get(repository.getCommits().size() - 1);

        Date startTime = first.getDate();
        System.out.println("Test started at " + startTime);

        Date endTime = last.getDate();
	    System.out.println("Test ended at " + endTime);

        long durationMinutes = (endTime.getTime() - startTime.getTime())/ (1000 * 60);
	    System.out.println("The test lasted " + durationMinutes + " minutes");

        int startChars = 0;
        int endChars = 0;

        for (SourceFile s : first.getFiles()) {
            startChars += s.getContents().length();
        }

        for (SourceFile s : last.getFiles()) {
            endChars += s.getContents().length();
        }

        System.out.println("Characters per minute " + (endChars - startChars)/durationMinutes);
    }
}
