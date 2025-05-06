package com.example.nihektaskback.controller.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface TaskHandler {

    Mono<ServerResponse> createTask(ServerRequest request);

    Mono<ServerResponse> getTask(ServerRequest request);

    Mono<ServerResponse> getTaskByID(ServerRequest request);

    Mono<ServerResponse> updateTask(ServerRequest request);

    Mono<ServerResponse> deleteTask(ServerRequest request);

}
