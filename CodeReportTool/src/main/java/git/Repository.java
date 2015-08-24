package git;

import com.github.javaparser.ast.CompilationUnit;
import com.google.common.collect.Lists;
import utils.Cmd;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    private String _path;

    public Repository(String path) {
        String thePath = path;

        // Remove trailing /'s
        while (thePath.charAt(thePath.length() - 1) == '/')
            thePath = thePath.substring(0, thePath.length() - 2);

        this._path = thePath;
    }

    public String executeGitCommand(String command) {
        return Cmd.execShell("cd " + _path + "; " + command);
    }

    /**
     * @return list of commits in chronological order
     */
    public List<Commit> getCommits() {
        String[] hashes = executeGitCommand("git log --pretty=format:%H").split("\n");
        List<Commit> commits = new ArrayList<>();

        for (String hash : hashes) {
            commits.add(new Commit(hash, this));
        }

        commits = Lists.reverse(commits);

        return commits;
    }

    public String getPath() {
        return _path;
    }

    public List<CompilationUnit> getAbstractSyntaxTrees() {
	    for (Commit c : getCommits()) {
		c.getFiles();
	    }
	    return null;
    }

    public void checkoutMaster() {
        executeGitCommand("git checkout master");
    }
}
