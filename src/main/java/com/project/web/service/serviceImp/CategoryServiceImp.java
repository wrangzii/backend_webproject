package com.project.web.service.serviceImp;

import com.project.web.model.Category;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository cateRepo;
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
    public ResponseEntity<ResponseObject> addCategory(Category category) {
        Boolean checkExisted = cateRepo.existsByCateName(category.getCateName());
        if (!checkExisted) {
            cateRepo.save(category);
            return ResponseEntity.ok(new ResponseObject(HttpStatus.CREATED.toString(),"Add category successfully!",category));
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
    public ResponseEntity<ResponseObject> editCategory(Category category, Long id) {
        Optional<Category> editCate = cateRepo.findById(id);
        if (editCate.isPresent()) {
            editCate.get().setCateName(category.getCateName());
            editCate.get().setDescription(category.getDescription());
            editCate.get().setLastModifyDate(category.getLastModifyDate());
            cateRepo.save(editCate.get());
            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(),"Edit category successfully!",editCate));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Category is not exist!"));
    }
}
