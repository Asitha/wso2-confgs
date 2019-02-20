import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Mojo(name = "merge")
public class Merge extends AbstractMojo {

    @Parameter(name = "tasks")
    private List<Task> tasks;

    public void execute() throws MojoExecutionException, MojoFailureException {

        for (Task taskModel : tasks) {
            String config = taskModel.getConfig();
            String input = taskModel.getInput();
            String output = taskModel.getOutput();

            Map inputMap = Utils.getReadMap(input);
            Map outputMap = Utils.getReadMap(config);
            Utils.mergeMaps(inputMap, outputMap);
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(output))) {
                bufferedWriter.write(Utils.convertIntoJson(outputMap));
                bufferedWriter.flush();
            } catch (IOException e) {
                throw new MojoFailureException(e, "Error while Writing Merged Json", "Error while writing Json");
            }
        }

    }
}
