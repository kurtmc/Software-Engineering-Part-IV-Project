package git;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by kurt on 4/08/15.
 */
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

    public void readContents() {
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
