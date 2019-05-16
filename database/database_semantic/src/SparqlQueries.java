import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;

public class SparqlQueries {
    private Dataset dataset;
    private Model model;


    public SparqlQueries(Model model) {
        this.dataset = TDBFactory.createDataset("movie_tdb");
        this.model = dataset.getDefaultModel();
    }


    public void sparqlEndpoint(String title) {
        String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX dbp: <http://dbpedia.org/ontology/> " +
                "SELECT DISTINCT ?title " +
                "WHERE { ?movie a dbp:Film ." +
                         "?movie rdfs:label ?title." +
                "FILTER regex(str(?title), \""+title+"\", \"i\")." +
                "}";
        System.out.println("QUERY: " + query);

        ResultSet resultSet = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql",query).execSelect();
        resultSet.forEachRemaining(qsol -> System.out.println("?title"));
    }

    public void sparqlEndpointGetSubjects(String title) {
        title += "_(film)";
        String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX dbo: <http://dbpedia.org/ontology/> PREFIX dbp: <http://dbpedia.org/page/> PREFIX dc: <http://purl.org/dc/terms/>" +
                "SELECT DISTINCT ?subject " +
                "WHERE {" +
//                "<http://dbpedia.org/resource/"+title+"> rdfs:label ?title." +
                "<http://dbpedia.org/resource/"+title+"> dc:subject ?subject." +
//                "FILTER (lang(?subject) = 'en')." +
                "}";
        System.out.println("QUERY: " + query);
        ResultSet resultSet = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql",query).execSelect();
        resultSet.forEachRemaining(qsol -> System.out.println(qsol.get("?subject")));
    }


    public void sparqlEndpointGetComment(String title) {
        title += "_(film)";
        String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX dbo: <http://dbpedia.org/ontology/> PREFIX dbp: <http://dbpedia.org/page/>" +
                "SELECT DISTINCT ?comment " +
                "WHERE {" +
//                "<http://dbpedia.org/resource/"+title+"> rdfs:label ?title." +
                "<http://dbpedia.org/resource/"+title+"> rdfs:comment ?comment." +
                "FILTER (lang(?comment) = 'en')." +
                "}";
        System.out.println("QUERY: " + query);

        ResultSet resultSet = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql",query).execSelect();
        resultSet.forEachRemaining(qsol -> System.out.println(qsol.get("?comment")));
    }




    public Boolean searchForAMovie(String title, Model model) {
        boolean match = false;
        String query = "PREFIX m: <http://info216.no/v2019/vocabulary/> SELECT DISTINCT ?movie ?property ?value WHERE { ?movie m:title ?title .?movie ?property ?value .FILTER regex(str(?title), \""+title+"\") .}";
        System.out.println("QUERY: " + query);

        ResultSet resultSet = QueryExecutionFactory
                .create(query, model)
                .execSelect();
        if(resultSet.hasNext()) {
            match = true;
        }
        resultSet.forEachRemaining(qsol -> System.out.println(qsol.get("?movie").toString()+ qsol.get("?property").toString() + qsol.get("?value").toString()));
        return match;
    }

    public Boolean searchForATitle(String title, Model model) {
        boolean match = false;
        String query = "PREFIX m: <http://info216.no/v2019/vocabulary/> SELECT DISTINCT ?title WHERE { ?movie m:title ?title.FILTER regex(str(?title), \""+title+"\", \"i\") .}";
        System.out.println("QUERY: " + query);

        ResultSet resultSet = QueryExecutionFactory
                .create(query, model)
                .execSelect();
        if(resultSet.hasNext()) {
            match = true;
        }
        resultSet.forEachRemaining(qsol -> System.out.println(qsol.get("?title")));
        return match;
    }


    public Boolean actorsOfTitle(String title, Model model) {
        boolean match = false;
        String query = "PREFIX m: <http://info216.no/v2019/vocabulary/> PREFIX dbp: <http://dbpedia.org/ontology/> PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#> "+
                "SELECT DISTINCT ?name WHERE { ?movie m:title ?value .?movie m:actors ?actors. ?actors dbp:starring ?actor. ?actor vcard:FN ?name." +
                "FILTER regex(str(?value), \""+title+"\", \"i\") .}";
        System.out.println(query);

        ResultSet resultSet = QueryExecutionFactory
                .create(query, model)
                .execSelect();
        if(resultSet.hasNext()) {
            match = true;
        }
        resultSet.forEachRemaining(qsol -> System.out.println(qsol.get("?name")));

        return match;
    }

    public Boolean AllMoviesOfDirector(String director) {
        boolean match = false;
        String query = "PREFIX m: <http://info216.no/v2019/vocabulary/> PREFIX dbp: <http://dbpedia.org/ontology/> PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>" +
                "SELECT DISTINCT ?title WHERE { ?movie m:title ?title .?movie dbp:director ?director. ?director vcard:FN ?name."+
                "FILTER regex(str(?name), \""+director+"\", \"i\") .}";
        System.out.println(query);

        ResultSet resultSet = QueryExecutionFactory
                .create(query, model)
                .execSelect();
        if(resultSet.hasNext()) {
            match = true;
        }
        resultSet.forEachRemaining(qsol -> System.out.println(qsol.get("?title")));

        return match;
    }

    public Boolean AllMoviesOfActor(String actor) {
        boolean match = false;
        String query = "PREFIX m: <http://info216.no/v2019/vocabulary/> PREFIX dbp: <http://dbpedia.org/ontology/> PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>" +
                "SELECT DISTINCT ?title WHERE { ?movie m:title ?title .?movie m:actors ?actors. ?actors dbp:starring ?actor. ?actor vcard:FN ?name." +
                "FILTER regex(str(?name), \""+actor+"\",\"i\").}";
        System.out.println(query);

        ResultSet resultSet = QueryExecutionFactory
                .create(query, model)
                .execSelect();
        if(resultSet.hasNext()) {
            match = true;
        }
        resultSet.forEachRemaining(qsol -> System.out.println(qsol.get("?title")));

        return match;
    }

    public Boolean personDirectorAndActor(Model model) {
        boolean match = false;
        String query = "PREFIX m: <http://info216.no/v2019/vocabulary/> PREFIX dbp: <http://dbpedia.org/ontology/>" +
                "SELECT DISTINCT ?title WHERE { ?movie m:title ?title.?movie dbp:director ?director.?movie m:actors ?actors. ?actors dbp:starring ?actor." +
                "FILTER (?director = ?actor).}";
        System.out.println(query);

        ResultSet resultSet = QueryExecutionFactory
                .create(query, model)
                .execSelect();
        if(resultSet.hasNext()) {
            match = true;
        }
        resultSet.forEachRemaining(qsol -> System.out.println(qsol.get("title")));
        return match;
    }

    public void printAllTriples(Model model) {
        ResultSet resultSet = QueryExecutionFactory
                .create(""
                        + "SELECT ?s ?p ?o WHERE {"
                        + "    ?s ?p ?o ."
                        + "}", model)
                .execSelect();
        resultSet.forEachRemaining(qsol -> System.out.println(qsol.toString()));
    }


    public Model getModel() {
        return this.model;
    }

    public void close() {
        this.dataset.close();
    }
}
