package com.okapi.stalker.data;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.okapi.stalker.data.storage.model.Room;
import com.okapi.stalker.data.storage.model.Department;
import com.okapi.stalker.data.storage.model.Instructor;

public class StalkerPOST {

	private static String httpPost(String path, String data) {
		StringBuilder page = new StringBuilder("");
		try {
			URL url = new URL(path);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "ISO8859-9"));
			String line;
			while ((line = rd.readLine()) != null) {
				page.append(line+"\n");
			}
			wr.close();
			rd.close();
		} 
		catch (Exception e) {
			System.err.println(e);
		}
		return page.toString();
	}

	public static String getHtml(String path) {
		StringBuilder page = new StringBuilder("");
		try {
			URL url = new URL(path);
			URLConnection conn = url.openConnection();

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = rd.readLine()) != null) {
				page.append(line+"\n");
			}
			rd.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
			//System.err.println(e);
		}
		return page.toString();
	}
	
	public static String getStudentProgram(String studentId) {
		try {
			String data = URLEncoder.encode("ogrencino", "UTF-8") + "=" + URLEncoder.encode(studentId, "UTF-8");
			data += "&" + URLEncoder.encode("btn_ogrenci", "UTF-8") + "=" + URLEncoder.encode("Program� G�ster", "UTF-8");
			return httpPost("http://kayit.etu.edu.tr/Ders/Ders_prg.php", data);
		}
		catch (Exception e) {
			System.err.println("student program exception:  " + e);
		}
		return "";
	}

	public static String getCourseStudentList(String courseId, String subeNo) {
		try {
			String data = URLEncoder.encode("dd_ders", "UTF-8") + "=" + URLEncoder.encode(courseId, "UTF-8");
			data += "&" + URLEncoder.encode("sube", "UTF-8") + "=" + URLEncoder.encode(subeNo, "UTF-8");
			data += "&" + URLEncoder.encode("btn_sube", "UTF-8") + "=" + URLEncoder.encode("�ube Listesini G�ster", "UTF-8");
			return httpPost("http://kayit.etu.edu.tr/Ders/Ders_prg.php", data);
		}
		catch (Exception e) {
			System.err.println("course program exception:  " + e);
		}
		return "";
	}

	public static String getCourseProgramList(String courseId, String subeNo) {
		try {
			String data = URLEncoder.encode("dd_ders", "UTF-8") + "=" + URLEncoder.encode(courseId, "UTF-8");
			data += "&" + URLEncoder.encode("sube", "UTF-8") + "=" + URLEncoder.encode(subeNo, "UTF-8");
			data += "&" + URLEncoder.encode("btn_ders", "UTF-8") + "=" + URLEncoder.encode("Se�ili Dersin Program�n� G�ster", "UTF-8");
			return httpPost("http://kayit.etu.edu.tr/Ders/Ders_prg.php", data);
		}
		catch (Exception e) {
			System.err.println("course program exception:  " + e);
		}
		return "";
	}

	public static String getProfessorProgram(String profId) {
		try {
			String data = URLEncoder.encode("dd_hoca", "UTF-8") + "=" + URLEncoder.encode(profId, "UTF-8");
			data += "&" + URLEncoder.encode("btn_hoca", "UTF-8") + "=" + URLEncoder.encode("Program� G�ster", "UTF-8");
			return httpPost("http://kayit.etu.edu.tr/Ders/Ders_prg.php", data);
		}
		catch (Exception e) {
			System.err.println("proffesor program exception:  " + e);
		}
		return "";
	}

