package com.conectatech.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * DTO para solicitar uma mentoria.
 */
public record CreateMentorshipRequest(

        @NotNull(message = "O ID do mentorado é obrigatório")
        UUID menteeId,

        @NotNull(message = "O ID do mentor é obrigatório")
        UUID mentorId,

        @Size(max = 1000, message = "A mensagem pode ter no máximo 1000 caracteres")
        String requestMessage
) {}
