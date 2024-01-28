import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            // Fortosi ton dedomenwn ekpaideusis
            String positivePath = "C:\\Users\\eirin\\OneDrive - aueb.gr\\Desktop\\ergasiatexniti\\postrain";
            String negativePath = "C:\\Users\\eirin\\OneDrive - aueb.gr\\Desktop\\ergasiatexniti\\negtrain";

            // Fortosi test
            String positiveTestPath = "C:\\Users\\eirin\\OneDrive - aueb.gr\\Desktop\\ergasiatexniti\\postest";
            String negativeTestPath = "C:\\Users\\eirin\\OneDrive - aueb.gr\\Desktop\\ergasiatexniti\\negtest";

            List<String> positiveTestTexts = NaiveBayes.loadTexts(positiveTestPath);
            List<String> negativeTestTexts = NaiveBayes.loadTexts(negativeTestPath);

            List<String> positiveTexts = NaiveBayes.loadTexts(positivePath);
            List<String> negativeTexts = NaiveBayes.loadTexts(negativePath);

            // Dimiourgia antikeimenou Naive Bayes kai train
            NaiveBayes nb = new NaiveBayes();
            nb.train(positiveTexts, negativeTexts);

            // Syxnotita lexewn
            Map<String, Integer> positiveFreq = nb.createWordFrequencyMap(positiveTexts);
            Map<String, Integer> negativeFreq = nb.createWordFrequencyMap(negativeTexts);

            // Sindiasmos thetikwn kai arntikwn listwn wste na vroume to lexilogio
            Map<String, Integer> combinedFreq = new HashMap<>();
            combinedFreq.putAll(positiveFreq);
            negativeFreq.forEach((key, value) -> combinedFreq.merge(key, value, Integer::sum));


            // Dimiourgia lexilogiou
            // Arithmos ton lexewn sto lexilogio
            // 240
            int m = 500;
            // Arithmos ton pio suxnwn lexewn pros afairesi
            // 150
            int n = 200;
            // Arithmos ton pio spaniwn pros afairesh
            // 20
            int k = 50;
            List<String> vocabulary = nb.createVocabulary(combinedFreq, m, n, k);


            // Lists to store precision, recall, and F1 score for each training size and accuracy 
            List<Double> trainingPrecisions = new ArrayList<>();
            List<Double> trainingRecalls = new ArrayList<>();
            List<Double> trainingF1Scores = new ArrayList<>();
            List<Double> trainingAccuracies = new ArrayList<>();
            List<Double> testAccuracies = new ArrayList<>();

            


    }  catch (IOException e) {
            e.printStackTrace();
        }
    }

}
