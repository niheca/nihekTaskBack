package com.example.nihektaskback.service.impl;

import com.example.nihektaskback.persistence.dto.MessageDTO;
import com.example.nihektaskback.persistence.dto.TaskPaginatedResponseDTO;
import com.example.nihektaskback.persistence.dto.TaskRequestDTO;
import com.example.nihektaskback.persistence.dto.TaskResponseDTO;
import com.example.nihektaskback.persistence.entities.TaskEntity;
import com.example.nihektaskback.persistence.repositories.TaskRepository;
import com.example.nihektaskback.service.TaskService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final Validator validator;

    public TaskServiceImpl(TaskRepository taskRepository, Validator validator) {
        this.taskRepository = taskRepository;
        this.validator = validator;
    }

    @Override
    public Mono<TaskResponseDTO> createTask(@Valid TaskRequestDTO taskRequest) {

        return Mono.just(taskRequest)
                .flatMap(this::validateCreationRequest)
                .flatMap(this::convertDtoToEntity)
                .flatMap(entity -> this.taskRepository.save(entity))
                .flatMap(this::convertEntityToDto);
    }

    @Override
    public Mono<TaskPaginatedResponseDTO> getTasks(int pageNumber, int pageSize) {

        int offset = (pageNumber - 1) * pageSize;

        Mono<Long> totalCount = this.taskRepository.count();

        Flux<TaskResponseDTO> tasksResponse = this.taskRepository.findAll()
                .skip(offset)
                .take(pageSize)
                .flatMap(this::convertEntityToDto);

        return Mono.zip(tasksResponse.collectList(), totalCount)
                .flatMap(tuple -> {
                    List<TaskResponseDTO> tasks = tuple.getT1();
                    Long total = tuple.getT2();

                    int totalPages = (int) Math.ceil((double) total / pageSize);

                    if(pageNumber > totalPages){
                        String error = "Number of pages exceeds total pages, the total number of pages is " + totalPages;
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error));
                    }

                    if(pageSize > total){
                        String error = "Number of task exceeds total task in bd , the total number of tasks is " + total;
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, error));
                    }

                    return Mono.just(TaskPaginatedResponseDTO.builder()
                            .count(pageSize)
                            .totalCount(total)
                            .page(pageNumber)
                            .totalPages(totalPages)
                            .tasks(tasks)
                            .build());
                });

        //Forma que habia pensado (Mal porque rompe reactividad con .block)
        /*return Mono.just(TaskPaginatedResponseDTO.builder()
                .count(pageSize)
                .totalCount(totalCount.block())
                .page(pageNumber)
                .totalPages( (int) Math.ceil((double) totalCount.block() / pageSize))
                .task(tasksResponse.collectList().block())
                .build());*/

    }

    @Override
    public Mono<TaskResponseDTO> getTaskByID(String id) {

        String error = "Task with id " + id + " not found";

        return this.taskRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,error)))
                .flatMap(this::convertEntityToDto);

    }

    @Override
    public Mono<TaskResponseDTO> updateTask(String id, TaskRequestDTO taskRequest) {

        String error = "Task with id " + id + " not found";

        return this.taskRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,error))
                )
                .flatMap(task -> applyPartialUpdates(task,taskRequest))
                .flatMap(taskUpdated -> this.taskRepository.save(taskUpdated))
                .flatMap(taskUpdated -> {
                   return convertEntityToDto(taskUpdated);
                });
    }

    @Override
    public Mono<MessageDTO> deleteTask(String id) {

        String error = "Task with id " + id + " not found";

        return this.taskRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,error)))
                .flatMap(this.taskRepository::delete)
                .thenReturn(
                    MessageDTO.builder()
                    .message("Task with id " + id + " successfully deleted")
                    .build()
                );

    }

    private <T> Mono<T> validateCreationRequest(T requestDto ) {

        Set<ConstraintViolation<T>> constraintViolations = validator.validate(requestDto);

        if(!constraintViolations.isEmpty()) {

            Map<String,String> errorsMap = constraintViolations.stream().
                collect(Collectors.toMap(
                        error -> error.getPropertyPath().toString() ,
                        ConstraintViolation::getMessage
                ));

            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, errorsMap.toString()));
        }

        return Mono.just(requestDto);

    }
    private Mono<TaskResponseDTO> convertEntityToDto(TaskEntity entity) {
        return Mono.just(TaskResponseDTO
                .builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .created(entity.getCreated())
                .expiration(entity.getExpiration())
                .importance(entity.getImportance())
                .completed(entity.getCompleted())
                .build());
    }
    private Mono<TaskEntity> convertDtoToEntity(TaskRequestDTO dto) {
        return Mono.just(TaskEntity.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .created(dto.getCreated())
                .expiration(dto.getExpiration())
                .importance(dto.getImportance())
                .completed(dto.getCompleted())
                .build());
    }
    private Mono<TaskEntity> applyPartialUpdates(TaskEntity existingEntity, TaskRequestDTO dtoUpdates) {

        return Mono.just(TaskEntity.builder()
                .id(existingEntity.getId())
                .title(dtoUpdates.getTitle() == null ? existingEntity.getTitle() : dtoUpdates.getTitle())
                .description(dtoUpdates.getDescription() == null ? existingEntity.getDescription() : dtoUpdates.getDescription())
                .created(dtoUpdates.getCreated() == null ? existingEntity.getCreated() : dtoUpdates.getCreated())
                .expiration(dtoUpdates.getExpiration() == null ? existingEntity.getExpiration() : dtoUpdates.getExpiration())
                .importance(dtoUpdates.getImportance() == null ? existingEntity.getImportance() : dtoUpdates.getImportance())
                .completed(dtoUpdates.getCompleted() == null ? existingEntity.getCompleted() : dtoUpdates.getCompleted())
                .build());

    }
}
