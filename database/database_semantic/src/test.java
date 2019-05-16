import com.google.gson.*;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.VCARD;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.apache.jena.sparql.engine.http.Service.base;

public class test {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Dataset dataset = TDBFactory.createDataset("movie_tdb");

        Model model = dataset.getDefaultModel();

        test j = new test(model);

//        printAllTriples(model);
//        searchForATitle("star wars",model);
//        actorsOfTitle("The Lion King", model);
//        keywordsOfTitle("inception", model);
//        AllMoviesOfActor("Christian Bale", model);
        AllMoviesOfDirector("quentin", model);
//        personDirectorAndActor(model);
        dataset.close();
    }


    public test(Model model) {
        if (model.isEmpty()) {
            String jsonString = "";
            try {
                jsonString = jsonFileToString("movies.json");
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            model = parseJSONString(jsonString, model);
            //        sparqlTest(model);
        }
    }

    public static Boolean searchForAMovie(String title, Model model) {
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

    public static Boolean searchForATitle(String title, Model model) {
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

    public static Boolean actorsOfTitle(String title, Model model) {
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


    public static Boolean AllMoviesOfDirector(String director, Model model) {
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

    public static Boolean AllMoviesOfActor(String actor, Model model) {
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


    public static Boolean personDirectorAndActor(Model model) {
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



    public Model parseJSONString(String json, Model model) {
        JsonElement jsonElement = new JsonParser().parse(json);
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        String dbpedia = "http://dbpedia.org/ontology/";
        String dbpedia_page = "http://dbpedia.org/page/";
        String dbpedia_extension = "http://dbpedia.org/ontology/extension/";

        String movieURI = "http://info216.no/v2019/vocabulary/";
        String keywordURI = "http://info216.no/v2019/vocabulary/keyword#";
        String genreURI = "http://info216.no/v2019/vocabulary/genre#";

//
//        String owl = "http://www.w3.org/2002/07/owl#";
//        String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
//
//        Resource movieClass = model.createResource(OWL.Class);
//        Resource personClass = model.createResource(OWL.Class);
//
//        Resource actorClass = model.createResource(OWL.Class);
//        actorClass.addProperty(RDFS.subClassOf, personClass);


        for(JsonElement movie : jsonArray) {
            JsonObject movieObject = movie.getAsJsonObject();

            Property title = model.createProperty(movieURI + "title");
            Property year = model.createProperty(movieURI + "year");
            Property runtime = model.createProperty(dbpedia + "filmRuntime");
            Property director = model.createProperty(dbpedia + "director");
            Property actors = model.createProperty(movieURI + "actors");
            Property actor = model.createProperty(dbpedia + "starring");
            Property genres = model.createProperty(movieURI + "genres");
            Property keywords = model.createProperty(movieURI + "keywords");
            Property keyword = model.createProperty(movieURI + "keyword");
            Property gross = model.createProperty(dbpedia + "gross");
            Property content_rating = model.createProperty(dbpedia_page + "Content_rating");
            Property country = model.createProperty(dbpedia + "country");
            Property language = model.createProperty(dbpedia + "language");
            Property num_votes = model.createProperty(movieURI + "num_votes");
            Property imdb_rating = model.createProperty(dbpedia + "rating");
            Property imdb_link = model.createProperty(dbpedia_page + "Hyperlink");
            Property imdb_id = model.createProperty(movieURI + "imdb_id");

            Resource directorPerson = model.createResource(dbpedia_page + movieObject.get("director").getAsString())
                    .addProperty(RDF.type, dbpedia + "MovieDirector")
                    .addProperty(RDFS.subClassOf, dbpedia + "Person")
                    .addProperty(VCARD.FN, movieObject.get("director").getAsString())
                    .addProperty(VCARD.TITLE, "Director");

//            System.out.println("HELLLO" + movieObject.get("title").getAsString());

            Resource movieRDF = model.createResource(dbpedia_page + movieObject.get("title").getAsString())
                    .addProperty(RDF.type, dbpedia + "director")
                    .addProperty(RDFS.label, movieObject.get("title").getAsString())
                    .addProperty(title, movieObject.get("title").getAsString())
                    .addProperty(director, directorPerson)
                    .addProperty(year, movieObject.get("year").getAsString(), XSDDatatype.XSDinteger)
                    .addProperty(genres, movieObject.get("genres").getAsString())
                    .addProperty(runtime, movieObject.get("runtime").getAsString(), XSDDatatype.XSDdouble)
                    .addProperty(imdb_rating, movieObject.get("imdb_rating").getAsString(), XSDDatatype.XSDdouble)
                    .addProperty(num_votes, movieObject.get("num_votes").getAsString(), XSDDatatype.XSDinteger)
                    .addProperty(gross, movieObject.get("gross").getAsString(), XSDDatatype.XSDdouble)
                    .addProperty(content_rating, movieObject.get("content_rating").getAsString())
                    .addProperty(country, movieObject.get("country").getAsString())
                    .addProperty(language, movieObject.get("language").getAsString())
                    .addProperty(imdb_id, movieObject.get("imdb_id").getAsString())
                    .addProperty(imdb_link, movieObject.get("imdb_link").getAsString());

//                    model.write(System.out, "TURTLE");



            // For each movie, create an actors property that points to a blank node.

            JsonObject actorsJson = movieObject.getAsJsonObject("actors");
            Set<Map.Entry<String, JsonElement>> entrySet = actorsJson.entrySet();
            Resource actorsBlankNode = model.createResource();

            // The blank node has an actor property for each actor that stars in this movie.
            for(Map.Entry<String,JsonElement> entry : entrySet){
                Resource actorPerson = model.createResource(dbpedia_page + actorsJson.get(entry.getKey()).getAsString())
                        .addProperty(RDF.type, dbpedia + "Actor")
                        .addProperty(RDFS.subClassOf, dbpedia + "Person")
                        .addProperty(VCARD.FN, actorsJson.get(entry.getKey()).getAsString())
                        .addProperty(VCARD.TITLE, "Actor");
                actorsBlankNode.addProperty(actor, actorPerson);
                movieRDF.addProperty(actors, actorsBlankNode);
            }

            // For each movie, create a keywords property that points to a blank node.
            // The blank node has a keyword property for each keyword that is associated with this movie.

            JsonObject keywordsJSON = movieObject.getAsJsonObject("keywords");
            Set<Map.Entry<String, JsonElement>> keywordEntrySet = keywordsJSON.entrySet();
            Resource keywordsBlankNode = model.createResource();

            for(Map.Entry<String,JsonElement> entry : keywordEntrySet){
                Resource keywordResource = model.createResource(keywordURI + keywordsJSON.get(entry.getKey()).getAsString())
                        .addProperty(RDF.type, keywordURI)
                        .addProperty(RDFS.label, keywordsJSON.get(entry.getKey()).getAsString());
                keywordsBlankNode.addProperty(keyword, keywordResource);
                movieRDF.addProperty(keywords, keywordsBlankNode);
            }
        }
//        return the model contain all semantic triples made from the original JSON document.
        return model;
    }


    public String jsonFileToString(String pathname) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(pathname));

        StringBuffer stringBuffer = new StringBuffer();
        String line = null;

        while((line =bufferedReader.readLine())!=null){

            stringBuffer.append(line).append("\n");
        }
        return stringBuffer.toString();

    }


    public void sparqlTest(Model model) {
//        UpdateAction.parseExecute(""
//                + "PREFIX info216: <http://dbpedia.org/ontology#>"
//                + "INSERT DATA {"
//                + "    info216:cade info216:teaches info216:ECO001 . "
//                + "    GRAPH <http://ex.org/personal#Graph> {"
//                + "        info216:cade info216:age '29' . "
//                + "    }"
//                + "}", dataset);
//
////        RDFDataMgr.write(System.out, dataset, Lang.TRIG);
    }

    public static void printAllTriples(Model model) {
        ResultSet resultSet = QueryExecutionFactory
                .create(""
                        + "SELECT ?s ?p ?o WHERE {"
                        + "    ?s ?p ?o ."
                        + "}", model)
                .execSelect();
        resultSet.forEachRemaining(qsol -> System.out.println(qsol.toString()));
    }
}
