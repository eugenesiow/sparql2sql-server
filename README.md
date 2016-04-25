# sparql2sql-server

A Jetty-based server to provide a SPARQL endpoint with an RDBMS backend and using the sparql2sql translation engine. sparql2sql is a modern SPARQL-to-SQL engine using S2SML. Most information about sparql2sql can be found on the [wiki](https://github.com/eugenesiow/sparql2sql/wiki). This includes a reference for the [S2SML](https://github.com/eugenesiow/sparql2sql/wiki/S2SML) language for sparql2sql mappings, [benchmarks](https://github.com/eugenesiow/sparql2sql/wiki/Benchmarks), implementation of the [swappable BGP resolution interface](https://github.com/eugenesiow/sparql2sql/wiki/SWIBRE), etc.

### Running the server
1. `git clone https://github.com/eugenesiow/sparql2sql-server.git`
2. You need to have [maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) installed.
3. `cd sparql2sql-serverL`
4. `mvn dependency:copy-dependencies package`
5. `cd target`
6. `java -jar sparql2sql-server-0.0.1-SNAPSHOT.jar`

### Other Projects
* [LSD-ETL](https://github.com/eugenesiow/lsd-ETL)
* [sparql2stream](https://github.com/eugenesiow/sparql2stream)
* [sparql2sql](https://github.com/eugenesiow/sparql2sql)
* [Linked Data Analytics](http://eugenesiow.github.io/iot/)
