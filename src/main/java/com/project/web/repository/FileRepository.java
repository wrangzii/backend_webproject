package com.project.web.repository;

import com.project.web.model.File;
import com.project.web.model.Idea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository<findByIdeaId> extends JpaRepository<File, Long> {
    List<File> findByIdeaId(Idea ideaId);
}
