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

import java.util.List;
import java.util.UUID;

/**
 * Arquivo: src/main/java/com/conectatech/controller/MentorshipController.java
 *
 * ALTERAÇÕES em relação ao original:
 *   + Novo endpoint GET /api/mentorships?mentorId= para o dashboard do Mentor.
 */
@RestController
@RequestMapping("/api/mentorships")
@Tag(name = "Mentorias", description = "Gerenciamento do fluxo de solicitações de mentoria")
public class MentorshipController {

    private final MentorshipService mentorshipService;

    public MentorshipController(MentorshipService mentorshipService) {
        this.mentorshipService = mentorshipService;
    }

    // ── POST /api/mentorships ─────────────────────────────────────────────────

    @PostMapping
    @Operation(
        summary = "Solicitar mentoria",
        description = "Um MENTEE envia uma solicitação de mentoria a um MENTOR.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Solicitação criada com status PENDING"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Mentorado ou mentor não encontrado"),
            @ApiResponse(responseCode = "422", description = "Violação de regra de negócio")
        }
    )
    public ResponseEntity<MentorshipResponse> createMentorship(
            @Valid @RequestBody CreateMentorshipRequest request) {

        MentorshipResponse response = mentorshipService.createMentorship(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ── GET /api/mentorships?mentorId= ────────────────────────────────────────

    @GetMapping
    @Operation(
        summary = "Listar solicitações de mentoria",
        description = "Retorna as solicitações recebidas por um MENTOR, ordenadas por data decrescente.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetro mentorId ausente ou inválido")
        }
    )
    public ResponseEntity<List<MentorshipResponse>> getMentorshipsByMentor(
            @Parameter(description = "ID do mentor para filtrar as solicitações", required = true)
            @RequestParam UUID mentorId) {

        List<MentorshipResponse> list = mentorshipService.getMentorshipsByMentor(mentorId);
        return ResponseEntity.ok(list);
    }

    // ── PATCH /api/mentorships/{id}/status ────────────────────────────────────

    @PatchMapping("/{id}/status")
    @Operation(
        summary = "Atualizar status da mentoria",
        description = "O MENTOR aceita (ACCEPTED) ou recusa (REJECTED) uma solicitação pendente.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada"),
            @ApiResponse(responseCode = "422", description = "Violação de regra de negócio")
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
