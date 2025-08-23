package app.tasks.core;

import app.tasks.model.Task;

import java.util.Optional;

public interface TaskSource {
    Task randomByRuleId(Optional<String> ruleIdOpt);
}