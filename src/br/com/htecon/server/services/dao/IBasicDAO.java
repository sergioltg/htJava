package br.com.htecon.server.services.dao;

import java.util.List;

import br.com.htecon.persistent.ExEntity;

public interface IBasicDAO {
	
	public List find(ExEntity entity);
	public List find(ExEntity entity, String criteriaString);	
	public List save(List list);
	public List delete(List list);
	public ExEntity findUnique(ExEntity entity);
	
	public String getParametro(String nmParametro);

}
