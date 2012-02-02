package br.com.htecon.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.beanutils.PropertyUtils;

import br.com.htecon.persistent.ExEntity;

public class EntityDependentes {

	public List<List<ExEntity>> dependentes = new ArrayList<List<ExEntity>>();
	private List<JoinColumn[]> joinColumns = new ArrayList<JoinColumn[]>();

	public void carregaDependentes(ExEntity entity) throws Exception {

		dependentes.clear();
		joinColumns.clear();

		ArrayList<Field> fields = getFieldsOnetoMany(entity);

		int nTotal = fields.size();

		for (int n = 0; n < nTotal; n++) {
			Field field = fields.get(n);
			Object valueP = PropertyUtils.getSimpleProperty(entity, field.getName());
			if (valueP instanceof List) {
				List lista = (List) valueP;
				joinColumns.add(getJoinColumns(field));				
				dependentes.add(lista);
			} else if (valueP instanceof ExEntity) {
				ExEntity exEntity = (ExEntity)valueP;
				if (exEntity.isStatusDeleted() || exEntity.isStatusInserted() || exEntity.isStatusUpdated()) {
					List<ExEntity> lista = new ArrayList<ExEntity>();
					lista.add(exEntity);
					dependentes.add(lista);
					joinColumns.add(getJoinColumns(field));					
				}
			}
			PropertyUtils.setSimpleProperty(entity, field.getName(), null);
		}
	}

	public void setaChavePai(ExEntity entity) throws Exception {
		
		int nTotal = dependentes.size();
		
		for (int n = 0; n < nTotal; n++) {
			List lista = dependentes.get(n);
			JoinColumn[] colunas = joinColumns.get(n);
			for (Object objEntity : lista) {
				ExEntity entityDependente = (ExEntity) objEntity;
				ArrayList<Field> fieldsID = getFieldsID(entityDependente);
				int x = 0;
				for (JoinColumn joinColumn : colunas) {
					PropertyUtils.setSimpleProperty(entityDependente,
							fieldsID.get(x++).getName(), PropertyUtils.getSimpleProperty(
									entity, joinColumn.name()));
				}
			}			
		}
	}

	public JoinColumn[] getJoinColumns(Field field) {
		if (field.isAnnotationPresent(JoinColumn.class)) {
			return new JoinColumn[] { field.getAnnotation(JoinColumn.class) };
		} else if (field.isAnnotationPresent(JoinColumns.class)) {
			return field.getAnnotation(JoinColumns.class).value();

		} else {
			return null;
		}
	}

	private ArrayList<Field> getFieldsOnetoMany(ExEntity entity) {
		ArrayList<Field> retorno = new ArrayList<Field>();

		Field[] arrayFields = entity.getClass().getDeclaredFields();

		for (int n = 0; n < arrayFields.length; n++) {
			if (arrayFields[n].isAnnotationPresent(OneToMany.class) || arrayFields[n].isAnnotationPresent(ManyToOne.class)) {
				retorno.add(arrayFields[n]);
			}
		}

		return retorno;
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
