import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.VCARD;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class JSONToTDB {
    private Model model;


    public JSONToTDB(String jsonPath) {
        Dataset dataset = TDBFactory.createDataset("movie_tdb");
        Model model = dataset.getDefaultModel();
        if (model.isEmpty()) {
            String jsonString = "";
            try {
                jsonString = jsonFileToString(jsonPath);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            this.model= parseJSONString(jsonString, model);
        }
        System.out.println("TEST: " + model.isEmpty());
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


    public Model parseJSONString(String json, Model model) {
        JsonElement jsonElement = new JsonParser().parse(json);
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        String dbo = "http://dbpedia.org/ontology/";
        String dbp = "http://dbpedia.org/page/";
        String dbpedia_extension = "http://dbpedia.org/ontology/extension/";

        String info216 = "http://info216.no/v2019/vocabulary/";
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

            Property title = model.createProperty(info216 + "title");
            Property year = model.createProperty(dbo + "year");
            Property runtime = model.createProperty(dbo + "filmRuntime");
            Property director = model.createProperty(dbo + "director");
            Property actors = model.createProperty(info216 + "actors");
            Property actor = model.createProperty(dbo + "starring");
            Property genres = model.createProperty(info216 + "genres");
            Property keywords = model.createProperty(info216 + "keywords");
            Property keyword = model.createProperty(info216 + "keyword");
            Property gross = model.createProperty(dbo + "gross");
            Property content_rating = model.createProperty(dbp + "Motion_Picture_Association_of_America_film_rating_system");
            Property country = model.createProperty(dbo + "country");
            Property language = model.createProperty(dbo + "language");
            Property num_votes = model.createProperty(info216 + "imdb_votes");
            Property imdb_rating = model.createProperty(dbo + "rating");
            Property imdb_link = model.createProperty(dbp + "Hyperlink");
            Property imdb_id = model.createProperty(dbo + "imdbId");

            Resource directorPerson = model.createResource(dbp + movieObject.get("director").getAsString())
                    .addProperty(RDF.type, dbo + "MovieDirector")
                    .addProperty(RDFS.subClassOf, dbo + "Person")
                    .addProperty(VCARD.FN, movieObject.get("director").getAsString())
                    .addProperty(VCARD.TITLE, "Director");

//            System.out.println("HELLLO" + movieObject.get("title").getAsString());

            Resource movieRDF = model.createResource(dbp + movieObject.get("title").getAsString())
                    .addProperty(RDF.type, dbp + "Film")
                    .addProperty(RDFS.label, movieObject.get("title").getAsString())
                    .addProperty(title, movieObject.get("title").getAsString())
                    .addProperty(director, directorPerson)
                    .addProperty(year, movieObject.get("year").getAsString(), XSDDatatype.XSDgYear)
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
                Resource actorPerson = model.createResource(dbp + actorsJson.get(entry.getKey()).getAsString())
                        .addProperty(RDF.type, dbo + "Actor")
                        .addProperty(RDFS.subClassOf, dbo + "Person")
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

    public Model getModel() {
        return this.model;
    }
}
