import java.io.IOException;
import java.util.List;

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

    }  catch (IOException e) {
            e.printStackTrace();
        }
    }

}
