package com.example.nihektaskback.persistence.dto;

import com.example.nihektaskback.commons.constants.ImportanceEnum;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequestDTO {

    @NotBlank
    @Size(max = 30 , min = 5 , message = "The title must contain 5-30 characters")
    private String title;

    @NotBlank
    @Size(min = 10 , message = "The title must contain 5-30 characters")
    private String description;

    @NotNull
    private Long created;

    @NotNull
    private Long expiration;

    @NotNull(message = "Importance is required. Available values: LOW, MEDIUM, HIGH")
    private ImportanceEnum importance;

    @NotNull
    private Boolean completed;

}
