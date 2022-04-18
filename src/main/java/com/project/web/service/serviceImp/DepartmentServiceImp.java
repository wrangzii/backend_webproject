package com.project.web.service.serviceImp;

import com.project.web.model.Department;
import com.project.web.payload.request.DepartmentRequest;
import com.project.web.payload.response.ResponseObject;
import com.project.web.repository.DepartmentRepository;
import com.project.web.service.DepartmentService;
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
public class DepartmentServiceImp implements DepartmentService {
    private final DepartmentRepository departmentRepo;

    @Override
    public List<Department> getAllDepartment(Integer pageNumber) {
        int pageSize = 10;
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<Department> pagedResult = departmentRepo.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public ResponseEntity<ResponseObject> getDepartmentById(Long id) {
        Optional<Department> checkExisted = departmentRepo.findById(id);
        if (checkExisted.isPresent()) {
            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(),"Get department successfully!",checkExisted));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Department name is already taken!"));
    }

    @Override
    public ResponseEntity<ResponseObject> addDepartment(DepartmentRequest departmentRequest) {
        Boolean checkExisted = departmentRepo.existsByDepartmentName(departmentRequest.getDepartmentName());
        Department department = new Department();
        if (!checkExisted) {
            department.setDepartmentName(departmentRequest.getDepartmentName());
            departmentRepo.save(department);
            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(),"Add department successfully!",department));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Department name is already taken!"));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteDepartment(Long id) {
        Optional<Department> deleteDepartment = departmentRepo.findById(id);
        if (deleteDepartment.isPresent()) {
            departmentRepo.deleteById(id);
            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(),"Delete department successfully!"));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Department name is not exist!"));
    }

    @Override
    public ResponseEntity<ResponseObject> editDepartment(DepartmentRequest department, Long id) {
        Optional<Department> editDepartment = departmentRepo.findById(id);
        if (editDepartment.isPresent()) {
            editDepartment.get().setDepartmentName(department.getDepartmentName());
            departmentRepo.save(editDepartment.get());
            return ResponseEntity.ok(new ResponseObject(HttpStatus.OK.toString(),"Edit Department successfully!",editDepartment));
        }
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(),"Error: Department is not exist!"));
    }
}
