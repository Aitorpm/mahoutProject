
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
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

import java.io.FileWriter;

import com.opencsv.CSVWriter;
import com.opencsv.*;
import org.apache.mahout.common.IntPairWritable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Thread.*;


public class Recommender extends EvaluateRecommender {

    public static void main(String[] args) throws Exception {

        int option = 0;
        int numRate = 0;
        float ratefilm = 0;

        String name;
        String namefilm;
        String genrefilm;

        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("Loading:");
        System.out.println("");
        sleep(300);
        System.out.print("[  * ");
        sleep(1000);
        System.out.print("* * * ");
        sleep(300);
        System.out.print("* ");
        sleep(1200);
        System.out.print("* * * * *  ]");
        sleep(300);
        Scanner scanner = new Scanner(System.in);
        Scanner wait = new Scanner(System.in);
        Scanner aux = new Scanner(System.in);
        while (option != 6) {
            //System.out.print("\f");
            //System.out.print("\033[H\033[2J");
            //System.out.flush();
            int lineas = 20;
            for (int i = 0; i < lineas; i++) {
                System.out.println();
            }

            System.out.println("");
            System.out.println("");
            System.out.println("========================================================================");
            System.out.println("*                               MENU                                   *");
            System.out.println("========================================================================");
            System.out.println("|                                                                      |");
            System.out.println("|                [1] - Predict movies by user name                     |");
            System.out.println("|                                                                      |");
            System.out.println("|                [2] - Rate a movie                                    |");
            System.out.println("|                                                                      |");
            System.out.println("|                [3] - Show movies rated by a user                     |");
            System.out.println("|                                                                      |");
            System.out.println("|                [4] - Evaluate recommender                            |");
            System.out.println("|                                                                      |");
            System.out.println("|                [5] - Add a new film into the system                  |");
            System.out.println("|                                                                      |");
            System.out.println("|                [6] - Exit                                            |");
            System.out.println("|                                                                      |");
            System.out.println("========================================================================");
            System.out.println("*                         < Choose an option >                         *");
            System.out.println("========================================================================");
            int bot = 2;
            for (int i = 0; i < bot; i++) {
                System.out.println();
            }
            System.out.println("========================================================================");
            System.out.println("*                         < User formulary >                           *");
            System.out.println("========================================================================");
            System.out.println("");
            System.out.print("* Selected option: ");
            try {
                option = scanner.nextInt();

            } catch (InputMismatchException e) {
                option = 10;
            }

            switch (option) {

                case 1:
                    System.out.println("");
                    System.out.print("* Enter the username: ");
                    name = scanner.next();
                    System.out.println("");
                    System.out.print("* Enter the number of ratings to show: ");
                    numRate = scanner.nextInt();
                    recommender(name, numRate);
                    System.out.print("\nPress any key to continue . . . \n");
                    wait.nextLine();
                    break;

                case 2:
                    System.out.println("");
                    System.out.print("* Enter the your name: ");
                    name = scanner.next();
                    System.out.println("");
                    System.out.print("* Enter the film name: ");
                    namefilm = aux.nextLine();
                    System.out.println("");
                    System.out.print("* Enter your rate film: ");
                    ratefilm = scanner.nextFloat();
                    getNewRate(name, namefilm, ratefilm);
                    System.out.println("");
                    System.out.print("\nPress any key to continue . . . \n");
                    wait.nextLine();
                    break;

                case 3:
                    System.out.println("");
                    System.out.print("* Enter the username: ");
                    name = scanner.next();
                    System.out.println("");
                    System.out.print("* Enter the number of ratings to show: ");
                    numRate = scanner.nextInt();
                    getRates(name, numRate);
                    System.out.println("");
                    System.out.print("\nPress any key to continue . . . \n");
                    wait.nextLine();
                    break;

                case 4:
                    evaluateRecommender();
                    System.out.print("\nPress any key to continue . . . \n");
                    wait.nextLine();
                    break;

                case 5:
                    System.out.println("");
                    System.out.print("* Enter the film name: ");
                    namefilm = aux.nextLine();
                    System.out.println("");
                    System.out.print("* Enter the genres film: ");
                    genrefilm = scanner.next();
                    newFilm(namefilm, genrefilm);
                    System.out.println("");
                    System.out.print("\nPress any key to continue . . . \n");
                    wait.nextLine();
                    break;

                case 6:
                    System.out.print("\n...SEE YOU SOON...!  \n");
            }
        }
    }

