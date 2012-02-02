package br.com.htecon.util;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.HibernateException;
import org.hibernate.transform.ResultTransformer;

public class AliasToBeanNestedResultTransformer implements ResultTransformer {

	private final Class resultClass;
	private final String[] attributes;

	public AliasToBeanNestedResultTransformer(Class resultClass,
			String[] attributes) {
		super();
		this.resultClass = resultClass;
		this.attributes = attributes;
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {

		Object result;

		try {
			result = resultClass.newInstance();

			for (int n = 0; n < aliases.length; n++) {
				initialisePath(result, attributes[n]);
				PropertyUtils.setProperty(result, attributes[n], tuple[n]);
			}

		} catch (InstantiationException e) {
			throw new HibernateException("Could not instantiate resultclass: "
					+ resultClass.getName());
		} catch (IllegalAccessException e) {
			throw new HibernateException("Could not instantiate resultclass: "
					+ resultClass.getName());
		} catch (InvocationTargetException e) {
			throw new HibernateException("Could not instantiate resultclass: "
					+ resultClass.getName());
		} catch (NoSuchMethodException e) {
			throw new HibernateException("Could not instantiate resultclass: "
					+ resultClass.getName());
		}

		return result;
	}

	@Override
	public List transformList(List arg0) {
		return arg0;
	}

	private void initialisePath(final Object bean, final String fieldName)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, InstantiationException {
		int dot = fieldName.indexOf('.');
		while (dot >= 0) {
			String attributeName = fieldName.substring(0, dot);
			Class attributeClass = PropertyUtils.getPropertyType(bean,
					attributeName);
			if (PropertyUtils.getProperty(bean, attributeName) == null) {
				PropertyUtils.setProperty(bean, attributeName, attributeClass
						.newInstance());
			}
			dot = fieldName.indexOf('.', dot + 1);
		}
	}

}
