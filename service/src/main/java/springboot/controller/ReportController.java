package springboot.controller;

import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.dao.MovieDao;
import springboot.service.MovieService;
import springboot.service.ReportService;
import springboot.token.annotation.UserLoginToken;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/report")
public class ReportController extends ApiController {
    @Autowired
    private ReportService reportService;

    @UserLoginToken
    @GetMapping("/getReport")
    public R getReport(){
        return success(reportService.getReport());
    }
}
