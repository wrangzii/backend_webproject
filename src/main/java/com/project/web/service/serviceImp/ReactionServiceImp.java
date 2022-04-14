package com.project.web.service.serviceImp;

import com.project.web.model.Idea;
import com.project.web.model.Reaction;
import com.project.web.model.User;
import com.project.web.payload.request.DeleteReactionRequest;
import com.project.web.payload.request.ReactionRequest;
import com.project.web.payload.response.ReactionResponse;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.ReactionRepository;
import com.project.web.repository.UserRepository;
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
    private final UserRepository userRepo;

    @Override
    public ResponseEntity<ResponseObject> getReactionOfAnIdea(Long ideaId) {
        List<Reaction> reactionList = reactionRepo.findByIdeaId(ideaId);
        List<ReactionResponse> response = new ArrayList<>();
        if (reactionList.size() > 0) {
            for (Reaction reaction : reactionList) {
                ReactionResponse reactionResponse = new ReactionResponse();
                reactionResponse.setReactionId(reaction.getReactionId());
                reactionResponse.setUsername(reaction.getUserId().getUsername());
                reactionResponse.setReactionType(reaction.getReactionType());
                response.add(reactionResponse);
            }
            return ResponseEntity.ok(new ResponseObject(response));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Reaction has not existed!"));
    }

    @Override
    public ResponseEntity<ResponseObject> addReactionOfAnIdea(ReactionRequest reaction) {
        Reaction addReaction = new Reaction();
        Date date = new Date();
        Idea idea = new Idea();
        idea.setIdeaId(reaction.getIdeaId());
        User user = new User();
        user.setUserId(reaction.getUserId());
        Reaction checkExistedIdea = reactionRepo.findByIdeaIdAndUserId(idea, user);
        if (checkExistedIdea == null) {
            addReaction.setIdeaId(idea);
            addReaction.setUserId(user);
            addReaction.setReactionType(reaction.getReactionType());
            addReaction.setCreateDate(date);
            addReaction.setLastModifyDate(date);
            reactionRepo.save(addReaction);
            return ResponseEntity.ok(new ResponseObject("Success!"));
        } else {
            reactionRepo.deleteById(checkExistedIdea.getReactionId());
            addReaction.setIdeaId(idea);
            addReaction.setUserId(user);
            addReaction.setReactionType(reaction.getReactionType());
            addReaction.setCreateDate(date);
            addReaction.setLastModifyDate(date);
            reactionRepo.save(addReaction);
            return ResponseEntity.ok(new ResponseObject("Success!"));
        }
    }

    @Override
    public ResponseEntity<ResponseObject> editReactionOfAnIdea(ReactionRequest reaction) {
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
            return ResponseEntity.ok(new ResponseObject("Success!"));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Reaction has not existed!"));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteReactionOfAnIdea(DeleteReactionRequest deleteRequest) {
        User user = new User();
        Idea idea = new Idea();
        user.setUserId(deleteRequest.getUserId());
        idea.setIdeaId(deleteRequest.getIdeaId());
        Reaction checkExistedUserId = reactionRepo.findByIdeaIdAndUserId(idea, user);
        if (checkExistedUserId != null) {
            reactionRepo.deleteById(checkExistedUserId.getReactionId());

            return ResponseEntity.ok(new ResponseObject("Success!"));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Reaction has not existed!"));
    }
}
