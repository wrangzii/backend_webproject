package com.project.web.service.serviceImp;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.model.File;
import com.project.web.model.Category;
import com.project.web.model.Idea;
import com.project.web.model.Submission;
import com.project.web.model.User;
import com.project.web.payload.request.SubmitIdeaRequest;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.FileRepository;
import com.project.web.repository.IdeaRepository;
import com.project.web.service.FileService;
import com.project.web.service.GoogleDriveService;
import com.project.web.service.IdeaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdeaServiceImp implements IdeaService {
    private final IdeaRepository ideaRepo;
    private final FileRepository fileRepo;
    private final FileService fileService;
    private final GoogleDriveService googleDriveServ;

    @Override
    public List<Idea> getAllIdea(Integer pageNumber) {
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
    public ResponseEntity<ResponseObject> addIdea(SubmitIdeaRequest idea, MultipartFile file) throws Exception {
        String filePath = idea.getCateId() + "_" + idea.getUserId();
        Idea addIdea = new Idea();
        File uploadFile = new File();
        String folderId = fileService.getFolderId(filePath);
        com.project.web.model.File fileModel = new com.project.web.model.File();
        if (!file.isEmpty()) {
            File fileMetadata = new File();
            fileMetadata.setParents(Collections.singletonList(folderId));
            fileMetadata.setName(file.getOriginalFilename());
            uploadFile = googleDriveServ.getInstance()
                    .files()
                    .create(fileMetadata, new InputStreamContent(
                            file.getContentType(),
                            new ByteArrayInputStream(file.getBytes()))
                    )
                    .setFields("id").execute();
        }
        Category cate = new Category();
        Submission submit = new Submission();
        User user = new User();
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
        fileModel.setFilePath(uploadFile.getId());
        Idea idea1 = new Idea();
        idea1.setIdeaId(addIdea.getIdeaId());
        fileModel.setIdeaId(idea1);
        fileModel.setCreateDate(idea.getCreateDate());
        fileModel.setLastModifyDate(idea.getLastModifyDate());
        fileRepo.save(fileModel);
        return ResponseEntity.ok(new ResponseObject(HttpStatus.CREATED.toString(),"Add idea successfully!", addIdea));
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
