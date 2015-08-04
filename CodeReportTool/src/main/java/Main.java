import git.Commit;
import git.GitUtils;
import git.Repository;
import git.SourceFile;

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

        Repository repository = new Repository(args[0]);

        Commit first = repository.getCommits().get(0);
        Commit last = repository.getCommits().get(repository.getCommits().size() - 1);

        Date startTime = first.getDate();
        System.out.println("Test started at " + startTime);

        Date endTime = last.getDate();
	    System.out.println("Test ended at " + endTime);

	    System.out.println("The test lasted " + (endTime.getTime() - startTime.getTime())/ (1000 * 60) + " minutes");

	    double chars = GitUtils.charsPerMinute(repo);
	    System.out.println("Characters per minute " + chars);

        System.out.println(last.getFiles());

        for (SourceFile s : last.getFiles()) {
            System.out.println("chars: " + s.getContents().length());
        }
    }
}
