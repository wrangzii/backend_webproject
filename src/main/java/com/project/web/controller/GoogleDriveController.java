package com.project.web.controller;

import com.google.api.services.drive.model.File;
import com.project.web.payload.response.ResponseObject;
import com.project.web.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path ="/file")
public class GoogleDriveController {
    @Autowired
    private FileService fileSer;

    @GetMapping({"/"})
    public ResponseEntity<List<File>> listEverything() throws IOException, GeneralSecurityException {
        List<File> files = fileSer.listEverything();
        return ResponseEntity.ok(files);
    }

    @GetMapping({"/list","/list/{parentId}"})
    public ResponseEntity<List<File>> list(@PathVariable(required = false) String parentId) throws IOException, GeneralSecurityException {
        List<File> files = fileSer.listFolderContent(parentId);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ResponseObject> download(@PathVariable String id, HttpServletResponse response) throws IOException, GeneralSecurityException {
        return fileSer.downloadFile(id, response.getOutputStream());
    }

    @GetMapping("/directory/create")
    public ResponseEntity<String> createDirectory(@RequestParam String path) throws Exception {
        String parentId = fileSer.getFolderId(path);
        return ResponseEntity.ok("parentId: "+parentId);
    }

//    @PostMapping(value = "/upload",
//            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
//            produces = {MediaType.APPLICATION_JSON_VALUE} )
//    public ResponseEntity<ResponseObject> uploadSingleFileExample4(@RequestBody MultipartFile file, @RequestParam String username,
//                                                           @RequestParam String category) {
//        log.info("Request contains, File: " + file.getOriginalFilename());
//        String fileId = fileSer.uploadFile(file, username, category);
//        if(fileId == null){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.ok(new ResponseObject("Success, FileId: "+ fileId));
//    }

    @GetMapping("/delete/{id}")
    public void delete(@PathVariable String id) throws Exception {
        fileSer.deleteFile(id);
    }
}
