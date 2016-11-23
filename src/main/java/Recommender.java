/**
 * Created by aitor on 16/11/16.
 */

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;


import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Recommender {
    public static void main(String[] args) throws Exception {
        DataModel model = new FileDataModel(new File("src/main/input/dataset.csv")); //load data from file
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model); //correlation coeficient
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model); //0.1-->threshold

        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

        //recommendations--> list of RecommendedItem objects with that obtain: movie_id and rate
        //first param--> userid and second param --> number of movies recommended
        List<RecommendedItem> recommendations = recommender.recommend(2, 10);

        /* Print the id of the items that we recommended
        for (RecommendedItem recommendedItem : recommendations) {
            System.out.println("ITEM ID:" + recommendedItem.getItemID());
        }
        */

        //Read the movies.csv
        String csvFilename = "src/main/input/movies.csv";
        CSVReader csvReader = new CSVReader(new FileReader(csvFilename));

        //parse the csv in a list of objects -->allMovies
        ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
        strat.setType(Movie.class);
        String[] columns = new String[] {"movieId", "title", "genres"}; // the fields to bind do in your JavaBean
        strat.setColumnMapping(columns);

        CsvToBean csv = new CsvToBean();

        List allMovies = csv.parse(strat, csvReader);

        /* Print all the movieId that we parse of the csv
        for (Object object : list) {
            Movie movie = (Movie) object;
            System.out.println(movie.getMovieId());
        }
        */

        //Add in a list all the movies that we recommended -->recommendedList
        List<Movie> recommendedList = new ArrayList<Movie>();
        System.out.println("_____________________________________________________");
        System.out.println("          Listado de películas recomendadas          ");
        System.out.println("-----------------------------------------------------");
        System.out.println("-----------------------------------------------------");
        System.out.println("                                                     ");
        for (RecommendedItem recommendedItem : recommendations) {
            String idRecommended = Long.toString(recommendedItem.getItemID());
            for (Object object : allMovies) {
                Movie movie = (Movie) object;
                if(idRecommended.equals(movie.getMovieId())){
                    recommendedList.add(movie);
                    System.out.println(" - "+movie.getTitle()+"  ");
                }
            }
        }
        System.out.println("                                                     ");
        System.out.println("-----------------------------------------------------");
    }
}

/*Coses per fer:
Quin threshold hem de posar
Quantes pelicules com a molt volem recomanar
el id de l'usuari ha de ser una variable per tant:
cargar els fitxers en el program principial i ficar el predictor amb una funcio que li pasem
el usuari per parametra

MENU:
1. Predecir peliculas a traves de un nombre
2. Puntuar una pelicula
3. Mostrar las peliculas que ha valorado un persona

Informacio treta de:
https://mahout.apache.org/users/recommender/userbased-5-minutes.html
http://viralpatel.net/blogs/java-read-write-csv-file/   seccio--> 3. Mapping CSV with Java beans
 */
