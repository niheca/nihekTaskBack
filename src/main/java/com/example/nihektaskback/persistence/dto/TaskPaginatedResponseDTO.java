package com.example.nihektaskback.persistence.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskPaginatedResponseDTO {

    private Long count;
    private Long pages;
    private List<TaskResponseDTO> tasks;

}
