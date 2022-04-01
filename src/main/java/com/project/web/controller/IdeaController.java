package com.project.web.controller;

import com.dropbox.core.DbxException;
import com.project.web.model.Idea;
import com.project.web.payload.request.SubmitIdeaRequest;
import com.project.web.payload.response.ExportDataResponse;
import com.project.web.payload.response.ResponseObject;
import com.project.web.service.IdeaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path ="/submit_idea")
@Slf4j
@CrossOrigin("http://localhost:3000")
public class IdeaController {
    private final IdeaService ideaService;

    @GetMapping("/all")
    public List<Idea> getAllIdea(@RequestParam int pageNumber) {return ideaService.getAllIdea(pageNumber);}

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getIdeaById(@PathVariable Long id) {
        return ideaService.getIdeaById(id);
    }

    @PostMapping(value = "/add",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<ResponseObject> addIdea(@Valid @ModelAttribute SubmitIdeaRequest idea, @RequestBody MultipartFile file) throws Exception {
        return ideaService.addIdea(idea, file);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseObject> editIdea(@Valid @ModelAttribute SubmitIdeaRequest idea, @RequestBody MultipartFile file, @PathVariable Long id) throws IOException, DbxException {
        return ideaService.editIdea(idea, file, id);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteIdea(@PathVariable Long id) throws DbxException {
        return ideaService.deleteIdea(id);
    }

    @GetMapping("/export")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ideas_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        List<ExportDataResponse> listIdeas = ideaService.getAllIdeaToExport();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Username", "Idea ID", "Submission Id", "Category Name", "title", "Description", "view count", "Create date", "Modify date", "Is anonymous"};
        String[] nameMapping = {"username", "ideaId", "submissionId", "categoryName", "title", "description", "viewCount", "createDate", "modifyDate", "isAnonymous"};
        csvWriter.writeHeader(csvHeader);

        for (ExportDataResponse idea : listIdeas) {
            csvWriter.write(idea, nameMapping);
        }

        csvWriter.close();
    }

    @GetMapping("/viewCount/{id}")
    public ResponseEntity<ResponseObject> viewCount(@PathVariable Long id) {
        return ideaService.addViewCount(id);
    }
}
