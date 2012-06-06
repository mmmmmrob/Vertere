/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertere;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.graph.query.QueryHandler;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.shared.Command;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.shared.ReificationStyle;
import java.io.*;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author RobSt
 */
public class SerializableModel implements Model, Serializable {

    private Model _model;

    public SerializableModel() {
        this._model = ModelFactory.createDefaultModel();
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        this._model.write(out, "RDF/XML");
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        this._model.read(in, "RDF/XML", "http://flawed/");
    }

    private void readObjectNoData() throws ObjectStreamException {
        throw new UnsupportedOperationException("Not prepared to deserialise without data.");
    }

    @Override
    public long size() {
        return this._model.size();
    }

    @Override
    public boolean isEmpty() {
        return this._model.isEmpty();
    }

    @Override
    public ResIterator listSubjects() {
        return this._model.listSubjects();
    }

    @Override
    public NsIterator listNameSpaces() {
        return this._model.listNameSpaces();
    }

    @Override
    public Resource getResource(String uri) {
        return this._model.getResource(uri);
    }

    @Override
    public Property getProperty(String nameSpace, String localName) {
        return this._model.getProperty(nameSpace, localName);
    }

    @Override
    public Resource createResource() {
        return this._model.createResource();
    }

    @Override
    public Resource createResource(AnonId id) {
        return this._model.createResource(id);
    }

    @Override
    public Resource createResource(String uri) {
        return this._model.createResource(uri);
    }

    @Override
    public Property createProperty(String nameSpace, String localName) {
        return this._model.createProperty(nameSpace, localName);
    }

    @Override
    public Literal createLiteral(String v, String language) {
        return this._model.createLiteral(v, language);
    }

    @Override
    public Literal createLiteral(String v, boolean wellFormed) {
        return this._model.createLiteral(v, wellFormed);
    }

    @Override
    public Literal createTypedLiteral(String lex, RDFDatatype dtype) {
        return this._model.createTypedLiteral(lex, dtype);
    }

    @Override
    public Literal createTypedLiteral(Object value, RDFDatatype dtype) {
        return this._model.createTypedLiteral(value, dtype);
    }

    @Override
    public Literal createTypedLiteral(Object value) {
        return this._model.createTypedLiteral(value);
    }

    @Override
    public Statement createStatement(Resource s, Property p, RDFNode o) {
        return this._model.createStatement(s, p, o);
    }

    @Override
    public RDFList createList() {
        return this._model.createList();
    }

    @Override
    public RDFList createList(Iterator<? extends RDFNode> members) {
        return this._model.createList(members);
    }

    @Override
    public RDFList createList(RDFNode[] members) {
        return this._model.createList(members);
    }

    @Override
    public Model add(Statement s) {
        return this._model.add(s);
    }

    @Override
    public Model add(Statement[] statements) {
        return this._model.add(statements);
    }

    @Override
    public Model remove(Statement[] statements) {
        return this._model.remove(statements);
    }

    @Override
    public Model add(List<Statement> statements) {
        return this._model.add(statements);
    }

    @Override
    public Model remove(List<Statement> statements) {
        return this._model.remove(statements);
    }

    @Override
    public Model add(StmtIterator iter) {
        return this._model.add(iter);
    }

    @Override
    public Model add(Model m) {
        return this._model.add(m);
    }

    @Override
    public Model add(Model m, boolean suppressReifications) {
        return this._model.add(m, suppressReifications);
    }

    @Override
    public Model read(String url) {
        return this._model.read(url);
    }

    @Override
    public Model read(InputStream in, String base) {
        return this._model.read(in, base);
    }

    @Override
    public Model read(InputStream in, String base, String lang) {
        return this._model.read(in, base, lang);
    }

    @Override
    public Model read(Reader reader, String base) {
        return this._model.read(reader, base);
    }

    @Override
    public Model read(String url, String lang) {
        return this._model.read(url, lang);
    }

