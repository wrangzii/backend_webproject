package com.project.web.service.serviceImp;


import com.dropbox.core.DbxException;
import com.project.web.model.*;
import com.project.web.payload.request.SubmitIdeaRequest;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.*;
import com.project.web.service.IdeaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdeaServiceImp implements IdeaService {
    private final IdeaRepository ideaRepo;
    private final FileRepository fileRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepository;
    private final SubmissionRepository submissionRepo;

    @Autowired
    DropboxService dropboxService;

    @Override
    public List<Idea> getAllIdea(int pageNumber) {
        int pageSize = 5;
        Pageable paging = PageRequest.of(pageNumber,pageSize);

        Page<Idea> pagedResult = ideaRepo.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public ResponseEntity<ResponseObject> getIdeaById(Long id) {
        Optional<Idea> checkExisted= ideaRepo.findById(id);
        if (checkExisted.isPresent()) {
            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(),"Get idea successfully!", checkExisted));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: idea name is not exist!"));
    }

    @Override
    public ResponseEntity<ResponseObject> addIdea(SubmitIdeaRequest idea, MultipartFile file) throws Exception {
        Optional<User> existedUser = userRepo.findById(idea.getUserId());
        Optional<Submission> checkClosureDate = submissionRepo.findById(idea.getSubmissionId());
        Date date = new Date();
        if (checkClosureDate.isPresent())
            if (checkClosureDate.get().getClosureDate().after(date)) {
                String filePath = "";
                Optional<Category> category = categoryRepository.findById(idea.getCateId());
                if (category.isPresent()) {
                    Idea addIdea = new Idea();
                    File fileModel = new File();
                    Category cate = new Category();
                    Submission submit = new Submission();
                    User user = new User();
                    if (existedUser.isPresent()) {
                        filePath = "/" + category.get().getCateName() + "/" + existedUser.get().getUsername() + "_idea" + "/" + existedUser.get().getEmail();
//                dropboxService.createFolder(filePath, category.get().getCateName());
                        dropboxService.uploadFile(file, filePath);
                        user.setUserId(idea.getUserId());
                        cate.setCateId(idea.getCateId());
                        addIdea.setDescription(idea.getDescription());
                        addIdea.setTitle(idea.getTitle());
                        addIdea.setCateId(cate);
                        addIdea.setViewCount(idea.getViewCount());
                        addIdea.setCreateDate(idea.getCreateDate());
                        addIdea.setLastModifyDate(idea.getLastModifyDate());
                        submit.setSubmissionId(idea.getSubmissionId());
                        addIdea.setSubmissionId(submit);
                        addIdea.setUserId(user);
                        ideaRepo.save(addIdea);
                        Idea idea1 = new Idea();
                        idea1.setIdeaId(addIdea.getIdeaId());
                        fileModel.setIdeaId(idea1);
                        fileModel.setFileName(existedUser.get().getEmail());
                        fileModel.setFilePath(filePath);
                        fileModel.setCreateDate(idea.getCreateDate());
                        fileModel.setLastModifyDate(idea.getLastModifyDate());
                        fileModel.setIdeaId(idea1);
                        fileRepo.save(fileModel);
                    }
                    return ResponseEntity.ok(new ResponseObject(HttpStatus.CREATED.toString(), "Add idea successfully!", addIdea));
                }
            }
        return ResponseEntity.badRequest().body(new ResponseObject("Could not add an idea because the time to submit is already closed!"));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteIdea(Long id) throws DbxException {
        Optional<Idea> deleteIdea = ideaRepo.findById(id);
            if (deleteIdea.isPresent()) {
                Optional<File> deleteFile = fileRepo.findById(deleteIdea.get().getIdeaId());
                dropboxService.deleteFile(deleteFile.get().getFilePath());
                deleteFile.ifPresent(file -> fileRepo.deleteById(file.getFileId()));
                ideaRepo.deleteById(id);
            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(),"Delete idea successfully!"));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: The idea is not exist!"));
    }

    @Override
    public ResponseEntity<ResponseObject> editIdea(SubmitIdeaRequest idea, MultipartFile file, Long id) throws IOException, DbxException {
        Optional<User> existedUser = userRepo.findById(idea.getUserId());
        String filePath = "";
        Optional<Category> category = categoryRepository.findById(idea.getCateId());
        Optional<Idea> editIdea = ideaRepo.findById(id);
        if (editIdea.isPresent()) {
            if (category.isPresent()) {
                File fileModel = new File();
                Category cate = new Category();
                Submission submit = new Submission();
                User user = new User();
                if (existedUser.isPresent()) {
                    filePath = "/" + category.get().getCateName() + "/" + existedUser.get().getUsername() + "_idea" + "/" + existedUser.get().getEmail();
    //                dropboxService.createFolder(filePath, category.get().getCateName());
                    dropboxService.uploadFile(file, filePath);
                    user.setUserId(idea.getUserId());
                    cate.setCateId(idea.getCateId());
                    editIdea.get().setDescription(idea.getDescription());
                    editIdea.get().setTitle(idea.getTitle());
                    editIdea.get().setCateId(cate);
                    editIdea.get().setViewCount(idea.getViewCount());
                    editIdea.get().setCreateDate(idea.getCreateDate());
                    editIdea.get().setLastModifyDate(idea.getLastModifyDate());
                    submit.setSubmissionId(idea.getSubmissionId());
                    editIdea.get().setSubmissionId(submit);
                    editIdea.get().setUserId(user);
                    ideaRepo.save(editIdea.get());
                    Idea idea1 = new Idea();
                    idea1.setIdeaId(editIdea.get().getIdeaId());
                    fileModel.setIdeaId(idea1);
                    fileModel.setFileName(existedUser.get().getEmail());
                    fileModel.setFilePath(filePath);
                    fileModel.setLastModifyDate(idea.getCreateDate());
                    fileModel.setLastModifyDate(idea.getLastModifyDate());
                    fileModel.setIdeaId(idea1);
                    fileRepo.save(fileModel);
                }
                return ResponseEntity.ok(new ResponseObject(HttpStatus.CREATED.toString(),"Add idea successfully!", editIdea));
            }
        }
        return ResponseEntity.badRequest().body(new ResponseObject("Could not edit idea, because idea is not created!"));
    }
}
