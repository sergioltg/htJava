package br.com.htecon.server.services;


import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;

import br.com.htecon.persistent.ExEntity;
import flex.messaging.io.BeanProxy;

public class EntityProxy extends BeanProxy {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public Object getValue(Object instance,
            String propertyName) {
		
//		Class propertyType = BeanUtils.getPropertyDescriptor(instance.getClass(), propertyName).getPropertyType();
		
		Object value;
		try {
			value = PropertyUtils.getProperty(instance, propertyName);
			
			if (value instanceof Collection || value instanceof ExEntity) {
				if (!Hibernate.isInitialized(value)) {
					PropertyUtils.setProperty(instance, propertyName, null);
					value = null;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		
		}		
		return super.getValue(instance, propertyName);
	}
	
	private boolean isNumber(Class clazz) {
		return clazz.equals(Long.class) || clazz.equals(Double.class) || clazz.equals(Integer.class);		
	}
	
	private boolean isDate(Class clazz) {
		return clazz.equals(Date.class);
	}
	
	private boolean isNulo(Object value) {
		if (value.getClass().equals(Long.class)) {
			if (value.equals(0L)) {
				return true;
			}
		} else if (value.getClass().equals(Integer.class)) {
			if (value.equals(0)) {
				return true;
			}
		} else if (value.getClass().equals(Double.class)) {
			if (value.equals(0D) || ((Double)value).isNaN()) {
				return true;
			}
		}
		
		return false;
	}

	public void setValue(Object instance,
            String propertyName, Object value) {
		
		if (value != null) {	
			PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(instance.getClass(), propertyName);
			if (propertyDescriptor != null) {
				Class propertyType = propertyDescriptor.getPropertyType();
				
				if (isNumber(propertyType) && isNulo(value)) {
					value = null;				
				} else if (isDate(propertyType) && value != null) {
					Date date = (Date)value;
					Date newDate = new Date(date.getTime() - TimeZone.getDefault().getOffset(date.getTime()));
					value = newDate;
				}
			}
		}
		
		super.setValue(instance, propertyName, value);		
	}
	
	

	

}
