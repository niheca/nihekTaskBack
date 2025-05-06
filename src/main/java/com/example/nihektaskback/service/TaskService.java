package com.example.nihektaskback.service;

import com.example.nihektaskback.persistence.dto.MessageDTO;
import com.example.nihektaskback.persistence.dto.TaskPaginatedResponseDTO;
import com.example.nihektaskback.persistence.dto.TaskRequestDTO;
import com.example.nihektaskback.persistence.dto.TaskResponseDTO;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface TaskService {

    Mono<TaskResponseDTO> createTask(TaskRequestDTO taskRequest);

    Mono<TaskPaginatedResponseDTO> getTasks(int pageNumber, int pageSize);

    Mono<TaskResponseDTO> getTaskByID(String id);

    Mono<TaskResponseDTO> updateTask(String id , TaskRequestDTO taskRequest);

    Mono<MessageDTO> deleteTask(String id);


}
