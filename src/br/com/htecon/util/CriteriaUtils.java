package br.com.htecon.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import br.com.htecon.annotations.Search;
import br.com.htecon.annotations.SearchPolicy;

public class CriteriaUtils {

	public static void addCriterias(Object entity, Criteria criteria)
			throws Exception {
		Class c = entity.getClass();
		Method methods[] = c.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().substring(0, 3).equals("get")) {
				int modifiers = methods[i].getModifiers();
				if (Modifier.isPublic(modifiers)) {
					if (methods[i].getReturnType() == Integer.class
							|| methods[i].getReturnType() == Long.class
							|| methods[i].getReturnType() == String.class
							|| methods[i].getReturnType() == Date.class) {

						String nmPropriedade = methods[i].getName()
								.substring(4);
						String first = methods[i].getName().substring(3, 4);
						nmPropriedade = first.toLowerCase() + nmPropriedade;

						Field fieldPropriedade = null;
						try {
							fieldPropriedade = c
									.getDeclaredField(nmPropriedade);
						} catch (NoSuchFieldException e) {
							continue;
						}

						Object value = methods[i].invoke(entity, null);

						if (value != null) {
							if (methods[i].getReturnType() == String.class) {
								boolean useLike = true;
								if (fieldPropriedade
										.isAnnotationPresent(Search.class)) {
									Search searchAnnotation = (Search) fieldPropriedade
											.getAnnotation(Search.class);
									useLike = searchAnnotation.value().equals(
											SearchPolicy.LIKE);
								}
								if (useLike) {
									criteria.add(Restrictions.ilike(
											nmPropriedade, (String) value,
											MatchMode.ANYWHERE));
								} else {
									criteria.add(Restrictions.eq(nmPropriedade,
											(String) value).ignoreCase());
								}
							} else {
								if (methods[i].getReturnType() == Long.class) {
									if (value.equals(0L)) {
										value = null;
									}
								}
								if (methods[i].getReturnType() == Integer.class) {
									if (value.equals(0)) {
										value = null;
									}
								}
								if (methods[i].getReturnType() == Date.class) {
									((Date) value).setHours(0);
									((Date) value).setMinutes(0);
									((Date) value).setSeconds(0);
								}
								if (value != null) {
									criteria.add(Restrictions.eq(nmPropriedade,
											value));
								}
							}
						}
					}
				}
			}
		}
	}

	public static void addCriterias(String criteriaString, Criteria criteria)
			throws Exception {

		String[] objects = criteriaString.split(";");

		for (String object : objects) {
			String[] parts = object.split(":");
			if ("alias".equalsIgnoreCase(parts[0])) {
				criteria.createAlias(parts[1], parts[1], getJoinType(parts[2]))
						.setFetchMode(parts[1], FetchMode.EAGER);
			} else if ("order".equalsIgnoreCase(parts[0])) {
				if ("true".equalsIgnoreCase(parts[2])) {
					criteria.addOrder(Order.asc(parts[1]));
				} else {
					criteria.addOrder(Order.desc(parts[1]));
				}
			} else if ("SimpleExpression".equalsIgnoreCase(parts[0])) {
				String type = parts[1];
				String propertyName = parts[2];
				Object value = getValueAsType(parts[3], parts[4]);
				if (value instanceof String) {
					value = URLDecoder.decode((String) value);
				}
				SimpleExpression simpleExpression = null;
				if ("eq".equals(type)) {
					simpleExpression = Restrictions.eq(propertyName, value);
				} else if ("ne".equals(type)) {
					simpleExpression = Restrictions.ne(propertyName, value);
				} else if ("ge".equals(type)) {
					simpleExpression = Restrictions.ge(propertyName, value);
				} else if ("gt".equals(type)) {
					simpleExpression = Restrictions.gt(propertyName, value);
				} else if ("le".equals(type)) {
					simpleExpression = Restrictions.le(propertyName, value);
				} else if ("lt".equals(type)) {
					simpleExpression = Restrictions.lt(propertyName, value);
				} else if ("like".equals(type)) {
					simpleExpression = Restrictions.like(propertyName,
							(String) value, getMatchMode(parts[6]));
				}
				if (simpleExpression != null) {
					if ("true".equals(parts[5])) {
						simpleExpression.ignoreCase();
					}
					criteria.add(simpleExpression);
				}
			}
		}
	}

	private static Object getValueAsType(String type, String value)
			throws Exception {
		if ("date".equals(type)) {
			return DateUtil.stringToDate(value);
		} else if ("string".equals(type)) {
			return value;
		} else if ("int".equals(type)) {
			return new Long(value);
		} else if ("number".equals(type)) {
			return new Double(value);
		} else
			return value;
	}

	private static MatchMode getMatchMode(String match) {
		if ("ANYWHERE".equals(match)) {
			return MatchMode.ANYWHERE;
		}
		if ("START".equals(match)) {
			return MatchMode.START;
		}
		if ("END".equals(match)) {
			return MatchMode.END;
		}
		if ("EXACT".equals(match)) {
			return MatchMode.EXACT;
		} else
			return MatchMode.ANYWHERE;
	}

	private static int getJoinType(String joinType) {
		if ("FULLJOIN".equals(joinType)) {
			return CriteriaSpecification.FULL_JOIN;
		} else if ("LEFTJOIN".equals(joinType)) {
			return CriteriaSpecification.LEFT_JOIN;
		} else {
			return CriteriaSpecification.INNER_JOIN;
		}
	}

}
