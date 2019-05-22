package edu.handong.analysis.datamodel;

public class Course {

		private String studentId;
		private String yearMonthGraduated;
		private String firstMajor;
		private String secondMajor;
		private String courseCode;
		private String courseName;
		private String courseCredit;
		private int yearTaken;
		private int semesterCourseTaken;
		
		public String getStudentId() {
			return studentId;
		}

		public String getYearMonthGraduated() {
			return yearMonthGraduated;
		}

		public String getFirstMajor() {
			return firstMajor;
		}

		public String getSecondMajor() {
			return secondMajor;
		}

		public String getCourseCode() {
			return courseCode;
		}

		public String getCourseName() {
			return courseName;
		}

		public String getCourseCredit() {
			return courseCredit;
		}

		public int getYearTaken() {
			return yearTaken;
		}

		public int getSemesterCourseTaken() {
			return semesterCourseTaken;
		}

		public Course(String line) {
			
			String[] data = line.split(",");
			studentId = data[0].trim();
			yearMonthGraduated = data[1].trim();
			firstMajor = data[2].trim();
			secondMajor = data[3].trim();
			courseCode = data[4].trim();
			courseName = data[5].trim();
			courseCredit = data[6].trim();
			yearTaken = Integer.parseInt(data[7].trim());
			semesterCourseTaken = Integer.parseInt(data[8].trim());
		}
		
		
}
