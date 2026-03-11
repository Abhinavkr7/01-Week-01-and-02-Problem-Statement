
import java.util.*;

class TrieNode {

    Map<Character, TrieNode> children = new HashMap<>();
    boolean isWord = false;
}

public class problem7 {

    private TrieNode root = new TrieNode();

    // query → frequency
    private Map<String, Integer> frequencyMap = new HashMap<>();


    // Insert query into trie
    public void insertQuery(String query) {

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());

            node = node.children.get(c);
        }

        node.isWord = true;

        frequencyMap.put(query,
                frequencyMap.getOrDefault(query, 0) + 1);
    }


    // Get suggestions for prefix
    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }

            node = node.children.get(c);
        }

        List<String> candidates = new ArrayList<>();

        dfs(node, prefix, candidates);

        PriorityQueue<String> heap =
                new PriorityQueue<>(
                        (a, b) -> frequencyMap.get(a) - frequencyMap.get(b)
                );

        for (String query : candidates) {

            heap.offer(query);

            if (heap.size() > 10) {
                heap.poll();
            }
        }

        List<String> result = new ArrayList<>(heap);

        result.sort((a, b) ->
                frequencyMap.get(b) - frequencyMap.get(a));

        return result;
    }


    // DFS to collect queries
    private void dfs(TrieNode node, String prefix, List<String> result) {

        if (node.isWord) {
            result.add(prefix);
        }

        for (char c : node.children.keySet()) {

            dfs(node.children.get(c),
                    prefix + c,
                    result);
        }
    }


    // Update frequency when new search happens
    public void updateFrequency(String query) {

        insertQuery(query);
    }


    public static void main(String[] args) {

        problem7 system = new problem7();

        system.insertQuery("java tutorial");
        system.insertQuery("javascript");
        system.insertQuery("java download");
        system.insertQuery("java tutorial");
        system.insertQuery("java tutorial");
        system.insertQuery("java 21 features");

        System.out.println("Suggestions for 'jav':");

        List<String> suggestions = system.search("jav");

        int rank = 1;

        for (String s : suggestions) {

            System.out.println(
                    rank + ". " + s +
                            " (" + system.frequencyMap.get(s) + " searches)"
            );

            rank++;
        }

        // simulate trending query
        system.updateFrequency("java 21 features");

        System.out.println("\nUpdated frequency of 'java 21 features': "
                + system.frequencyMap.get("java 21 features"));
    }
}

