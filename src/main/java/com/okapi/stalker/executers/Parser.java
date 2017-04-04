package com.okapi.stalker.executers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import com.okapi.stalker.data.StalkerPOST;
import com.okapi.stalker.data.storage.model.Course;
import com.okapi.stalker.data.storage.model.Department;
import com.okapi.stalker.data.storage.model.Instructor;
import com.okapi.stalker.data.storage.model.Interval;
import com.okapi.stalker.data.storage.model.Section;
import com.okapi.stalker.data.storage.model.Student;
import com.okapi.stalker.util.HTMLReader;
import com.okapi.stalker.util.MyStringUtil;

public class Parser extends Matchable{
	
	Map<String, String> idDepMap;
	Map<String, Set<Department>> departmentsOfStudents;
	
	public Parser() {
		super();
		departmentsOfStudents = new HashMap<String, Set<Department>>();
		buildIdDepMap();
	}
	public boolean parse(){
		try{
			System.out.println("Sectionlar parse ediliyor.");
			parseSectionLists();
			System.out.println("Departmanlar parse ediliyor.");
			departmentParse();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	private void departmentParse(){
		Map<String, String> urls = 
				departmentURLs(StalkerPOST.getHtml("https://www.etu.edu.tr/tr/akademik"));
		// Departman ana sayfa url'lerini toplar.
		for (String departmentName : urls.keySet()) {
			String departmentURL = urls.get(departmentName);
			System.out.println(departmentName + " " + departmentURL);
			Department department = getDep(departmentName);
			department.setMainURL(departmentURL);
			if(departmentURL != null){
				// Eger akademik kadro linkine sahip bir departmansa, bu sayfalarin htmllerini gezerek hocalari parse eder.
				// Dikkat! Bu islemlerde html clean yapilmamistir. Sebebi, href taglarını siliyor olmasıdır.
				if(!departmentURL.contains("https")){
					departmentURL = departmentURL.replaceFirst("http", "https");
				}
				try {
					parseAcademics(department, StalkerPOST.getHtml(departmentURL+"/akademik-kadro")); 
				} catch (Exception e) {
					continue;
				}
			}
		}
	}
	private Department getDep(final String depName){
		String lowerDepName = depName.toLowerCase();
		for (String string : departments.keySet()) {
			if(MyStringUtil.isSubSequence(string.toLowerCase(), lowerDepName)){
				return departments.get(string);
			}
		}
		Department department = new Department();
		department.setName(depName);
		departments.put(depName, department);
		return department;
	}

	private void parseSectionLists(){
		List<String> htmlStudentList = new ArrayList<String>();
		List<String> htmlProgramList = new ArrayList<String>();
		ArrayList<String> courseIds = StalkerPOST.getCoursesIds();
		System.out.println(courseIds.size() + " adet ders bulundu.");
		float i = 1;
		for (String courseId : courseIds) {
			if(i % 50 == 0)
				System.out.println(i + "/" + htmlStudentList.size() + " ---> %" + ((i/htmlStudentList.size())*100));
			String html = StalkerPOST.getCourseStudentList(courseId, "0");
			htmlStudentList.add(html);
			html = StalkerPOST.getCourseProgramList(courseId, "0");
			htmlProgramList.add(html);
			i++;
		}
		htmlStudentList = cleanHtmls(htmlStudentList);
		htmlProgramList = cleanHtmls(htmlProgramList);
		System.out.println(htmlStudentList.size() + " adet ders listesi bulundu.");
		System.out.println(htmlProgramList.size() + " adet program listesi bulundu.");
		i = 1;
		System.out.println("Ogrenci listeleri parse ediliyor!");
		for (String html :htmlStudentList) {
			if(i % 10 == 0)
				System.out.println(i + "/" + htmlStudentList.size() + " ---> %" + ((i/htmlStudentList.size())*100));
			courseStudentParse(html);
			i++;
		}
		i = 1;
		System.out.println("Interval listeleri parse ediliyor!");
		for (String html :htmlProgramList) {
			if(i % 10 == 0)
				System.out.println(i + "/" + htmlProgramList.size() + " ---> %" + ((i/htmlProgramList.size())*100));
			courseProgramParse(html);
			i++;
		}
		for (Map.Entry<String, Set<Department>> entry : departmentsOfStudents.entrySet()) {
			Set<Department> departmentsOfStudent = entry.getValue();
			Student student = students.get(entry.getKey());
			if(departmentsOfStudent.size() > 1){
				String mainDepName = getDepartmentByID(student.getId());
				Department department = departments.get(mainDepName);
				if(departmentsOfStudent.contains(department)){
					departmentsOfStudent.remove(department);
					student.setDepartment(department);
					student.setDepartment2(((Department)departmentsOfStudent.toArray()[0]).getName());
				}else{
					student.setDepartment(((Department)departmentsOfStudent.toArray()[0]));
					student.setDepartment2(((Department)departmentsOfStudent.toArray()[1]).getName());
				}
			}else{
				student.setDepartment(departmentsOfStudent.iterator().next());
			}
		}
	}
	private List<String> cleanHtmls(List<String> htmls){
		List<String> clearHtmls = new ArrayList<String>();
		for (String html : htmls) {
			clearHtmls.add(Jsoup.clean(html, Whitelist.relaxed()));
		}
		return clearHtmls;
	}
	

	private String parseAcademics(Department department, String html){
		if(html == null)
			return "";
		Document doc = Jsoup.parse(html);
		Elements departments = doc.getElementsByAttributeValue("class", "user-list__item nopad grid-d-12 grid-tl-12 grid-t-12 grid-m-12");
		Iterator iterator = departments.iterator();
		for (Element departmentElement : departments) {
			//System.out.println(departmentElement+"\n-----------");
			doc = Jsoup.parse(departmentElement.toString());

			Elements name = departmentElement.getElementsByAttributeValue("class", "user-list__item__content grid-d-3 grid-tl-3 grid-t-3 grid-m-12");
			String nameInstructor = "";
			if(name.first().getElementsByTag("b").isEmpty()){
				nameInstructor = name.first().getElementsByTag("strong").first().ownText();

			}else{
				nameInstructor = name.first().getElementsByTag("b").first().ownText();
				if(nameInstructor.isEmpty() && !name.first().getElementsByTag("a").isEmpty())
					nameInstructor = name.first().getElementsByTag("a").first().ownText();

			}

			//System.out.println(nameInstructor);
			Instructor instructor = MyStringUtil.getIns(nameInstructor, instructors);
			if(instructor == null){
				instructor = new Instructor();
				instructor.setName(nameInstructor);
				instructors.put(nameInstructor, instructor);
			}
			instructor.setDepartment(department);
			if(department != null)
				department.addInstructor(instructor);
			
			
			Elements sites = departmentElement.getElementsByAttribute("href");
			for (Element element : sites) {
				String sElement = element.attr("href");
				if(sElement.startsWith("tel")){
					instructor.setTel(sElement.substring(4));
				}else if(sElement.startsWith("mailto")){
					instructor.setMail(sElement.substring(7));
				}else{
					instructor.setWebsite(sElement);
				}
			}
			Elements images = departmentElement.getElementsByAttribute("src");
			int i = 0;
			for (Element element : images) {

				if(++i > 2)
					break;
				String image = element.attr("src");
				if(image.startsWith("../")){
					image = "https://etu.edu.tr/".concat(image.substring(3));
				}else if(image.startsWith("/files")){
					image = "https://etu.edu.tr".concat(image);
				}
				instructor.setImage(image);
			}

			Elements infos = departmentElement.getElementsByAttributeValue("class", "ofisWrap");
			if(!infos.isEmpty()){
				String office =  infos.first().getElementsByTag("strong").first().ownText();
				instructor.setOffice(office);
			}
		}
		return null;
	}

	private Map<String, String> departmentURLs(String html){
		Map<String, String> urls = new HashMap<String, String>();
		Document doc = Jsoup.parse(html);
		Elements departments = doc.getElementsByAttributeValue("class", "fakulte-listesi__item__menu__list__item--link");
		for (Element departmentElement : departments) {
			doc = Jsoup.parse(departmentElement.toString());
			String departmentName = doc.text();
			if(departmentName.equals("Elektrik-Elektronik Mühendisliği"))
				departmentName = "Elektrik ve Elektronik Müh.";
			Element link = doc.getElementsByAttribute("href").first();
			String url = link.attr("href");
			if(url.contains("http")){
				if(!url.contains("etu.edu.tr")){
					continue;
				}
			}else{
				url = "http://etu.edu.tr".concat(url);
			}
			urls.put(departmentName, url);
		}
		return urls;
	}
	private void courseProgramParse(String html){
		HTMLReader reader = new HTMLReader(html);
		String temp = reader.findAfter("h3").get(0);
		String courseCode = temp.substring(0, temp.indexOf(" - "));

		List<String> sectionNodes = reader.findBetween("table");
		for (String sectionNode : sectionNodes) {

			reader = new HTMLReader(sectionNode);
			temp = reader.findAfter("b").get(0);
			temp = temp.substring(5);
			int sectionNo = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));

			Section section = sections.get(courseCode + sectionNo);
			
			sections.put(courseCode + sectionNo, section);
			List<String> intervalNodes = reader.findBetween("tr");
			Iterator<String> iterator = intervalNodes.listIterator(2);
			for (int i = 2; i < intervalNodes.size(); i++) {
				reader = new HTMLReader(iterator.next());
				List<String> infos = reader.findAfter("td");
				for (int j = 0; j < infos.size(); j++) {
					String tmp = infos.get(j);
					if(tmp.length() < 3 || !tmp.toLowerCase().contains("derslik"))
						continue;
					Integer day = j;
					Integer hour = i-2;
					String roomName = tmp.substring(9).trim();
					Interval interval = new Interval();
					interval.setDay(day);
					interval.setHour(hour);
					interval.setRoom(roomName);
					interval.setSection(section);
					section.addInterval(interval);
					intervals.add(interval);
				}
			}

		}

	}