    //functions case 1
    static void recommender(String user, int num) throws IOException, TasteException, InterruptedException {
        int id = -1;
        int numRate;
        numRate = num;

        //Read the nombres.csv
        String csvFilename1 = "src/main/input/nombres.csv";
        CSVReader csvReader1 = new CSVReader(new FileReader(csvFilename1));

        //parse the csv in a list of objects -->allUsers
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
        if (id == -1) {
            System.out.println("");
            System.out.println("=======================================================================");
            System.out.println("");
            System.out.println("* THE USER [" + user + "] DON'T EXIST...");
            System.out.println("");
            System.out.println("=======================================================================");
            sleep(1000);
            System.out.println("|                                                                     |");
            System.out.println("|                             TRY IT NOW!!                            |");
            System.out.println("|                                                                     |");
            System.out.println("=======================================================================");
            System.out.println("");
        } else {
            DataModel model = new FileDataModel(new File("src/main/input/dataset.csv")); //load data from file
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model); //correlation coeficient
            UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.5, similarity, model); //0.9-->threshold

            UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            //recommendations--> list of RecommendedItem objects with that obtain: movie_id and rate
            //first param--> userid and second param --> number of movies recommended
            List<RecommendedItem> recommendations = recommender.recommend(id, numRate);

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
            for (int i = 0; i < lineas; i++) {
                System.out.println();
            }

