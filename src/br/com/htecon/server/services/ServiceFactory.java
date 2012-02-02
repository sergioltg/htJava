package br.com.htecon.server.services;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import br.com.htecon.context.ApplicationContextProvider;

public class ServiceFactory {
	
	public static ApplicationContext ctx = null; 

	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}
	
	public static BasicServiceServer getBasicService() {
		return (BasicServiceServer)getBean("BasicServiceServer");
	}
	
	
	private static ApplicationContext getApplicationContext() {
		if (ctx == null) {
			if (ApplicationContextProvider.ctx != null) {
				ctx = ApplicationContextProvider.ctx;
			} else {
				ctx = new FileSystemXmlApplicationContext("classpath:/applicationContext.xml");
			}
		}
		
		return ctx;		
	}
	
	
}
