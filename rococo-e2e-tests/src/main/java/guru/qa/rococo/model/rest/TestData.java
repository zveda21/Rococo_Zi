package guru.qa.rococo.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;

public record TestData(
        @JsonIgnore @Nonnull String password) {
}