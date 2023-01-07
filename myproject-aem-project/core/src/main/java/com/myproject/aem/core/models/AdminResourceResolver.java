package com.myproject.aem.core.models;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;

@Component(service = AdminResourceResolver.class)
public class AdminResourceResolver {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    public ResourceResolver getAdminResourceResolver() throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, "mySampleSystemUser");
        return resourceResolverFactory.getServiceResourceResolver(param);
    }
}
