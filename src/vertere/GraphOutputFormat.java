/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertere;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.Progressable;

/**
 *
 * @author RobSt
 */
public class GraphOutputFormat implements OutputFormat<Text, Text> {

    @Override
    public RecordWriter<Text, Text> getRecordWriter(FileSystem fs, JobConf jc, String name, Progressable progress) throws IOException {
        return new NTriplesRecordWriter(fs, jc, name, progress);
    }

    @Override
    public void checkOutputSpecs(FileSystem fs, JobConf jc) throws IOException {
        //TODO implement checks here
    }

    public static class NTriplesRecordWriter implements RecordWriter<Text, Text> {

        private DataOutputStream _out;
        private BufferedWriter _writer;
        private Progressable _progress;

        public NTriplesRecordWriter(FileSystem fs, JobConf jc, String name, Progressable progress) throws IOException {
            Path outputFolder = FileOutputFormat.getOutputPath(jc);
            Path outputFile = new Path(outputFolder, name + ".nt");
            DataOutputStream out = fs.create(outputFile);
            _out = out;
            Writer writer = new OutputStreamWriter(_out);
            _writer = new BufferedWriter(writer);
            _progress = progress;
        }

        @Override
        public void write(Text key, Text value) throws IOException {
            _writer.write(value.toString());
            _progress.progress();
        }

        @Override
        public void close(Reporter rprtr) throws IOException {
            _writer.flush();
            _writer.close();
            this._out.flush();
            this._out.close();
        }
    }
}
