import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import java.math.BigDecimal;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;


public class EvaluateRecommender {
    public static void evaluate() throws IOException, TasteException {
        class MyRecommenderBuilder implements RecommenderBuilder{
            public Recommender buildRecommender(DataModel dataModel) throws TasteException {
                UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
                UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.9, similarity, dataModel);
                return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
            }
        }

        DataModel model = new FileDataModel(new File("src/main/input/dataset.csv"));
        RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
        RecommenderBuilder builder = new MyRecommenderBuilder();
        double result = evaluator.evaluate(builder, null, model, 0.5, 1.0); //trainingset-->90% and a testset-->10%
        //double resultado = Math.round(result);
        double resultado = result*100;
        BigDecimal big = new BigDecimal(resultado);
        big = big.setScale(2, RoundingMode.HALF_UP);
        System.out.println();
        System.out.println();
        System.out.println("===================================================");
        System.out.println("|                                                 |");
        //System.out.println("  * Exactitud del Recomendador: "+Math.round(result)*100+"% *");
        //System.out.println("  * Exactitud del Recomendador: "+Math.rint(result*100)+"% *");
        System.out.println("|      * Accurazy of Recommender: "+big+"%          |");
        System.out.println("|                                                 |");
        System.out.println("===================================================");
    }
}


