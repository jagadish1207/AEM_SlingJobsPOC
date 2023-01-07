package com.myproject.aem.core.jobs;

import com.myproject.aem.core.models.AdminResourceResolver;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.apache.sling.event.jobs.consumer.JobExecutionContext;
import org.apache.sling.event.jobs.consumer.JobExecutionResult;
import org.apache.sling.event.jobs.consumer.JobExecutor;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component(service = JobExecutor.class,property = {
        JobExecutor.PROPERTY_TOPICS + "=my/sample/job"
})
public class MySampleJobExecutor implements JobExecutor {
    @Reference
    ResourceResolverFactory resolverFactory;

    @Reference
    private AdminResourceResolver adminResourceResolver;
    ResourceResolver rr;

    @Override
    public JobExecutionResult process(Job job, JobExecutionContext jobExecutionContext) {
        try {
            rr = adminResourceResolver.getAdminResourceResolver();
            Resource res = rr.getResource("/content/mysampleproject/us");

            System.out.println(job.getQueueName());
            String prop1 = job.getProperty("item1", String.class);
            Integer prop2 = job.getProperty("count", Integer.class);
            setJobDetailsInNode(job, rr);
            //TimeUnit.MINUTES.sleep(2);
            setJobDetailsInNode(job, rr);
        }catch (Exception e){
            rr.close();
            return jobExecutionContext.result().message("YOUR JOB IS FAILED").failed();
        }
        rr.close();
        return jobExecutionContext.result().message("YOUR JOB IS SUCCESSFUL").succeeded();
    }

    private void setJobDetailsInNode(Job job,ResourceResolver rr) throws RepositoryException {
        Session adminSession = rr.adaptTo(Session.class);
        Node jobNode = adminSession.getNode("/content/myJobDetails");
        Node rootNode = adminSession.getRootNode();
        String[] name = job.getId().split("/");
        String nodename = name[name.length-1];
        Node newJobNode;
        if(rootNode.hasNode("content/myJobDetails/"+nodename)){
            newJobNode = adminSession.getNode("/content/myJobDetails/"+nodename);
            newJobNode.setProperty("progress","COMPLETED");
        }else{
            newJobNode = jobNode.addNode(nodename);
            newJobNode.setProperty("progress",""+job.getJobState());
            newJobNode.setProperty("jobId",""+job.getId());
        }
        adminSession.save();
    }
}
