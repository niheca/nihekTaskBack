package com.example.nihektaskback.persistence.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskPaginatedResponseDTO {

    private int count;
    private Long totalCount;
    private int page;
    private int totalPages;
    private List<TaskResponseDTO> tasks;

}
