package com.conectatech.controller;

import com.conectatech.dto.request.CreateUserRequest;
import com.conectatech.dto.response.UserResponse;
import com.conectatech.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários", description = "Gerenciamento de mentores e mentorados")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(
        summary = "Cadastrar usuário",
        description = "Cria um novo usuário como MENTOR ou MENTEE na plataforma",
        responses = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "422", description = "Email já cadastrado")
        }
    )
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/mentors")
    @Operation(
        summary = "Listar mentores",
        description = "Retorna todos os mentores. Filtra por skill quando `skillId` é informado.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de mentores retornada com sucesso")
        }
    )
    public ResponseEntity<List<UserResponse>> getMentors(
            @Parameter(description = "ID da skill para filtrar mentores")
            @RequestParam(required = false) UUID skillId) {

        List<UserResponse> mentors = userService.getMentors(skillId);
        return ResponseEntity.ok(mentors);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar usuário por ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
        }
    )
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }
}
