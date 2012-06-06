/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertere;

import au.com.bytecode.opencsv.CSVParser;
import com.hp.hpl.jena.rdf.model.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

/**
 *
 * @author RobSt
 */
public class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {

    private Spec _spec;
    private CSVParser _parser;
    private Logger _log = Logger.getLogger(Map.class.getName());

    public Map() {
        _parser = new CSVParser();
    }

    @Override
    public void configure(JobConf job) {
        super.configure(job);
        final String spec_filename = job.get("vertere.jobspec_filename");
        final String spec_uri = job.get("vertere.jobspec_uri");
        this._spec = new Spec(spec_filename, spec_uri);
    }

    @Override
    public void map(LongWritable k1, Text v1, OutputCollector<Text, Text> oc, Reporter rprtr) throws IOException {
        String[] record = _parser.parseLine(v1.toString());
        HashMap<Resource, Resource> uris = createUris(record);
        Set<Resource> keySet = uris.keySet();
        Iterator<Resource> keyitrtr = keySet.iterator();
        while (keyitrtr.hasNext()) {
            Resource key = keyitrtr.next();
            Resource value = uris.get(key);
            oc.collect(new Text(key.getURI()), new Text(value.getURI()));
        }
    }

    private String addTrailingSlashIfNeeded(String uri) {
        if (uri.length() == 0) { return uri; }
        if (!uri.endsWith("/") && !uri.endsWith("#")) {
            return uri + "/";
        } else {
            return uri;
        }
    }

    private HashMap<Resource, Resource> createUris(String[] record) {
        HashMap<Resource, Resource> uris = new HashMap<Resource, Resource>();
        NodeIterator resources = _spec.getResources();
        
        while (resources.hasNext()) {
            Resource resource = resources.next().asResource();
            createUri(record, uris, resource);
        }
        
        return uris;
    }

    private void createUri(String[] record, HashMap<Resource, Resource> uris, Resource resource) {
        Resource identity = _spec.getIdentity(resource);
        createUri(record, uris, resource, identity);
    }

    private void createUri(String[] record, HashMap<Resource, Resource> uris, Resource resource, Resource identity) {

        String sourceValue = getSourceValueFromSourceColumns(identity, record);
        if (sourceValue.length() == 0) {
            sourceValue = getSourceValueFromSourceResource(identity, uris, record);
        }
        
        if (sourceValue.length() == 0) { return; }

        //Check for lookups on the value. If lookup results in a named resource then use that
        if (_spec.hasLookup(identity)) {
            RDFNode lookupValue = _spec.lookup(identity, sourceValue);
            if (lookupValue.isURIResource()) {
                uris.put(resource, lookupValue.asResource());
                return;
            } else {
                sourceValue = lookupValue.asLiteral().getLexicalForm();
            }
        }
        
        //Decide on a base for the URI
        String baseUri = _spec.getBaseUri(identity);
        if (baseUri == null) {
            baseUri = _spec.getBaseUri();
        }

        //Decide if the resource should be nested (overrides the base_uri)
        Resource nestedUnder = _spec.getNestedUnder(identity);
        if (nestedUnder != null && !uris.containsKey(nestedUnder)) {
            createUri(record, uris, nestedUnder);
        }
        if (uris.containsKey(nestedUnder)) {
            baseUri = uris.get(nestedUnder).getURI();
            baseUri = addTrailingSlashIfNeeded(baseUri);
        }
        
        String container = addTrailingSlashIfNeeded(_spec.getContainer(identity));

        //TODO add processing in here
        
        if (sourceValue.length() != 0) {
            Resource subject = ResourceFactory.createResource(baseUri + container + sourceValue);
            uris.put(resource, subject);
        } else {
            Resource alternativeIdentity = _spec.getAlternativeIdentity(resource);
            if (alternativeIdentity != null) {
                createUri(record, uris, resource, alternativeIdentity);
            }
        }
    }

    private String getSourceValueFromSourceResource(Resource identity, HashMap<Resource, Resource> uris, String[] record) {
        String sourceValue = "";
        Resource sourceResourceSpec = _spec.getSourceResource(identity);
        if (sourceResourceSpec != null && !uris.containsKey(sourceResourceSpec)) {
            createUri(record, uris, sourceResourceSpec);
        }
        if (uris.containsKey(sourceResourceSpec)) {
            sourceValue = uris.get(sourceResourceSpec).getURI();
        }
        return sourceValue;
    }

    private String getSourceValueFromSourceColumns(Resource identity, String[] record) {
        String sourceValue = "";
        int[] sourceColumnNumbers = _spec.getSourceColumns(identity);
        String sourceColumnGlue = _spec.getGlue(identity);
        for (int i=0; i < sourceColumnNumbers.length; i++) {
            if (record[sourceColumnNumbers[i] - 1].length() > 0) {
                if (i > 0) { sourceValue += sourceColumnGlue; }
                sourceValue += record[sourceColumnNumbers[i] - 1];
            }
        }
        return sourceValue;
    }
}
