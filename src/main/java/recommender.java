/**
 * Created by aitor on 16/11/16.
 */
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;


import java.io.File;
import java.util.List;

public class recommender {
    public static void main(String[] args) throws Exception {

        DataModel model = new FileDataModel(new File("src/main/input/dataset.csv")); //load data from file
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model); //correlation coeficient
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model); //0.1-->threshold

        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

        List recommendations = recommender.recommend(2, 40); //recommend 10 users to user with userid = 2
        System.out.println(recommendations);

        int cont =0;
        for (Object recommendedItem : recommendations){
            cont++;
            System.out.println(recommendedItem + "" + cont);
        }

        //Print the neighborhoods
        //Iterator iter = recommendations.iterator();
        //while (iter.hasNext())
        //    System.out.println(iter.next());
    }
}


/*
Comprobar que la prediccio s'ha fet correctament
Extreure el id de cada pelicula
Cargar la bd de peliculas
Relacionar les id de la pelicula amb la seva informacio i mostrarla a l'usuari
Quin threshold hem de posar
Quantes pelicules com a molt volem recomanar
el id de l'usuari ha de ser una variable per tant:
cargar els fitxers en el program principial i ficar el predictor amb una funcio que li pasem
el usuari per parametra
 */