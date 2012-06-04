/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertere;

import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

/**
 *
 * @author RobSt
 */
public class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {

    @Override
    public void reduce(Text k2, Iterator<IntWritable> itrtr, OutputCollector<Text, IntWritable> oc, Reporter rprtr) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
