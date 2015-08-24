package tests;

import com.github.javaparser.ast.CompilationUnit;
import git.AbstractSyntaxTree;
import git.Commit;
import git.Repository;
import git.SourceFile;
import utils.Cmd;

import java.io.FileWriter;
import java.io.IOException;

public class TestRunner {

    /**
     * @param srcFiles       /my/path/to/project/src
     * @param testFiles      /my/path/to/project/test
     * @param gradleBuild    /my/path/to/project/build.gradle
     * @param gradleSettings /my/path/to/project/settings.gradle
     * @param tmpDirectory   /tmp/CodeReportTmp
     */
    public static void runTests(String srcFiles, String testFiles, String gradleBuild, String gradleSettings, String tmpDirectory) {
        // Initially create temporary directory
        Cmd.execShell("mkdir " + tmpDirectory);

        Cmd.execShell("cp -r " + srcFiles + " " + tmpDirectory + "/");

        Cmd.execShell("cp -r " + testFiles + " " + tmpDirectory + "/");

        Cmd.execShell("cp " + gradleBuild + " " + tmpDirectory + "/");

        Cmd.execShell("cp " + gradleSettings + " " + tmpDirectory + "/");

        String test = Cmd.execShell("cd " + tmpDirectory + "; gradle test ");

        System.out.println(test);

        // Delete temporary directory
        Cmd.execShell("rm -rf " + tmpDirectory);
    }

    /**
     * @param projectDir /my/path/to/project
     */
    public static void runTests(String projectDir, Repository repository) {
        runTests(projectDir, projectDir + "/src");
    }

    public static void runTests(String projectDir, String srcDir) {
        runTests(srcDir, projectDir + "/test", projectDir + "/build.gradle", projectDir + "/settings.gradle", "/tmp/CodeReportTmp");
    }

    public static void runTestOnEveryAST(String projectDir, Repository repository) {
        for (Commit c : repository.getCommits()) {
            if (c.getFiles().size() > 1)
                continue; // TODO: Can't handle more than one file for the moment
            for (SourceFile f : c.getFiles()) {
                AbstractSyntaxTree ast = f.getAST();

                if (ast != null) {
                    CompilationUnit cu = ast.getCompilationUnit();

                    if (cu != null) {

                        String relativeDirectory = f.getRelativePath().substring(0, f.getRelativePath().lastIndexOf("/"));

                        // Initially create temporary directory
                        Cmd.execShell("mkdir -p /tmp/CodeReportTmpSourceFile/" + relativeDirectory);

                        FileWriter fw;
                        try {
                            fw = new FileWriter("/tmp/CodeReportTmpSourceFile/" + f.getRelativePath(), false);
                            fw.write(f.getContents());
                            fw.flush();
                            fw.close();
                        } catch (IOException e) {
                            System.err.println("Failed to open " + "/tmp/CodeReportTmpSourceFile/" + f.getRelativePath());
                        }

                        runTests(projectDir, "/tmp/CodeReportTmpSourceFile/src");

                        // Remove temp dir
                        Cmd.execShell("rm -r /tmp/CodeReportTmpSourceFile");
                    }

                }
            }
        }
    }

}
