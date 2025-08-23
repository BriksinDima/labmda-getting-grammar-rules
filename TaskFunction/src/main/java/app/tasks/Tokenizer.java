package app.tasks;

import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class Tokenizer {

    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "(?:[A-Za-z]+(?:'[A-Za-z]+)*)" +
                    "|\\d+(?:[.,]\\d+)*" +
                    "|\\.\\.\\." +
                    "|…" +
                    "|[\\p{Punct}]"
    );

    public List<String> tokenize(String text) {
        Matcher m = TOKEN_PATTERN.matcher(text);
        List<String> tokens = new ArrayList<>();
        while (m.find()) {
            tokens.add(m.group());
        }
        return tokens;
    }
}