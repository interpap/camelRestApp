package com.camel.restapp.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.camel.BeanInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.camel.restapp.service.RepositoryService;

@Component
public class RepoProcessor implements Processor {
	
	@Autowired
	private RepositoryService service;
	
	public static final Logger logger = LogManager.getLogger(RepoProcessor.class.getName());	
	
	@Override
    public void process(Exchange exchange) throws Exception {
		
		//The response body
    	String body = exchange.getIn().getBody(String.class);
		
    	JSONObject jsonObject = new JSONObject(body);
    	JSONArray items = (JSONArray) jsonObject.getJSONArray("items");
    	
    	Map<String, String> repos = new HashMap<String, String>();
    	Map<String, Set<String>> map = new HashMap<String, Set<String>>();
    	Map<String, String> countMap = new HashMap<String, String>();
    	
    	for (int i = 0; i < items.length(); i++)
    	{ 
    		//Put the 100 repos and their languages in a map
    		repos.put(items.getJSONObject(i).get("html_url").toString(),items.getJSONObject(i).get("language").toString());
    	}
    	
    	for (Map.Entry<String,String> entry : repos.entrySet()) {
    		//This map to avoid duplicates values in repos map
    		map.put(entry.getValue(), getKeysByValue(repos,entry.getValue()));
    		
    		//Maps languages and nb of repos using
    		countMap.put(entry.getValue(),String.valueOf(Collections.frequency(repos.values(),entry.getValue())));
    	}
    	
    	StringBuilder sb = new StringBuilder();
    	
    	//Building the output
    	sb.append("<h3>List of languages used by the 100 trending public repos on GitHub 	: </h3>");
    	
    	for (Map.Entry<String,Set<String>> entry : map.entrySet())
    	{ 
    		String key = entry.getKey();
    		if(entry.getKey().equals("null"))
    			key = (String) entry.getKey().concat(" or empty repo");
    		sb.append("<br> <font size=\"+1\" > Language : <b>"+
    					key+"</b> <br> ------ Repos using this language : <b>"+
    					countMap.get(entry.getKey())+"</b><br> ------ List of repositories using this language : </font><br><b>"+
    					entry.getValue()+"</b><br><br>");
    		
    	}
    	
    	service.setOutput(sb.toString());
    	
    }
    
    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        Set<T> keys = new HashSet<T>();
        for (Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }
}
