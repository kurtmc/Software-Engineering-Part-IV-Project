package git;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SourceFile {

    String _path;

    String _contents;

    public SourceFile(String path) {
        _path = path;
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
            byte[] encoded = Files.readAllBytes(Paths.get(_path));
            _contents = new String(encoded, Charset.defaultCharset());
        } catch (IOException e) {
            _contents =  null;
        }
    }

    public String toString() {
        return _path;
    }
}
