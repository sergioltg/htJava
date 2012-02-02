package br.com.htecon.server.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import br.com.htecon.persistent.ExEntity;
import br.com.htecon.server.services.dao.IBasicDAO;
import flex.messaging.io.PropertyProxyRegistry;

@Transactional(readOnly = true)
public class BasicServiceServer {
	
	protected IBasicDAO basicDAO;	

	public IBasicDAO getBasicDAO() {
		return basicDAO;
	}

	public void setBasicDAO(IBasicDAO basicDAO) {
		this.basicDAO = basicDAO;
	}

	public List find(ExEntity entity) {
		return basicDAO.find(entity);		
	}
	
	public ExEntity findUnique(ExEntity entity) {
		return basicDAO.findUnique(entity);		
	}	
	
	public List find(ExEntity entity, String criteriaString) {
		return basicDAO.find(entity, criteriaString);		
	}	

	@Transactional(readOnly = false)
	public List save(List<ExEntity> list) {
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
	public List delete(List<ExEntity> list) {
		return basicDAO.delete(list);
	}
	
	
	public String getParametro(String nmParametro) {
		return basicDAO.getParametro(nmParametro);		
	}
		
}