    @Override
    public Model read(Reader reader, String base, String lang) {
        return this._model.read(reader, base, lang);
    }

    @Override
    public Model read(String url, String base, String lang) {
        return this._model.read(url, base, lang);
    }

    @Override
    public Model write(Writer writer) {
        return this._model.write(writer);
    }

    @Override
    public Model write(Writer writer, String lang) {
        return this._model.write(writer, lang);
    }

    @Override
    public Model write(Writer writer, String lang, String base) {
        return this._model.write(writer, lang, base);
    }

    @Override
    public Model write(OutputStream out) {
        return this._model.write(out);
    }

    @Override
    public Model write(OutputStream out, String lang) {
        return this._model.write(out, lang);
    }

    @Override
    public Model write(OutputStream out, String lang, String base) {
        return this._model.write(out, lang, base);
    }

    @Override
    public Model remove(Statement s) {
        return this._model.remove(s);
    }

    @Override
    public Statement getRequiredProperty(Resource s, Property p) {
        return this._model.getRequiredProperty(s, p);
    }

    @Override
    public Statement getProperty(Resource s, Property p) {
        return this._model.getProperty(s, p);
    }

    @Override
    public ResIterator listSubjectsWithProperty(Property p) {
        return this._model.listSubjectsWithProperty(p);
    }

    @Override
    public ResIterator listResourcesWithProperty(Property p) {
        return this._model.listResourcesWithProperty(p);
    }

    @Override
    public ResIterator listSubjectsWithProperty(Property p, RDFNode o) {
        return this._model.listSubjectsWithProperty(p, o);
    }

    @Override
    public ResIterator listResourcesWithProperty(Property p, RDFNode o) {
        return this._model.listResourcesWithProperty(p, o);
    }

    @Override
    public NodeIterator listObjects() {
        return this._model.listObjects();
    }

    @Override
    public NodeIterator listObjectsOfProperty(Property p) {
        return this._model.listObjectsOfProperty(p);
    }

    @Override
    public NodeIterator listObjectsOfProperty(Resource s, Property p) {
        return this._model.listObjectsOfProperty(s, p);
    }

    @Override
    public boolean contains(Resource s, Property p) {
        return this._model.contains(s, p);
    }

    @Override
    public boolean containsResource(RDFNode r) {
        return this._model.containsResource(r);
    }

    @Override
    public boolean contains(Resource s, Property p, RDFNode o) {
        return this._model.contains(s, p, o);
    }

    @Override
    public boolean contains(Statement s) {
        return this._model.contains(s);
    }

    @Override
    public boolean containsAny(StmtIterator iter) {
        return this._model.containsAny(iter);
    }

    @Override
    public boolean containsAll(StmtIterator iter) {
        return this._model.containsAll(iter);
    }

    @Override
    public boolean containsAny(Model model) {
        return this._model.containsAny(model);
    }

    @Override
    public boolean containsAll(Model model) {
        return this._model.containsAll(model);
    }

    @Override
    public boolean isReified(Statement s) {
        return this._model.isReified(s);
    }

    @Override
    public Resource getAnyReifiedStatement(Statement s) {
        return this._model.getAnyReifiedStatement(s);
    }

    @Override
    public void removeAllReifications(Statement s) {
        this._model.removeAllReifications(s);
    }

    @Override
    public void removeReification(ReifiedStatement rs) {
        this._model.removeReification(rs);
    }

    @Override
    public StmtIterator listStatements() {
        return this._model.listStatements();
    }

    @Override
    public StmtIterator listStatements(Selector s) {
        return this._model.listStatements(s);
    }

    @Override
    public StmtIterator listStatements(Resource s, Property p, RDFNode o) {
        return this._model.listStatements(s, p, o);
    }

    @Override
    public ReifiedStatement createReifiedStatement(Statement s) {
        return this._model.createReifiedStatement(s);
    }

