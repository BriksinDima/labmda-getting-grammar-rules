package app.tasks.model;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record Task(String id, String ruleId, String sourceText) { }