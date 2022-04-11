package com.project.web.service.serviceImp;

import com.project.web.payload.response.IdeaReport;
import com.project.web.repository.CategoryRepository;
import com.project.web.repository.DepartmentRepository;
import com.project.web.repository.IdeaRepository;
import com.project.web.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public List<IdeaRepository.IdeaReport> getDataForReport(HttpServletResponse response) throws IOException {
        List<IdeaRepository.IdeaReport> ideas = ideaRepo.getReport();
        IdeaReport data = new IdeaReport();
        List<IdeaReport> dataExport = new ArrayList<>();
        for (IdeaRepository.IdeaReport idea : ideas) {
            data.setIsAnonymous(idea.getIsAnonymous());
            data.setIdea(idea.getTitle());
            data.setCategory(idea.getCategory());
            data.setSubmission(idea.getSubmission());
            data.setTotalComment(idea.getCount());

            dataExport.add(data);
        }
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ideas_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Is anonymous idea?", "Idea", "Submission", "Category", "Total comments"};
        String[] nameMapping = {"isAnonymous", "idea", "submission", "category", "totalComment"};
        csvWriter.writeHeader(csvHeader);

        for (IdeaReport idea : dataExport) {
            csvWriter.write(idea, nameMapping);
        }
        csvWriter.close();

        return null;
    }

    @Override
    public List<IdeaRepository.IdeaReport> getDataForReportObject(HttpServletResponse response) {
        List<IdeaRepository.IdeaReport> ideas = ideaRepo.getReport();
        if (ideas.size() > 0) {
            return ideas;
        }
        return null;
    }
}
