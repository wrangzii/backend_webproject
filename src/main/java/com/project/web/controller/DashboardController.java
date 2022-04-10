package com.project.web.controller;

import com.project.web.repository.CategoryRepository;
import com.project.web.repository.DepartmentRepository;
import com.project.web.repository.IdeaRepository;
import com.project.web.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardSer;

    @GetMapping("/idea")
    public List<DepartmentRepository.IdeaChart> getIdeaDataForChart() {
        return dashboardSer.getIdeaDataForChart();
    }

    @GetMapping("/category")
    public List<CategoryRepository.CategoryChart> getIdeaOfCateDataForChart() {
        return dashboardSer.getIdeaOfCateDataForChart();
    }

    @GetMapping("/report")
    public List<IdeaRepository.IdeaReport> getDataForReport(HttpServletResponse response) throws IOException {
        return dashboardSer.getDataForReport(response);
    }
}
