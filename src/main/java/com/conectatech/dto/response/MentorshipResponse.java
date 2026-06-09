package com.conectatech.dto.response;

import com.conectatech.entity.MentorshipRequest;
import com.conectatech.entity.enums.MentorshipStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de resposta com os dados de uma solicitação de mentoria.
 */
public record MentorshipResponse(
        UUID id,
        UUID menteeId,
        String menteeName,
        UUID mentorId,
        String mentorName,
        MentorshipStatus status,
        String requestMessage,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static MentorshipResponse from(MentorshipRequest request) {
        return new MentorshipResponse(
                request.getId(),
                request.getMentee().getId(),
                request.getMentee().getName(),
                request.getMentor().getId(),
                request.getMentor().getName(),
                request.getStatus(),
                request.getRequestMessage(),
                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }
}
