import analysis.Analysis;
import com.github.javaparser.ast.visitor.LogVisitor;
import git.AbstractSyntaxTree;
import git.Repository;
import git.SourceFile;

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

        System.out.println("Test started at " + Analysis.getStartDate(repository));

        System.out.println("Test ended at " + Analysis.getEndDate(repository));

        System.out.println("The test lasted " + Analysis.getTestLength(repository) + " minutes");

        System.out.println("Characters per minute " + Analysis.getCharactersPerMinute(repository));

        for (SourceFile f : repository.getCommits().get(repository.getCommits().size() - 1).getFiles()) {
            AbstractSyntaxTree ast = f.getAST();

            LogVisitor v = new LogVisitor();
            ast.getCompilationUnit().accept(v, null);
            System.out.println(v.getSource());
        }

    }
}
