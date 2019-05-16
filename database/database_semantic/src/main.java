import org.apache.jena.rdf.model.Model;

public class main {

    private static Model model;

    public static void main(String[] args) {
        firstTimeSetup();
        SparqlQueries sparql = new SparqlQueries(model);
//        sparql.sparqlEndpoint("Interstellar");
//        sparql.sparqlEndpointGetComment("Interstellar");
//        sparql.sparqlEndpointGetSubjects("Interstellar");
//        sparql.AllMoviesOfDirector("quentin");
//        sparql.AllMoviesOfActor("leonardo");
        sparql.close();
    }


    public static Model firstTimeSetup() {
        JSONToTDB jsontoTDB = new JSONToTDB("movies.json");
        Model model = jsontoTDB.getModel();
        return model;
    }
}
