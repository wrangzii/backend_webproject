package com.project.web.service.serviceImp;

import com.project.web.model.Idea;
import com.project.web.model.Reaction;
import com.project.web.model.User;
import com.project.web.payload.request.ReactionRequest;
import com.project.web.payload.response.ReactionResponse;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.ReactionRepository;
import com.project.web.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReactionServiceImp implements ReactionService {
    private final ReactionRepository reactionRepo;
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
        Boolean checkExisted = reactionRepo.existsByReactionId(reaction.getIdeaId());
        if (!checkExisted) {
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
}
