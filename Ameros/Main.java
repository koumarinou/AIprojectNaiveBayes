import java.io.IOException;
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


    }  catch (IOException e) {
            e.printStackTrace();
        }
    }

}
