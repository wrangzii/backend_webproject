package com.project.web.service;

import com.project.web.repository.CategoryRepository;
import com.project.web.repository.DepartmentRepository;
import com.project.web.repository.IdeaRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface DashboardService {
    List<DepartmentRepository.IdeaChart> getIdeaDataForChart();
    List<CategoryRepository.CategoryChart> getIdeaOfCateDataForChart();
    List<IdeaRepository.IdeaReport> getDataForReport(HttpServletResponse response) throws IOException;
    List<IdeaRepository.IdeaReport> getDataForReportObject(HttpServletResponse response) throws IOException;

}
