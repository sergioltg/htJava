package br.com.htecon.server.services.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

import br.com.htecon.persistent.ExEntity;
import br.com.htecon.util.CriteriaUtils;
import br.com.htecon.util.EntityDependentes;
import br.com.htecon.util.GeraCodigoEntity;

public class BasicDAO implements IBasicDAO {

	@PersistenceContext
	protected EntityManager entityManager;
	
	public ExEntity findUnique(ExEntity entity) {
		List list = find(entity);
		if (list.size() > 0) {
			return (ExEntity)list.get(0);
		} else {
			return null;
		}
	}

	public List find(ExEntity entity) {
		return find(entity, null);	
	}
	
	public void sessionClear() {
		Session session = (Session) entityManager.getDelegate();		
		session.clear();
	}
	
	@Override
	public List find(ExEntity entity, String criteriaString) {
		Session session = (Session) entityManager.getDelegate();

		Criteria criteria = session.createCriteria(entity.getClass());

		try {
			CriteriaUtils.addCriterias(entity, criteria);
			if (criteriaString != null) {
				CriteriaUtils.addCriterias(criteriaString, criteria);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);		

		return criteria.list();
	}	

	// todo Esta rotina precisa ser mudada, quando nao for lazy vai dar erro -
	// Restruturar codigo - todo
	public List save(List list) {
		Session session = (Session) entityManager.getDelegate();
		try {
			GeraCodigoEntity geraCodigo = new GeraCodigoEntity(entityManager);
			EntityDependentes entityDependentes = new EntityDependentes();
			int nTotal = list.size();
			for (int n = 0; n < nTotal; n++) {
				ExEntity entity = (ExEntity) list.get(n);
				if (entity.isStatusInserted()) {
					entityDependentes.carregaDependentes(entity);
					geraCodigo.geraCodigo(entity);
					session.persist(entity);
					session.flush();
					entityDependentes.setaChavePai(entity);
					salvaDependentes(entityDependentes.dependentes);
					session.refresh(entity);
					list.set(n, entity);
				} else if (entity.isStatusUpdated()) {
					entityDependentes.carregaDependentes(entity);
					entityDependentes.setaChavePai(entity);
					salvaDependentes(entityDependentes.dependentes);
					entity = (ExEntity) session.merge(entity);
					session.flush();
					session.refresh(entity);
					list.set(n, entity);
				} else if (entity.isStatusDeleted()) {
					entityDependentes.carregaDependentes(entity);
					entity = (ExEntity) session.merge(entity);
					session.delete(entity);
					list.remove(n);
					nTotal--;
					n--;
				}
				entity.setStatus(null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		session.flush();

		return list;
	}

	protected void salvaDependentes(List<List<ExEntity>> lista) {
		for (List<ExEntity> l : lista) {
			save(l);
		}
	}

	public List delete(List list) {
		Session session = (Session) entityManager.getDelegate();

		EntityDependentes entityDependentes = new EntityDependentes();

		try {

			for (Object object : list) {
				ExEntity entity = (ExEntity) object;
				entityDependentes.carregaDependentes(entity);
				session.delete(session.merge(entity));
			}

			session.flush();

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void persist(Object object) {
		Session session = (Session) entityManager.getDelegate();
		session.persist(object);
		session.flush();
	}

	public void merge(Object object) {
		Session session = (Session) entityManager.getDelegate();
		session.merge(object);
		session.flush();
	}

	public String getParametro(String nmParametro) {
		Session session = (Session) entityManager.getDelegate();

		Query query = session
				.createSQLQuery("SELECT c.vlParametro FROM esegParametro c "
						+ "where c.nmParametro = :nmParametro ");
		query.setParameter("nmParametro", nmParametro);

		return (String) query.uniqueResult();
	}


}
