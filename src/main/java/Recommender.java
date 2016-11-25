/**
 * Created by aitor on 16/11/16.
 */

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;

import org.apache.mahout.cf.taste.common.TasteException;
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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class Recommender extends EvaluateRecommender {


    public static void main(String[] args) throws Exception {

        int option = 0;
        Scanner scanner = new Scanner(System.in);
        Scanner wait = new Scanner(System.in);
        while (option != 5) {
            //System.out.print("\f");
            //System.out.print("\033[H\033[2J");
            //System.out.flush();
            int lineas = 20;
            for (int i=0; i < lineas; i++) {
                System.out.println();
            }

            System.out.println("");
            System.out.println("");
            System.out.println("========================================================================");
            System.out.println("|                               MENU                                   |");
            System.out.println("|======================================================================|");
            System.out.println("|                                                                      |");
            System.out.println("|         1- Predecir películas a través de un nombre                  |");
            System.out.println("|                                                                      |");
            System.out.println("|         2- Puntuar una película                                      |");
            System.out.println("|                                                                      |");
            System.out.println("|         3- Mostrar las películas que ha valorado un usuario          |");
            System.out.println("|                                                                      |");
            System.out.println("|         4- Evaluar recomendador                                      |");
            System.out.println("|                                                                      |");
            System.out.println("|         5- Salir                                                     |");
            System.out.println("|                                                                      |");
            System.out.println("|----------------------------------------------------------------------|");
            System.out.println("|                     Elije una de las opciones:                       |");
            System.out.println("========================================================================");
            System.out.println("");
            int bot = 10;
            for (int i=0; i < bot; i++) {
                System.out.println();
            }

            try {
                option = scanner.nextInt();

            } catch (InputMismatchException e) {
                option = 10;
            }

            switch (option) {

                case 1:
                    //System.out.print("\f");
                    //System.out.print("\033[H\033[2J");
                    //System.out.flush();
                    System.out.println("Escribe el nombre del usuario:");
                    recommender(scanner.next());
                    System.out.print("\nPress any key to continue . . . \n");
                    wait.nextLine();
                    break;

                case 3:
                    //System.out.print("\f");
                    //System.out.print("\033[H\033[2J");
                    //System.out.flush();
                    System.out.println("Escribe el nombre del usuario:");
                    getRates(scanner.next());
                    System.out.print("\nPress any key to continue . . . \n");
                    wait.nextLine();
                    break;

                case 4:
                    evaluateRecommender();
                    System.out.print("\nPress any key to continue . . . \n");
                    wait.nextLine();
                    break;
            }
        }
    }

    static void recommender(String user) throws IOException, TasteException {
        int id = -1;

        //Read the movies.csv
        String csvFilename1 = "src/main/input/nombres.csv";
        CSVReader csvReader1 = new CSVReader(new FileReader(csvFilename1));

        //parse the csv in a list of objects -->allMovies
        ColumnPositionMappingStrategy strat1 = new ColumnPositionMappingStrategy();
        strat1.setType(User.class);
        String[] columns1 = new String[]{"id", "name"}; // the fields to bind do in your JavaBean
        strat1.setColumnMapping(columns1);

        CsvToBean csv1 = new CsvToBean();

        List allUsers = csv1.parse(strat1, csvReader1);
        for (Object object : allUsers) {
            User user1 = (User) object;
            if (user1.getName().equals(user)) {
                id = user1.getId();
            }
        }

        DataModel model = new FileDataModel(new File("src/main/input/dataset.csv")); //load data from file
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model); //correlation coeficient
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.9, similarity, model); //0.9-->threshold

        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

        //recommendations--> list of RecommendedItem objects with that obtain: movie_id and rate
        //first param--> userid and second param --> number of movies recommended
        List<RecommendedItem> recommendations = recommender.recommend(id, 10);

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
        String[] columns = new String[]{"movieId", "title", "genres"}; // the fields to bind do in your JavaBean
        strat.setColumnMapping(columns);

        CsvToBean csv = new CsvToBean();

        List allMovies = csv.parse(strat, csvReader);

        /* Print all the movieId that we parse of the csv
        for (Object object : allMovies) {
            Movie movie = (Movie) object;
            System.out.println(movie.getMovieId());
        }
        */

        //System.out.print("\f");
        //System.out.print("\033[H\033[2J");
        //System.out.flush();

        //Add in a list all the movies that we recommended -->recommendedList
        List<Movie> recommendedList = new ArrayList<Movie>();
        int lineas = 30;
        for (int i=0; i < lineas; i++) {
            System.out.println();
        }

        System.out.println("");
        System.out.println("");
        System.out.println("========================================================================");
        System.out.println("                       LIST OF RECOMMENDED FILMS                        ");
        int num =1;
        for (RecommendedItem recommendedItem : recommendations) {
            String idRecommended = Long.toString(recommendedItem.getItemID());
            for (Object object : allMovies) {
                Movie movie = (Movie) object;
                if (idRecommended.equals(movie.getMovieId())) {
                    recommendedList.add(movie);
                    System.out.println("========================================================================");
                    System.out.println("||· Nº: "+ num);
                    System.out.println("||· MOVIE: "+ movie.getTitle());
                    System.out.println("||· GENERES: "+ movie.getGenres());
                    //System.out.println(" - " + movie.getTitle() + "  " + "GENERES: " + movie.getGenres());
                    num++;
                }
            }
        }
        System.out.println("========================================================================");
        System.out.println("");
    }

    static void evaluateRecommender() throws IOException, TasteException {
        evaluate();
    }

    static void getRates(String user) throws FileNotFoundException {
        int id = -1;

        //Read the movies.csv
        String csvFilename1 = "src/main/input/nombres.csv";
        CSVReader csvReader1 = new CSVReader(new FileReader(csvFilename1));

        //parse the csv in a list of objects -->allMovies
        ColumnPositionMappingStrategy strat1 = new ColumnPositionMappingStrategy();
        strat1.setType(User.class);
        String[] columns1 = new String[]{"id", "name"}; // the fields to bind do in your JavaBean
        strat1.setColumnMapping(columns1);

        CsvToBean csv1 = new CsvToBean();

        List allUsers = csv1.parse(strat1, csvReader1);
        for (Object object : allUsers) {
            User user1 = (User) object;
            if (user1.getName().equals(user)) {
                id = user1.getId();
            }
        }


        //Read the dataset.csv
        String csvFilename = "src/main/input/dataset.csv";
        CSVReader csvReader = new CSVReader(new FileReader(csvFilename));

        //parse the csv in a list of objects -->allRates
        ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
        strat.setType(Rate.class);
        String[] columns = new String[]{"user", "movie", "rate"}; // the fields to bind do in your JavaBean
        strat.setColumnMapping(columns);

        CsvToBean csv = new CsvToBean();

        List allRates = csv.parse(strat, csvReader);

        List<Rate> myRates = new ArrayList<Rate>();
        for (Object object : allRates) {
            Rate rate1 = (Rate) object;
            if (rate1.getUser() == id) {
                myRates.add(rate1);
            }
        }

        //Read the movies.csv
        String csvFilename2 = "src/main/input/movies.csv";
        CSVReader csvReader2 = new CSVReader(new FileReader(csvFilename2));

        //parse the csv in a list of objects -->allMovies
        ColumnPositionMappingStrategy strat2 = new ColumnPositionMappingStrategy();
        strat2.setType(Movie.class);
        String[] columns2 = new String[]{"movieId", "title", "genres"}; // the fields to bind do in your JavaBean
        strat2.setColumnMapping(columns2);

        CsvToBean csv2 = new CsvToBean();

        List allMovies = csv2.parse(strat2, csvReader2);
        int lineas = 30;
        for (int i=0; i < lineas; i++) {
            System.out.println();
        }
        //System.out.print("\f");
        //System.out.print("\033[H\033[2J");
        //System.out.flush();
        System.out.println("");
        System.out.println("");
        System.out.println("========================================================================");
        System.out.println("                          LIST OF RATED FILMS                           ");
        int cont = 0;
        int num =1;
        for (Object object : myRates) {
            Rate rate1 = (Rate) object;
            for (Object object2 : allMovies) {
                Movie movie = (Movie) object2;
                int idEquals = Integer.parseInt(movie.getMovieId());
                if (rate1.getMovie() == idEquals && cont < 10) {
                    //System.out.println(" ");
                    System.out.println("========================================================================");
                    System.out.println("||· Nº: "+ num);
                    System.out.println("||· MOVIE: "+ movie.getTitle());
                    System.out.println("||· RATE: "+ rate1.getRate());
                    //System.out.println("===========================================================================");
                    //System.out.println("MOVIE "+ num +": " + movie.getTitle() + " RATE: " + rate1.getRate());
                    cont++;
                    num++;
                }
            }
        }
        System.out.println("========================================================================");
        System.out.println("");

    }
}



/*Coses per fer:
Quin threshold hem de posar
Quantes pelicules com a molt volem recomanar


Informacio treta de:
https://mahout.apache.org/users/recommender/userbased-5-minutes.html
http://viralpatel.net/blogs/java-read-write-csv-file/   seccio--> 3. Mapping CSV with Java beans
 */
