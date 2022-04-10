package com.project.web.service.serviceImp;

import com.project.web.repository.CategoryRepository;
import com.project.web.repository.DepartmentRepository;
import com.project.web.repository.IdeaRepository;
import com.project.web.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImp implements DashboardService {
    private final DepartmentRepository departRepo;
    private final CategoryRepository cateRepo;
    private final IdeaRepository ideaRepo;

    @Override
        public List<DepartmentRepository.IdeaChart> getIdeaDataForChart() {
        return departRepo.getIdeaChart();
    }

    @Override
    public List<CategoryRepository.CategoryChart> getIdeaOfCateDataForChart() {
        return cateRepo.getCateChart();
    }

    @Override
    public List<IdeaRepository.IdeaReport> getDataForReport() {
        return ideaRepo.getReport();
    }
}
