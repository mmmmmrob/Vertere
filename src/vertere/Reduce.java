/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertere;

import com.hp.hpl.jena.rdf.model.Model;
import java.io.IOException;
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
        while (itrtr.hasNext()) {
            oc.collect(k2, itrtr.next());
        }
    }

    
}
