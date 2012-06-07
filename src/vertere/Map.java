/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertere;

import au.com.bytecode.opencsv.CSVParser;
import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
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
    public void map(LongWritable k1, Text line, OutputCollector<Text, Text> oc, Reporter rprtr) throws IOException {
        
        String expectedHeader = _spec.getExpectedHeader();
        if (line.toString().equals(expectedHeader)) {
            return;
        }

        String[] record = _parser.parseLine(line.toString());

        HashMap<Resource, Resource> uris = createUris(record);

        //Iterate over the resources to build a model for each
        Set<Resource> keySet = uris.keySet();
        Iterator<Resource> keyitrtr = keySet.iterator();
        HashMap<Resource, Model> models = new HashMap<Resource, Model>();
        while (keyitrtr.hasNext()) {
            /*
             * TODO Add default types Create Relationships Create Attributes
             * oc.collect each subject
             */
            Resource resourceSpec = keyitrtr.next();
            Resource subject = uris.get(resourceSpec);
            Model model = ModelFactory.createDefaultModel();
            stateDefaultTypes(resourceSpec, subject, model);
            stateRelationships(resourceSpec, subject, uris, model);
            stateAttributes(resourceSpec, subject, record, model);
            models.put(subject, model);
        }

        //Iterate over subject to write each model out to ntriples
        Set<Resource> subjectSet = models.keySet();
        Iterator<Resource> subjectIterator = subjectSet.iterator();
        while (subjectIterator.hasNext()) {
            Resource subject = subjectIterator.next();
            Writer writer = new StringWriter();
            Model subjectModel = models.get(subject);
            subjectModel.write(writer, "N-TRIPLES");
            oc.collect(new Text(subject.getURI()), new Text(writer.toString()));
        }
    }

    private void stateAttributes(Resource resourceSpec, Resource subject, String[] record, Model model) {
        NodeIterator attributes = _spec.getAttributes(resourceSpec);
        if (null == attributes) {
            return;
        }
        while (attributes.hasNext()) {
            Resource attribute = attributes.next().asResource();
            String sourceValue = getSourceValueFromSourceColumns(attribute, record);
            Property property = _spec.getRelationshipProperty(attribute);
            RDFDatatype datatype = _spec.getDatatype(attribute);
            String language = _spec.getLanguage(attribute);
            RDFNode lookupValue = _spec.lookup(attribute, sourceValue);
            sourceValue = process(attribute, sourceValue);
            if (null != lookupValue) {
                model.add(subject, property, lookupValue);
            } else if (null != language && language.length() != 0) {
                model.add(subject, property, sourceValue, language);
            } else if (null != datatype) {
                model.add(subject, property, sourceValue, datatype);
            } else {
                model.add(subject, property, sourceValue);
            }
        }
    }

    private void stateRelationships(Resource resourceSpec, Resource subject, HashMap<Resource, Resource> uris, Model model) {
        NodeIterator relationships = _spec.getRelationships(resourceSpec);
        if (null == relationships) {
            return;
        }
        while (relationships.hasNext()) {
            Resource relationship = relationships.next().asResource();
            Property property = _spec.getRelationshipProperty(relationship);
            Resource objectFrom = _spec.getRelationshipObjectFrom(relationship);
            if (null != objectFrom && null != property) {
                Resource object = uris.get(objectFrom);
                if (null != object) {
                    model.add(subject, property, object);
                }
            }
        }
    }

    private void stateDefaultTypes(Resource resourceSpec, Resource subject, Model model) {
        NodeIterator specifiedTypes = _spec.getSpecifiedTypes(resourceSpec);
        while (specifiedTypes.hasNext()) {
            Resource type = specifiedTypes.next().asResource();
            model.add(subject, RDF.type, type);
        }
    }

    private String addTrailingSlashIfNeeded(String uri) {
        if (uri.length() == 0) {
            return uri;
        }
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
        Resource sourceResource = getSourceValueFromSourceResource(identity, uris, record);
        if (sourceValue.length() > 0) {
            createUri(record, uris, resource, identity, sourceValue);
        } else if (sourceResource != null) {
            createUri(record, uris, resource, identity, sourceResource);
        }
    }

    private void createUri(String[] record, HashMap<Resource, Resource> uris, Resource resource, Resource identity, Resource sourceResource) {
        String sourceValue = sourceResource.getURI();
        sourceValue = process(identity, sourceValue);

        if (sourceValue.length() != 0) {
            Resource subject = ResourceFactory.createResource(sourceValue);
            uris.put(resource, subject);
        } else {
            Resource alternativeIdentity = _spec.getAlternativeIdentity(resource);
            if (alternativeIdentity != null) {
                createUri(record, uris, resource, alternativeIdentity);
            }
        }
    }

    private void createUri(String[] record, HashMap<Resource, Resource> uris, Resource resource, Resource identity, String sourceValue) {

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
        sourceValue = process(identity, sourceValue);

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

    private String process(Resource resource, String value) {
        String newValue = value;
        RDFList processingSteps = _spec.getProcessingSteps(resource);
        if (null == processingSteps) {
            return value;
        }

        ExtendedIterator<RDFNode> iterator = processingSteps.iterator();
        while (iterator.hasNext()) {
            Resource processStep = iterator.next().asResource();
            Resource stepType = OWL.Thing;
            if (processStep.hasProperty(RDF.type)) {
                stepType = processStep.getProperty(RDF.type).getResource();
            }
            if (processStep.equals(Vertere.Processes.feet_to_metres) || stepType.equals(Vertere.Processes.feet_to_metres)) {
                newValue = Processor.feetToMetres(newValue);
            }
            if (processStep.equals(Vertere.Processes.flatten_utf8) || stepType.equals(Vertere.Processes.flatten_utf8)) {
                newValue = Processor.flattenUtf8(newValue);
            }
            if (processStep.equals(Vertere.Processes.normalise) || stepType.equals(Vertere.Processes.normalise)) {
                newValue = Processor.normalise(newValue);
            }
            if (processStep.equals(Vertere.Processes.regex)) {
                newValue = Processor.regex(newValue, _spec, resource);
            }
            if (stepType.equals(Vertere.Processes.regex)) {
                newValue = Processor.regex(newValue, _spec, processStep);
            }
            if (processStep.equals(Vertere.Processes.round) || stepType.equals(Vertere.Processes.round)) {
                newValue = Processor.round(newValue);
            }
            if (processStep.equals(Vertere.Processes.sha512)) {
                newValue = Processor.sha512(newValue, _spec, resource);
            }
            if (stepType.equals(Vertere.Processes.sha512)) {
                newValue = Processor.sha512(newValue, _spec, processStep);
            }
            if (processStep.equals(Vertere.Processes.substr) || stepType.equals(Vertere.Processes.substr)) {
                newValue = Processor.substr(newValue, _spec, resource, processStep);
            }
            if (processStep.equals(Vertere.Processes.title_case) || stepType.equals(Vertere.Processes.title_case)) {
                newValue = Processor.titleCase(newValue);
            }
            if (processStep.equals(Vertere.Processes.trim_quotes) || stepType.equals(Vertere.Processes.trim_quotes)) {
                newValue = Processor.trimQuotes(newValue);
            }
            if (processStep.equals(Vertere.Processes.trim) || stepType.equals(Vertere.Processes.trim)) {
                newValue = Processor.trim(newValue);
            }
        }
        return newValue;
    }

    private Resource getSourceValueFromSourceResource(Resource identity, HashMap<Resource, Resource> uris, String[] record) {
        Resource sourceResourceSpec = _spec.getSourceResource(identity);
        if (sourceResourceSpec != null && !uris.containsKey(sourceResourceSpec)) {
            createUri(record, uris, sourceResourceSpec);
        }
        if (uris.containsKey(sourceResourceSpec)) {
            return uris.get(sourceResourceSpec);
        } else {
            return null;
        }
    }

    private String getSourceValueFromSourceColumns(Resource resource, String[] record) {
        String sourceValue = "";
        int[] sourceColumnNumbers = _spec.getSourceColumns(resource);
        String sourceColumnGlue = _spec.getGlue(resource);
        for (int i = 0; i < sourceColumnNumbers.length; i++) {
            if (record[sourceColumnNumbers[i] - 1].length() > 0) {
                if (i > 0) {
                    sourceValue += sourceColumnGlue;
                }
                sourceValue += record[sourceColumnNumbers[i] - 1];
            }
        }
        return sourceValue;
    }
}
