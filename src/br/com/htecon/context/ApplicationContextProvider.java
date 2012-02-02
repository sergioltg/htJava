package br.com.htecon.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextProvider implements ApplicationContextAware {
	
	public static ApplicationContext ctx;
	
	 public void setApplicationContext(ApplicationContext _ctx) throws BeansException { 
	        ctx = _ctx;
    }
	

}
