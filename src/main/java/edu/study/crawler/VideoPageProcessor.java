package edu.study.crawler;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class VideoPageProcessor implements PageProcessor {

    private Site site;

    public VideoPageProcessor(){//构造函数
        site=Site.me().setRetryTimes(3).setSleepTime(1000).setCharset("utf-8");
        site.setUserAgent("Spider");
    }

    @Override
    public void process(Page page) {//重载页面处理函数
        page.putField("name",page.getHtml().xpath("//div[@class=\"title\"]/text()").toString());
        page.putField("url",page.getHtml().xpath("//source/@src").toString());
    }

    @Override
    public Site getSite() {
        return site;
    }

}
