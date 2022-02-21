package com.project.web.service.serviceImp;

import com.project.web.model.Department;
import com.project.web.model.User;
import com.project.web.payload.response.MessageResponse;
import com.project.web.repository.DepartmentRepository;
import com.project.web.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DepartmentServiceImp implements DepartmentService {
    private final DepartmentRepository departmentRepo;

    @Override
    public List<Department> getAllDepartment() {
        return departmentRepo.findAll();
    }

    @Override
    public ResponseEntity<MessageResponse> addDepartment(Department department) {
        Boolean checkExisted = departmentRepo.existsByDepartmentName(department.getDepartmentName());
        if (!checkExisted) {
            departmentRepo.save(department);
            return ResponseEntity.ok(new MessageResponse("Add department successfully!"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Department name is already taken!"));
    }

    @Override
    public ResponseEntity<MessageResponse> deleteDepartment(Long id) {
        Optional<Department> deleteDepartment = departmentRepo.findById(id);
        if (deleteDepartment.isPresent()) {
            departmentRepo.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Delete department successfully!"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Department name is not exist!"));
    }

    @Override
    public ResponseEntity<MessageResponse> editDepartment(Department department, Long id) {
        Optional<Department> editDepartment = departmentRepo.findById(id);
        if (editDepartment.isPresent()) {
            editDepartment.get().setDepartmentName(department.getDepartmentName());
            departmentRepo.save(editDepartment.get());
            return ResponseEntity.ok(new MessageResponse("Edit user successfully!"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Department is not exist!"));
    }
}
