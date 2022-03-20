package com.project.web.controller;

import com.dropbox.core.v2.DbxClientV2;
import com.project.web.Utils.DropboxAction;
import com.project.web.service.DropboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(path ="/file")
public class DropboxController {

    private static final Logger logger = LoggerFactory.getLogger(DropboxController.class);

    @Autowired
    DropboxService dropboxService;

    @Autowired
    DbxClientV2 dropboxClient;

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("filePath") String filePath) throws Exception {
        dropboxService.uploadFile(file, filePath);
        return "You successfully uploaded " + filePath + "!!";
    }

    @GetMapping("/list")
    public List<Map<String, Object>> index(@RequestParam(value = "target", required = false, defaultValue = "") String target) throws Exception {
        return dropboxService.getFileList(target);
    }

    @GetMapping("/browse")
    public Map<String, Object> browser(@RequestParam(value = "target", required = false, defaultValue = "") String target) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("data", dropboxService.getDropboxItems(target));

        return data;
    }

    @GetMapping("/download")
    public void downloadFile(HttpServletResponse response, @RequestBody DropboxAction.Download download) throws Exception {
        dropboxService.downloadFile(response, download);
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteFile(@RequestBody DropboxAction.Delete delete) throws Exception {
        dropboxService.deleteFile(delete);

        DropboxAction.Response response = new DropboxAction.Response(200, "success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}