/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertere;

import com.hp.hpl.jena.rdf.model.Model;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.Progressable;

/**
 *
 * @author RobSt
 */
public class GraphOutputFormat implements OutputFormat<Text, Model> {

    @Override
    public RecordWriter<Text, Model> getRecordWriter(FileSystem fs, JobConf jc, String string, Progressable p) throws IOException {
        Path outputPath = FileOutputFormat.getOutputPath(jc);
        DataOutputStream out = fs.create(outputPath);
        return new NTriplesRecordWriter(out);
    }

    @Override
    public void checkOutputSpecs(FileSystem fs, JobConf jc) throws IOException {
        //TODO implement checks here
    }
    
    public static class NTriplesRecordWriter implements RecordWriter<Text, Model> {

        private DataOutputStream _out;
        
        public NTriplesRecordWriter(DataOutputStream out) {
            this._out = out;
        }
        
        @Override
        public void write(Text key, Model model) throws IOException {
            model.write(this._out, "N-TRIPLE");
        }

        @Override
        public void close(Reporter rprtr) throws IOException {
            this._out.flush();
            this._out.close();
        }
        
    }
    
}
