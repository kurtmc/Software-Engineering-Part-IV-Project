package git;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;

public class Repository {

    private String _path;

    public Repository(String path) {
        this._path = path;
    }

    public String executeGitCommand(String command) {
        return GitUtils.execShell("cd " + _path + "; " + command);
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
}
