/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertere;

import java.io.IOException;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputFormat;
import org.apache.hadoop.mapred.RecordWriter;
import org.apache.hadoop.util.Progressable;

/**
 *
 * @author RobSt
 */
public class GraphOutputFormat implements OutputFormat<Object, Object> {

    @Override
    public RecordWriter<Object, Object> getRecordWriter(FileSystem fs, JobConf jc, String string, Progressable p) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void checkOutputSpecs(FileSystem fs, JobConf jc) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
