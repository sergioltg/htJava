package br.com.htecon.server.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import flex.messaging.io.PropertyProxyRegistry;

import br.com.htecon.persistent.ExEntity;
import br.com.htecon.server.services.dao.IBasicDAO;

@Transactional(readOnly = true)
public class BasicService {
	
	static {		
		PropertyProxyRegistry registry = PropertyProxyRegistry.getRegistry();
		registry.register(ExEntity.class, new EntityProxy());
	}	
	
	protected IBasicDAO basicDAO;	

	public IBasicDAO getBasicDAO() {
		return basicDAO;
	}

	@Resource(name="BasicDAO")
	public void setBasicDAO(IBasicDAO basicDAO) {
		this.basicDAO = basicDAO;
	}

	public List findAll(ExEntity entity) {
		return basicDAO.find(entity);		
	}
	
	public ExEntity findUnique(ExEntity entity) {
		return basicDAO.findUnique(entity);		
	}	
	
	public List findAll(ExEntity entity, String criteriaString) {
		System.out.println("here");
		return basicDAO.find(entity, criteriaString);		
	}	

	@Transactional(readOnly = false)
	public List saveAll(List<ExEntity> list) {
		return basicDAO.save(list);
	}
	
	@Transactional(readOnly = false)
	public ExEntity save(ExEntity entity) {
		List save = new ArrayList();
		save.add(entity);
		save = basicDAO.save(save);
		if (save.size() > 0) {
			return (ExEntity)save.get(0);
		} else {
			return null;
		}
	}	
	
	@Transactional(readOnly = false)
	public List deleteAll(List<ExEntity> list) {
		return basicDAO.delete(list);
	}
	
	
	public String getParametro(String nmParametro) {
		return basicDAO.getParametro(nmParametro);		
	}
		
}
