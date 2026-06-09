package com.conectatech.controller;

import com.conectatech.dto.request.CreateMentorshipRequest;
import com.conectatech.dto.request.UpdateMentorshipStatusRequest;
import com.conectatech.dto.response.MentorshipResponse;
import com.conectatech.service.MentorshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/mentorships")
@Tag(name = "Mentorias", description = "Gerenciamento do fluxo de solicitações de mentoria")
public class MentorshipController {

    private final MentorshipService mentorshipService;

    public MentorshipController(MentorshipService mentorshipService) {
        this.mentorshipService = mentorshipService;
    }

    @PostMapping
    @Operation(
        summary = "Solicitar mentoria",
        description = "Um MENTEE envia uma solicitação de mentoria a um MENTOR. " +
                      "Validações: solicitante deve ser MENTEE e receptor deve ser MENTOR.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Solicitação criada com status PENDING"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Mentorado ou mentor não encontrado"),
            @ApiResponse(responseCode = "422", description = "Violação de regra de negócio " +
                         "(role incorreto, duplicata, auto-mentoria)")
        }
    )
    public ResponseEntity<MentorshipResponse> createMentorship(
            @Valid @RequestBody CreateMentorshipRequest request) {

        MentorshipResponse response = mentorshipService.createMentorship(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/status")
    @Operation(
        summary = "Atualizar status da mentoria",
        description = "O MENTOR aceita (ACCEPTED) ou recusa (REJECTED) uma solicitação pendente. " +
                      "Apenas o mentor responsável pode executar esta ação. " +
                      "Status finais (ACCEPTED/REJECTED) não podem ser alterados novamente.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada"),
            @ApiResponse(responseCode = "422", description = "Violação de regra de negócio " +
                         "(não é o mentor responsável, status já finalizado)")
        }
    )
    public ResponseEntity<MentorshipResponse> updateStatus(
            @Parameter(description = "ID da solicitação de mentoria")
            @PathVariable UUID id,
            @Valid @RequestBody UpdateMentorshipStatusRequest request) {

        MentorshipResponse response = mentorshipService.updateStatus(id, request);
        return ResponseEntity.ok(response);
    }
}
