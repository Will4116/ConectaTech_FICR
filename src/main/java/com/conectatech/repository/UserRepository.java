package com.conectatech.repository;

import com.conectatech.entity.User;
import com.conectatech.entity.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    /**
     * Busca todos os mentores. Se skillId for informado, filtra apenas os
     * mentores que possuem aquela skill associada.
     */
    @Query("""
        SELECT DISTINCT u FROM User u
        LEFT JOIN u.skills s
        WHERE u.role = 'MENTOR'
          AND (:skillId IS NULL OR s.id = :skillId)
        ORDER BY u.name ASC
        """)
    List<User> findMentorsByOptionalSkill(@Param("skillId") UUID skillId);

    List<User> findAllByRole(UserRole role);
}
