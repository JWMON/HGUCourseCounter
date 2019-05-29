package edu.handong.analysis.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class Utils {

	public static Iterable<CSVRecord> getLines(String file, boolean removeHeader){
		
		Iterable<CSVRecord> records = null;
		try {
			Reader in = new FileReader(file);
			
			records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);

		} catch(FileNotFoundException e){
			System.out.println("The file path does not exist. Please check your CLI argument!");
			System.exit(0);
		} catch(Exception ee) {
			System.out.println(ee.getMessage());
			System.exit(0);
		}
		
		return records;
	}
	
	public static void writeAFile(ArrayList<String> lines, String targetFileName, boolean a2) {
		
		PrintWriter outputStream = null;
		
		while (outputStream == null) {
			try
			{
				outputStream = new PrintWriter(new FileOutputStream(targetFileName));

				if(a2)
					outputStream.println("Year,Semester,CourseCode,CourseName,TotalStudents,StudentsTaken,Rate");
				else
					outputStream.println("StudentID,TotalNumberOfSemestersRegistered,Semester,NumCoursesTakenInTheSemester");	
			
			for (String line:lines) {
				outputStream.println(line);
			}
			outputStream.close();
			
			}
			catch (FileNotFoundException e)
			{
				File makeDir = new File(targetFileName);
				makeDir.getParentFile().mkdirs();
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}

	}

}
