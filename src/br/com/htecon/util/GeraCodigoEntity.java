package br.com.htecon.util;

import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;

import br.com.htecon.persistent.ExEntity;

public class GeraCodigoEntity {
	
	private GeraCodigoBanco geraCodigoBanco;
	
	public GeraCodigoEntity(EntityManager entityManager) {
		geraCodigoBanco = new GeraCodigoBanco(entityManager);		
	}
	
	public void geraCodigo(ExEntity entity) {		
		geraCodigoBanco.setEntidade(getTable(entity));		
		geraCodigoBanco.clearChaves();
		
		ArrayList<Field> fields = getFieldsID(entity);
		
		int nTotal = fields.size();
		
		geraCodigoBanco.setCampoChave(fields.get(nTotal - 1).getName());		
		
		// Verifica se a chave ja ta gerada
		try {
			if (BeanUtils.getProperty(entity, fields.get(nTotal - 1).getName()) != null) {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (int n = 0; n < nTotal - 1; n++) {
			try {
				geraCodigoBanco.addChave(BeanUtils.getProperty(entity, fields.get(n).getName()), fields.get(n).getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			BeanUtils.setProperty(entity, fields.get(nTotal - 1).getName(), geraCodigoBanco.geraCodigo());
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private String getTable(ExEntity entity) {
		if (entity.getClass().isAnnotationPresent(Table.class)) {
			Table table = entity.getClass().getAnnotation(Table.class);
			return table.name();
		} else {
			return null;
		}
	}
	
	private ArrayList<Field> getFieldsID(ExEntity entity) {
		ArrayList<Field> retorno = new ArrayList<Field>();
		
		Field[] arrayFields = entity.getClass().getDeclaredFields(); 
		
		for (int n = 0; n < arrayFields.length; n++) {
			if (arrayFields[n].isAnnotationPresent(Id.class)) {
				retorno.add(arrayFields[n]);
			}
		}
		
		return retorno;		
	}

}