	private void courseStudentParse(String html){
		HTMLReader reader = new HTMLReader(html);
		Course course = new Course();
		String temp = reader.findAfter("h3").get(0);
		String courseCode = temp.substring(0, temp.indexOf(" - "));
		String courseTitle = temp.substring(temp.indexOf(" - ")+3);
		//System.out.println("Course Code: " + courseCode + "\nCourse Title: " + courseTitle + "\n----------");
		course.setCode(courseCode);
		course.setTitle(courseTitle);
		course.setActive(true);
		courses.put(courseCode, course);

		List<String> sectionNodes = reader.findBetween("table");
		for (String sectionNode : sectionNodes) {
			Section section = new Section();
			course.addSection(section);

			reader = new HTMLReader(sectionNode);
			temp = reader.findAfter("b").get(0);
			//System.out.println(temp);
			temp = temp.substring(5);
			int sectionNo = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));

			String instructorName = temp.substring(temp.indexOf(" ")+1);

			Instructor instructor = instructors.get(instructorName);
			if(instructor == null){
				instructor = new Instructor();
				instructor.setName(instructorName);
				instructor.addSection(section);
				//System.out.println(instructorName);
			}
			instructors.put(instructorName, instructor);
			section.setInstructor(instructor);

			temp = reader.findAfter("b").get(1);
			int sectionSize = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
			section.setSize(sectionSize);
			section.setSectionNo(sectionNo);
			sections.put(course.getCode()+sectionNo, section);

