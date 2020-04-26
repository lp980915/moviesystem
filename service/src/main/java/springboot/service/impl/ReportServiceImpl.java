package springboot.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.bean.Movie;
import springboot.bean.Report;
import springboot.dao.MovieDao;
import springboot.service.ReportService;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {
    @Resource
    private MovieDao movieDao;
    @Override
    public Object getReport() {
        List<Report> reportList=movieDao.getTopMovie();
        for(Report report:reportList){
            report.setCollectNum(movieDao.getCollectNumByMovieid(report))
            .setCommentNum(movieDao.getCommentNumByMovieid(report))
            .setMoviescore(movieDao.getMoviescoreByMovieid(report));
        }
        System.out.println("reportList为"+reportList);
        return reportList;
    }
}
