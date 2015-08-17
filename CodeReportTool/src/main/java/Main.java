import analysis.Analysis;
import com.beust.jcommander.JCommander;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.LogVisitor;
import git.AbstractSyntaxTree;
import git.Commit;
import git.Repository;
import git.SourceFile;

public class Main {

    private static Repository repository;

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

        repository = new Repository(args[0]);

        repository.checkoutMaster();

        ArgumentParser arguments = new ArgumentParser();
        new JCommander(arguments, args);

        if (arguments.astlog) {
            int counter = 0;

            for (Commit c : repository.getCommits()) {
                for (SourceFile f : c.getFiles()) {
                    AbstractSyntaxTree ast = f.getAST();

                    if (ast != null) {
                        LogVisitor v = new LogVisitor();
                        CompilationUnit cu = ast.getCompilationUnit();

                        if (cu != null) {
                            cu.accept(v, null);
                            System.out.println("AST_snapshot_version:" + counter);
                            System.out.println("Timestamp:" + c.getDate().getTime());
                            System.out.println(v.getSource());
                            counter++;

                        }

                    }
                }
            }

            System.exit(0);
        }

        System.out.println("Test started at " + Analysis.getStartDate(repository));

        System.out.println("Test ended at " + Analysis.getEndDate(repository));

        System.out.println("The test lasted " + Analysis.getTestLength(repository) + " minutes");

        System.out.println("Characters per minute " + Analysis.getCharactersPerMinute(repository));
    }
}
