package com.project.web.repository;

import com.project.web.model.Idea;
import com.project.web.model.Reaction;
import com.project.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    @Query(value = "SELECT * FROM reactions WHERE idea_id =?1", nativeQuery = true)
    List<Reaction> findByIdeaId(Long ideaId);
    Optional<Reaction> findByUserId(User userId);
    @Query(value = "SELECT * FROM reactions WHERE idea_id =?1 and user_id =?2", nativeQuery = true)
    Reaction findByIdeaIdAndUserId(Idea ideaId, User userid);
    @Query(value = "SELECT * FROM reactions WHERE idea_id =?1", nativeQuery = true)
    Boolean existsByIdeaId(Idea ideaId);
}
