package app.tasks.model;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

/** Ответ API: исходный текст, токены, перемешанные токены, метаданные. */
@Serdeable
public record TaskVariant(
        String taskId,
        String ruleId,
        String sourceText,
        List<String> tokens,
        List<String> shuffledTokens,
        double p,
        long seed,
        List<Bigram> preservedBigrams
) { }