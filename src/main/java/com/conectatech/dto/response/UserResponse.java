package com.conectatech.dto.response;

import com.conectatech.entity.Skill;
import com.conectatech.entity.User;
import com.conectatech.entity.enums.UserRole;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * DTO de resposta com os dados públicos de um usuário.
 */
public record UserResponse(
        UUID id,
        String name,
        String email,
        UserRole role,
        String bio,
        String meetingLink,
        Set<SkillResponse> skills,
        LocalDateTime createdAt
) {
    public static UserResponse from(User user) {
        Set<SkillResponse> skillResponses = user.getSkills()
                .stream()
                .map(SkillResponse::from)
                .collect(Collectors.toSet());

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getBio(),
                user.getMeetingLink(),
                skillResponses,
                user.getCreatedAt()
        );
    }
}