    @Override
    public ReifiedStatement createReifiedStatement(String uri, Statement s) {
        return this._model.createReifiedStatement(uri, s);
    }

    @Override
    public RSIterator listReifiedStatements() {
        return this._model.listReifiedStatements();
    }

    @Override
    public RSIterator listReifiedStatements(Statement st) {
        return this._model.listReifiedStatements(st);
    }

    @Override
    public ReificationStyle getReificationStyle() {
        return this._model.getReificationStyle();
    }

    @Override
    public Model query(Selector s) {
        return this._model.query(s);
    }

    @Override
    public Model union(Model model) {
        return this._model.union(model);
    }

    @Override
    public Model intersection(Model model) {
        return this._model.intersection(model);
    }

    @Override
    public Model difference(Model model) {
        return this._model.difference(model);
    }

    @Override
    public Model begin() {
        return this._model.begin();
    }

    @Override
    public Model abort() {
        return this._model.abort();
    }

    @Override
    public Model commit() {
        return this._model.commit();
    }

    @Override
    public Object executeInTransaction(Command cmd) {
        return this._model.executeInTransaction(cmd);
    }

    @Override
    public boolean independent() {
        return this._model.independent();
    }

    @Override
    public boolean supportsTransactions() {
        return this._model.supportsTransactions();
    }

    @Override
    public boolean supportsSetOperations() {
        return this._model.supportsSetOperations();
    }

    @Override
    public boolean isIsomorphicWith(Model g) {
        return this._model.isIsomorphicWith(g);
    }

    @Override
    public void close() {
        this._model.close();
    }

    @Override
    public Lock getLock() {
        return this._model.getLock();
    }

    @Override
    public Model register(ModelChangedListener listener) {
        return this._model.register(listener);
    }

    @Override
    public Model unregister(ModelChangedListener listener) {
        return this._model.unregister(listener);
    }

    @Override
    public Model notifyEvent(Object e) {
        return this._model.notifyEvent(e);
    }

    @Override
    public Model removeAll() {
        return this._model.removeAll();
    }

    @Override
    public Model removeAll(Resource s, Property p, RDFNode r) {
        return this._model.removeAll(s, p, r);
    }

    @Override
    public boolean isClosed() {
        return this._model.isClosed();
    }

    @Override
    public Resource getResource(String uri, ResourceF f) {
        return this._model.getResource(uri, f);
    }

    @Override
    public Property getProperty(String uri) {
        return this._model.getProperty(uri);
    }

    @Override
    public Bag getBag(String uri) {
        return this._model.getBag(uri);
    }

    @Override
    public Bag getBag(Resource r) {
        return this._model.getBag(r);
    }

    @Override
    public Alt getAlt(String uri) {
        return this._model.getAlt(uri);
    }

    @Override
    public Alt getAlt(Resource r) {
        return this._model.getAlt(r);
    }

    @Override
    public Seq getSeq(String uri) {
        return this._model.getSeq(uri);
    }

    @Override
    public Seq getSeq(Resource r) {
        return this._model.getSeq(r);
    }

    @Override
    public Resource createResource(Resource type) {
        return this._model.createResource(type);
    }

    @Override
    public RDFNode getRDFNode(Node n) {
        return this._model.getRDFNode(n);
    }

    @Override
    public Resource createResource(String uri, Resource type) {
        return this._model.createResource(uri, type);
    }

    @Override
    public Resource createResource(ResourceF f) {
        return this._model.createResource(f);
    }

    @Override
    public Resource createResource(String uri, ResourceF f) {
        return this._model.createResource(uri, f);
    }

    @Override
    public Property createProperty(String uri) {
        return this._model.createProperty(uri);
    }

    @Override
    public Literal createLiteral(String v) {
        return this._model.createLiteral(v);
    }

    @Override
    public Literal createTypedLiteral(boolean v) {
        return this._model.createTypedLiteral(v);
    }

    @Override
    public Literal createTypedLiteral(int v) {
        return this._model.createTypedLiteral(v);
    }

