package com.project.web.service.serviceImp;

import com.dropbox.core.DbxException;
import com.project.web.model.Category;
import com.project.web.payload.request.CategoryRequest;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.CategoryRepository;
import com.project.web.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository cateRepo;
    private final DropboxService dropboxService;

    @Override
    public List<Category> getAllCategory(int pageNumber) {
        int pageSize = 10;
        Pageable paging = PageRequest.of(pageNumber,pageSize);
        Page<Category> pagedResult = cateRepo.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public ResponseEntity<ResponseObject> getCateById(Long id) {
        Optional<Category> checkExisted = cateRepo.findById(id);
        if (checkExisted.isPresent()) {
            return ResponseEntity.ok(new ResponseObject(HttpStatus.CREATED.toString(),"Get category successfully!",checkExisted));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Category name is already taken!"));
    }

    @Override
    public ResponseEntity<ResponseObject> addCategory(CategoryRequest category) {
        Boolean checkExisted = cateRepo.existsByCateName(category.getCateName());
        Category addCate = new Category();
        if (!checkExisted) {
            addCate.setCateId(category.getCateId());
            addCate.setCateName(category.getCateName());
            addCate.setDescription(category.getDescription());
            addCate.setCreateDate(new Date());
            addCate.setLastModifyDate(new Date());
            cateRepo.save(addCate);
            return ResponseEntity.ok(new ResponseObject(HttpStatus.CREATED.toString(),"Add category successfully!",addCate));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Category name is already taken!"));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteCategory(Long id) {
        Optional<Category> deleteCate = cateRepo.findById(id);
        if (deleteCate.isPresent()) {
            cateRepo.deleteById(id);
            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(),"Delete category successfully!"));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Category name is not exist!"));
    }

    @Override
    public ResponseEntity<ResponseObject> editCategory(CategoryRequest category, Long id) {
        Optional<Category> editCate = cateRepo.findById(id);
        if (editCate.isPresent()) {
            editCate.get().setCateName(category.getCateName());
            editCate.get().setDescription(category.getDescription());
            editCate.get().setLastModifyDate(new Date());
            cateRepo.save(editCate.get());
            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(),"Edit category successfully!",editCate));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Category is not exist!"));
    }

    @Override
    public ResponseEntity<ResponseObject> downloadAllFileIdea(HttpServletResponse response, Long id) throws IOException, DbxException {
        Optional<Category> categoryExists = cateRepo.findById(id);
        if (categoryExists.isPresent()) {
            dropboxService.downloadFile(response,"/" +  categoryExists.get().getCateName() + ".zip");
            return ResponseEntity.ok().body(new ResponseObject("Download successfully!"));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("Download fail!"));
    }
}
