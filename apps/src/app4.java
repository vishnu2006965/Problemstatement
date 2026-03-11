import java.util.*;

public class app4 {

    // n-gram index
    private HashMap<String, Set<String>> ngramIndex = new HashMap<>();

    // document storage
    private HashMap<String, List<String>> documentNgrams = new HashMap<>();

    private int N = 5; // size of n-gram

    // Extract n-grams from text
    private List<String> extractNgrams(String text) {

        String[] words = text.toLowerCase().split("\\s+");

        List<String> ngrams = new ArrayList<>();

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }

            ngrams.add(gram.toString().trim());
        }

        return ngrams;
    }

    // Add document to database
    public void addDocument(String docId, String text) {

        List<String> ngrams = extractNgrams(text);

        documentNgrams.put(docId, ngrams);

        for (String gram : ngrams) {

            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(docId);
        }
    }

    // Analyze new document
    public void analyzeDocument(String docId, String text) {

        List<String> newNgrams = extractNgrams(text);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : newNgrams) {

            if (ngramIndex.containsKey(gram)) {

                for (String doc : ngramIndex.get(gram)) {

                    matchCount.put(doc,
                            matchCount.getOrDefault(doc, 0) + 1);
                }
            }
        }

        System.out.println("Extracted " + newNgrams.size() + " n-grams");

        for (Map.Entry<String, Integer> entry : matchCount.entrySet()) {

            String doc = entry.getKey();
            int matches = entry.getValue();

            double similarity =
                    (matches * 100.0) / newNgrams.size();

            System.out.println("Found " + matches +
                    " matching n-grams with " + doc);

            System.out.printf("Similarity: %.2f%%\n", similarity);

            if (similarity > 60) {
                System.out.println("PLAGIARISM DETECTED\n");
            } else if (similarity > 10) {
                System.out.println("Suspicious\n");
            } else {
                System.out.println("Low similarity\n");
            }
        }
    }

    public static void main(String[] args) {

        app4 detector = new app4();

        String doc1 = "Artificial intelligence is transforming modern technology and education systems worldwide";
        String doc2 = "Artificial intelligence is transforming modern technology and healthcare industries globally";

        detector.addDocument("essay_089.txt", doc1);
        detector.addDocument("essay_092.txt", doc2);

        String newEssay = "Artificial intelligence is transforming modern technology and education worldwide";

        detector.analyzeDocument("essay_123.txt", newEssay);
    }
}