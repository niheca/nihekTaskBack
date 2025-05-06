package com.example.nihektaskback.persistence.dto;

import com.example.nihektaskback.commons.constants.ImportanceEnum;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TaskResponseDTO {

    private String id;

    private String title;

    private String description;

    private Long created;

    private Long expiration;

    private ImportanceEnum importance;

    private Boolean completed;

}
