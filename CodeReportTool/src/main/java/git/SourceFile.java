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

    public SourceFile(String path) {
        _path = path;
    }

    public String getContents() {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(_path));
            return new String(encoded, Charset.defaultCharset());
        } catch (IOException e) {
            return null;
        }
    }

    public String toString() {
        return _path;
    }
}