//	public static HashSet<Section> getSectionsForInstructor(Instructor instructor, HashSet<ClassRoom> classRooms, HashSet<Course> courses){
//		HashSet<Section> sections = new HashSet<Section>();
//		String program = getProfessorProgram(instructor.getId());
//		program = program.substring(program.indexOf("<tr>") + 2);
//		program = program.substring(program.indexOf("<tr>"), program.lastIndexOf("</tr>"));
//		for (int i = 0; i < 12; i++) {
//			program = program.substring(program.indexOf(":30-"));
//			for(int j = 0; j < 6; j++){
//				program = program.substring(program.indexOf("<center>") + 8);
//				if(program.charAt(0) == '-'){
//					continue;
//				}
//				else{
//					String ders = program.substring(0, program.indexOf("<br>"));
//					String dersCode = ders.substring(0, ders.indexOf('-'));
//					byte sectionNumber = Byte.parseByte(ders.substring(ders.indexOf('-') +1));
//					int iTd, iTr, i2Tr;
//					iTd = program.indexOf("<td>");
//					iTr = program.indexOf("<tr>");
//					i2Tr = program.indexOf("</tr>");
//					if(iTd == -1){
//						iTd = Integer.MAX_VALUE;
//					}
//					if(iTr == -1)
//						iTr = Integer.MAX_VALUE;
//
//					if(i2Tr == -1)
//						i2Tr = Integer.MAX_VALUE;
//
//					String derslik = program.substring(program.indexOf("Derslik:") + 8, Math.min(i2Tr, Math.min(iTd, iTr)));
//					derslik = derslik.trim();
//					if(derslik.contains("<br>")){
//						derslik = derslik.substring(0, derslik.indexOf("<br>"));
//					}
//					ClassRoom mClassRoom = new ClassRoom("", "");
//					for (ClassRoom classRoom : classRooms) {
//						if(classRoom.getName().equals(derslik)){
//							mClassRoom = classRoom;
//							break;
//						}
//					}
//					Course mCourse = new Course("", "", "");
//					for (Course course : courses) {
//						if(course.getCode().equals(dersCode)){
//							mCourse = course;
//							break;
//						}
//					}
//					Section mSection = null;
//					boolean var = false;
//					for (Section section : sections) {
//						if(section.getCourse().getCode().equals(dersCode) && section.getSectionNumber() == sectionNumber){
//							mSection = section;
//							var = true;
//							break;
//						}
//					}
//
//					Interval interval = new Interval(Interval.Time.values()[i], Interval.Day.values()[j], mClassRoom);
//					if(!var){
//						mSection = new Section(sectionNumber, mCourse, instructor);
//						sections.add(mSection);
//					}
//					mSection.addInterval(interval);
//					if(derslik.contains("<br>")){
//						derslik = derslik.substring(derslik.indexOf("<br>") + 4);
//						ders = derslik.substring(0, derslik.indexOf("<br>"));
//						dersCode = ders.substring(0, ders.indexOf('-'));
//						sectionNumber = Byte.parseByte(ders.substring(ders.indexOf('-') +1));
//
//						derslik = program.substring(program.indexOf("Derslik:") + 8);
//						derslik = derslik.trim();
//
//						mClassRoom = new ClassRoom("", "");
//						for (ClassRoom classRoom : classRooms) {
//							if(classRoom.getName().equals(classRoom)){
//								mClassRoom = classRoom;
//								break;
//							}
//						}
//						mCourse = new Course("", "", "");
//						for (Course course : courses) {
//							if(course.getCode().equals(dersCode)){
//								mCourse = course;
//								break;
//							}
//						}
//						mSection = null;
//						var = false;
//						for (Section section : sections) {
//							if(section.getCourse().getCode().equals(dersCode) && section.getSectionNumber() == sectionNumber){
//								mSection = section;
//								var = true;
//								break;
//							}
//						}
//
//						interval = new Interval(Interval.Time.values()[i], Interval.Day.values()[j], mClassRoom);
//						if(!var){
//							mSection = new Section(sectionNumber, mCourse, instructor);
//							sections.add(mSection);
//						}
//						mSection.addInterval(interval);
//					}
//				}
//			}
//		}
//		return sections;
//	}

	public static ArrayList<String> getCoursesIds(){
		ArrayList<String> list = new ArrayList<String>();
		String path = "http://kayit.etu.edu.tr/Ders/_Ders_prg_start.php";
		try {
			URL url = new URL(path);
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				if(line.contains("<b>Ders"))
					break;
			}
			line = line.substring(line.indexOf("<option value"), line.indexOf("</select>"));
			while(true){
				line = line.substring(14);
				list.add(line.substring(0, line.indexOf(">")));
				if(line.indexOf("<option") != -1)
					line = line.substring(line.indexOf("<option"));
				else
					break;
			}
		} catch (IOException e) {
		}

		return list;
	}