    @Override
    public Literal createTypedLiteral(long v) {
        return this._model.createTypedLiteral(v);
    }

    @Override
    public Literal createTypedLiteral(Calendar d) {
        return this._model.createTypedLiteral(d);
    }

    @Override
    public Literal createTypedLiteral(char v) {
        return this._model.createTypedLiteral(v);
    }

    @Override
    public Literal createTypedLiteral(float v) {
        return this._model.createTypedLiteral(v);
    }

    @Override
    public Literal createTypedLiteral(double v) {
        return this._model.createTypedLiteral(v);
    }

    @Override
    public Literal createTypedLiteral(String v) {
        return this._model.createTypedLiteral(v);
    }

    @Override
    public Literal createTypedLiteral(String lex, String typeURI) {
        return this._model.createTypedLiteral(lex, typeURI);
    }

    @Override
    public Literal createTypedLiteral(Object value, String typeURI) {
        return this._model.createTypedLiteral(value, typeURI);
    }

    @Override
    public Statement createLiteralStatement(Resource s, Property p, boolean o) {
        return this._model.createLiteralStatement(s, p, o);
    }

    @Override
    public Statement createLiteralStatement(Resource s, Property p, float o) {
        return this._model.createLiteralStatement(s, p, o);
    }

    @Override
    public Statement createLiteralStatement(Resource s, Property p, double o) {
        return this._model.createLiteralStatement(s, p, o);
    }

    @Override
    public Statement createLiteralStatement(Resource s, Property p, long o) {
        return this._model.createLiteralStatement(s, p, o);
    }

    @Override
    public Statement createLiteralStatement(Resource s, Property p, int o) {
        return this._model.createLiteralStatement(s, p, o);
    }

    @Override
    public Statement createLiteralStatement(Resource s, Property p, char o) {
        return this._model.createLiteralStatement(s, p, o);
    }

    @Override
    public Statement createLiteralStatement(Resource s, Property p, Object o) {
        return this._model.createLiteralStatement(s, p, o);
    }

    @Override
    public Statement createStatement(Resource s, Property p, String o) {
        return this._model.createStatement(s, p, o);
    }

    @Override
    public Statement createStatement(Resource s, Property p, String o, String l) {
        return this._model.createStatement(s, p, o, l);
    }

    @Override
    public Statement createStatement(Resource s, Property p, String o, boolean wellFormed) {
        return this._model.createStatement(s, p, o, wellFormed);
    }

    @Override
    public Statement createStatement(Resource s, Property p, String o, String l, boolean wellFormed) {
        return this._model.createStatement(s, p, o, l, wellFormed);
    }

    @Override
    public Bag createBag() {
        return this._model.createBag();
    }

    @Override
    public Bag createBag(String uri) {
        return this._model.createBag(uri);
    }

    @Override
    public Alt createAlt() {
        return this._model.createAlt();
    }

    @Override
    public Alt createAlt(String uri) {
        return this._model.createAlt(uri);
    }

    @Override
    public Seq createSeq() {
        return this._model.createSeq();
    }

    @Override
    public Seq createSeq(String uri) {
        return this._model.createSeq(uri);
    }

    @Override
    public Model add(Resource s, Property p, RDFNode o) {
        return this._model.add(s, p, o);
    }

    @Override
    public Model addLiteral(Resource s, Property p, boolean o) {
        return this._model.addLiteral(s, p, o);
    }

    @Override
    public Model addLiteral(Resource s, Property p, long o) {
        return this._model.addLiteral(s, p, o);
    }

    @Override
    public Model addLiteral(Resource s, Property p, int o) {
        return this._model.addLiteral(s, p, o);
    }

    @Override
    public Model addLiteral(Resource s, Property p, char o) {
        return this._model.addLiteral(s, p, o);
    }

    @Override
    public Model addLiteral(Resource s, Property p, float o) {
        return this._model.addLiteral(s, p, o);
    }

