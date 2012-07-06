/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertere;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import java.text.DecimalFormat;
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
        conf.setOutputValueClass(Text.class);

        conf.setMapperClass(Map.class);
        conf.setCombinerClass(Reduce.class);
        conf.setReducerClass(Reduce.class);

        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(GraphOutputFormat.class);

        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));
        conf.setStrings("vertere.jobspec_filename", args[2]);
        conf.setStrings("vertere.jobspec_uri", args[3]);

        JobClient.runJob(conf);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.err.print("Usage:\nhadoop jar vertere.jar vertere.Verter /some/input.csv /some/output-folder /path/to/spec http://uri/of/spec");
            System.exit(1);
        }
        int res = ToolRunner.run(new Configuration(), new Vertere(), args);
        System.exit(res);
    }
    public static Property alternative_identity = ResourceFactory.createProperty("http://purl.org/ontology/vertere#alternative_identity");
    public static Property attribute = ResourceFactory.createProperty("http://purl.org/ontology/vertere#attribute");
    public static Property base_uri = ResourceFactory.createProperty("http://purl.org/ontology/vertere#base_uri");
    public static Property column = ResourceFactory.createProperty("http://purl.org/ontology/vertere#column");
    public static Property container = ResourceFactory.createProperty("http://purl.org/ontology/vertere#container");
    public static Property datatype = ResourceFactory.createProperty("http://purl.org/ontology/vertere#datatype");
    public static Property expected_header = ResourceFactory.createProperty("http://purl.org/ontology/vertere#expected_header");
    public static Property format = ResourceFactory.createProperty("http://purl.org/ontology/vertere#format");
    public static Property header_rows = ResourceFactory.createProperty("http://purl.org/ontology/vertere#header_rows");
    public static Property identity = ResourceFactory.createProperty("http://purl.org/ontology/vertere#identity");
    public static Property language = ResourceFactory.createProperty("http://purl.org/ontology/vertere#language");
    public static Property lookup = ResourceFactory.createProperty("http://purl.org/ontology/vertere#lookup");
    public static Property lookup_entry = ResourceFactory.createProperty("http://purl.org/ontology/vertere#lookup_entry");
    public static Property lookup_key = ResourceFactory.createProperty("http://purl.org/ontology/vertere#lookup_key");
    public static Property lookup_value = ResourceFactory.createProperty("http://purl.org/ontology/vertere#lookup_value");
    public static Property nest_under = ResourceFactory.createProperty("http://purl.org/ontology/vertere#nest_under");
    public static Property object_from = ResourceFactory.createProperty("http://purl.org/ontology/vertere#object_from");
    public static Property only_if = ResourceFactory.createProperty("http://purl.org/ontology/vertere#only_if");
    public static Property process = ResourceFactory.createProperty("http://purl.org/ontology/vertere#process");
    public static Property property = ResourceFactory.createProperty("http://purl.org/ontology/vertere#property");
    public static Property regex_match = ResourceFactory.createProperty("http://purl.org/ontology/vertere#regex_match");
    public static Property regex_output = ResourceFactory.createProperty("http://purl.org/ontology/vertere#regex_output");
    public static Property relationship = ResourceFactory.createProperty("http://purl.org/ontology/vertere#relationship");
    public static Property resource = ResourceFactory.createProperty("http://purl.org/ontology/vertere#resource");
    public static Property salt = ResourceFactory.createProperty("http://purl.org/ontology/vertere#salt");
    public static Property source_column = ResourceFactory.createProperty("http://purl.org/ontology/vertere#source_column");
    public static Property source_column_glue = ResourceFactory.createProperty("http://purl.org/ontology/vertere#source_column_glue");
    public static Property source_columns = ResourceFactory.createProperty("http://purl.org/ontology/vertere#source_columns");
    public static Property source_resource = ResourceFactory.createProperty("http://purl.org/ontology/vertere#source_resource");
    public static Property substring_start = ResourceFactory.createProperty("http://purl.org/ontology/vertere#substring_start");
    public static Property substring_length = ResourceFactory.createProperty("http://purl.org/ontology/vertere#substring_length");
    public static Property type = ResourceFactory.createProperty("http://purl.org/ontology/vertere#type");
    public static Resource Lookup = ResourceFactory.createResource("http://purl.org/ontology/vertere#Lookup");
    public static Resource Resource = ResourceFactory.createResource("http://purl.org/ontology/vertere#Resource");
    public static Resource Spec = ResourceFactory.createResource("http://purl.org/ontology/vertere#Spec");

    public static class Processes {

        public static Resource feet_to_metres = ResourceFactory.createResource("http://purl.org/ontology/vertere#feet_to_metres");
        public static Resource flatten_utf8 = ResourceFactory.createResource("http://purl.org/ontology/vertere#flatten_utf8");
        public static Resource normalise = ResourceFactory.createResource("http://purl.org/ontology/vertere#normalise");
        public static Resource regex = ResourceFactory.createResource("http://purl.org/ontology/vertere#regex");
        public static Resource round = ResourceFactory.createResource("http://purl.org/ontology/vertere#round");
        public static Resource sha512 = ResourceFactory.createResource("http://purl.org/ontology/vertere#sha512");
        public static Resource substr = ResourceFactory.createResource("http://purl.org/ontology/vertere#substr");
        public static Resource title_case = ResourceFactory.createResource("http://purl.org/ontology/vertere#title_case");
        public static Resource trim_quotes = ResourceFactory.createResource("http://purl.org/ontology/vertere#trim_quotes");
        public static Resource trim = ResourceFactory.createResource("http://purl.org/ontology/vertere#trim");
        public static Resource urlify = ResourceFactory.createResource("http://purl.org/ontology/vertere#urlify");
        public static Resource sql_date = ResourceFactory.createResource("http://purl.org/ontology/vertere#sql_date");
    }
    
    public static class Tests {
        public static Resource distinct = ResourceFactory.createResource("http://purl.org/ontology/vertere#distinct");
        public static Resource empty = ResourceFactory.createResource("http://purl.org/ontology/vertere#empty");
    }
}
