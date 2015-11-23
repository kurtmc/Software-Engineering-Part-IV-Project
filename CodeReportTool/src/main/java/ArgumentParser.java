import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class ArgumentParser {

    @Parameter(names = "-sourceFile", description = "Source file to analyse")
    public String sourceFile = null;

    @Parameter(names = {"-ast", "-astlog"}, description = "Print out the ast log")
    public boolean astlog = false;

    @Parameter(names = "-defaultTest")
    public String defaultTest = null;

    @Parameter
    private List<String> parameters = new ArrayList<>();

}
