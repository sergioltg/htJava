package br.com.htecon.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Util {

	public static Object getProperty(Object object, String property) {
		String nmproperty = property.substring(0, 1).toUpperCase()
				+ property.substring(1);

		Method m;
		try {
			m = object.getClass().getMethod("get" + nmproperty);
			Object value = m.invoke(object);
			return value;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Integer getDia(Date data) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(data);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	public static Integer getMes(Date data) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(data);
		return calendar.get(Calendar.MONTH) + 1;
	}

	public static Integer getAno(Date data) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(data);
		return calendar.get(Calendar.YEAR);
	}

	public static byte[] zipFiles(File[] files, String[] names) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		byte[] buf = new byte[1024];
		ZipOutputStream out = new ZipOutputStream(bos);
		try {
			int n = 0;
			for (File file : files) {
				FileInputStream in = new FileInputStream(file);
				String nameFile;
				if (names != null) {
					nameFile = names[n++];
				} else {
					nameFile = file.getName();
				}				
				out.putNextEntry(new ZipEntry(nameFile));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
		} finally {
			out.close();
		}

		return bos.toByteArray();
	}

	public static String modulo11(String s_aux) {

		int d1, d2;
		d1 = 0;
		int multi = 2;
		int soma = 0;
		for (int n_Count = s_aux.length() - 1; n_Count > -1; n_Count--) {
			int carac = Integer.parseInt(s_aux.substring(n_Count, n_Count + 1));
			int temp = carac * multi;
			soma += temp;
			multi++;

		}

		soma = soma * 10;

		d1 = soma % 11;
		if (d1 == 10)
			d1 = 1;

		s_aux += d1;

		soma = 0;
		multi = 2;
		for (int n_Count = s_aux.length() - 1; n_Count > -1; n_Count--) {
			int carac = Integer.parseInt(s_aux.substring(n_Count, n_Count + 1));
			int temp = carac * multi;
			soma += temp;
			multi++;
		}

		soma = soma * 10;

		d2 = soma % 11;
		if (d2 == 10)
			d2 = 1;
		return "" + d1 + "" + d2;
	}

	public static List removeDuplicates(List lista) {
		int nLast = lista.size() - 1;
		Object lastObject = null;
		for (int n = nLast; n >= 0; n--) {
			if (lastObject == lista.get(n)) {
				lista.remove(n);
			} else {
				lastObject = lista.get(n);
			}
		}
		return lista;
	}

	public static String getDataExtenso(Date data) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(data);
		int mes = calendar.get(Calendar.MONTH) + 1;
		int dia = calendar.get(Calendar.DAY_OF_MONTH);
		int ano = calendar.get(Calendar.YEAR);
		return dia + " de " + getMesExtenso(mes) + " de " + ano;
	}

	public static String getMesExtenso(Integer mes) {
		String mesf = "";
		switch (mes) {
		case 1:
			mesf = "Janeiro";
			break;
		case 2:
			mesf = "Fevereiro";
			break;
		case 3:
			mesf = "Março";
			break;
		case 4:
			mesf = "Abril";
			break;
		case 5:
			mesf = "Maio";
			break;
		case 6:
			mesf = "Junho";
			break;
		case 7:
			mesf = "Julho";
			break;
		case 8:
			mesf = "Agosto";
			break;
		case 9:
			mesf = "Setembro";
			break;
		case 10:
			mesf = "Outubro";
			break;
		case 11:
			mesf = "Novembro";
			break;
		case 12:
			mesf = "Dezembro";
			break;
		}
		return mesf;
	}

	public static String getUFExtenso(String uf) {
		String retorno = "";
		if ("AC".equals(uf)) {
			retorno = "Acre";
		} else if ("AL".equals(uf)) {
			retorno = "Alagoas";
		} else if ("AP".equals(uf)) {
			retorno = "Amapá";
		} else if ("AM".equals(uf)) {
			retorno = "Amazonas";
		} else if ("BA".equals(uf)) {
			retorno = "Bahia";
		} else if ("CE".equals(uf)) {
			retorno = "Ceará";
		} else if ("DF".equals(uf)) {
			retorno = "Distrito Federal";
		} else if ("ES".equals(uf)) {
			retorno = "Espírito Santo";
		} else if ("GO".equals(uf)) {
			retorno = "Goiás";
		} else if ("MA".equals(uf)) {
			retorno = "Maranhão";
		} else if ("MT".equals(uf)) {
			retorno = "Mato Grosso";
		} else if ("MS".equals(uf)) {
			retorno = "Mato Grosso do Sul";
		} else if ("MG".equals(uf)) {
			retorno = "Minas Gerais";
		} else if ("PA".equals(uf)) {
			retorno = "Pará";
		} else if ("PB".equals(uf)) {
			retorno = "Paraíba";
		} else if ("PR".equals(uf)) {
			retorno = "Paraná";
		} else if ("PE".equals(uf)) {
			retorno = "Pernambuco";
		} else if ("PI".equals(uf)) {
			retorno = "Piauí";
		} else if ("RJ".equals(uf)) {
			retorno = "Rio de Janeiro";
		} else if ("RN".equals(uf)) {
			retorno = "Rio Grande do Norte";
		} else if ("RS".equals(uf)) {
			retorno = "Rio Grande do Sul";
		} else if ("RO".equals(uf)) {
			retorno = "Rondônia";
		} else if ("RR".equals(uf)) {
			retorno = "Roraima";
		} else if ("SC".equals(uf)) {
			retorno = "Santa Catarina";
		} else if ("SP".equals(uf)) {
			retorno = "São Paulo";
		} else if ("SE".equals(uf)) {
			retorno = "Sergipe";
		} else if ("TO".equals(uf)) {
			retorno = "Tocantins";
		} else if ("BR".equals(uf)) {
			retorno = "Brasil";
		} else if ("IG".equals(uf)) {
			retorno = "Ignorado";
		} else if ("ET".equals(uf)) {
			retorno = "Estrangeiro";			
		}

		return retorno;
	}
	
	public static String getUFAuxiliar(String uf) {
		String retorno = "";
		if ("AC".equals(uf)) {
			retorno = "do";
		} else if ("AL".equals(uf)) {
			retorno = "da";
		} else if ("AP".equals(uf)) {
			retorno = "do";
		} else if ("AM".equals(uf)) {
			retorno = "do";
		} else if ("BA".equals(uf)) {
			retorno = "da";
		} else if ("CE".equals(uf)) {
			retorno = "do";
		} else if ("DF".equals(uf)) {
			retorno = "do";
		} else if ("ES".equals(uf)) {
			retorno = "do";
		} else if ("GO".equals(uf)) {
			retorno = "do";
		} else if ("MA".equals(uf)) {
			retorno = "do";
		} else if ("MT".equals(uf)) {
			retorno = "do";
		} else if ("MS".equals(uf)) {
			retorno = "do";
		} else if ("MG".equals(uf)) {
			retorno = "de";
		} else if ("PA".equals(uf)) {
			retorno = "do";
		} else if ("PB".equals(uf)) {
			retorno = "da";
		} else if ("PR".equals(uf)) {
			retorno = "do";
		} else if ("PE".equals(uf)) {
			retorno = "de";
		} else if ("PI".equals(uf)) {
			retorno = "do";
		} else if ("RJ".equals(uf)) {
			retorno = "do";
		} else if ("RN".equals(uf)) {
			retorno = "do";
		} else if ("RS".equals(uf)) {
			retorno = "do";
		} else if ("RO".equals(uf)) {
			retorno = "de";
		} else if ("RR".equals(uf)) {
			retorno = "de";
		} else if ("SC".equals(uf)) {
			retorno = "de";
		} else if ("SP".equals(uf)) {
			retorno = "de";
		} else if ("SE".equals(uf)) {
			retorno = "de";
		} else if ("TO".equals(uf)) {
			retorno = "do";
		}

		return retorno;
	}
	
	public static int[] retornaTotalHora(String horaInicial, String horaFinal) {
		String[] splitHora = horaInicial.split(":");
		int nuHorainicio = Integer.parseInt(splitHora[0]);
		int nuMininicio = Integer.parseInt(splitHora[1]);
	
		splitHora = horaFinal.split(":");
		int nuHorafim = Integer.parseInt(splitHora[0]);
		int nuMinfim = Integer.parseInt(splitHora[1]);	
	
		int totalHora = nuHorafim - nuHorainicio;
		int totalMin = nuMinfim - nuMininicio;
	
		if (totalMin < 0) {
			totalHora--;
			totalMin = 60 + totalMin;
		}
		
		return new int[] {totalHora, totalMin};
	}
	

}
