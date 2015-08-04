package git;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by kurt on 4/08/15.
 */
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
            Date date = format.parse(dateString);

            return date;
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
}
