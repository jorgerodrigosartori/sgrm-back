package br.com.sartori.sgrm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilData {
	
	public static Date converteData(String data, String formato) {
		
		if(data == null || formato == null)
			return null;
		
		SimpleDateFormat sf = new SimpleDateFormat(formato); 
		try {
			return sf.parse(data);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String converteData(Date data, String formato) {
		
		if(data == null || formato == null)
			return null;
		
		SimpleDateFormat sf = new SimpleDateFormat(formato); 
		return sf.format(data);
	}
}
