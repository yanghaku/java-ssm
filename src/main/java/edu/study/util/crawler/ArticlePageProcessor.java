package edu.study.util.crawler;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

public class ArticlePageProcessor implements PageProcessor {

    private Site site;

    public ArticlePageProcessor(){// 构造函数
        site=Site.me().setRetryTimes(5).setSleepTime(1000).setCharset("gb2312");
        site.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.106 Safari/537.36");
    }


    private static final String LIST_URL="http://www.hxen.com/englishnews/index*";
    //private static final String ITEM_URL="http://www.hxen.com/englishnews/nation*";

    // 页面的处理函数
    @Override
    public void process(Page page) {
        if(page.getUrl().regex(LIST_URL).match()){// list
            // add 所有list的链接
            page.addTargetRequests(page.getHtml().xpath("//div[@class=\"pageBar fr \"]/a/@href").all());

            // add list 里面的所有item的链接
            page.addTargetRequests(page.getHtml().xpath("//div[@class=\"newsListBar\"]/div/ul/li/div/div/h3/a/@href").all());
        }
        else{// item
            String title=page.getHtml().xpath("//div[@class=\"arcinfo center\"]/b/h1/text()").toString();
            if(title==null || title.isEmpty())return;
            if (title.contains(":")) {// 如果有冒号，就去除前面的中文
                page.putField("title", title.substring(title.indexOf(':') + 1, title.length()));
            } else if (title.contains("：")) {// 还有可能是中文冒号
                page.putField("title", title.substring(title.indexOf("：") + 1, title.length()));
            } else page.putField("title", title);
            List<String> allP = page.getHtml().xpath("//div[@id=\"arctext\"]/p/text()").all();
            StringBuilder content = new StringBuilder();
            for (String s : allP) {
                content.append("\n\n").append(s);
            }
            if (content.length() == 0) return;
            page.putField("content",content.toString());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] arg){// for test
        String list="http://www.hxen.com/englishnews/index.html";
        String item="http://www.hxen.com/englishnews/sports/2020-02-15/531590.html";
        Spider.create(new ArticlePageProcessor())
                .addUrl(item)
                .addPipeline(new ConsolePipeline())
                .run();
    }

}
