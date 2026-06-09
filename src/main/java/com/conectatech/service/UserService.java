package com.conectatech.service;

import com.conectatech.dto.request.CreateUserRequest;
import com.conectatech.dto.response.UserResponse;
import com.conectatech.entity.Skill;
import com.conectatech.entity.User;
import com.conectatech.entity.enums.UserRole;
import com.conectatech.exception.BusinessException;
import com.conectatech.exception.ResourceNotFoundException;
import com.conectatech.repository.SkillRepository;
import com.conectatech.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    public UserService(UserRepository userRepository, SkillRepository skillRepository) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Já existe um usuário cadastrado com o email: " + request.email());
        }

        // Em produção, a senha seria criptografada com BCryptPasswordEncoder
        User user = new User(
                request.name(),
                request.email(),
                request.password(), // TODO: encode password
                request.role(),
                request.bio(),
                request.meetingLink()
        );

        if (request.skillIds() != null && !request.skillIds().isEmpty()) {
            Set<Skill> skills = resolveSkills(request.skillIds());
            user.setSkills(skills);
        }

        User saved = userRepository.save(user);
        return UserResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getMentors(UUID skillId) {
        return userRepository.findMentorsByOptionalSkill(skillId)
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        User user = findUserOrThrow(id);
        return UserResponse.from(user);
    }

    // ── Métodos de uso interno ────────────────────────────────────────────────

    public User findUserOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com id: " + id));
    }

    private Set<Skill> resolveSkills(Set<UUID> skillIds) {
        Set<Skill> skills = new HashSet<>(skillRepository.findAllById(skillIds));
        if (skills.size() != skillIds.size()) {
            throw new ResourceNotFoundException("Uma ou mais skills informadas não foram encontradas.");
        }
        return skills;
    }
}
