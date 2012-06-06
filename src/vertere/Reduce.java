/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertere;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

/**
 *
 * @author RobSt
 */
public class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text k2, Iterator<Text> itrtr, OutputCollector<Text, Text> oc, Reporter rprtr) throws IOException {
        int numIterations = 0;
        Model model = ModelFactory.createDefaultModel();
        while (itrtr.hasNext()) {
            Text value = itrtr.next();
            if (numIterations % 10 == 0) {
                rprtr.progress();
            }
            StringReader reader = new StringReader(value.toString());
            model.read(reader, "http://create.absolute.uris.to.remove.this/", "N-TRIPLE");
        }
        StringWriter writer = new StringWriter();
        model.write(writer, "N-TRIPLE");
        oc.collect(k2, new Text(writer.toString()));
    }
}
