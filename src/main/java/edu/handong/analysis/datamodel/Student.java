package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;

public class Student {

	private String studentID;
	private ArrayList<Course> coursesTaken;
	private HashMap<String, Integer> semestersByYearAndSemester;
	
	public Student(String studentID) {
		
		coursesTaken = new ArrayList<Course>();
		this.studentID = studentID;
	}

	public void addCourse(Course newRecord) {
	
		coursesTaken.add(newRecord);

	}
	
	public HashMap<String, Integer> getSemestersByYearAndSemester(){
		
		semestersByYearAndSemester = new HashMap<String, Integer>();
		
		int yearTaken = coursesTaken.get(0).getYearTaken();
		int semesterTaken = coursesTaken.get(0).getSemesterCourseTaken();
		int semester = 1;
		
		semestersByYearAndSemester.put(yearTaken + "-" + semesterTaken, semester);
		
		for(int i = 0; i < coursesTaken.size(); i++) {
			
			if(yearTaken == coursesTaken.get(i).getYearTaken() && semesterTaken != coursesTaken.get(i).getSemesterCourseTaken()) {
				
				semester++;
				semesterTaken = coursesTaken.get(i).getSemesterCourseTaken();
				semestersByYearAndSemester.put(yearTaken + "-" + semesterTaken, semester);
				
			} else if(yearTaken != coursesTaken.get(i).getYearTaken()) {
				
				semester++;
				yearTaken = coursesTaken.get(i).getYearTaken();
				semesterTaken = coursesTaken.get(i).getSemesterCourseTaken();
				semestersByYearAndSemester.put(yearTaken + "-" + semesterTaken, semester);
				
			}
		}
		
		return semestersByYearAndSemester;
	}
	
	
	public int getNumCourseInNthSemester(int semester) {
		
		String sem = "";
		
		for(String yearSemester : semestersByYearAndSemester.keySet()) {
			if(semestersByYearAndSemester.get(yearSemester).equals(semester))
				sem = yearSemester;
		}
		
		String[] yearSemester = sem.split("-");
		
		int count = 0;
		
		for(int i = 0; i < coursesTaken.size(); i++) {
			
			if(Integer.parseInt(yearSemester[0].trim()) == coursesTaken.get(i).getYearTaken() && Integer.parseInt(yearSemester[1].trim()) == coursesTaken.get(i).getSemesterCourseTaken())
				count++;
		}

		return count;
	}
}
