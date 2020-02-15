package edu.study.util.crawler;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class VideoPageProcessor implements PageProcessor {

    private Site site;

    public static final String LIST_URL = "https://www.51voa.com/Learning_English_Videos_*";

    public static final String ITEM_URL = "https://www.51voa.com/VOA_Videos/*";

    public static final String DOMIN = "https://www.51voa.com";

    public VideoPageProcessor(){//构造函数
        site=Site.me().setRetryTimes(5).setSleepTime(1000).setCharset("utf-8");
        site.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.106 Safari/537.36");
    }

    @Override
    public void process(Page page) {//重载页面处理函数
        if(page.getUrl().regex(LIST_URL).match()){ // 列表页
            // 将列表里的所有item增加进去
            page.addTargetRequests(page.getHtml().xpath("//div[@class=\"List\"]/ul/li/a/@href").all());
            // 将新的列表增加进去
            page.addTargetRequests(page.getHtml().xpath("//div[@class=\"pagelist\"]/a/@href").all());
        }
        else if(page.getUrl().regex(ITEM_URL).match()){ // 具体视频页
            page.putField("name", page.getHtml().xpath("//div[@class=\"title\"]/text()").toString());
            page.putField("url", page.getHtml().xpath("//source/@src").toString());

            page.putField("description",DOMIN+
                    page.getHtml().xpath("//div[@class=\"Video_Content\"]/video/@poster").toString());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] arg){// test
        Spider.create(new VideoPageProcessor())
                .addUrl("https://www.51voa.com/Learning_English_Videos_12.html")
                .addPipeline(new ConsolePipeline())
                .run();
    }

}
