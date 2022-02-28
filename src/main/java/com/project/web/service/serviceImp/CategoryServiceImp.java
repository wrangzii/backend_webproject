package com.project.web.service.serviceImp;

import com.project.web.model.Category;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.CategoryRepository;
import com.project.web.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository cateRepo;
    @Override
    public List<Category> getAllCategory() {
        return cateRepo.findAll();
    }

    @Override
    @ResponseStatus(value = HttpStatus.CREATED)
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
