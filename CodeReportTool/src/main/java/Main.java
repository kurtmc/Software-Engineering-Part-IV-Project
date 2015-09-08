import analysis.Analysis;
import analysis.AstAnalysis;
import com.beust.jcommander.JCommander;
import git.Repository;
import tests.TestRunner;

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

	String s = repository.executeGitCommand("git status");
	System.out.println(s);
	s = repository.executeGitCommand("pwd");
	System.out.println(s);
	System.exit(0);

        repository.checkoutMaster();

        ArgumentParser arguments = new ArgumentParser();
        new JCommander(arguments, args);

        if (arguments.astlog) {
            AstAnalysis.printAstLogFile(repository);
            System.exit(0);
        } else if (arguments.defaultTest != null) {
            TestRunner.runTestOnEveryAST(arguments.defaultTest, repository);
            System.exit(0);
        } else {

            System.out.println("Test started at " + Analysis.getStartDate(repository));

            System.out.println("Test ended at " + Analysis.getEndDate(repository));

            System.out.println("The test lasted " + Analysis.getTestLength(repository) + " minutes");

            System.out.println("Characters per minute " + Analysis.getCharactersPerMinute(repository));

            AstAnalysis.printChangedNodes(repository);
        }
    }
}
