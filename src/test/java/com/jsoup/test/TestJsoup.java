package com.jsoup.test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by liubing on 2015/11/24.
 */
public class TestJsoup
{
    private Map<String, String> hrefs = new HashMap<String, String>();

    /**
     * 爬虫
     */
    @Test
    public void TestJsoup(){
        Document document = parse("http://www.baidu.com");
        crawler(document);
    }

    /**
     * 解析url为document对象
     * @param url
     * @return
     */
    private Document parse(String url){
        Document document = null;
        try {
            Connection connection = Jsoup.connect( url );
            document = connection.get();
        } catch (IOException e) {

        }
        return document;
    }

    /**
     * 爬虫，循环获取页面中a连接页面中的a连接
     * @param document
     */
    private void crawler(Document document){
        if(document != null){
            String title = document.title();
            Elements elements = document.select("a[href]");
            Iterator<Element> iterator = elements.iterator();
            while (iterator.hasNext()){
                Element element = iterator.next();
                run(title, element);
            }
        }
    }

    /**
     * 爬虫是否接着奔跑
     * @param title
     * @param a
     */
    private void run(String title, Element a){
        String name = a.html();
        String href = a.attr("href");
        if(isHref(href) && !hrefs.containsKey(href)){
            resultHandle(title, name, href);
            crawler(parse(href));
        }
    }

    /**
     * 处理结果
     * @param title
     * @param aName
     * @param aHref
     */
    private void resultHandle(String title, String aName, String aHref){
        hrefs.put(aHref, aName);
        System.out.println(title + "[" + aName+ ":" + aHref + "]");
    }

    /**
     * 判断是否是正确的连接地址
     * @param href
     * @return
     */
    private boolean isHref(String href){
        boolean is = false;
        try {
            URL url = new URL(href);
            is = true;
        } catch (MalformedURLException e) {
            is = false;
        }
        return is;
    }


}