//	public static ArrayList<Course> getCourses(){
//		ArrayList<Course> courses = new ArrayList<Course>();
//		String path = "http://kayit.etu.edu.tr/Ders/_Ders_prg_start.php";
//		try {
//			URL url = new URL(path);
//			URLConnection conn = url.openConnection();
//			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			String line;
//			while ((line = rd.readLine()) != null) {
//				if(line.contains("<b>Ders"))
//					break;
//			}
//			line = line.substring(line.indexOf("<option value"), line.indexOf("</select>"));
//			while(true){
//
//				line = line.substring(14);
//				String id = line.substring(0, 7);
//				line = line.substring(8);
//				String code;
//				if(line.charAt(7) == ' '){
//					code = line.substring(0, 7);
//					line = line.substring(8);
//				}
//				else{
//					code = line.substring(0, 8);
//					line = line.substring(9);
//				}
//				String name = line.substring(0, line.indexOf("</option>"));
//				
//				courses.add(new Course(name.trim(), code, id));
//
//				if(line.indexOf("<option") != -1)
//					line = line.substring(line.indexOf("<option"));
//				else
//					break;
//			}
//		} catch (IOException e) {
//		}
//
//		return courses;
//	}
		public static Set<Department> getDepartments(){
			HashSet<Department> departments = new HashSet<Department>();
			String path = "http://kayit.etu.edu.tr/Ders/_Ders_prg_start.php";
			try {
				URL url = new URL(path);
				URLConnection conn = url.openConnection();
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "ISO8859-9"));
				String line;
				while ((line = rd.readLine()) != null) {
					if(line.contains("dd_bolum"))
						break;
				}
				line = line.substring(line.indexOf("<option value"), line.indexOf("</select>"));
				while(true){
	
					line = line.substring(14);
					String id = line.substring(0, line.indexOf(">"));
					line = line.substring(line.indexOf(">")+1);
					String name = line.substring(0, line.indexOf("</option>"));
					Department department  = new Department();
					department.setName(name.trim());
					departments.add(department);
					if(line.indexOf("<option") != -1)
						line = line.substring(line.indexOf("<option"));
					else
						break;
				}
			} catch (IOException e) {
			}
	
			return departments;
		}
	public static Set<Instructor> getInstructors(){
		HashSet<Instructor> instructors = new HashSet<Instructor>();
		String path = "http://kayit.etu.edu.tr/Ders/_Ders_prg_start.php";
		try {
			URL url = new URL(path);
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				if(line.contains("dd_hoca"))
					break;
			}
			line = line.substring(line.indexOf("<option value"), line.indexOf("</select>"));
			while(true){
				line = line.substring(14);
				String id = line.substring(0, 7);
				line = line.substring(8);
				String name = line.substring(0, line.indexOf("</option>"));
				Instructor instructor = new Instructor();
				instructor.setName(name.trim());
				instructors.add(instructor);

				if(line.indexOf("<option") != -1)
					line = line.substring(line.indexOf("<option"));
				else
					break;
			}
		} catch (IOException e) {
		}

		return instructors;
	}

	public static Set<Room> getClassRooms(){
		HashSet<Room> classRooms = new HashSet<Room>();
		String path = "http://kayit.etu.edu.tr/Ders/_Ders_prg_start.php";
		try {
			URL url = new URL(path);
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				if(line.contains("dd_derslik"))
					break;
			}
			line = line.substring(line.indexOf("<option value"), line.indexOf("</select>"));
			while(true){
				line = line.substring(14);
				String id = line.substring(0, line.indexOf(">"));
				line = line.substring(line.indexOf(">") +1 );
				String name = line.substring(0, line.indexOf("</option>"));
				Room room = new Room();
				room.setName(name.trim());
				classRooms.add(room);

				if(line.indexOf("<option") != -1)
					line = line.substring(line.indexOf("<option"));
				else
					break;
			}
		} catch (IOException e) {
		}

		return classRooms;
	}

	public static void main(String[] args) {
		Iterator<Department> iter = getDepartments().iterator();
	}
}
