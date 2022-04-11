package com.project.web.service;

import com.project.web.model.Idea;
import com.project.web.model.Reaction;
import com.project.web.model.User;
import com.project.web.payload.request.DeleteReactionRequest;
import com.project.web.payload.request.ReactionRequest;
import com.project.web.payload.response.ReactionResponse;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.IdeaRepository;
import com.project.web.repository.ReactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class ReactionServiceTest {
    @Autowired
    ReactionService reactionService;
    @MockBean
    ReactionRepository reactionRepo;
    @MockBean
    IdeaRepository ideaRepo;

    @Test
    void testGetReactionOfAnIdeaService_thenReturnAnObject() {
        Reaction reaction = new Reaction();
        reaction.setReactionType("like");
        User user = new User();
        user.setUsername("test");
        reaction.setUserId(user);
        List<ReactionResponse> expected = new ArrayList<>();
        ReactionResponse reactionResponse = new ReactionResponse();
        reactionResponse.setUsername(reaction.getUserId().getUsername());
        reactionResponse.setReactionType("like");
        expected.add(reactionResponse);
        when(reactionRepo.findByIdeaId(1L)).thenReturn(Collections.singletonList(reaction));
        ResponseEntity<ResponseObject> actual = reactionService.getReactionOfAnIdea(1L);
        assertEquals(expected , Objects.requireNonNull(actual.getBody()).getData());
    }

    @Test
    void testGetReactionOfAnIdeaService_thenReturnError() {
        List<Reaction> reactionList = Collections.emptyList();
        when(reactionRepo.findByIdeaId(1L)).thenReturn(reactionList);
        ResponseEntity<ResponseObject> actual = reactionService.getReactionOfAnIdea(1L);
        assertEquals("Error: Reaction has not existed!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void addReactionOfAnIdeaWithIdeaHasExisted_thenReturnError() {
        ReactionRequest reaction = new ReactionRequest();
        reaction.setIdeaId(1L);
        when(ideaRepo.existsById(reaction.getIdeaId())).thenReturn(true);
        ResponseEntity<ResponseObject> actual = reactionService.addReactionOfAnIdea(reaction);
        assertEquals("Error: Reaction has existed!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void addSubmissionWithSubmissionNameHasExisted_thenReturnAnObject() {
        ReactionRequest reaction = new ReactionRequest();
        reaction.setIdeaId(1L);
        reaction.setUserId(1L);
        reaction.setReactionType("test");
        when(ideaRepo.existsById(1L)).thenReturn(false);
        ResponseEntity<ResponseObject> actual = reactionService.addReactionOfAnIdea(reaction);
        assertEquals("Success!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void deleteReactionOfAnIdeaWithReactionHasExisted_thenReturnAMessageSuccess() {
        DeleteReactionRequest reactionRequest = new DeleteReactionRequest();
        reactionRequest.setUserId(1L);
        Reaction reaction = new Reaction();
        reaction.setReactionType("test");
        User user = new User();
        user.setUserId(1L);
        Idea idea = new Idea();
        idea.setIdeaId(reactionRequest.getIdeaId());
        Optional<Reaction> reactionOpt = Optional.of(reaction);
        when(reactionRepo.findByUserId(user)).thenReturn(reactionOpt);
        when(reactionRepo.existsByIdeaId(idea)).thenReturn(null);
        ResponseEntity<ResponseObject> actual = reactionService.deleteReactionOfAnIdea(reactionRequest);
        assertEquals("Success!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void deleteReactionOfAnIdeaWithReactionHasNotExisted_thenReturnAErrorMessage() {
        DeleteReactionRequest reactionRequest = new DeleteReactionRequest();
        Optional<Reaction> reaction = Optional.empty();
        when(reactionRepo.findByUserId(null)).thenReturn(reaction);
        ResponseEntity<ResponseObject> actual = reactionService.deleteReactionOfAnIdea(reactionRequest);
        assertEquals("Error: Reaction has not existed!", Objects.requireNonNull(actual.getBody()).getMessage());
    }

    @Test
    void editReactionOfAnIdeaHasExisted_thenReturnAMessageSuccess() {
        ReactionRequest reactionInput = new ReactionRequest();
        reactionInput.setReactionId(1L);
        Reaction reaction = new Reaction();
        Idea idea = new Idea();
        User user = new User();
        user.setUserId(1L);
        idea.setIdeaId(1L);
        reaction.setReactionId(1L);
        reaction.setIdeaId(idea);
        reaction.setUserId(user);
        reaction.setReactionType("test");
        Optional<Reaction> reactionOpt = Optional.of(reaction);
        when(reactionRepo.findById(reactionInput.getReactionId())).thenReturn(reactionOpt);
        ResponseEntity<ResponseObject> actual = reactionService.editReactionOfAnIdea(reactionInput);
        assertEquals("Success!", Objects.requireNonNull(actual.getBody()).getMessage());
    }
    @Test
    void editReactionOfAnIdeaHasNotExisted_thenReturnAMessageError() {
        ReactionRequest reactionInput = new ReactionRequest();
        Optional<Reaction> reactionOpt = Optional.empty();
        when(reactionRepo.findById(reactionInput.getReactionId())).thenReturn(reactionOpt);
        ResponseEntity<ResponseObject> actual = reactionService.editReactionOfAnIdea(reactionInput);
        assertEquals("Error: Reaction has not existed!", Objects.requireNonNull(actual.getBody()).getMessage());
    }
}
