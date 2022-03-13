package com.project.web.repository;

import com.project.web.model.Idea;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IdeaRepository extends PagingAndSortingRepository<Idea, Long> {
}
