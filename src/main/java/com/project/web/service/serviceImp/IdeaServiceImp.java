package com.project.web.service.serviceImp;

import com.project.web.model.Idea;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.IdeaRepository;
import com.project.web.service.IdeaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdeaServiceImp implements IdeaService {
    private final IdeaRepository ideaRepo;
    @Override
    public List<Idea> getAllIdea() {
        return ideaRepo.findAll();
    }

    @Override
    public ResponseEntity<ResponseObject> addIdea(Idea idea) {
        ideaRepo.save(idea);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.CREATED.toString(),"Add idea successfully!", idea));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteIdea(Long id) {
        Optional<Idea> deleteIdea = ideaRepo.findById(id);
        if (deleteIdea.isPresent()) {
            ideaRepo.deleteById(id);
            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(),"Delete submission successfully!"));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Submission name is not exist!"));
    }

    @Override
    public ResponseEntity<ResponseObject> editIdea(Idea idea, Long id) {
        Optional<Idea> editIdea= ideaRepo.findById(id);
        if (editIdea.isPresent()) {
            editIdea.get().setTitle(idea.getTitle());
            editIdea.get().setDescription(idea.getDescription());
            ideaRepo.save(editIdea.get());
            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(),"Edit idea successfully!",editIdea));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Idea is not exist!"));
    }
}
