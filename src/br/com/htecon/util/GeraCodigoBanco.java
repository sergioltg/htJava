package br.com.htecon.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Query;
import org.hibernate.Session;

public class GeraCodigoBanco {

	private static String NM_TABELAGERACODIGO = "ESEGCODIGO";

	private EntityManager entityManager;

	private String entidade;
	
	private String entidadeGeraCodigo;
	
	private String campoChave;

	private ArrayList<String> valuesChave = new ArrayList<String>();
	private ArrayList<String> namesChave = new ArrayList<String>();	

	public GeraCodigoBanco(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	public void clearChaves() {
		valuesChave.clear();
	}

	public void addChave(String vlChave, String name) {
		valuesChave.add(vlChave);
		namesChave.add(name);
	}

	public Integer geraCodigo() {
		
		if (entidadeGeraCodigo == null) {
			entidadeGeraCodigo = entidade;
		}

		Session session = (Session) entityManager.getDelegate();
		
		StringBuilder sb = new StringBuilder();
		sb.append("select vlCodigo from " + NM_TABELAGERACODIGO + " where 1=1 ");
		for (int n = 0; n < valuesChave.size(); n++) {
			sb.append("and vlChave" + (n + 1) + " = '" + valuesChave.get(n) + "'");
		}
		sb.append(" and nmTabela = :nmTabela ");
		sb.append(" for update ");

		Query query = session.createSQLQuery(sb.toString());
		
		query.setParameter("nmTabela", entidadeGeraCodigo);

		List resultList = query.list();
		
		if (resultList.size() == 0) {			
			
			StringBuilder sbSelectMax = new StringBuilder();
			
			sbSelectMax.append(" select max(" + campoChave + ") from " + entidade);
			sbSelectMax.append(" where 1=1 ");
			for (int n = 0; n < valuesChave.size(); n++) {
				sbSelectMax.append(" and " + namesChave.get(n) + " = '" + valuesChave.get(n) + "'");
			}
			
			query = session.createSQLQuery(sbSelectMax.toString());
			
			Integer codigoMax;			
			
			Object objetoCodigo = query.uniqueResult();
			if (objetoCodigo instanceof BigDecimal) {
				codigoMax = ((BigDecimal)objetoCodigo).intValue();
			} else {
				codigoMax = (Integer)objetoCodigo;
			}
			
			if (codigoMax != null) {
				codigoMax++;
			} else {
				codigoMax = 1;
			}
			
			StringBuilder sbInsert = new StringBuilder();
			sbInsert.append("insert into " + NM_TABELAGERACODIGO);
			sbInsert.append(" (nmTabela, ");
			for (int n = 1; n <= valuesChave.size(); n++) {
				sbInsert.append("vlChave" + n + ", ");
			}			
			sbInsert.append("vlCodigo)");
			sbInsert.append(" values ");
			
			sbInsert.append(" (:nmTabela, ");
			for (int n = 1; n <= valuesChave.size(); n++) {
				sbInsert.append(":vlChave" + n + ", ");
			}			
			sbInsert.append(":vlCodigo)");
			
			query = session.createSQLQuery(sbInsert.toString());
			query.setParameter("nmTabela", entidadeGeraCodigo);
			for (int n = 0; n < valuesChave.size(); n++) {
				query.setParameter("vlChave" + (n + 1), valuesChave.get(n));				
			}
			query.setParameter("vlCodigo", codigoMax);
			query.executeUpdate();
			
			return codigoMax;
		} else {
			Integer codigo;
			if (resultList.get(0) instanceof BigDecimal) {
				codigo = ((BigDecimal)resultList.get(0)).intValue() + 1;
			} else {
				codigo = (Integer)resultList.get(0) + 1; 
			}
			
			StringBuilder sbUpdate = new StringBuilder();
			sbUpdate.append("update " + NM_TABELAGERACODIGO);
			sbUpdate.append(" set vlCodigo = :vlCodigo");
			sbUpdate.append(" where 1=1 ");
			for (int n = 0; n < valuesChave.size(); n++) {
				sbUpdate.append("and vlChave" + (n + 1) + " = '" + valuesChave.get(n) + "'");
			}
			sbUpdate.append(" and nmTabela = :nmTabela ");
			query = session.createSQLQuery(sbUpdate.toString());
			query.setParameter("nmTabela", entidadeGeraCodigo);
			query.setParameter("vlCodigo", codigo);
			query.executeUpdate();			
			
			return codigo;
		}
	}

	public String getEntidade() {
		return entidade;
	}

	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}

	public String getCampoChave() {
		return campoChave;
	}

	public void setCampoChave(String campoChave) {
		this.campoChave = campoChave;
	}

	public void setEntidadeGeraCodigo(String entidadeGeraCodigo) {
		this.entidadeGeraCodigo = entidadeGeraCodigo;
	}

	public String getEntidadeGeraCodigo() {
		return entidadeGeraCodigo;
	}
	
	

}
