/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertere;

import com.hp.hpl.jena.datatypes.BaseDatatype;
import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 *
 * @author RobSt
 */
public class Spec {

    private Model _model;
    private Resource _specResource;

    public Spec(final String spec_filename, final String spec_uri) {
        if (null == spec_filename) {
            throw new RuntimeException("Can't create spec and filename was null");
        }
        Logger log = Logger.getLogger(Spec.class.getName());

        try {
            log.log(Level.INFO, "Attempting to create Spec from {0}", spec_filename);
            Path pt = new Path(spec_filename);
            FileSystem fs = FileSystem.get(new Configuration());
            log.log(Level.INFO, "Opening {0}", spec_filename);
            FSDataInputStream stream = fs.open(pt);
            _model = ModelFactory.createDefaultModel();
//            _model.setNsPrefixes(getPrefixes());
            RDFReader reader = this._model.getReader("TURTLE");
            log.log(Level.INFO, "Reading {0}", spec_filename);
            reader.read(this._model, stream, "http://put.a.base.in.your.spec/");
            log.log(Level.INFO, "Successfully created Spec from {0}", spec_filename);
            _specResource = _model.getResource(spec_uri);
            if (!_model.containsResource(_specResource)) {
                log.log(Level.WARNING, "Spec file {0} does not contain spec {1}", new Object[]{spec_filename, spec_uri});
                throw new RuntimeException("Spec file " + spec_filename + " does not contain spec " + spec_uri);
            }
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Caught IOException, converting to RuntimeException", ex);
            throw new RuntimeException(ex);
        }
    }

    public NodeIterator getResources() {
        NodeIterator listObjectsOfProperty = _model.listObjectsOfProperty(_specResource, Vertere.resource);
        return listObjectsOfProperty;
    }

    public Resource getIdentity(Resource resource) {
        Statement statement = _model.getProperty(resource, Vertere.identity);
        Resource identity = statement.getResource();
        return identity;
    }

