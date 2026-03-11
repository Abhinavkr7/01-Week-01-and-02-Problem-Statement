import java.util.*;

public class problem4 {

    // n-gram → set of document IDs containing that n-gram
    private Map<String, Set<String>> ngramIndex;

    private int nGramSize;

    public problem4(int n) {
        this.nGramSize = n;
        this.ngramIndex = new HashMap<>();
    }

    // Add document to database
    public void addDocument(String docId, String text) {

        List<String> ngrams = generateNGrams(text);

        for (String gram : ngrams) {

            ngramIndex
                    .computeIfAbsent(gram, k -> new HashSet<>())
                    .add(docId);
        }
    }

    // Analyze new document for plagiarism
    public void analyzeDocument(String docId, String text) {

        List<String> ngrams = generateNGrams(text);

        Map<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {

            if (ngramIndex.containsKey(gram)) {

                for (String existingDoc : ngramIndex.get(gram)) {

                    matchCount.put(
                            existingDoc,
                            matchCount.getOrDefault(existingDoc, 0) + 1
                    );
                }
            }
        }

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for (Map.Entry<String, Integer> entry : matchCount.entrySet()) {

            String existingDoc = entry.getKey();
            int matches = entry.getValue();

            double similarity =
                    (matches * 100.0) / ngrams.size();

            System.out.println(
                    "Found " + matches +
                            " matching n-grams with \"" +
                            existingDoc + "\""
            );

            System.out.println(
                    "Similarity: " +
                            String.format("%.2f", similarity) + "%"
            );

            if (similarity > 60) {
                System.out.println("⚠ PLAGIARISM DETECTED");
            } else if (similarity > 10) {
                System.out.println("⚠ Suspicious similarity");
            }

            System.out.println();
        }

        // Add analyzed document to index
        addDocument(docId, text);
    }

    // Generate n-grams from text
    private List<String> generateNGrams(String text) {

        List<String> result = new ArrayList<>();

        String[] words = text
                .toLowerCase()
                .replaceAll("[^a-z ]", "")
                .split("\\s+");

        for (int i = 0; i <= words.length - nGramSize; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < nGramSize; j++) {
                gram.append(words[i + j]).append(" ");
            }

            result.add(gram.toString().trim());
        }

        return result;
    }

    public static void main(String[] args) {

        problem4 detector = new problem4(5);

        String essay1 =
                "Artificial intelligence is transforming the world of technology and research.";

        String essay2 =
                "Artificial intelligence is transforming the world of technology and research with new innovations.";

        String essay3 =
                "Machine learning and deep learning are important fields in computer science.";

        detector.addDocument("essay_089.txt", essay1);
        detector.addDocument("essay_092.txt", essay3);

        detector.analyzeDocument("essay_123.txt", essay2);
    }
}
