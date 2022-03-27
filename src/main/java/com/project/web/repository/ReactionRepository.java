package com.project.web.repository;

import com.project.web.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Boolean existsByReactionId(Long id);
    @Query(value = "SELECT * FROM reactions WHERE idea_id =?1", nativeQuery = true)
    List<Reaction> findByIdeaId(Long ideaId);
}
