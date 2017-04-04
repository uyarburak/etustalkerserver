package com.okapi.stalker.executers;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.okapi.stalker.data.storage.model.Course;
import com.okapi.stalker.data.storage.model.Department;
import com.okapi.stalker.data.storage.model.Instructor;
import com.okapi.stalker.data.storage.model.Interval;
import com.okapi.stalker.data.storage.model.Section;
import com.okapi.stalker.data.storage.model.Student;

public class AppMain {
	Matcher matcher;

	Session session;
	public AppMain() {
		matcher = new Matcher();
	}
	public static void main(String[] args) {
		AppMain app = new AppMain();
		if(!app.matcher.match()){
			if(args.length == 0){
				Scanner sc = new Scanner(System.in);
				if(app.matcher.coursesHasChanged)
					System.out.println("Derslerde degisiklik var!");
				if(app.matcher.sectionsHasChanged)
					System.out.println("Subelerde degisiklik var!");
				if(app.matcher.instructorsHasChanged)
					System.out.println("Hocalarda degisiklik var!");
				if(app.matcher.studentsHasChanged)
					System.out.println("Ogrencilerde degisiklik var!");
				if(app.matcher.intervalsHasChanged)
					System.out.println("Intervallerde degisiklik var!");
				if(app.matcher.departmentsHasChanged)
					System.out.println("Departmanlarda degisiklik var!");
				System.out.println("--------------------------------------\n"
						+ "Veritabaninda degisiklik yapmak istiyor musunuz? (Y/N)");
				String line = null;
				do{
					if(line != null)
						System.out.println("Gecersiz input girdiniz!\nVeritabaninda degisiklik yapmak istiyor musunuz? (Y/N)");
					line = sc.nextLine();
					if(line.equalsIgnoreCase("N")){
						System.out.println("Program elle sonlandirildi.");
						System.exit(0);
						return;
					}
				}while(!line.equalsIgnoreCase("Y"));
			}
			app.doDBJob();
		}
		else
			System.out.println("Nothing is changed");
		System.exit(0);

	}
	
	private void doDBJob(){
		if(matcher.students.size() > 0){
			
			Configuration configuration = new Configuration();
	        configuration.configure();
			configuration.setProperty("hibernate.hbm2ddl.auto", "create");
			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
		    SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
			session = sessionFactory.openSession();
			session.beginTransaction();
			for (Department dep : matcher.departments.values()) {
				session.save(dep);
			}
			for (Course course : matcher.courses.values()) {
				session.save(course);
			}
			for (Instructor ins : matcher.instructors.values()) {
				session.save(ins);
			}
			for (Student st : matcher.students.values()) {
				session.save(st);
			}
			
			for (Section sec : matcher.sections.values()) {
				session.save(sec);
			}
			
			for (Interval interval : matcher.intervals) {
				session.save(interval);
			}
			session.createSQLQuery("insert into versions (id, time) values(null, null)").executeUpdate();

			session.getTransaction().commit();
			session.close();
			System.exit(0);
		}else{
			System.out.println("ogrenci yok");
		}
	}
	
}