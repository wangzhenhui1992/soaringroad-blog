package com.soaringroad.blog.service.impl;

import com.soaringroad.blog.common.DataManager;
import com.soaringroad.blog.entity.Article;
import com.soaringroad.blog.service.SiteMapService;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SiteMapServiceImpl implements SiteMapService {

    @Autowired
    private DataManager<Article,Long> articleManager;

    @Override
    public Document generateSiteMap() {
        Iterable<Article> itr = articleManager.findAll();
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("urlset", "https://www.sitemaps.org/schemas/sitemap/0.9");
        Element host = root.addElement("url");
        host.addElement("loc").setText("https://www.soaringroad.com");
        host.addElement("changefreq").setText("daily");
        host.addElement("priority").setText("1.0");
        Set<String> categories = new HashSet<String>();
        Set<String> labels = new HashSet<String>();
        for (Article article : itr) {
            Element url = root.addElement("url");
            url.addElement("loc").setText("https://www.soaringroad.com/article/" + article.getId());
//            Element lastmod = url.addElement("lastmod");
            url.addElement("changefreq").setText("daily");
            url.addElement("priority").setText("0.8");
            categories.add(article.getCategory());
            labels.addAll(article.getLabels());
        }
        for (String category : categories) {
            Element url = root.addElement("url");
            url.addElement("loc").setText("https://www.soaringroad.com/result/category/" + category);
//            Element lastmod = url.addElement("lastmod");
            url.addElement("changefreq").setText("daily");
            url.addElement("priority").setText("0.5");
        }

        for (String label : labels) {
            Element url = root.addElement("url");
            url.addElement("loc").setText("https://www.soaringroad.com/result/label/" + label);
//            Element lastmod = url.addElement("lastmod");
            url.addElement("changefreq").setText("daily");
            url.addElement("priority").setText("0.5");
        }
        return doc;
    }

}
