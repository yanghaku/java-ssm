package edu.study.crawler;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class SpiderTest implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setCharset("utf-8");


    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        if (!page.getUrl().regex("http://www.cnblogs.com/[a-z 0-9 -]+/p/[0-9]{7}.html").match()) {
            page.addTargetRequests(
                    page.getHtml().xpath("//*[@id=\"mainContent\"]/div/div/div[@class=\"postTitle\"]/a/@href").all());
        } else {
            page.putField(page.getHtml().xpath("//*[@id=\"cb_post_title_url\"]/text()").toString(),
                    page.getHtml().xpath("//*[@id=\"cb_post_title_url\"]/@href").toString());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider spider=Spider.create(new VideoPageProcessor());
        spider.addUrl("https://www.cnblogs.com/justcooooode/");
        //spider.thread(5); //5个线程
        spider.addPipeline(new ConsolePipeline());//设置pipeline 为控制台
        spider.run();//启动爬虫
    }
}
