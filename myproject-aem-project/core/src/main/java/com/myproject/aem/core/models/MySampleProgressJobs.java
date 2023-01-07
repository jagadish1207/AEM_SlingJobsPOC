package com.myproject.aem.core.models;

import com.drew.lang.StringUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.event.jobs.Queue;
import org.apache.sling.event.jobs.Statistics;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Map;

@Model(adaptables = { SlingHttpServletRequest.class,
        Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MySampleProgressJobs {
    @OSGiService
    JobManager jobManager;

    private String jobsInQueue = "";

    @PostConstruct
    public void init() {
        ResourceResolver resolver = null;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(ResourceResolverFactory.SUBSERVICE, "mySampleSystemUser");
        Queue mySampleQueue = jobManager.getQueue("mySampleJobQueue");
        Statistics myStats = mySampleQueue.getStatistics();
        jobsInQueue = ""+myStats.getNumberOfActiveJobs();
    }

    public String getJobsInQueue() {
        return jobsInQueue;
    }
}
