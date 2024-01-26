import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NaiveBayes {

    private Map<String, Double> positiveProbabilities;
    private Map<String, Double> negativeProbabilities;
    private double probPositive;
    private double probNegative;
    
    // Constructor
    public NaiveBayes() {

        positiveProbabilities = new HashMap<>();
        negativeProbabilities = new HashMap<>();
        probPositive = 0.0;
        probNegative = 0.0;
        
    }


    // Fortonei ta keimena apo ti diadromi
    public static List<String> loadTexts(String directoryPath) throws IOException {
        // Dimiourgei lista gia ta keimena pou tha fortothoun
        List<String> texts = new ArrayList<>(); 
        // Dimiourgei antikeimeno file 
        File dir = new File(directoryPath);
        // Anakta arxeia pou vriskonte sto fakelo kai tha apothikevei se ena pinaka
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            // Diatrexei kathe arxeio sto fakelo
            for (File child : directoryListing) {
                // Metatrepei to periexomeno se sumboloseira 
                String content = new String(Files.readAllBytes(child.toPath()));
                // To prosthetei sto list text
                texts.add(content);
            }
        }
        // Ola ta keimena thetika h arnitika analogos me to path 
        return texts; 
    }
}