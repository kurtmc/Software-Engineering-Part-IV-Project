package git;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.JavaParser;
import java.io.InputStream;
import java.io.IOException;
import com.github.javaparser.ParseException;
import com.github.javaparser.TokenMgrError;

public class AbstractSyntaxTree {

	private CompilationUnit _comp;
	private SourceFile _source;

	public AbstractSyntaxTree(SourceFile sourceFile) {
		_source = sourceFile;
		InputStream in = _source.getInputStream();

		_comp = null;
		try {
			_comp = JavaParser.parse(in);
			in.close();
		} catch (IOException e) {
		} catch (ParseException e) {
		} catch (TokenMgrError e) {
		} finally {
		}
	}

	public CompilationUnit getCompilationUnit() {
		return _comp;
	}

	public String toString() {
		if (_comp != null)
			return _comp.toString();
		return null;
	}
}
