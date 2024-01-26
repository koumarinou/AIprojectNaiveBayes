import java.util.Map;
import java.util.stream.Collectors;
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


    // Metraei ti sixnotita kathe lexis
    public Map<String, Integer> createWordFrequencyMap(List<String> texts) {
        // Dimiourgia enos Map opou metraei tin suxnotita kathes lexhs sto keimeno 
        Map<String, Integer> wordFreq = new HashMap<>(); 
        for (String text : texts) {
            // Xwrizei to keimeno se lexeis
            String[] words = text.toLowerCase().split("\\s+"); 
            for (String word : words) {
                wordFreq.put(word, wordFreq.getOrDefault(word, 0) + 1);
            }
        }
        return wordFreq;
    }

    // Dimiourgei to lexilogio
    public List<String> createVocabulary(Map<String, Integer> wordFreq, int m, int n, int k) {

        // Taxinomei ta entries me vasi tin suxnotita apo tin ypsiloterh sthn xamiloterh kai ta silegei se mia lista
        List<String> sortedWords = wordFreq.entrySet().stream()
                                           .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                                           .map(Map.Entry::getKey)
                                           .collect(Collectors.toList());

        // Afairoume tis n pio suxnes lexeis
        if (n > 0 && sortedWords.size() > n) {
            // Oi n tha einai oi protes lexeis sth lista 
            sortedWords = sortedWords.subList(n, sortedWords.size());
        }

        // Afairoume tis k pio spanies lexeis
        if (k > 0 && sortedWords.size() > k) {
            // Oi k oi teleftaies lexeis sth lista 
            sortedWords = sortedWords.subList(0, sortedWords.size() - k);
        }

        // Epilegoume tis m suxnoteres lexeis apo tin kaionourgia lista 
        return sortedWords.stream()
                          .limit(m)
                          .collect(Collectors.toList());
    }
}