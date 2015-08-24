package git;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SourceFile {

    String _relativePath;

    Repository _repository;

    String _contents;

    public SourceFile(Repository repository, String relativePath) {
        _relativePath = relativePath;
        _repository = repository;
        readContents();
    }

    public String getContents() {
        return _contents;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(getContents().getBytes(Charset.defaultCharset()));
    }

    private void readContents() {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(getPath()));
            _contents = new String(encoded, Charset.defaultCharset());
        } catch (IOException e) {
            _contents =  null;
        }
    }

    public String toString() {
        return getPath();
    }

    public AbstractSyntaxTree getAST() {
	    return new AbstractSyntaxTree(this);
    }

    public String getPath() {
        return _repository.getPath() + "/" + _relativePath;
    }

    public String getRelativePath() {
        return _relativePath;
    }
}
