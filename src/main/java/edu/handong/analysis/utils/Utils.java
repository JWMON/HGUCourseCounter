package edu.handong.analysis.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Utils {

	public static ArrayList<String> getLines(String file, boolean removeHeader){
		
		ArrayList<String> lines = new ArrayList<String>();
		String thisLine = "";
		
		try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				while((thisLine = br.readLine()) != null)
					lines.add(thisLine);
				br.close();
		}
		catch(IOException e) 
		{
				System.err.println("The file path does not exist. Please check your CLI argument!");
				System.exit(-1);
		}
		if(removeHeader)
			
			lines.remove(0);
		
		return lines;
	}
	
	public static void writeAFile(ArrayList<String> lines, String targetFileName) {
		
		PrintWriter outputStream = null;
		
		while (outputStream == null) {
			try
			{
				outputStream = new PrintWriter(new FileOutputStream(targetFileName));
				
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
