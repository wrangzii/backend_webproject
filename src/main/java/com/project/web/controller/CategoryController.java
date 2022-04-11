package com.project.web.controller;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.project.web.model.Category;
import com.project.web.payload.request.CategoryRequest;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.CategoryRepository;
import com.project.web.service.CategoryService;
import com.project.web.service.serviceImp.DropboxService;
import com.project.web.util.MediaTypeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/category")
@Slf4j
@CrossOrigin("http://localhost:3000")
public class CategoryController {
    private static final String DIRECTORY = "../backend_webproject/build/tmp";
    @Autowired
    DropboxService dropboxService;
    @Autowired
    DbxClientV2 dropboxClient;
    private final CategoryService cateSer;
    @Autowired
    private ServletContext servletContext;
    private final CategoryRepository cateRepo;

    @GetMapping("/all")
    public List<Category> getAllCategory(@RequestParam Integer pageNumber) {
        return cateSer.getAllCategory(pageNumber);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getCateById(@PathVariable Long id) {
        return cateSer.getCateById(id);
    }

    @PreAuthorize("hasRole('QA_MANAGER') or hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addCategory(@Valid @RequestBody CategoryRequest category) throws DbxException {
        return cateSer.addCategory(category);
    }

    @PreAuthorize("hasRole('QA_MANAGER') or hasRole('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseObject> editCategory(@Valid @RequestBody CategoryRequest category, @PathVariable Long id) {
        return cateSer.editCategory(category,id);
    }

    @PreAuthorize("hasRole('QA_MANAGER') or hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteCategory(@PathVariable Long id) {
        return cateSer.deleteCategory(id);
    }

    @PreAuthorize("hasRole('QA_MANAGER')")
    @GetMapping("/download/{cateId}")
    public ResponseEntity<ByteArrayResource> downloadFile2(
            @PathVariable Long cateId) throws IOException {
        Optional<Category> categoryExists = cateRepo.findById(cateId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String fileName = categoryExists.get().getCateName() + ".zip";
        categoryExists.ifPresent(category -> {
            try {
                OutputStream out = new FileOutputStream("../backend_webproject/build/tmp/" + fileName);
                dropboxClient.files().downloadZipBuilder("/" + fileName).download(out);
                dropboxClient.files().downloadZipBuilder("/" + category.getCateName() + ".zip").download(outputStream);
            } catch (DbxException | IOException e) {
                e.printStackTrace();
            }
        });
        Path path = Paths.get(DIRECTORY + "/" + fileName);
        byte[] data = Files.readAllBytes(path);

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + (path).getFileName().toString())
                // Content-Type
                .contentType(mediaType) //
                // Content-Length
                .contentLength(data.length) //
                .body(resource);
    }
}