    @Override
    public Model addLiteral(Resource s, Property p, double o) {
        return this._model.addLiteral(s, p, o);
    }

    @Override
    public Model addLiteral(Resource s, Property p, Object o) {
        return this._model.addLiteral(s, p, o);
    }

    @Override
    public Model addLiteral(Resource s, Property p, Literal o) {
        return this._model.addLiteral(s, p, o);
    }

    @Override
    public Model add(Resource s, Property p, String o) {
        return this._model.add(s, p, o);
    }

    @Override
    public Model add(Resource s, Property p, String lex, RDFDatatype datatype) {
        return this._model.add(s, p, lex, datatype);
    }

    @Override
    public Model add(Resource s, Property p, String o, boolean wellFormed) {
        return this._model.add(s, p, o, wellFormed);
    }

    @Override
    public Model add(Resource s, Property p, String o, String l) {
        return this._model.add(s, p, o, l);
    }

    @Override
    public Model remove(Resource s, Property p, RDFNode o) {
        return this._model.remove(s, p, o);
    }

    @Override
    public Model remove(StmtIterator iter) {
        return this._model.remove(iter);
    }

    @Override
    public Model remove(Model m) {
        return this._model.remove(m);
    }

    @Override
    public Model remove(Model m, boolean suppressReifications) {
        return this._model.remove(m, suppressReifications);
    }

    @Override
    public StmtIterator listLiteralStatements(Resource subject, Property predicate, boolean object) {
        return this._model.listLiteralStatements(subject, predicate, object);
    }

    @Override
    public StmtIterator listLiteralStatements(Resource subject, Property predicate, char object) {
        return this._model.listLiteralStatements(subject, predicate, object);
    }

    @Override
    public StmtIterator listLiteralStatements(Resource subject, Property predicate, long object) {
        return this._model.listLiteralStatements(subject, predicate, object);
    }

    @Override
    public StmtIterator listLiteralStatements(Resource subject, Property predicate, float object) {
        return this._model.listLiteralStatements(subject, predicate, object);
    }

    @Override
    public StmtIterator listLiteralStatements(Resource subject, Property predicate, double object) {
        return this._model.listLiteralStatements(subject, predicate, object);
    }

    @Override
    public StmtIterator listStatements(Resource subject, Property predicate, String object) {
        return this._model.listStatements(subject, predicate, object);
    }

    @Override
    public StmtIterator listStatements(Resource subject, Property predicate, String object, String lang) {
        return this._model.listStatements(subject, predicate, object, lang);
    }

    @Override
    public ResIterator listResourcesWithProperty(Property p, boolean o) {
        return this._model.listResourcesWithProperty(p, o);
    }

    @Override
    public ResIterator listResourcesWithProperty(Property p, long o) {
        return this._model.listResourcesWithProperty(p, o);
    }

    @Override
    public ResIterator listResourcesWithProperty(Property p, char o) {
        return this._model.listResourcesWithProperty(p, o);
    }

    @Override
    public ResIterator listResourcesWithProperty(Property p, float o) {
        return this._model.listResourcesWithProperty(p, o);
    }

    @Override
    public ResIterator listResourcesWithProperty(Property p, double o) {
        return this._model.listResourcesWithProperty(p, o);
    }

    @Override
    public ResIterator listResourcesWithProperty(Property p, Object o) {
        return this._model.listResourcesWithProperty(p, o);
    }

    @Override
    public ResIterator listSubjectsWithProperty(Property p, String o) {
        return this._model.listSubjectsWithProperty(p, o);
    }

    @Override
    public ResIterator listSubjectsWithProperty(Property p, String o, String l) {
        return this._model.listSubjectsWithProperty(p, o, l);
    }

    @Override
    public boolean containsLiteral(Resource s, Property p, boolean o) {
        return this._model.containsLiteral(s, p, o);
    }

    @Override
    public boolean containsLiteral(Resource s, Property p, long o) {
        return this._model.containsLiteral(s, p, o);
    }

