package com.conectatech.service;

import com.conectatech.dto.request.CreateMentorshipRequest;
import com.conectatech.dto.request.UpdateMentorshipStatusRequest;
import com.conectatech.dto.response.MentorshipResponse;
import com.conectatech.entity.MentorshipRequest;
import com.conectatech.entity.User;
import com.conectatech.entity.enums.MentorshipStatus;
import com.conectatech.entity.enums.UserRole;
import com.conectatech.exception.BusinessException;
import com.conectatech.exception.ResourceNotFoundException;
import com.conectatech.repository.MentorshipRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class MentorshipService {

    private final MentorshipRequestRepository mentorshipRequestRepository;
    private final UserService userService;

    public MentorshipService(MentorshipRequestRepository mentorshipRequestRepository,
                              UserService userService) {
        this.mentorshipRequestRepository = mentorshipRequestRepository;
        this.userService = userService;
    }

    /**
     * Cria uma nova solicitação de mentoria.
     *
     * <p>Regras de negócio:
     * <ul>
     *   <li>O solicitante (mentee) deve ter o role MENTEE.</li>
     *   <li>O receptor (mentor) deve ter o role MENTOR.</li>
     *   <li>Um mentorado não pode solicitar mentoria para si mesmo.</li>
     *   <li>Não pode haver uma solicitação PENDING ou ACCEPTED já existente entre o mesmo par.</li>
     * </ul>
     */
    @Transactional
    public MentorshipResponse createMentorship(CreateMentorshipRequest request) {
        User mentee = userService.findUserOrThrow(request.menteeId());
        User mentor = userService.findUserOrThrow(request.mentorId());

        validateMenteeRole(mentee);
        validateMentorRole(mentor);
        validateNotSelfRequest(mentee, mentor);
        validateNoDuplicateRequest(mentee, mentor);

        MentorshipRequest mentorship = new MentorshipRequest(mentee, mentor, request.requestMessage());
        MentorshipRequest saved = mentorshipRequestRepository.save(mentorship);

        return MentorshipResponse.from(saved);
    }

    /**
     * Atualiza o status de uma solicitação de mentoria.
     *
     * <p>Regras de negócio:
     * <ul>
     *   <li>Apenas o mentor da solicitação pode aceitar ou recusar.</li>
     *   <li>O mentor não pode voltar atrás: ACCEPTED ou REJECTED são estados finais.</li>
     *   <li>Não é permitido definir o status como PENDING manualmente.</li>
     * </ul>
     */
    @Transactional
    public MentorshipResponse updateStatus(UUID mentorshipId, UpdateMentorshipStatusRequest request) {
        MentorshipRequest mentorship = findMentorshipOrThrow(mentorshipId);

        validateMentorIsOwner(mentorship, request.mentorId());
        validateStatusTransition(mentorship.getStatus(), request.status());

        mentorship.setStatus(request.status());
        MentorshipRequest updated = mentorshipRequestRepository.save(mentorship);

        return MentorshipResponse.from(updated);
    }

    // ── Validações ────────────────────────────────────────────────────────────

    private void validateMenteeRole(User user) {
        if (user.getRole() != UserRole.MENTEE) {
            throw new BusinessException(
                "O solicitante com id '" + user.getId() + "' não é um MENTEE. " +
                "Apenas mentorados podem solicitar mentorias."
            );
        }
    }

    private void validateMentorRole(User user) {
        if (user.getRole() != UserRole.MENTOR) {
            throw new BusinessException(
                "O receptor com id '" + user.getId() + "' não é um MENTOR. " +
                "Solicitações só podem ser enviadas a mentores."
            );
        }
    }

    private void validateNotSelfRequest(User mentee, User mentor) {
        if (mentee.getId().equals(mentor.getId())) {
            throw new BusinessException("Um usuário não pode solicitar mentoria para si mesmo.");
        }
    }

    private void validateNoDuplicateRequest(User mentee, User mentor) {
        boolean alreadyExists = mentorshipRequestRepository
                .existsActiveBetween(mentee.getId(), mentor.getId());

        if (alreadyExists) {
            throw new BusinessException(
                "Já existe uma solicitação de mentoria ativa (PENDING ou ACCEPTED) " +
                "entre este mentorado e mentor."
            );
        }
    }

    private void validateMentorIsOwner(MentorshipRequest mentorship, UUID requestingMentorId) {
        if (!mentorship.getMentor().getId().equals(requestingMentorId)) {
            throw new BusinessException(
                "Apenas o mentor responsável pela solicitação pode alterar seu status."
            );
        }
    }

    private void validateStatusTransition(MentorshipStatus current, MentorshipStatus next) {
        if (current != MentorshipStatus.PENDING) {
            throw new BusinessException(
                "A solicitação já foi processada com o status '" + current + "' e não pode ser alterada."
            );
        }
        if (next == MentorshipStatus.PENDING) {
            throw new BusinessException(
                "Não é permitido definir o status manualmente como PENDING."
            );
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private MentorshipRequest findMentorshipOrThrow(UUID id) {
        return mentorshipRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Solicitação de mentoria não encontrada com id: " + id));
    }
}
