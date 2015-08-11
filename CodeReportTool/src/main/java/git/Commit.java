package git;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commit {

    private String _hash;
    private Repository _repository;

    public Commit(String hash, Repository repository) {
        _hash = hash;
        _repository = repository;
    }

    public Date getDate() {
        try {
            String dateString = _repository.executeGitCommand("git show " + _hash + " --pretty=format:\"%ad\" --date=iso").split("\n")[0];

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.ENGLISH);

            return format.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }

    public List<SourceFile> getFiles() {
        String currentRef = _repository.executeGitCommand("git rev-parse --abbrev-ref HEAD");

        _repository.executeGitCommand("git checkout " + _hash);

        String[] filenames = _repository.executeGitCommand("git ls-tree -r --name-only HEAD").split("\n");
        if (filenames.length < 1) {
            return null;
        }
        List<SourceFile> files = new ArrayList<>();
        for (String s : filenames) {
            files.add(new SourceFile(_repository.getPath() + "/" + s));
        }

        _repository.executeGitCommand("git checkout " + currentRef);

        return files;
    }

    public List<String> getLineChanges(String path) {
	    String result = _repository.executeGitCommand("git blame -l " + path + " | grep ^" + _hash);
	    String[] lines = result.split("\n");
	    List<String> changes = new ArrayList<String>();
	    for (String l : lines) {
		if (l.length() > 0) {
			changes.add(l);
		}
	    }
	    String pattern = "([0-9]{1,3})\\)";
	    Pattern r = Pattern.compile(pattern);
	    Matcher m = r.matcher("the string");
	    for (String l : changes) {
        }
	    if (changes.size() > 0) {
		    return changes;
	    }
	    return null;
    }
}
