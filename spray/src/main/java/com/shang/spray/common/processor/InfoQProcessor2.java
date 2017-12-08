package com.shang.spray.common.processor;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.shang.spray.common.utils.Constant;
import com.shang.spray.entity.News;
import com.shang.spray.entity.Sources;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

/**
 * info:知乎发现
 * Created by shang on 16/9/9.
 */
public class InfoQProcessor2 implements PageProcessor {

    private Site site = Site.me()
            .setDomain("infoq.com")
            .setSleepTime(100)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");

    public static final String list = "http://www.infoq.com/cn/search.action?queryString=java&page=1&searchOrder=&sst=l4jI6oY5Inh6WnSL";

    
    public void process2(Page page) {
        if (page.getUrl().regex(list).match()) {
            List<Selectable> list=page.getHtml().xpath("//div[@id='wrapper']/div/div[@id='content']/div[@class='news_type_block']").nodes();
            
            for (Selectable s : list) {
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
    public void process(Page page) {
        List<String> requests = page.getHtml().links().regex(".*java.*").all();
        page.addTargetRequests(requests);
        
        String title=page.getHtml().xpath("//div[@id='content']/h1[@class='general']/text()").toString();
        String content = page.getHtml().xpath("//div[@id='content']/div[@class='article_page_left news_container text_content_container']/tidyText()").toString();
        if(title != null){
        	page.putField("title",title);
        	page.putField("content",content); 
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new InfoQProcessor2()).
        addUrl("http://www.infoq.com/cn").
        thread(5).
        setExitWhenComplete(true).
        start();
        
        
    }
}
