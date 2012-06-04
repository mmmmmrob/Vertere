/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertere;

import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 *
 * @author RobSt
 */
public class Vertere extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        JobConf conf = new JobConf(getConf(), Vertere.class);
        conf.setJobName("vertere");

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(com.hp.hpl.jena.rdf.model.Model.class);

        conf.setMapperClass(Map.class);
        conf.setCombinerClass(Reduce.class);
        conf.setReducerClass(Reduce.class);

        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(GraphOutputFormat.class);

        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));
        conf.setStrings("vertere.jobspec", args[2]);

        JobClient.runJob(conf);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.print("Usage:\nhadoop jar vertere.jar vertere.Verter /some/input.csv /some/output-folder /path/to/spec");
            System.exit(1);
        }
        int res = ToolRunner.run(new Configuration(), new Vertere(), args);
        System.exit(res);
    }
}
