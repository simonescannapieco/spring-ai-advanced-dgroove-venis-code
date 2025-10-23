package it.venis.ai.spring.demo.model;

import java.util.Objects;
import java.util.UUID;

public record QuestionRequest(UUID id, String username, Question body) {

    public QuestionRequest(String username, Question body) {

        this(UUID.randomUUID(), username, body);

    }

    @Override
    public String username() {

        return Objects.requireNonNullElse(this.username, "default");

    }

}
