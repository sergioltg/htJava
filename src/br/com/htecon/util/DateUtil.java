package br.com.htecon.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	public static Long getDifferenceInDays(Calendar cal1, Calendar cal2) {
		long milis1 = cal1.getTimeInMillis();
		long milis2 = cal2.getTimeInMillis();
		long diff = milis2 - milis1;
		long diffDays = diff / (24 * 60 * 60 * 1000);
		return diffDays;
	}
	
	public static Date proximaDataUtil(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		
		while (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return calendar.getTime();
	}

	public static Date stringToDate(String date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.parse(date);
	}
	
	public static Date stringToDateTime(String date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return format.parse(date);
	}
	
	public static String dateToString(Date date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(date);
	}
	
	public static String getDataExtensoFull(Date data) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		int mes = calendar.get(Calendar.MONTH) + 1;
		int dia = calendar.get(Calendar.DAY_OF_MONTH);
		int ano = calendar.get(Calendar.YEAR);

		return Extenso.converte(dia) + " de " + Util.getMesExtenso(mes) + " de " + Extenso.converte(ano);
	}
	

}
