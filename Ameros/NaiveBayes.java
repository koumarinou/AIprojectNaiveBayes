import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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


    // Metatropi tou keimenou se 1,0 an yparxoun h den yparxoun antistoixa oi lexeis
    public int[] createFeatureVector(String text, List<String> sortedWords) {
        int[] features = new int[sortedWords.size()];

        // Metatrepei yo keimeno se lexeis kai peza
        Set<String> wordsInText = new HashSet<>(Arrays.asList(text.toLowerCase().split("\\s+")));

        for (int i = 0; i < sortedWords.size(); i++) {
            features[i] = wordsInText.contains(sortedWords.get(i)) ? 1 : 0;
        }

        return features;
    }

    // Methodos gia tin ekpaideusi
    public void train(List<String> positiveTexts, List<String> negativeTexts) {
        // Ypologismos suxnotitas thetikon kai arntikon lexewn
        Map<String, Integer> freqPos = createWordFrequencyMap(positiveTexts);
        Map<String, Integer> freqNeg = createWordFrequencyMap(negativeTexts);

        // Ypologismos synolikon lexewn gia thetika kai anritika
        double totalPosWords = freqPos.values().stream().mapToInt(Number::intValue).sum();
        double totalNegWords = freqNeg.values().stream().mapToInt(Number::intValue).sum();

        // Ypologismos pithanotiton gia kathe lexi se kathe katigoria
        positiveProbabilities = new HashMap<>();
        negativeProbabilities = new HashMap<>();
        // Prosthetoume oles tis lexeis apo ta thetika keimena
        Set<String> vocabulary = new HashSet<>(freqPos.keySet()); 
        // Prosthetoume oles tis lexeis apo ta arnitika keimena
        vocabulary.addAll(freqNeg.keySet());

        for (String word : vocabulary) {
            positiveProbabilities.put(word, Math.log((freqPos.getOrDefault(word, 0) + 1) / (totalPosWords + vocabulary.size())));
            negativeProbabilities.put(word, Math.log((freqNeg.getOrDefault(word, 0) + 1) / (totalNegWords + vocabulary.size())));
        }

        // Ypologismos pithanotiton gia thetika kai arnitika keimena 
        probPositive = Math.log((double) positiveTexts.size() / (positiveTexts.size() + negativeTexts.size()));
        probNegative = Math.log((double) negativeTexts.size() / (positiveTexts.size() + negativeTexts.size()));
    }
}