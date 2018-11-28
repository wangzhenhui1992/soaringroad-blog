package com.soaringroad.blog.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.soaringroad.blog.entity.common.Article;
import com.soaringroad.blog.repository.h2.ArticleH2Repository;
import com.soaringroad.blog.service.SiteMapService;

@Component
public class SiteMapServiceImpl implements SiteMapService {

    @Autowired
    private ArticleH2Repository articleRepo;

    @Override
    public Document generateSiteMap() {
        Iterable<Article> itr = articleRepo.findAll();
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("urlset");
        root.addNamespace("", "http://www.sitemaps.org/schemas/sitemap/0.9");
//        root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
//        root.addAttribute("xsi:schemaLocation",
//                "http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd");
        Element host = root.addElement("url");
        host.addElement("loc").setText("http://www.soaringroad.com");
        host.addElement("changefreq").setText("daily");
        host.addElement("priority").setText("1.0");
        Set<String> categories = new HashSet<String>();
        Set<String> labels = new HashSet<String>();
        for (Article article : itr) {
            Element url = root.addElement("url");
            url.addElement("loc").setText("http://wwww.soaringroad.com/article/" + article.getId());
//            Element lastmod = url.addElement("lastmod");
            url.addElement("changefreq").setText("daily");
            url.addElement("priority").setText("0.8");
            categories.add(article.getCategory());
            labels.addAll(article.getLabels());
        }
        for (String category : categories) {
            Element url = root.addElement("url");
            url.addElement("loc").setText("http://wwww.soaringroad.com/result/category/" + category);
//            Element lastmod = url.addElement("lastmod");
            url.addElement("changefreq").setText("weekly");
            url.addElement("priority").setText("0.8");
        }

        for (String label : labels) {
            Element url = root.addElement("url");
            url.addElement("loc").setText("http://wwww.soaringroad.com/result/label/" + label);
//            Element lastmod = url.addElement("lastmod");
            url.addElement("changefreq").setText("weekly");
            url.addElement("priority").setText("0.8");
        }
        return doc;
    }

}
