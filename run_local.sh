ant clean
ant
rm -rf local_out
java -cp dist/lib/vertere.jar:lib/* vertere.Vertere examples/ourairports.com/2011-11-09/airports.csv local_out/ examples/ourairports.com/airports.csv.spec.ttl "http://example.com/specs/airports.csv.spec.ttl#"
