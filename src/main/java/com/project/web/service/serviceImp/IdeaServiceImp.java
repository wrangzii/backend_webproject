package com.project.web.service.serviceImp;


import com.dropbox.core.DbxException;
import com.project.web.model.*;
import com.project.web.payload.request.SubmitIdeaRequest;
import com.project.web.payload.response.ExportDataResponse;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.*;
import com.project.web.service.EmailSenderService;
import com.project.web.service.IdeaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private final EmailSenderService emailSenderService;
    private final ReactionRepository reactionRepo;
    private final Integer pageSize = 5;
    @Autowired
    DropboxService dropboxService;

    @Override
    public List<Idea> getAllIdea(Integer pageNumber) {
        Pageable paging = PageRequest.of(pageNumber,pageSize);

        Page<Idea> pagedResult = ideaRepo.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Idea> getLatestIdeas(int pageNumber) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by("createDate").descending());

        Page<Idea> pagedResult = ideaRepo.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Idea> sortByViewCount(Integer pageNumber) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by("viewCount").descending());

        Page<Idea> pagedResult = ideaRepo.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Idea> sortIdeaByLatestComment(Integer pageNumber) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        Page<Idea> pagedResult = ideaRepo.sortByLatestComment(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public ResponseEntity<ResponseObject> getAllIdeaToExport(HttpServletResponse response) throws IOException {
        Iterable<Idea> ideas = ideaRepo.findAll();
        ExportDataResponse data = new ExportDataResponse();
        List<ExportDataResponse> dataExport = new ArrayList<>();
        for (Idea idea : ideas) {
            data.setIdeaId(idea.getIdeaId());
            data.setUsername(idea.getUserId().getUsername());
            data.setSubmissionId(idea.getSubmissionId().getSubmissionId());
            data.setCategoryName(idea.getCateId().getCateName());
            data.setTitle(idea.getTitle());
            data.setDescription(idea.getDescription());
            data.setViewCount(idea.getViewCount());
            data.setCreateDate(idea.getCreateDate().toString());
            data.setModifyDate(idea.getLastModifyDate().toString());
            data.setIsAnonymous(idea.getIsAnonymous());

            dataExport.add(data);
        }
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ideas_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Username", "Idea ID", "Submission Id", "Category Name", "title", "Description", "view count", "Create date", "Modify date", "Is anonymous"};
        String[] nameMapping = {"username", "ideaId", "submissionId", "categoryName", "title", "description", "viewCount", "createDate", "modifyDate", "isAnonymous"};
        csvWriter.writeHeader(csvHeader);

        for (ExportDataResponse idea : dataExport) {
            csvWriter.write(idea, nameMapping);
        }
        csvWriter.close();

        return null;
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
        if (checkClosureDate.isPresent() && checkClosureDate.get().getClosureDate().after(date)) {
            String filePath = "";
            Optional<Category> category = categoryRepository.findById(idea.getCateId());
            if (category.isPresent()) {
                Idea addIdea = new Idea();
                File fileModel = new File();
                Category cate = new Category();
                Submission submit = new Submission();
                User user = new User();
                if (existedUser.isPresent()) {
                    filePath = "/" + category.get().getCateName() + ".zip/" + existedUser.get().getUsername() + "_idea" + "/" + file.getOriginalFilename();
                    dropboxService.uploadFile(file, filePath);
                    user.setUserId(idea.getUserId());
                    cate.setCateId(idea.getCateId());
                    addIdea.setDescription(idea.getDescription());
                    addIdea.setTitle(idea.getTitle());
                    addIdea.setCateId(cate);
                    addIdea.setCreateDate(date);
                    addIdea.setLastModifyDate(date);
                    submit.setSubmissionId(idea.getSubmissionId());
                    addIdea.setSubmissionId(submit);
                    addIdea.setUserId(user);
                    addIdea.setViewCount(0);
                    addIdea.setIsAnonymous(idea.getIsAnonymous());
                    ideaRepo.save(addIdea);
                    Idea idea1 = new Idea();
                    idea1.setIdeaId(addIdea.getIdeaId());
                    fileModel.setIdeaId(idea1);
                    fileModel.setFileName(existedUser.get().getEmail());
                    fileModel.setFilePath(filePath);
                    fileModel.setCreateDate(date);
                    fileModel.setLastModifyDate(date);
                    fileModel.setIdeaId(idea1);
                    fileRepo.save(fileModel);
                    Optional<User> getDepartment = userRepo.findById(idea.getUserId());
                    getDepartment.ifPresent(value -> sendMailToQaCoordinator(value.getDepartmentId().getDepartmentId()));
                }
                return ResponseEntity.ok(new ResponseObject(HttpStatus.CREATED.toString(), "Add idea successfully!", addIdea));
            }
        }
        return ResponseEntity.badRequest().body(new ResponseObject("Could not add an idea because the time to submit is already closed!"));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteIdea(Long ideaId, Long userId) throws DbxException {
        Optional<Idea> deleteIdea = ideaRepo.findById(ideaId);
        Optional<User> userOpt = userRepo.findById(userId);
        User user = new User();
        user.setUserId(userId);
        Idea idea = new Idea();
        idea.setIdeaId(ideaId);
        if (userOpt.isPresent()) {
            if (deleteIdea.isPresent()) {
                Reaction deleteReaction = reactionRepo.findByIdeaIdAndUserId(idea, user);
                if (deleteReaction != null) {
                    reactionRepo.deleteByIdeaId(idea);
                }
                Idea idea1 = new Idea();
                idea1.setIdeaId(deleteIdea.get().getIdeaId());
                List<File> deleteFile = fileRepo.findByIdeaId(idea1);
                if (deleteFile != null) {
                    for (File file1 : deleteFile) {
                        fileRepo.deleteById(file1.getFileId());
                    }
                }
                ideaRepo.deleteById(ideaId);
                return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(), "Delete idea successfully!"));
            }
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: The idea is not exist!"));
    }

    @Override
    public ResponseEntity<ResponseObject> addViewCount(Long id) {
        Optional<Idea> idea = ideaRepo.findById(id);
        if (idea.isPresent()) {
            idea.get().setViewCount(idea.get().getViewCount() + 1);
            ideaRepo.save(idea.get());
        }
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> editIdea(SubmitIdeaRequest idea, MultipartFile file, Long id) throws IOException, DbxException {
        Optional<User> existedUser = userRepo.findById(idea.getUserId());
        Optional<Submission> checkClosureDate = submissionRepo.findById(idea.getSubmissionId());
        Date date = new Date();
        if (checkClosureDate.isPresent() && checkClosureDate.get().getClosureDate().after(date)) {
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
                        if (file != null) {
                            filePath = "/" + category.get().getCateName() + ".zip/" + existedUser.get().getUsername() + "_idea" + "/" + file.getOriginalFilename();
                            dropboxService.uploadFile(file, filePath);
                        }
                        user.setUserId(idea.getUserId());
                        cate.setCateId(idea.getCateId());
                        editIdea.get().setDescription(idea.getDescription());
                        editIdea.get().setTitle(idea.getTitle());
                        editIdea.get().setCateId(cate);
                        editIdea.get().setLastModifyDate(date);
                        submit.setSubmissionId(idea.getSubmissionId());
                        editIdea.get().setSubmissionId(submit);
                        editIdea.get().setUserId(user);
                        editIdea.get().setIsAnonymous(idea.getIsAnonymous());
                        ideaRepo.save(editIdea.get());
                        Idea idea1 = new Idea();
                        idea1.setIdeaId(editIdea.get().getIdeaId());
                        fileModel.setIdeaId(idea1);
                        fileModel.setFileName(existedUser.get().getEmail());
                        fileModel.setFilePath(filePath);
                        fileModel.setLastModifyDate(date);
                        fileModel.setIdeaId(idea1);
                        fileRepo.save(fileModel);
                    }
                    return ResponseEntity.ok(new ResponseObject(HttpStatus.CREATED.toString(), "Edit idea successfully!", editIdea));
                }
            }
        }
        return ResponseEntity.badRequest().body(new ResponseObject("Could not edit idea, because idea is not created!"));
    }

    private void sendMailToQaCoordinator(Long departmentId) {
        List<User> users = userRepo.findByDepartmentId(departmentId);
        for (User user: users) {
            for (Role role : user.getRoles()) {
                if (role.getRoleName().toString().equals("ROLE_QA_COORDINATOR")) {
                    // Create the email
                    SimpleMailMessage mailMessage = new SimpleMailMessage();
                    mailMessage.setTo(user.getEmail());
                    mailMessage.setSubject("A new idea submission");
                    mailMessage.setFrom("test-email@gmail.com");
                    mailMessage.setText("A staff just submit a new idea. Let check it now!");

                    // Send the email
                    emailSenderService.sendEmail(mailMessage);
                }
            }
        }
    }
}
