package com.conectatech.dto.request;

import com.conectatech.entity.enums.MentorshipStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO para o mentor atualizar o status de uma solicitação.
 * O mentorId é necessário para validar que quem está atualizando é o mentor da solicitação.
 */
public record UpdateMentorshipStatusRequest(

        @NotNull(message = "O ID do mentor é obrigatório para autorização")
        UUID mentorId,

        @NotNull(message = "O novo status é obrigatório")
        MentorshipStatus status
) {}
