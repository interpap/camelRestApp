package com.camel.restapp.config;


import com.camel.restapp.processor.RepoProcessor;
import com.camel.restapp.service.RepositoryService;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class ReposController {

    @Autowired
    private RepoProcessor processor;
    
    @Autowired
    private RepositoryService service;
    
    @RequestMapping("/")
    public String repos(){
    	return "<h3>Welcome , <br><br> This is 	a REST microservice that list the languages used by the 100 trending public repos on GitHub. <br><br> ENDPOINT : <a href=\"http://localhost:8080/getReposByLanguages\">/getReposByLanguages</a> </h3>";
    }
    
    @RequestMapping("/getReposByLanguages")
    public String reposByLanaguages() throws Exception {
    	
    	CamelContext context = new DefaultCamelContext();
    	context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() {
            	
            	 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
        	     LocalDateTime date = LocalDateTime.now().minusDays(1);  
        	        
        	     //This route fetches the 100 trending repos using today's date
        	     from("direct:getrepos")
	                .to("https://api.github.com/search/repositories?q=created:>"+dtf.format(date)+"&sort=stars&order=desc&per_page=100")
	                .log("Response code from the GET REPOS operation was: ${header.CamelHttpResponseCode}")
	                //to process the response body  
	                .process(processor);
            }
        });
    	
        context.start();
        ProducerTemplate producerTemplate = context.createProducerTemplate();
        
        // Set the body to null, because it is required by the GitHub API.
        producerTemplate.sendBody("direct:getrepos",null);
    	
    	 return service.getOutput();
    }
    
}
