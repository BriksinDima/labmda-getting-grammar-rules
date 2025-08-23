package app.tasks;

import app.tasks.model.Bigram;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


@Singleton
public class Shuffler {

    public record ShuffleResult(List<String> shuffledTokens, List<Bigram> preservedBigrams) { }

    public ShuffleResult partialShuffle(List<String> tokens, double p, long seed) {
        int n = tokens.size();
        if (n <= 2) {
            return new ShuffleResult(List.copyOf(tokens), List.of());
        }

        Random rnd = new Random(seed);

        List<String> middle = new ArrayList<>(tokens.subList(1, n - 1));

        List<List<String>> chunks = new ArrayList<>();
        List<Bigram> preserved = new ArrayList<>();
        for (int i = 0; i < middle.size();) {
            int globalLeft = i + 1;
            if (i < middle.size() - 1 && rnd.nextDouble() < p) {
                chunks.add(List.of(middle.get(i), middle.get(i + 1)));
                preserved.add(new Bigram(globalLeft, globalLeft + 1));
                i += 2;
            } else {
                chunks.add(List.of(middle.get(i)));
                i += 1;
            }
        }

        Collections.shuffle(chunks, rnd);

        List<String> result = new ArrayList<>(n);
        result.add(tokens.get(0));
        for (List<String> c : chunks) result.addAll(c);
        result.add(tokens.get(n - 1));

        return new ShuffleResult(List.copyOf(result), List.copyOf(preserved));
    }
}