            System.out.println("");
            System.out.println("");
            System.out.println("========================================================================");
            System.out.println("*                      LIST OF RECOMMENDED FILMS                       *");
            int aux = 1;
            for (RecommendedItem recommendedItem : recommendations) {
                String idRecommended = Long.toString(recommendedItem.getItemID());
                for (Object object : allMovies) {
                    Movie movie = (Movie) object;
                    if (Integer.parseInt( idRecommended ) == movie.getMovieId()) {
                        recommendedList.add(movie);
                        System.out.println("========================================================================");
                        System.out.println("||· Nº: " + aux);
                        System.out.println("||· MOVIE: " + movie.getTitle());
                        System.out.println("||· GENRES: " + movie.getGenres());
                        //System.out.println(" - " + movie.getTitle() + "  " + "GENERES: " + movie.getGenres());
                        aux++;
                    }
                }
            }
            System.out.println("========================================================================");
            System.out.println("");
        }
    }

    //functions case 5
    private static void newFilm(String namefilm, String genrefilm) throws IOException, InterruptedException {
        int im = -1;

        //Work with movies.csv
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
        for (Object object2 : allMovies) {
            Movie movie = (Movie) object2;
            if (movie.getTitle().equals(namefilm)) {
                im = movie.getMovieId();
            }
        }
        if (im != -1){
            System.out.println("");
            System.out.println("=======================================================================");
            System.out.println("");
            System.out.println("* THIS FILM [ " + namefilm + " ] ALLREADY EXISTS!!");
            System.out.println("");
            System.out.println("=======================================================================");
            sleep(1000);
            System.out.println("");
            System.out.println("");
            System.out.println("* INTRODUCE A DIFFERENT FILM!!");
            System.out.println("");
            System.out.println("=======================================================================");
            System.out.println("");
        } else {
            System.out.println("");
            System.out.println("=======================================================================");
            System.out.println("");
            System.out.println("* THIS FILM [ " + namefilm + " ] NOT EXISTS");
            System.out.println("");
            System.out.println("=======================================================================");
            System.out.println("");
            System.out.println("");
            System.out.println("* YOU SHOULD INTRODUCE THE INFORMATION ABOUT" + namefilm);
            System.out.println("");
            System.out.println("=======================================================================");
            System.out.println("");
            System.out.println("FILM: " + namefilm);
            System.out.println("GENRES: " + genrefilm);
            System.out.println("");
            writeCSVN(namefilm, genrefilm);
        }
    }

    private static void writeCSVN(String namefilm, String genrefilm) throws IOException {
        int numMov = 164978;
        String csv = "src/main/input/movies.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csv, true), ',', '\0', "\r\n");
        //String[] header= new String[]{"movieId", "title", "genres"};
        //writer.writeNext(header);
        List<String[]> allData = new ArrayList<String[]>();
        String[] data = new String[]{String.valueOf(numMov), namefilm, genrefilm};
        allData.add(data);
        System.out.println(" ");
        System.out.println("Estamos en fichero: "+ csv);
        System.out.println("ID: " + numMov);
        System.out.println("FILM: " + namefilm);
        System.out.println("GENRE: " + genrefilm);
        System.out.println(" ");
        writer.writeAll(allData);
        writer.close();
    }

    //functions case 2
    static void getNewRate(String name, String namefilm, float ratefilm) throws IOException, TasteException, InterruptedException {

        int id = -1;
        int im = -1;

        //Work with movies.csv
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
        for (Object object2 : allMovies) {
            Movie movie = (Movie) object2;
            if (movie.getTitle().equals(namefilm)) {
                im = movie.getMovieId();
            }
        }


        //Work with nombres.csv
        //Read the nombres.csv
        String csvFilename1 = "src/main/input/nombres.csv";
        CSVReader csvReader1 = new CSVReader(new FileReader(csvFilename1));

        //parse the csv in a list of objects -->allUsers
        ColumnPositionMappingStrategy strat1 = new ColumnPositionMappingStrategy();
        strat1.setType(User.class);
        String[] columns1 = new String[]{"id", "name"}; // the fields to bind do in your JavaBean
        strat1.setColumnMapping(columns1);

        CsvToBean csv1 = new CsvToBean();

        List allUsers = csv1.parse(strat1, csvReader1);
        for (Object object : allUsers) {
            User user1 = (User) object;
            if (user1.getName().equals(name)) {
                id = user1.getId();
            }
        }

        if (id != -1) {
            System.out.println("");
            System.out.println("=======================================================================");
            System.out.println("");
            System.out.println("* WELCOME ANOTHER TIME " + name + " MAKE YOUR RATE!");
            System.out.println("");
            System.out.println("=======================================================================");
            sleep(1000);
            System.out.println("");
            System.out.println("");
            System.out.println("* THE OLDER IDUSER = [" + id + "] ");
            System.out.println("");
            System.out.println("=======================================================================");
            System.out.println("");

            if (im != -1){
                System.out.println("");
                System.out.println("=======================================================================");
                System.out.println("");
                System.out.println("* THIS FILM [ " + namefilm + " ] ALLREADY EXISTS!!");
                System.out.println("");
                System.out.println("=======================================================================");
                sleep(1000);
                System.out.println("");
                System.out.println("");
                System.out.println("* THE OLDER IDMOVIE = [" + im + "] ");
                System.out.println("");
                System.out.println("=======================================================================");
                System.out.println("");
                System.out.println("NAME: " + name);
                System.out.println("FILM: " + namefilm);
                System.out.println("RATE: " + ratefilm);
                System.out.println("");
                writeCSV(name, id, namefilm, im, ratefilm);
            } else {
                System.out.println("");
                System.out.println("=======================================================================");
                System.out.println("");
                System.out.println("* THIS FILM [ " + namefilm + " ] NOT EXISTS");
                System.out.println("");
                System.out.println("=======================================================================");
                System.out.println("");
                System.out.println("");
                System.out.println("* YOU SHOULD INTRODUCE [ " + namefilm + " ] INTO THE SYSTEM PREVIOUSLY");
                System.out.println("");
                System.out.println("=======================================================================");
                System.out.println("");
            }
        } else {
            System.out.println("");
            System.out.println("=======================================================================");
            System.out.println("");
            System.out.println("* IS YOUR FIRST TIME, WELCOME " + name + " MAKE YOUR RATE!");
            System.out.println("");
            System.out.println("=======================================================================");
            System.out.println("");
            System.out.println("");
            System.out.println("* THE NEW IDUSER = [" + id + "] ");
            System.out.println("");
            System.out.println("=======================================================================");
            System.out.println("");


            if (im != -1){
                System.out.println("");
                System.out.println("=======================================================================");
                System.out.println("");
                System.out.println("* THIS FILM [ " + namefilm + " ] ALLREADY EXISTS!!");
                System.out.println("");
                System.out.println("=======================================================================");
                sleep(1000);
                System.out.println("");
                System.out.println("");
                System.out.println("* THE OLDER IDMOVIE = [" + im + "] ");
                System.out.println("");
                System.out.println("=======================================================================");
                System.out.println("");
                System.out.println("NAME: " + name);
                System.out.println("FILM: " + namefilm);
                System.out.println("RATE: " + ratefilm);
                System.out.println("");
                writeCSV(name, id, namefilm, im, ratefilm);
            } else {
                System.out.println("");
                System.out.println("=======================================================================");
                System.out.println("");
                System.out.println("* THIS FILM [ " + namefilm + " ] NOT EXISTS");
                System.out.println("");
                System.out.println("=======================================================================");
                System.out.println("");
                System.out.println("");
                System.out.println("* YOU SHOULD INTRODUCE [ " + namefilm + " ] INTO THE SYSTEM PREVIOUSLY");
                System.out.println("");
                System.out.println("=======================================================================");
                System.out.println("");
            }
        }
    }

    static void writeCSV(String name, int id, String namefilm, int im, float ratefilm) throws IOException {

        int numName = 672;
        if (id == -1){
            String csv2 = "src/main/input/nombres.csv";
            CSVWriter writer2 = new CSVWriter(new FileWriter(csv2, true), ',', '\0', "\r\n");
            //String[] header= new String[]{"movieId", "title", "genres"};
            //writer.writeNext(header);
            List<String[]> allData2 = new ArrayList<String[]>();
            if (id != -1){
                String[] data2 = new String[]{String.valueOf(id), name};
                allData2.add(data2);
            }else{
                String[] data2 = new String[]{String.valueOf(numName), name};
                allData2.add(data2);
            }
            System.out.println(" ");
            System.out.println("Estamos en fichero: "+ csv2);
            System.out.println("ID: " + id);
            System.out.println("USERNAME: " + name);
            System.out.println(" ");
            writer2.writeAll(allData2);
            writer2.close();

        }

        String csv3 = "src/main/input/dataset.csv";
        CSVWriter writer3 = new CSVWriter(new FileWriter(csv3, true), ',', '\0', "\r\n");
        //String[] header= new String[]{"movieId", "title", "genres"};
        //writer.writeNext(header);
        List<String[]> allData3 = new ArrayList<String[]>();
        if (id != -1){
            String[] data3 = new String[]{String.valueOf(id),String.valueOf(im), String.valueOf(ratefilm)};
            allData3.add(data3);
        }else{
            String[] data3 = new String[]{String.valueOf(numName),String.valueOf(im), String.valueOf(ratefilm)};
            allData3.add(data3);
            numName++;
        }
        System.out.println(" ");
        System.out.println("Estamos en fichero: "+csv3);
        System.out.println("IDUSER: " + id);
        System.out.println("IDFILM: " + im);
        System.out.println("IDFILM: " + ratefilm);
        System.out.println(" ");
        writer3.writeAll(allData3);
        writer3.close();

    }

    //functions case 3
    static void getRates(String user, int num) throws FileNotFoundException, InterruptedException {
        int id = -1;
        int numRate = 0;
        numRate = num;

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
        if (id == -1) {
            System.out.println("");
            System.out.println("=======================================================================");
            System.out.println("");
            System.out.println("* THE USER [" + user + "] DON'T EXIST...");
            System.out.println("");
            System.out.println("=======================================================================");
            sleep(1000);
            System.out.println("|                                                                     |");
            System.out.println("|                             TRY IT NOW!!                            |");
            System.out.println("|                                                                     |");
            System.out.println("=======================================================================");
            System.out.println("");
        } else {

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
            //ORDENA LA LISTA POR VALORACIONES EN ORDEN DESCENDENTE
            //BeanComparator fieldComparator = new BeanComparator("rate"); PARA ORDEN ASCENDENTE
            BeanComparator fieldComparator = new BeanComparator("rate",new ReverseComparator(new ComparableComparator()));
            Collections.sort(myRates,fieldComparator);



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
            for (int i = 0; i < lineas; i++) {
                System.out.println();
            }
            //System.out.print("\f");
            //System.out.print("\033[H\033[2J");
            //System.out.flush();
            System.out.println("");
            System.out.println("");
            System.out.println("========================================================================");
            System.out.println("*                         LIST OF RATED FILMS                          *");
            int cont = 0;
            int aux = 1;
            for (Object object : myRates) {
                Rate rate1 = (Rate) object;
                for (Object object2 : allMovies) {
                    Movie movie = (Movie) object2;
                    int idEquals = movie.getMovieId();
                    if (rate1.getMovie() == idEquals && cont < numRate) {
                        //System.out.println(" ");
                        System.out.println("========================================================================");
                        System.out.println("||· Nº: " + aux);
                        System.out.println("||· MOVIE: " + movie.getTitle());
                        System.out.println("||· GENRES: " + movie.getGenres());
                        System.out.println("||· RATE: " + rate1.getRate());
                        //System.out.println("MOVIE "+ num +": " + movie.getTitle() + " RATE: " + rate1.getRate());
                        cont++;
                        aux++;
                    }
                }
            }
            System.out.println("========================================================================");
            System.out.println("");
        }
    }

    //functions case 3
    static void evaluateRecommender() throws IOException, TasteException {
        evaluate();
    }
}