package com.project.web.service.serviceImp;

import com.project.web.model.Idea;
import com.project.web.model.Reaction;
import com.project.web.model.User;
import com.project.web.payload.request.DeleteReactionRequest;
import com.project.web.payload.request.ReactionRequest;
import com.project.web.payload.response.ReactionResponse;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.IdeaRepository;
import com.project.web.repository.ReactionRepository;
import com.project.web.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionServiceImp implements ReactionService {
    private final ReactionRepository reactionRepo;
    private final IdeaRepository ideaRepo;

    @Override
    public ResponseEntity<ResponseObject> getReactionOfAnIdea(Long ideaId) {
        List<Reaction> reactionList = reactionRepo.findByIdeaId(ideaId);
        List<ReactionResponse> response = new ArrayList<>();
        ReactionResponse reactionResponse = new ReactionResponse();
        if (reactionList != null) {
            for (Reaction reaction : reactionList) {
                reactionResponse.setUsername(reaction.getUserId().getUsername());
                reactionResponse.setReactionType(reaction.getReactionType());
                response.add(reactionResponse);
            }
            return ResponseEntity.ok(new ResponseObject(response));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Reaction has not existed!"));
    }

    @Override
    public void addReactionOfAnIdea(ReactionRequest reaction) {
        boolean checkExistedIdea = ideaRepo.existsById(reaction.getIdeaId());
        if (!checkExistedIdea) {
            Reaction addReaction = new Reaction();
            Date date = new Date();
            Idea idea = new Idea();
            idea.setIdeaId(reaction.getIdeaId());
            User user = new User();
            user.setUserId(reaction.getUserId());
            addReaction.setIdeaId(idea);
            addReaction.setUserId(user);
            addReaction.setReactionType(reaction.getReactionType());
            addReaction.setCreateDate(date);
            addReaction.setLastModifyDate(date);
            reactionRepo.save(addReaction);
        }
    }

    @Override
    public void editReactionOfAnIdea(ReactionRequest reaction) {
        Optional<Reaction> editReaction = reactionRepo.findById(reaction.getReactionId());
        if (editReaction.isPresent()) {
            Date date = new Date();
            Idea idea = new Idea();
            idea.setIdeaId(reaction.getIdeaId());
            User user = new User();
            user.setUserId(reaction.getUserId());
            editReaction.get().setIdeaId(idea);
            editReaction.get().setUserId(user);
            editReaction.get().setReactionType(reaction.getReactionType());
            editReaction.get().setLastModifyDate(date);
            reactionRepo.save(editReaction.get());
        }
    }

    @Override
    public void deleteReactionOfAnIdea(DeleteReactionRequest deleteRequest) {
        User user = new User();
        Idea idea = new Idea();
        user.setUserId(deleteRequest.getUserId());
        Optional<Reaction> checkExistedUserId = reactionRepo.findByUserId(user);
        if (checkExistedUserId.isPresent()) {
            idea.setIdeaId(deleteRequest.getIdeaId());
            Boolean checkExistedIdea = reactionRepo.existsByIdeaId(idea);
            if (checkExistedIdea) {
                reactionRepo.deleteById(checkExistedUserId.get().getReactionId());
            }
        }
    }
}