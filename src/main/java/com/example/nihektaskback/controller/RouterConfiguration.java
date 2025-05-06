package com.example.nihektaskback.controller;

import com.example.nihektaskback.commons.constants.ApiPathConstants;
import com.example.nihektaskback.controller.handler.TaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

    private final TaskHandler taskHandler;

    private final String baseUrl =  ApiPathConstants.API_ROOT + ApiPathConstants.API_VERSION;

    public RouterConfiguration(TaskHandler taskHandler) {
        this.taskHandler = taskHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .POST(  baseUrl + ApiPathConstants.CREATE_TASK , taskHandler::createTask)
                .GET(   baseUrl + ApiPathConstants.GET_TASK , taskHandler::getTask)
                .GET(   baseUrl + ApiPathConstants.GET_TASK + ApiPathConstants.ID , taskHandler::getTaskByID)
                .PATCH( baseUrl + ApiPathConstants.UPDATE_TASK + ApiPathConstants.ID , taskHandler::updateTask)
                .DELETE(baseUrl + ApiPathConstants.DELETE_TASK + ApiPathConstants.ID , taskHandler::deleteTask)
                .build();
    }
}
