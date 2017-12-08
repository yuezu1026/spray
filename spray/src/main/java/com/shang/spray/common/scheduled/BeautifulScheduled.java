package com.shang.spray.common.scheduled;

import com.shang.spray.common.processor.WallProcessor;
import com.shang.spray.entity.Config;
import com.shang.spray.pipeline.BeautifulPipeline;
import com.shang.spray.service.ConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * info:美图定时任务
 * Created by shang on 16/8/30.
 */
@Component
public class BeautifulScheduled {

    @Autowired
    private BeautifulPipeline beautifulPipeline;
    @Autowired
    private ConfigService configService;

    /**
     * 3G壁纸站
     */
    ////[秒] [分] [小时] [日] [月] [周] [年]
    @Scheduled(cron = "0 0 0/4 * * ? ")//从0点开始,每4个小时执行一次
    public void wallScheduled() {
        Specification<Config> specification=new Specification<Config>() {
            @Override
            public Predicate toPredicate(Root<Config> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                criteriaQuery.where(criteriaBuilder.equal(root.get("code"),"WallScheduled"));
                return null;
            }
        };
        Config config=configService.findOne(specification);
        if (config != null && config.getContent().equals("1")) {
            Specification<Config> specification1=new Specification<Config>() {
                @Override
                public Predicate toPredicate(Root<Config> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    criteriaQuery.where(criteriaBuilder.equal(root.get("code"),"WallScheduledNum"));
                    return null;
                }
            };
            Config config1=configService.findOne(specification1);
            if (config1 != null && StringUtils.isNotEmpty(config.getContent())) {
                System.out.println("----开始执行3G壁纸站定时任务");
                Spider spider=Spider.create(new WallProcessor());
                int num=Integer.valueOf(config1.getContent())+1;
                System.out.println(config1.getContent());
                spider.addUrl("http://www.3gbizhi.com/lists-%E5%85%A8%E9%83%A8/"+num+".html");
                spider.addPipeline(beautifulPipeline);
                spider.thread(5);
                spider.setExitWhenComplete(true);
                spider.start();
                spider.stop();
                config1.setContent(num+"");
                configService.save(config1);
            }
        }

    }
}
