package app.tasks;

import app.tasks.core.TaskSource;
import app.tasks.model.Task;
import app.tasks.model.TaskVariant;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Controller("/tasks")
public class TaskController {

    private final TaskSource taskSource;
    private final Tokenizer tokenizer;
    private final Shuffler shuffler;

    public TaskController(TaskSource taskSource, Tokenizer tokenizer, Shuffler shuffler) {
        this.taskSource = taskSource;
        this.tokenizer = tokenizer;
        this.shuffler = shuffler;
    }

    /**
     * Пример: GET /tasks/next?p=0.35&ruleId=present-simple&seed=42
     */
    @Get("/next{?ruleId,p,seed}")
    public HttpResponse<TaskVariant> next(@Nullable String ruleId,
                                          @Nullable Double p,
                                          @Nullable Long seed) {

        Task task = taskSource.randomByRuleId(Optional.ofNullable(ruleId));
        double prob = clamp(p != null ? p : 0.35, 0.0, 1.0);
        long usedSeed = (seed != null) ? seed : ThreadLocalRandom.current().nextLong();

        List<String> tokens = tokenizer.tokenize(task.sourceText());
        var shuffle = shuffler.partialShuffle(tokens, prob, usedSeed);

        TaskVariant out = new TaskVariant(
                task.id(),
                task.ruleId(),
                task.sourceText(),
                tokens,
                shuffle.shuffledTokens(),
                prob,
                usedSeed,
                shuffle.preservedBigrams()
        );
        return HttpResponse.ok(out);
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}