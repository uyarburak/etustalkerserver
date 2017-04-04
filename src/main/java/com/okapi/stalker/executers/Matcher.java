package com.okapi.stalker.executers;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Sets;
import com.okapi.stalker.data.storage.model.Course;
import com.okapi.stalker.data.storage.model.Department;
import com.okapi.stalker.data.storage.model.Instructor;
import com.okapi.stalker.data.storage.model.Student;
import com.okapi.stalker.util.MyStringUtil;

public class Matcher extends Matchable{
	// Kodu biraz kirletelim.
	boolean intervalsHasChanged;
	boolean studentsHasChanged;
	boolean departmentsHasChanged;
	boolean instructorsHasChanged;
	boolean coursesHasChanged;
	boolean sectionsHasChanged;
	Parser parser;
	DatabaseReader reader;
	public Matcher() {
		super();
		parser = new Parser();
		reader = new DatabaseReader();
	}
	public boolean match(){
		if(parser.parse() && reader.read()){
			return matchExecute();
		}else{
			System.err.println("Hata!");
			System.exit(0);
		}
		return true;
	}
	private boolean matchExecute(){
		studentsHasChanged = matchStudents();
		intervalsHasChanged = matchIntervals();
		instructorsHasChanged = matchInstructors();
		coursesHasChanged = matchCourses();
		departmentsHasChanged = matchDepartments();
		sectionsHasChanged = matchSections();
		parser.printSizes();
		reader.printSizes();
		printSizes();
		boolean changed = studentsHasChanged || intervalsHasChanged || instructorsHasChanged || coursesHasChanged || departmentsHasChanged || sectionsHasChanged;
		return !changed;
	}
	
	private boolean matchIntervals(){
		this.intervals = parser.intervals;
		return !Sets.difference(parser.intervals, reader.intervals).isEmpty() || !Sets.difference(reader.intervals, parser.intervals).isEmpty();
	}
	
	private boolean matchDepartments(){
		this.departments = parser.departments;
		for (Entry<String, Department> entry : parser.departments.entrySet()) {
			String key = entry.getKey();
			Department department = entry.getValue();
			if(reader.departments.containsKey(key)){
				Department old = reader.departments.get(key);
				if(department.getMainURL() == null && old.getMainURL() != null){
					department.setMainURL(old.getMainURL());
				}
				if(department.getFaculty() == null && old.getFaculty() != null){
					department.setFaculty(old.getFaculty());
				}
			}
		}
		return parser.departments.size() != reader.departments.size();
	}
	
	private boolean matchSections(){
		this.sections = parser.sections;
		return parser.sections.size() != reader.sections.size();
	}
	
	private boolean matchCourses(){
		this.courses = parser.courses;
		Set<String> parserCourses = parser.courses.keySet();
		Set<String> readerCourses = reader.courses.keySet();
		boolean changed = !Sets.difference(parserCourses, readerCourses).isEmpty() || !Sets.difference(readerCourses, parserCourses).isEmpty();
		return changed;
	}
	
	private boolean matchInstructors(){
		// hocalar degistiginde cinsiyet haric hicbir veri korunmasin
		boolean changed = false;
		for(Entry<String, Instructor> entry : parser.instructors.entrySet()){
			String key = entry.getKey();
			Instructor old = MyStringUtil.getIns(key, reader.instructors);
			if(old != null){
				Instructor fresh = entry.getValue();
				fresh.setGender(old.getGender());
			}else{
				System.out.println("Instructor farklı: " + key);
				changed = true;
			}
		}
		for(Entry<String, Instructor> entry : reader.instructors.entrySet()){
			String key = entry.getKey();
			Instructor fresh = MyStringUtil.getIns(key, parser.instructors);
			if(fresh == null){
				System.out.println("Instructor farklı: " + key);
				changed = true;
				break;
			}
		}
		this.instructors = parser.instructors;
		System.out.println("match instructors: " + changed);
		return changed;
	}
	
	private boolean matchStudents(){
		boolean changed = false;
		// yenilerdekiler birebir ayni kalacak, cinsiyetleri eskiden alinacak.
		for(Entry<String, Student> entry : parser.students.entrySet()){
			String key = entry.getKey();
			if(reader.students.containsKey(key)){
				Student old = reader.students.get(key);
				Student fresh = entry.getValue();
				fresh.setGender(old.getGender());
			}else{
				changed = true; // yeni ogrenci gelmis
				System.out.println("Yeni ogrenci: " + entry.getValue().getId());
			}
		}
		// Yeni duzende gozukmeyen ogrencilerin aktifligini 0 yapalim
		// Ve yeni duzene ekleyelim
		for(Entry<String, Student> entry : reader.students.entrySet()){
			String key = entry.getKey();
			if(!parser.students.containsKey(key)){
				Student old = entry.getValue();
				old.setActive(false); // artik olmayan bir ogrenci
				old.reset(); // eskiden olan derslerindne ayirmamiz gerekecek
				this.students.put(key, old);
			}
		}
		this.students.putAll(parser.students);
		if(!changed){
			for(Entry<String, Student> entry : parser.students.entrySet()){
				String key = entry.getKey();
				Student old = reader.students.get(key);
				Student fresh = entry.getValue();
				if(!Sets.difference(old.getSections(), fresh.getSections()).isEmpty() || !Sets.difference(fresh.getSections(), old.getSections()).isEmpty())
					return true;
			}
		}
		System.out.println("match student: " + changed);
		return changed;
	}
}
