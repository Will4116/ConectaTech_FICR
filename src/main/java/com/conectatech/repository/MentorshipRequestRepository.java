package com.conectatech.repository;

import com.conectatech.entity.MentorshipRequest;
import com.conectatech.entity.enums.MentorshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MentorshipRequestRepository extends JpaRepository<MentorshipRequest, UUID> {

    /**
     * Verifica se já existe uma solicitação PENDENTE ou ACEITA entre
     * o mesmo mentorado e mentor, evitando duplicatas.
     */
    @Query("""
        SELECT COUNT(mr) > 0 FROM MentorshipRequest mr
        WHERE mr.mentee.id = :menteeId
          AND mr.mentor.id = :mentorId
          AND mr.status IN ('PENDING', 'ACCEPTED')
        """)
    boolean existsActiveBetween(@Param("menteeId") UUID menteeId,
                                @Param("mentorId") UUID mentorId);

    List<MentorshipRequest> findByMentorIdOrderByCreatedAtDesc(UUID mentorId);

    List<MentorshipRequest> findByMenteeIdOrderByCreatedAtDesc(UUID menteeId);

    List<MentorshipRequest> findByStatus(MentorshipStatus status);
}
