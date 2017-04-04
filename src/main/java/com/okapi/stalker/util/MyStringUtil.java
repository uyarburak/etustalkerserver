package com.okapi.stalker.util;
import java.util.Map;

import com.okapi.stalker.data.storage.model.Instructor;


public class MyStringUtil {

	public static Instructor getIns(String insName, Map<String, Instructor> map){
		insName = insName.toLowerCase().replaceAll("\\.", "");
		for (String string : map.keySet()) {
			if(benzetKontrolEt(insName, string.toLowerCase())){
				return map.get(string);
			}
		}
		return null;
	}
	private static boolean benzetKontrolEt(String s1, String s2){
		s1 = s1.replace('ı', 'i').replace('ş', 's').replace('ç', 'c').replace('ğ', 'g').replace('ö', 'o').replace('ü', 'u');
		s2 = s2.replace('ı', 'i').replace('ş', 's').replace('ç', 'c').replace('ğ', 'g').replace('ö', 'o').replace('ü', 'u');
		return isSubSequence(s1, s2);
	}

	public static boolean isSubSequence(String s1, String s2){
		
	    // Base Cases
	    if (s1.isEmpty()) return true;
	    if (s2.isEmpty()) return false;
	 
	    // If last characters of two strings are matching
	    if(s1.charAt(s1.length()-1) == s2.charAt(s2.length()-1))
	    	return isSubSequence(s1.substring(0,  s1.length()-1), s2.substring(0,  s2.length()-1)); 
	 
	    // If last characters are not matching
	    return isSubSequence(s1, s2.substring(0,  s2.length()-1)); 
	}
}
