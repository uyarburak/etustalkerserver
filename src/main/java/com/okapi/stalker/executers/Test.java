package com.okapi.stalker.executers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.okapi.stalker.data.storage.model.Course;
import com.okapi.stalker.data.storage.model.Interval;
import com.okapi.stalker.data.storage.model.Section;

public class Test {

	public static void main(String[] args) {
		Set<Interval> intervals1 = new HashSet<>();
		Set<Interval> intervals2 = new HashSet<>();
		
		Course course = new Course();
		course.setCode("DERS101");
		
		Section s1 = new Section();
		Section s2 = new Section();
		s1.setCourse(course);
		s1.setSectionNo(1);
		s2.setCourse(course);
		s2.setSectionNo(2);
		
		Interval i1 = new Interval();
		i1.setDay(1);
		i1.setHour(1);
		i1.setSection(s1);
		i1.setRoom("zaa");
		Interval i2 = new Interval();
		i2.setDay(1);
		i2.setHour(2);
		i2.setSection(s2);
		i2.setRoom("abc");
		Interval i3 = new Interval();
		i3.setDay(1);
		i3.setHour(3);
		i3.setSection(s1);
		i3.setRoom("zaa");
		
		intervals1.add(i1);
		intervals1.add(i2);
		intervals1.add(i3);
		intervals2.addAll(intervals1);
		System.out.println(!Sets.difference(intervals1, intervals2).isEmpty() || !Sets.difference(intervals2, intervals1).isEmpty());
		
		intervals2.remove(i3);
		System.out.println(!Sets.difference(intervals1, intervals2).isEmpty() || !Sets.difference(intervals2, intervals1).isEmpty());
		Interval i4 = new Interval();
		i4.setDay(1);
		i4.setHour(3);
		i4.setSection(s1);
		i4.setRoom("zaa");
		
		intervals2.add(i4);
		System.out.println(!Sets.difference(intervals1, intervals2).isEmpty() || !Sets.difference(intervals2, intervals1).isEmpty());
		i4.setRoom("Zaa");
		System.out.println(!Sets.difference(intervals1, intervals2).isEmpty() || !Sets.difference(intervals2, intervals1).isEmpty());
		
		Interval[] d1 = Sets.difference(intervals1, intervals2).toArray(new Interval[0]);
		Interval[] d2 = Sets.difference(intervals2, intervals1).toArray(new Interval[0]);
		
		System.out.println(Arrays.toString(d1));
		System.out.println(Arrays.toString(d2));
		
	}
	

}
