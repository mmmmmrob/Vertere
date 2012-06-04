/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertere;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 *
 * @author RobSt
 */
public class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Model> {

    private Model _spec;
    
    public Map() {
    }

    @Override
    public void configure(JobConf job) {
        super.configure(job);

        BufferedReader br = null;
        try {
            Path pt = new Path(job.get("vertere.spec"));
            FileSystem fs = FileSystem.get(new Configuration());
            FSDataInputStream stream = fs.open(pt);
            this._spec = ModelFactory.createDefaultModel();
            RDFReader reader = this._spec.getReader("TURTLE");
            reader.read(this._spec, stream, "");
        } catch (IOException ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }
    }
    

    @Override
    public void map(LongWritable k1, Text v1, OutputCollector<Text, Model> oc, Reporter rprtr) throws IOException {
        
        throw new UnsupportedOperationException("Not supported yet.  **" + v1 + "**");
    }
    
}