			List<String> studentNodes = reader.findBetween("tr");
			Iterator<String> iterator = studentNodes.listIterator(2);
			for (int i = 2; i < studentNodes.size() - 1; i++) {
				reader = new HTMLReader(iterator.next());
				List<String> infos = reader.findAfter("td");
				String id = infos.get(0);
				String name = infos.get(1);
				String departmentName = infos.get(2);
				if(departmentName.equals("Elektrik ve Elektronik Mühendi")){
					departmentName = "Elektrik ve Elektronik Müh. DK";
				}else if(departmentName.equals("Malzeme Bilimi ve Nanoteknoloj")){
					departmentName = "Malzeme Bilimi ve Nanoteknoloji Müh.";
				}
				Integer year = isNumeric(infos.get(3)) ? Integer.parseInt(infos.get(3)) : 31;
				reader.findAfter("a");
				String mail = reader.findAfter("a").get(0);

				Department department = departments.get(departmentName);
				if(department == null){
					department = new Department();
					department.setName(departmentName);
					departments.put(departmentName, department);
				}

				Student student = students.get(id);
				if(student == null){
					student = new Student();
					student.setActive(true);
					student.setId(id);
					student.setMail(mail);
					student.setName(name);
					student.setYear(year);
					department.addStudent(student);
				}
				Set<Department> departmentsOfStudent = departmentsOfStudents.get(id);
				if(departmentsOfStudent == null){
					departmentsOfStudent = new HashSet<Department>();
					departmentsOfStudent.add(department);
					departmentsOfStudents.put(id, departmentsOfStudent);
				}else if(!departmentsOfStudent.contains(departmentName)){
					departmentsOfStudent.add(department);
				}
				student.addSection(section);
				students.put(id, student);
			}

		}

	}
	
	private boolean isNumeric(String str){  
		try  
		{  
			int d = Integer.parseInt(str);  
		}  
		catch(NumberFormatException nfe)  
		{  
			return false;  
		}  
		return true;  
	}
	
	private String getDepartmentByID(String id){
		String token = id.substring(id.length() - 7, id.length() -3);
		return idDepMap.get(token);
	}
	
	private void buildIdDepMap(){
		idDepMap = new HashMap<String, String>(60);
		// MUHENDISLIK FAKULTESI
		idDepMap.put("1101", "Bilgisayar Mühendisliği");
		idDepMap.put("1111", "Bilgisayar Mühendisliği YL");
		idDepMap.put("1117", "Bilgisayar Mühendisliği DK");
		idDepMap.put("1701", "Biyomedikal Mühendisliği");
		idDepMap.put("1711", "Biyomedikal Mühendisliği YL");
		idDepMap.put("1717", "Biyomedikal Mühendisliği DK");
		idDepMap.put("1201", "Elektrik ve Elektronik Müh.");
		idDepMap.put("1211", "Elektrik ve Elektronik Müh. YL");
		idDepMap.put("1217", "Elektrik ve Elektronik Müh. DK"); // Doktora olarak degistir
		idDepMap.put("1301", "Endüstri Mühendisliği");
		idDepMap.put("1311", "Endüstri Mühendisliği YL");
		idDepMap.put("1317", "Endüstri Mühendisliği DK");
		idDepMap.put("1501", "Makine Mühendisliği");
		idDepMap.put("1511", "Makine Mühendisliği YL");
		idDepMap.put("1517", "Makine Mühendisliği DK");
		idDepMap.put("1801", "Malzeme Bilimi ve Nanoteknoloj"); // Malzeme Bilimi ve Nanoteknoloji olarak degistir
		idDepMap.put("1611", "Mikro ve Nano Teknoloji YL");
		idDepMap.put("1617", "Mikro ve Nano Teknoloji DK");
		
		// TIP FAKULTESI
		idDepMap.put("5201", "Tıp");
		idDepMap.put("5204", "Tıp");
		
		// IKTISADI IDARI BILIMLER FAKULTESI
		idDepMap.put("3201", "İktisat");
		idDepMap.put("3202", "İktisat");
		idDepMap.put("3205", "İktisat");
		idDepMap.put("3211", "İktisat YL");
		idDepMap.put("3110", "İşletme");
		idDepMap.put("3101", "İşletme");
		idDepMap.put("3102", "İşletme");
		idDepMap.put("3104", "İşletme");
		idDepMap.put("3111", "İşletme YL");
		idDepMap.put("3116", "İşletme YL");
		idDepMap.put("3501", "Siyaset Bilimi");
		idDepMap.put("3301", "Uluslararası İlişkiler");
		idDepMap.put("3311", "Uluslararası İlişkiler YL");
		idDepMap.put("3401", "Uluslararası Girişimcilik");
		idDepMap.put("3404", "Uluslararası Girişimcilik");
		
		//HUKUK FAKULTESI
		idDepMap.put("5101", "Hukuk");
		idDepMap.put("5104", "Hukuk");
		
		//GUZEL SANATLAR TASARIM VE MIMARLIK FAKULTESI
		idDepMap.put("4303", "Endüstriyel Tasarım");
		idDepMap.put("4301", "Endüstriyel Tasarım");
		idDepMap.put("4403", "Görsel İletişim Tasarımı");
		idDepMap.put("4401", "Görsel İletişim Tasarımı");
		idDepMap.put("4711", "Tasarım Yüksek Lisans");
		idDepMap.put("4201", "İçmimarlık ve Çevre Tasarımı");
		idDepMap.put("4203", "İçmimarlık ve Çevre Tasarımı");
		idDepMap.put("4213", "İçmimarlık ve Çevre Tasarımı");
		idDepMap.put("4601", "Mimarlık");
		idDepMap.put("4611", "Mimarlık YL");
		idDepMap.put("4803", "Sanat ve Tasarım Yönetimi");
		idDepMap.put("4103", "Sanat ve Tasarım Yönetimi");
		
		//FEN EDEBIYAT FAKULTESI
		idDepMap.put("2401", "İngiliz Dili ve Edebiyatı");
		idDepMap.put("2101", "Matematik");
		idDepMap.put("2111", "Matematik YL");
		idDepMap.put("2117", "Matematik Doktora");
		idDepMap.put("2501", "Psikoloji");
		idDepMap.put("2301", "Tarih");
		idDepMap.put("2302", "Tarih");
		idDepMap.put("2304", "Tarih");
		idDepMap.put("2201", "Türk Dili ve Edebiyatı");
		idDepMap.put("2211", "Türk Dili ve Edebiyatı YL");
		
	}
}
