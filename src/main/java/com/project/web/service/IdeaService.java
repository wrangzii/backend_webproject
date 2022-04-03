package com.project.web.service;

import com.dropbox.core.DbxException;
import com.project.web.model.Idea;
import com.project.web.payload.request.SubmitIdeaRequest;
import com.project.web.payload.response.ExportDataResponse;
import com.project.web.payload.response.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IdeaService {
    List<Idea> getAllIdea(Integer pageNumber);
    List<Idea> getLatestIdeas(int pageNumber);
    List<ExportDataResponse> getAllIdeaToExport();
    ResponseEntity<ResponseObject> getIdeaById(Long id);
    ResponseEntity<ResponseObject> addIdea(SubmitIdeaRequest idea, MultipartFile file) throws Exception;
    ResponseEntity<ResponseObject> deleteIdea(Long id) throws DbxException;
    ResponseEntity<ResponseObject> addViewCount(Long id);
    ResponseEntity<ResponseObject> editIdea(SubmitIdeaRequest idea, MultipartFile file, Long id) throws IOException, DbxException;
}
