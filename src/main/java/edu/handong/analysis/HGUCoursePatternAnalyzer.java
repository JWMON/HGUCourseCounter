package edu.handong.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.csv.CSVRecord;

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;

public class HGUCoursePatternAnalyzer {

	private HashMap<String, Student> students;
	private String inputPath;
	private String outputPath;
	private String analysis;
	private String courseCode;
	private String startYear;
	private String endYear;
	private boolean help;
	private boolean a2 = false;

	 
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 */
	public void run(String[] args) {
		//
		Options options = createOptions();
		
		if(parseOptions(options, args)) {
			if(help) {
				printHelp(options);
				return;
			}

			Iterable<CSVRecord> lines = Utils.getLines(inputPath,  true);
			
			students = loadStudentCourseRecords(lines);
			Map<String, Student> sortedStudents = new TreeMap<String,Student>(students); 
			ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
			Utils.writeAFile(linesToBeSaved, outputPath, a2);

			} 
	}
	
	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private HashMap<String,Student> loadStudentCourseRecords(Iterable<CSVRecord> lines) {
		
		HashMap<String, Student> hm = new HashMap<String, Student>();
		
		for (CSVRecord record : lines) {
			
			int year = Integer.parseInt(record.get(7).trim());
			if((year < Integer.parseInt(startYear) || year > Integer.parseInt(endYear))) continue;

			if(!hm.containsKey(record.get(0))) {
				Student student = new Student(record.get(0));
				hm.put(record.get(0), student);
			}
			
			hm.get(record.get(0)).addCourse(new Course(record));
		}
		
		return hm; // do not forget to return a proper variable.
		
	}

	/**
	 * This method generate the number of courses taken by a student in each semester. The result file look like this:
	 * StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester
	 * 0001,14,1,9
     * 0001,14,2,8
	 * ....
	 * 
	 * 0001,14,1,9 => this means, 0001 student registered 14 semeters in total. In the first semeter (1), the student took 9 courses.
	 * 
	 * 
	 * @param sortedStudents
	 * @return
	 */
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		
		ArrayList<String> al = new ArrayList<String>();

		if(a2) {
			int eYear = Integer.parseInt(endYear);
			int sYear = Integer.parseInt(startYear);
			
			String courseName = "";
			for (String key : sortedStudents.keySet()) {
				Iterator<Course> course = sortedStudents.get(key).getCoursesTaken().iterator();
				while(course.hasNext()) {
					Course currentCourse = course.next();
					if(currentCourse.getCourseCode().trim().equals(courseCode.trim())) {
						courseName = currentCourse.getCourseName().trim();
						break;
					}
				}
			}

			int TotalStudents;
			int StudentsTaken;
			double ratio = 0;
			
			for(int year = sYear; year <= eYear; year++) {
				for(int sem = 1; sem <= 4; sem++) {
					
					TotalStudents = 0;
					StudentsTaken = 0;
					
					ArrayList<String> studentsSemYear = new ArrayList<String>();
					
					for (String key : sortedStudents.keySet()) {
						
						Iterator<Course> course = sortedStudents.get(key).getCoursesTaken().iterator();
						
						while(course.hasNext()) {
							
							Course currentCourse = course.next();
							
							if(currentCourse.getYearTaken() == year && currentCourse.getSemesterCourseTaken() == sem) {
								
								if(currentCourse.getCourseCode().trim().equals(courseCode.trim())) {
									StudentsTaken++;
								} 
								if(!studentsSemYear.contains(currentCourse.getStudentId()))
									studentsSemYear.add(currentCourse.getStudentId());
							}
						}
					}
					TotalStudents = studentsSemYear.size();
					if(StudentsTaken == 0) ratio = 0;
					else ratio = StudentsTaken/(double)TotalStudents;

					String text = "";
					text = year + "," + sem + "," + courseCode + "," + courseName + "," + TotalStudents + "," + StudentsTaken + "," + String.format("%.1f", ratio*100) + "%";
					al.add(text);
					
				}
			}

		} 
		else {
			for (String key : sortedStudents.keySet()) {
				
				int len = sortedStudents.get(key).getSemestersByYearAndSemester().size();
				
				for(int i = 1; i <= len; i++) {
					
					String text = "";
					text = key + "," + len + "," + i + "," + sortedStudents.get(key).getNumCourseInNthSemester(i);
					al.add(text);
				}
			}
		}
		
		
		
		return al; // do not forget to return a proper variable.
	}
	
	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();
		
		try {
			CommandLine cmd = parser.parse(options, args);
			
			analysis = cmd.getOptionValue("a");
			a2 = Integer.parseInt(analysis) == 2 ? true : false;
			
			if(a2 && cmd.getOptionValue("c") == null) 
			{
				throw new Exception();
			} 
			else if(a2 && cmd.getOptionValue("c") != null || Integer.parseInt(cmd.getOptionValue("a")) == 1) 
			{
				inputPath = cmd.getOptionValue("i");
				outputPath = cmd.getOptionValue("o");
				analysis = cmd.getOptionValue("a");
				courseCode = cmd.getOptionValue("c");
				startYear = cmd.getOptionValue("s");
				endYear = cmd.getOptionValue("e");
				help = cmd.hasOption("h");
			}
			else
				throw new Exception();
		}
		
		catch(Exception e) {
			printHelp(options);
			return false;
		}
		
		return true;
	}
	
	private Options createOptions() {
		Options options = new Options();
		
		options.addOption(Option.builder("i").longOpt("input")
			.desc("Set an input file path")
			.hasArg()
			.argName("Input path")
			.required()
			.build());
			
		options.addOption(Option.builder("o").longOpt("output")
			.desc("Set an output file path")
			.hasArg()
			.argName("Ouput path")
			.required()
			.build());
			
		options.addOption(Option.builder("a").longOpt("analysis")
			.desc("1: Count courses per semester, 2: Count per course name and year")
			.hasArg()
			.argName("Analysis option")
			.required()
			.build());
			
		options.addOption(Option.builder("c").longOpt("coursecode")
			.desc("Course code for '-a 2' option")
			.hasArg()
			.argName("course code")
//			.required()
			.build());
			
		options.addOption(Option.builder("s").longOpt("startyear")
			.desc("Set the start year for anlysis e.g., -s 2002")
			.hasArg()
			.argName("Start year for analysis")
			.required()
			.build());
			
		options.addOption(Option.builder("e").longOpt("endyear")
			.desc("Set the end year for anlysis e.g., -e 2005")
			.hasArg()
			.argName("End year for analysis")
			.required()
			.build());
			
		options.addOption(Option.builder("h").longOpt("help")
			.desc("Show a Help page")
			.argName("Help")
			.build());
			
		return options;
		
	}
	
	private void printHelp(Options options) {

		HelpFormatter formatter = new HelpFormatter();
		String header = "HGU Course Analyzer";
		String footer ="";
		formatter.printHelp("HGUCourseCounter", header, options, footer, true);
	}
}
