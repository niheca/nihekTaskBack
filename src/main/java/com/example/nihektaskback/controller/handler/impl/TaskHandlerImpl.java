package com.example.nihektaskback.controller.handler.impl;

import com.example.nihektaskback.controller.handler.TaskHandler;
import com.example.nihektaskback.persistence.dto.MessageDTO;
import com.example.nihektaskback.persistence.dto.TaskPaginatedResponseDTO;
import com.example.nihektaskback.persistence.dto.TaskRequestDTO;
import com.example.nihektaskback.persistence.dto.TaskResponseDTO;
import com.example.nihektaskback.service.TaskService;
import com.mongodb.internal.connection.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class TaskHandlerImpl implements TaskHandler {

    private final TaskService taskService;

    public TaskHandlerImpl(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public Mono<ServerResponse> createTask(ServerRequest request) {

        return request.bodyToMono(TaskRequestDTO.class)
                .flatMap(task -> this.taskService.createTask(task))
                .flatMap(task -> {

                    log.info("Task succesfully created: {}", task);

                    return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(task);

                })
                .onErrorResume( ResponseStatusException.class,error -> {

                    log.error("Error saving task: {}", error.getMessage());

                    MessageDTO bodyError = MessageDTO.builder().message(error.getMessage()).build();

                    return ServerResponse
                            .status(error.getStatusCode())
                            .bodyValue(bodyError);
                    }
                );
    }

    @Override
    public Mono<ServerResponse> getTask(ServerRequest request) {

        int pageNumber = Integer.parseInt(request.queryParam("page").orElse("1"));
        int pageSize = Integer.parseInt(request.queryParam("size").orElse("5"));

        return this.taskService.getTasks(pageNumber,pageSize)
                .flatMap((tasks) -> {

                    log.info("Tasks were successfully retrieved:");

                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(tasks);

                })
                .onErrorResume( ResponseStatusException.class,error -> {

                    log.error("Error getting tasks: {}", error.getMessage());

                    MessageDTO bodyError = MessageDTO.builder().message(error.getMessage()).build();

                    return ServerResponse
                            .status(error.getStatusCode())
                            .bodyValue(bodyError);
                    }
                );
    }

    @Override
    public Mono<ServerResponse> getTaskByID(ServerRequest request) {

        String id = request.pathVariable("id");

        return this.taskService.getTaskByID(id)
                .flatMap((task) -> {

                    log.info("Task with ID {} found",id);

                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(task);

                })
                .onErrorResume(ResponseStatusException.class,error -> {

                    log.error(error.getMessage());

                    MessageDTO bodyError = MessageDTO.builder().message(error.getMessage()).build();

                    return ServerResponse
                            .status(error.getStatusCode())
                            .bodyValue(bodyError);
                    }
                );
    }

    @Override
    public Mono<ServerResponse> updateTask(ServerRequest request) {

        String id = request.pathVariable("id");

        return request.bodyToMono(TaskRequestDTO.class)
                .flatMap(dto -> this.taskService.updateTask(id,dto))
                .flatMap(task -> {

                    log.info("Task succesfully updated: {}", task);

                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(task);
                })
                .onErrorResume(ResponseStatusException.class ,error -> {

                    log.error(error.getMessage());

                    MessageDTO bodyError = MessageDTO.builder().message(error.getMessage()).build();

                    return ServerResponse.status(error.getStatusCode()).bodyValue(bodyError);

                });
    }
    @Override
    public Mono<ServerResponse> deleteTask(ServerRequest request) {

        String id = request.pathVariable("id");

        return this.taskService.deleteTask(id)
                .flatMap(task -> {
                    log.info("Task with ID {} successfully deleted", id);
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(task);
                })
                .onErrorResume(ResponseStatusException.class, error -> {

                    log.error("Error deleting task: {}", error.getMessage());

                    MessageDTO bodyError = MessageDTO.builder()
                            .message(error.getMessage())
                            .build();

                    return ServerResponse.status(error.getStatusCode())
                            .bodyValue(bodyError);

                });
    }

}
