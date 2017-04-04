//package com.okapi.stalker.data.storage;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import org.jsoup.Jsoup;
//import org.jsoup.safety.Whitelist;
//
//import com.okapi.stalker.data.StalkerPOST;
//import com.okapi.stalker.data.storage.model.Interval.Day;
//import com.okapi.stalker.data.storage.model.Interval.Hour;
//
//import com.okapi.stalker.util.HTMLReader;
//import com.okapi.stalker.util.URLReader;
//
//public class Stacker {
//
//
//	public void readFrom() throws IOException {
//		parseSectionLists();
//
//
//		//    	Set<Department> deps = StalkerPOST.getDepartments();
//		//    	for (Department department : deps) {
//		//    		db.insert(department);
//		//		}
//		//        for (int i = 1; i < size; i++)
//		//            parseCoursePage(source + "/student_lists/" + i + ".html");
//		//
//		//        for (int i = 1; i < size; i++)
//		//            parseProgramPage(source + "/interval_lists/" + i + ".html");
//		//
//		//        parseDepartmentPages();
//	}
//	private void parseSectionLists(){
//		List<String> htmls = new ArrayList<String>();
//		for (String courseId : StalkerPOST.getCoursesIds()) {
//			String html = StalkerPOST.getCourseStudentList(courseId, "0");
//			htmls.add(html);
//		}
//		List<String> clearHtmls = cleanHtmls(htmls);
//		htmls = null;
//
//		for (String html : clearHtmls) {
//			parseCoursePage(html);
//		}
//	}
//	private List<String> cleanHtmls(List<String> htmls){
//		List<String> clearHtmls = new ArrayList<String>();
//		for (String html : htmls) {
//			clearHtmls.add(Jsoup.clean(html, Whitelist.relaxed()));
//		}
//		return clearHtmls;
//	}
//
//	private void parseDepartmentPages(){
//		String path = "http://etu.edu.tr/c/indexefd0.html?q=tr/bolumler";
//		HTMLReader reader = new HTMLReader(URLReader.readURL(path));
//		List<String> faculties = reader.findBetween("akad-fakulte-kutusu");
//
//	}
//	private void parseCoursePage(String html) {
//		HTMLReader reader = new HTMLReader(html);
//		String temp = reader.findAfter("h3").get(0);
//		String courseCode = temp.substring(0, temp.indexOf(" - "));
//		String courseTitle = temp.substring(temp.indexOf(" - ")+3);
//		System.out.println("Course Code: " + courseCode + "\nCourse Title: " + courseTitle + "\n----------");
//
//		List<String> sectionNodes = reader.findBetween("table");
//		for (String sectionNode : sectionNodes) {
//			reader = new HTMLReader(sectionNode);
//			temp = reader.findAfter("b").get(0);
//			temp = temp.substring(5);
//			int sectionNo = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
//
//			String instructorName = temp.substring(temp.indexOf(" ")+1);
//
//			temp = reader.findAfter("b").get(1);
//			int sectionSize = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
//
//			System.out.println("Section No: " + sectionNo + "\nSize: " + sectionSize + "\nInstructor Name: " + instructorName);
//			System.out.println("Students: ");
//			List<String> studentNodes = reader.findBetween("tr");
//			Iterator<String> iterator = studentNodes.listIterator(2);
//			for (int i = 2; i < studentNodes.size() - 1; i++) {
//				reader = new HTMLReader(iterator.next());
//				List<String> infos = reader.findAfter("td");
//				String id = infos.get(0);
//				String name = infos.get(1);
//				String department = infos.get(2);
//				Integer year = isNumeric(infos.get(3)) ? Integer.parseInt(infos.get(3)) : 31;
//				reader.findAfter("a");
//				String mail = reader.findAfter("a").get(0);
//				System.out.printf("ID: %s\nName: %s\nDepartment: %s\nYear: %d\nMail: %s\n", id, name, department, year, mail);
//				System.out.println("-----------");
//			}
//
//		}
//	}
//
//	private boolean isNumeric(String str)
//	{
//		try
//		{
//			int d = Integer.parseInt(str);
//		}
//		catch(NumberFormatException nfe)
//		{
//			return false;
//		}
//		return true;
//	}
//
//	//    private void parseProgramPage(String path) {
//	//        Course course = new Course();
//	//        Instructor instructor;
//	//
//	//        HTMLReader reader = new HTMLReader(new File(path), "ISO8859-9");
//	//        List<String> sections = reader.findAfter("b");
//	//        List<String> intervals = reader.findBetween("tr");
//	//
//	//        int index;
//	//        String codeAndTitle;
//	//        try {
//	//            codeAndTitle = reader.findAfter("h3").get(0);
//	//        } catch (IndexOutOfBoundsException e) {
//	//            return;
//	//        }
//	//        index = codeAndTitle.indexOf('-');
//	//        course.code = codeAndTitle.substring(0, index - 1);
//	//        course.title = codeAndTitle.substring(index + 2);
//	//
//	//        for (int i = 0; i < sections.size(); i++) {
//	//            Section section = new Section();
//	//            section.course = course.code;
//	//
//	//            String numberAndInstructor = sections.get(i);
//	//            index = numberAndInstructor.indexOf(' ');
//	//            section.number = numberAndInstructor.substring(5, 7).trim();
//	//            section.instructor = numberAndInstructor.substring(index + 2);
//	//
//	//            section = sectionMap.get(section.key());
//	//
//	//            String[] split;
//	//            for (int j = 1; j < 13; j++) {
//	//                split = intervals.get(j).split("<td>");
//	//                for (int j2 = 1; j2 < 7; j2++) {
//	//                    String classRoom = split[j2];
//	//                    if (classRoom.equals("-")) {
//	//                        continue;
//	//                    }
//	//
//	//                    Interval interval = new Interval();
//	//                    interval.time = Time.values()[j - 1];
//	//                    interval.day = Day.values()[j2 - 1];
//	//                    ClassRoom mClassRoom = new ClassRoom(classRoom.substring(9).trim());
//	//                    if (!classRoomMap.containsKey(mClassRoom.key())) {
//	//                        classRoomMap.put(mClassRoom.key(), mClassRoom);
//	//                    }
//	//                    mClassRoom = classRoomMap.get(mClassRoom.key());
//	//                    interval.classRoom = mClassRoom;
//	//                    mClassRoom.addInterval(interval);
//	//                    intervalMap.put(interval.key(), interval);
//	//                    section.addInterval(interval);
//	//                }
//	//            }
//	//
//	//            for (int k = 0; k < 13; k++)
//	//                intervals.remove(0);
//	//        }
//	//    }
//
//	public static void main(String args[]) throws IOException {
//		Stacker stacker = new Stacker();
//		stacker.readFrom();
//	}
//}
