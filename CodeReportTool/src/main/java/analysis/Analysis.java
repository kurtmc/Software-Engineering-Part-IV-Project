package analysis;

import git.Commit;
import git.Repository;
import git.SourceFile;

import java.util.Date;

public class Analysis {

    public static java.util.Date getStartDate(Repository repository) {
        return repository.getCommits().get(0).getDate();
    }

    public static Date getEndDate(Repository repository) {
        return repository.getCommits().get(repository.getCommits().size() - 1).getDate();
    }

    public static int getTestLength(Repository repository) {
        return (int) (getEndDate(repository).getTime() - getStartDate(repository).getTime()) / (1000 * 60);
    }

    public static int getCharactersPerMinute(Repository repository) {
            Commit first = repository.getCommits().get(0);
            Commit last = repository.getCommits().get(repository.getCommits().size() - 1);

            int startChars = 0;
            int endChars = 0;

            if (first == null)
                return 0;

            for (SourceFile s : first.getFiles()) {
                startChars += s.getContents().length();
            }

            for (SourceFile s : last.getFiles()) {
                endChars += s.getContents().length();
            }

            int testLength = getTestLength(repository);
            if (testLength > 0)
                return (endChars - startChars) / getTestLength(repository);

        return 0;
    }
}
