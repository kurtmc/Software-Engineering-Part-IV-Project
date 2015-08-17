import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kurt McAlpine on 18/08/15.
 */
public class ArgumentParser {
    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = {"-ast", "-astlog"}, description = "Print out the ast log")
    public boolean astlog = false;

}
