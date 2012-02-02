package br.com.htecon.server;

import java.util.List;

import br.com.htecon.persistent.ExEntity;
import br.com.htecon.server.services.EntityProxy;
import br.com.htecon.server.services.ServiceFactory;
import flex.messaging.io.PropertyProxyRegistry;

public class BasicRemote {
	
	static {		
		PropertyProxyRegistry registry = PropertyProxyRegistry.getRegistry();
		registry.register(ExEntity.class, new EntityProxy());
	}	
	
	public BasicRemote() {
		
	}	

	public List findAll(ExEntity ex, String criteriaString) {
		return ServiceFactory.getBasicService().find(ex, criteriaString);
	}
	
	
	public List deleteAll(List data) {
		return ServiceFactory.getBasicService().delete(data);
	}	
	
	public List saveAll(List data) {
		return ServiceFactory.getBasicService().save(data);
	}
	
	public String getParametro(String nmParametro) {
		return ServiceFactory.getBasicService().getParametro(nmParametro);
	}	


}

