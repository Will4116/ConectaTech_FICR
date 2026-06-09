package com.conectatech.dto.response;

import com.conectatech.entity.Skill;

import java.util.UUID;

/**
 * DTO de resposta com dados de uma skill.
 */
public record SkillResponse(
        UUID id,
        String name
) {
    public static SkillResponse from(Skill skill) {
        return new SkillResponse(skill.getId(), skill.getName());
    }
}
