package com.shang.spray.common.processor;

import com.shang.spray.common.utils.Constant;
import com.shang.spray.entity.News;
import com.shang.spray.entity.Sources;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * info:知乎发现
 * Created by shang on 16/9/9.
 */
public class InfoQProcessor implements PageProcessor {

    private Site site = Site.me()
            .setDomain("infoq.com")
            .setSleepTime(100)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");

    public static final String list = "http://www.infoq.com/cn/news";

    @Override
    public void process(Page page) {
        if (page.getUrl().regex(list).match()) {
           // List<Selectable> list=page.getHtml().xpath("//div[@data-type='daily']/div[@class='explore-feed feed-item']").nodes();
            List<Selectable> list=page.getHtml().xpath("//div[@id='wrapper']/div/div[@id='content']/div[@class='news_type_block']").nodes();
           // page page_news /ul[@class='l l_large']/li
            
            for (Selectable s : list) {
               // String title=s.xpath("//h2/a/text()").toString();
                String title=s.xpath("//h2/a/text()").toString();
                String link=s.xpath("//h2/a").links().toString();
                if (StringUtils.isNotEmpty(title) && StringUtils.isNotEmpty(link)) {
                    News news = new News();
                    news.setTitle(title);
                    news.setInfo(title);
                    news.setLink(link);
                    news.setTypeId(Constant.Type_InfoQ);
                    news.setSources(new Sources(Constant.Sources_InfoQ));
                    page.putField("news" + title, news);
                }
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider spider=Spider.create(new InfoQProcessor());
        spider.addUrl("http://www.infoq.com/cn/news");
//        spider.addPipeline(new NewsPipeline());
        spider.thread(5);
        spider.setExitWhenComplete(true);
        spider.start();
    }
}
