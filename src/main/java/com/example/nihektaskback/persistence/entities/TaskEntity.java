package com.example.nihektaskback.persistence.entities;

import com.example.nihektaskback.commons.constants.ImportanceEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "tasks")
public class TaskEntity {

    @Id
    private String id;

    private String title;

    private String description;

    private Long created;

    private Long expiration;

    private ImportanceEnum importance;

    private Boolean completed;

}
