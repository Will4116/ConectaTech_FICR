package com.conectatech.controller;

import com.conectatech.dto.response.SkillResponse;
import com.conectatech.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@Tag(name = "Habilidades", description = "Listagem das habilidades disponíveis na plataforma")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    @Operation(
        summary = "Listar todas as habilidades",
        description = "Retorna todas as skills cadastradas, ordenadas por nome.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de skills retornada com sucesso")
        }
    )
    public ResponseEntity<List<SkillResponse>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }
}
