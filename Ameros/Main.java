import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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

            int[] trainingSizes = { 50, 100, 150, 200, 250, 300, 350, 400, 450, 500, 550, 600 };
            for (int size : trainingSizes) {
                // Ekpaideusi tou montelou me to sigekrimeno megethos
                List<String> limitedPositiveTexts = positiveTexts.subList(0, size);
                List<String> limitedNegativeTexts = negativeTexts.subList(0, size);
                
                nb.train(limitedPositiveTexts, limitedNegativeTexts);

                // Axiologisi tou montelou kai katagrafi akriveias
                double trainingAccuracy = (nb.calculateAccuracy(limitedPositiveTexts, vocabulary, "Positive")
                        + nb.calculateAccuracy(limitedNegativeTexts, vocabulary, "Negative")) / 2.0;
                double testAccuracy = (nb.calculateAccuracy(positiveTestTexts, vocabulary, "Positive")
                        + nb.calculateAccuracy(negativeTestTexts, vocabulary, "Negative")) / 2.0;

                trainingAccuracies.add(trainingAccuracy);
                testAccuracies.add(testAccuracy);

                // Ektiposi
                // System.out.println("Training size: " + size + ", Training Accuracy: " +
                // trainingAccuracy + ", Test Accuracy: " + testAccuracy);

                // Calculate TP, FP, FN for the current model
                Map<String, Integer> tpfpfnPos = nb.calculateTPFPFN(limitedPositiveTexts, vocabulary, "Positive");
                Map<String, Integer> tpfpfnNeg = nb.calculateTPFPFN(limitedNegativeTexts, vocabulary, "Negative");

                // Combine TP, FP, FN for positive and negative classes
                int TP = tpfpfnPos.get("TP") + tpfpfnNeg.get("TP");
                int FP = tpfpfnPos.get("FP") + tpfpfnNeg.get("FP");
                int FN = tpfpfnPos.get("FN") + tpfpfnNeg.get("FN");

                // Calculate precision, recall, and F1 score
                double precision = nb.calculatePrecision(TP, FP);
                double recall = nb.calculateRecall(TP, FN);
                double f1Score = nb.calculateF1Score(TP, FP, FN);

                // Store the metrics
                trainingPrecisions.add(precision);
                trainingRecalls.add(recall);
                trainingF1Scores.add(f1Score);

            }

            // Neo keimeno , ayto einai positive
            String newText = "This movie is an absolute gem, a delightful blend of humor, heart, and creativity. The performances are top-notch, the storyline is engaging and full of surprises, and the cinematography is breathtaking. It's a film that not only entertains but also inspires. Highly recommended for anyone seeking a memorable movie experience!";
            String classification = nb.classifyText(newText, vocabulary);

            // Print katigorias neou keimenou
            System.out.println("The text classified as : " + classification);

            // Pause gia 2 deuterolepta
            try {
                Thread.sleep(2000); // 20000 milliseconds = 20 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Accuracy
            JFrame frame = new JFrame("Accuracy Graph");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            List<Integer> trainingSize = List.of(50, 100, 150, 200, 250, 300, 350, 400, 450, 500, 550, 600);

            AccuracyGraph graphPanel = new AccuracyGraph(trainingSize, trainingAccuracies, testAccuracies);
            frame.add(graphPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // Pause gia 3 deuterolepta
            try {
                Thread.sleep(3000); // 20000 milliseconds = 20 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Precision , Recall , F1
            JFrame frameMetrics = new JFrame("Precision, Recall, and F1 Score Graph");
            frameMetrics.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            PreRecF1Graph prfGraphPanel = new PreRecF1Graph(trainingSize, trainingPrecisions, trainingRecalls,
                    trainingF1Scores);
            frameMetrics.add(prfGraphPanel);
            frameMetrics.pack();
            frameMetrics.setLocationRelativeTo(null);
            frameMetrics.setVisible(true);

             for(double score:trainingPrecisions){
                 System.out.println(score + "Pre\n");
             }

            for(double score:trainingRecalls){
                 System.out.println(score + "Rec\n");
             }

             for(double score:trainingF1Scores){
                 System.out.println(score + "F1\n" );
             }

            // for(double score:trainingAccuracies){
            //    System.out.println(score + "trainingAccuracie\n" );
            // }

            // for(double score:testAccuracies){
            //     System.out.println(score + "testAccuracie\n" );
            // }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
