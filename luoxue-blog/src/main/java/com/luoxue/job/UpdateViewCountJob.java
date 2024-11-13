package com.luoxue.job;

import com.luoxue.domin.entity.Article;
import com.luoxue.service.ArticleService;
import com.luoxue.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;
    @Scheduled(cron = "0 0/5 * * * *")
    public void updateViewCount() {
        //获取redis中的数据
        Map<String, Integer> ViewCountMap = redisCache.getCacheMap("article:viewCount");
        List<Article> articles = ViewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //更新到数据库中
        articleService.updateBatchById(articles);
    }
}
