package datn.com.cosmetics.services;

import java.util.List;

import datn.com.cosmetics.bean.response.CategoryRevenueDTO;
import datn.com.cosmetics.bean.response.MonthlyRevenueDTO;

public interface IReportService {
    List<MonthlyRevenueDTO> calculateMonthlyRevenue(Integer year);
    List<CategoryRevenueDTO> calculateCategoryRevenue(Integer month, Integer year);
}
