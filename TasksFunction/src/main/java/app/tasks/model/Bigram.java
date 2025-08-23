package app.tasks.model;

import io.micronaut.serde.annotation.Serdeable;

/** Пара индексов (оригинальные позиции токенов), которые сохранены как биграмма. */
@Serdeable
public record Bigram(int leftIndex, int rightIndex) { }