    private HashMap<String, String> getPrefixes() {
        HashMap<String, String> prefixes = new HashMap<String, String>();
        prefixes.put("vertere", "http://purl.org/ontology/vertere#");
        prefixes.put("bibo", "http://purl.org/ontology/bibo/");
        prefixes.put("fly", "http://vocab.org/fly/schema/");
        prefixes.put("foaf", "http://xmlns.com/foaf/0.1/");
        prefixes.put("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
        prefixes.put("georss", "http://www.georss.org/georss/");
        prefixes.put("naptan", "http://transport.data.gov.uk/def/naptan/");
        prefixes.put("owl", "http://www.w3.org/2002/07/owl#");
        prefixes.put("places", "http://purl.org/ontology/places#");
        prefixes.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        prefixes.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        prefixes.put("spacerel", "http://data.ordnancesurvey.co.uk/ontology/spatialrelations/");
        prefixes.put("transit", "http://vocab.org/transit/terms/");
        prefixes.put("xsd", "http://www.w3.org/2001/XMLSchema#");

        return prefixes;
    }

    public int[] getSourceColumns(Resource resource) {
        if (_model.contains(resource, Vertere.source_column)) {
            Statement sourceColumn = _model.getProperty(resource, Vertere.source_column);
            return new int[] { sourceColumn.getInt() };
        } else if (_model.contains(resource, Vertere.source_columns)) {
            Statement sourceColumns = _model.getProperty(resource, Vertere.source_columns);
            Resource listResource = sourceColumns.getResource();
            RDFList list = listResource.as(RDFList.class);
            List<RDFNode> javalist = list.asJavaList();
            int[] sourceColumnNumbers = new int[javalist.size()];
            for (int i = 0; i < javalist.size(); i++) {
                RDFNode node = javalist.get(i);
                Literal value = node.asLiteral();
                sourceColumnNumbers[i] = value.getInt();
            }
            return sourceColumnNumbers;
        } else {
            return new int[0];
        }
    }

    public String getGlue(Resource resource) {
        if (_model.contains(resource, Vertere.source_column_glue)) {
            return _model.getProperty(resource, Vertere.source_column_glue).getString();
        } else {
            return "";
        }
    }

    public Resource getSourceResource(Resource identity) {
        if (_model.contains(identity, Vertere.source_resource)) {
            return _model.getProperty(identity, Vertere.source_resource).getResource();
        } else {
            return null;
        }
    }

    public boolean hasLookup(Resource identity) {
        return _model.contains(identity, Vertere.lookup);
    }

    public RDFNode lookup(Resource identity, String sourceValue) {
        if (!_model.contains(identity, Vertere.lookup)) {
            return null;
        }
        Resource lookupResource = _model.getProperty(identity, Vertere.lookup).getResource();
        NodeIterator listObjectsOfProperty = _model.listObjectsOfProperty(lookupResource, Vertere.lookup_entry);
        while (listObjectsOfProperty.hasNext()) {
            RDFNode entry = listObjectsOfProperty.next();
            Resource asResource = entry.asResource();
            String key = _model.getProperty(asResource, Vertere.lookup_key).getString();
            if (sourceValue.equals(key)) {
                return _model.getProperty(asResource, Vertere.lookup_value).getObject();
            }
        }
        return null;
    }

    public String getBaseUri(Resource resource) {
        if (_model.contains(resource, Vertere.base_uri)) {
            return _model.getProperty(resource, Vertere.base_uri).getString();
        } else {
            return null;
        }
    }

    public String getBaseUri() {
        return getBaseUri(_specResource);
    }

    public Resource getNestedUnder(Resource identity) {
        if (_model.contains(identity, Vertere.nest_under)) {
            return _model.getProperty(identity, Vertere.nest_under).getResource();
        } else {
            return null;
        }
    }

    public String getContainer(Resource identity) {
        if (_model.contains(identity, Vertere.container)) {
            return _model.getProperty(identity, Vertere.container).getString();
        } else {
            return "";
        }
    }

    public Resource getAlternativeIdentity(Resource resource) {
        if (_model.contains(resource, Vertere.alternative_identity)) {
            return _model.getProperty(resource, Vertere.identity).getResource();
        } else {
            return null;
        }
    }

    public RDFList getProcessingSteps(Resource identity) {
        if (_model.contains(identity, Vertere.process)) {
            Resource processResource = _model.getProperty(identity, Vertere.process).getResource();
            RDFList processSteps = processResource.as(RDFList.class);
            return processSteps;
        } else {
            return null;
        }
    }

    public String getRegexMatch(Resource resource) {
        if (_model.contains(resource, Vertere.regex_match)) {
            return _model.getProperty(resource, Vertere.regex_match).getString();
        } else {
            return null;
        }
    }

    public String getRegexOutput(Resource resource) {
        if (_model.contains(resource, Vertere.regex_output)) {
            return _model.getProperty(resource, Vertere.regex_output).getString();
        } else {
            return null;
        }
    }

    int getSubstrStart(Resource resource) {
        if (_model.contains(resource, Vertere.substring_start)) {
            return _model.getProperty(resource, Vertere.substring_start).getInt();
        } else {
            return -1;
        }
    }

    int getSubstrLength(Resource resource) {
        if (_model.contains(resource, Vertere.substring_length)) {
            return _model.getProperty(resource, Vertere.substring_length).getInt();
        } else {
            return -1;
        }
    }

    public NodeIterator getSpecifiedTypes(Resource resourceSpec) {
        NodeIterator listObjectsOfProperty = _model.listObjectsOfProperty(resourceSpec, Vertere.type);
        return listObjectsOfProperty;
    }

    public NodeIterator getRelationships(Resource resourceSpec) {
        NodeIterator listObjectsOfProperty = _model.listObjectsOfProperty(resourceSpec, Vertere.relationship);
        return listObjectsOfProperty;
    }

    public Property getRelationshipProperty(Resource relationship) {
        if (_model.contains(relationship, Vertere.property)) {
            Resource resource = _model.getProperty(relationship, Vertere.property).getResource();
            return ResourceFactory.createProperty(resource.getURI());
        } else {
            return null;
        }
    }

    public Resource getRelationshipObjectFrom(Resource relationship) {
        if (_model.contains(relationship, Vertere.object_from)) {
            return _model.getProperty(relationship, Vertere.object_from).getResource();
        } else {
            return null;
        }
    }

    public NodeIterator getAttributes(Resource resourceSpec) {
        NodeIterator listObjectsOfProperty = _model.listObjectsOfProperty(resourceSpec, Vertere.attribute);
        return listObjectsOfProperty;
    }

    public RDFDatatype getDatatype(Resource attribute) {
        if (_model.contains(attribute, Vertere.datatype)) {
            Resource resource = _model.getProperty(attribute, Vertere.datatype).getResource();
            RDFDatatype datatype = new BaseDatatype(resource.getURI());
            return datatype;
        } else {
            return null;
        }
    }

    public String getLanguage(Resource attribute) {
        if (_model.contains(attribute, Vertere.language)) {
            return _model.getProperty(attribute, Vertere.language).getString();
        } else {
            return null;
        }
    }

    public String getExpectedHeader() {
        if (_model.contains(_specResource, Vertere.expected_header)) {
            return _model.getProperty(_specResource, Vertere.expected_header).getString();
        } else {
            return null;
        }
    }
}
