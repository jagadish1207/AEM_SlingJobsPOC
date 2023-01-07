package com.myproject.aem.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(service = {Servlet.class},property={"sling.servlet.methods=get", "sling.servlet.paths=/bin/mergedataWithAcroform"})
public class MySampleServelt extends SlingSafeMethodsServlet {
    @Reference
    JobManager jobManager;

    public void startJob(){
        //null
    }

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse res) throws IOException {
        final Map<String,Object> props= new HashMap<String,Object>();
        props.put("item1","/hello/123");
        props.put("count",5);
        jobManager.addJob("my/sample/job",props);
        res.getWriter().write("servlet is working");
    }

}