    @Override
    public boolean containsLiteral(Resource s, Property p, int o) {
        return this._model.containsLiteral(s, p, o);
    }

    @Override
    public boolean containsLiteral(Resource s, Property p, char o) {
        return this._model.containsLiteral(s, p, o);
    }

    @Override
    public boolean containsLiteral(Resource s, Property p, float o) {
        return this._model.containsLiteral(s, p, o);
    }

    @Override
    public boolean containsLiteral(Resource s, Property p, double o) {
        return this._model.containsLiteral(s, p, o);
    }

    @Override
    public boolean containsLiteral(Resource s, Property p, Object o) {
        return this._model.containsLiteral(s, p, o);
    }

    @Override
    public boolean contains(Resource s, Property p, String o) {
        return this._model.contains(s, p, o);
    }

    @Override
    public boolean contains(Resource s, Property p, String o, String l) {
        return this._model.contains(s, p, o, l);
    }

    @Override
    public Statement asStatement(Triple t) {
        return this._model.asStatement(t);
    }

    @Override
    public Graph getGraph() {
        return this._model.getGraph();
    }

    @Override
    public QueryHandler queryHandler() {
        return this._model.queryHandler();
    }

    @Override
    public RDFNode asRDFNode(Node n) {
        return this._model.asRDFNode(n);
    }

    @Override
    public Resource wrapAsResource(Node n) {
        return this._model.wrapAsResource(n);
    }

    @Override
    public RDFReader getReader() {
        return this._model.getReader();
    }

    @Override
    public RDFReader getReader(String lang) {
        return this._model.getReader(lang);
    }

    @Override
    public String setReaderClassName(String lang, String className) {
        return this._model.setReaderClassName(lang, className);
    }

    @Override
    public RDFWriter getWriter() {
        return this._model.getWriter();
    }

    @Override
    public RDFWriter getWriter(String lang) {
        return this._model.getWriter(lang);
    }

    @Override
    public String setWriterClassName(String lang, String className) {
        return this._model.setWriterClassName(lang, className);
    }

    @Override
    public PrefixMapping setNsPrefix(String prefix, String uri) {
        return this._model.setNsPrefix(prefix, uri);
    }

    @Override
    public PrefixMapping removeNsPrefix(String prefix) {
        return this._model.removeNsPrefix(prefix);
    }

    @Override
    public PrefixMapping setNsPrefixes(PrefixMapping other) {
        return this._model.setNsPrefixes(other);
    }

    @Override
    public PrefixMapping setNsPrefixes(Map<String, String> map) {
        return this._model.setNsPrefixes(map);
    }

    @Override
    public PrefixMapping withDefaultMappings(PrefixMapping map) {
        return this._model.withDefaultMappings(map);
    }

    @Override
    public String getNsPrefixURI(String prefix) {
        return this._model.getNsPrefixURI(prefix);
    }

    @Override
    public String getNsURIPrefix(String uri) {
        return this._model.getNsURIPrefix(uri);
    }

    @Override
    public Map<String, String> getNsPrefixMap() {
        return this._model.getNsPrefixMap();
    }

    @Override
    public String expandPrefix(String prefixed) {
        return this._model.expandPrefix(prefixed);
    }

    @Override
    public String shortForm(String uri) {
        return this._model.shortForm(uri);
    }

    @Override
    public String qnameFor(String uri) {
        return this._model.qnameFor(uri);
    }

    @Override
    public PrefixMapping lock() {
        return this._model.lock();
    }

    @Override
    public boolean samePrefixMappingAs(PrefixMapping other) {
        return this._model.samePrefixMappingAs(other);
    }

    @Override
    public void enterCriticalSection(boolean readLockRequested) {
        this._model.enterCriticalSection(readLockRequested);
    }

    @Override
    public void leaveCriticalSection() {
        this._model.leaveCriticalSection();
    }
}
