package com.soaringroad.blog.api.visit;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.soaringroad.blog.service.SiteMapService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/sitemap")
@Slf4j
public class SiteMapApi {

    @Autowired
    private SiteMapService siteMapService;
    
    @RequestMapping(value = {"","/"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void siteMap(HttpServletResponse response) {
        Document doc = siteMapService.generateSiteMap();
        try {
            XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
            writer.setOutputStream(response.getOutputStream());
            writer.write(doc);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
