//-------------------------------
//Assignment #1
//Question: A tool requires the user to enter an author name, then searches
//all .bib files for any articles for any author(s) with that name, and crease 3 
//different files with the correct reference formats with all found records
//Written by: Jinchen Hu 


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class AuthorBibCreator {
	// creates a Scanner array to store Scanner objects which are to read the .bib
	static Scanner[] ins;
	// declares a String variable to store the author name the user enter
	static String author;
	// creates a File array to store the .json files
	static File[] Json;
	//create a File array to store the BU.json files
	static File[] BUJson;
	//creates a PrintWriter array to store OutputStream files
	static PrintWriter[] out;
	
	public static void main(String[] args) {
		//call the method to print the welcomen banner
		printWel();
		System.out.print("Please enter an author name: ");
		// declares a Scanner object which read the author name the user enter
		Scanner sc = new Scanner(System.in);
		//read the input value and assign it to author
		author = sc.next();
		System.out.println();
		//creates a String array to store the path names of BU.json files
		String[] BUPath = { author + "-IEEE-BU.json", author + "-ACM-BU.json", author + "-NJ-BU.json" };
		//creates a String array to store the path names of .json files
		String[] JPath = { author + "-IEEE.json", author + "-ACM.json", author + "-NJ.json" };
		//initializes the ins
		ins = new Scanner[10];
		//initiates the out
		out = new PrintWriter[3];
		//class the main process method
		processBibFiles(ins, out, author, JPath, BUPath);
		//close the sc
		sc.close();
		//ends the program
		System.exit(0);
	}// end of main()

	/**
	 * core engine to read input files and create output files, as well as handle any Exception
	 * @param ins input Stream array
	 * @param out output stream array
	 * @param author author name the user enter
	 * @param JPath	path names of .json files
	 * @param BUPath path names of BU.json files
	 */
	public static void processBibFiles(Scanner[] ins, PrintWriter[] out, String author, String[] JPath, String[] BUPath ) {
		//call the readFile method that read all input files
		readFile();
		//declare a boolean variable
		boolean check = true;
		try {
			//initializes the Json array
			Json = new File[3];
			//if the the .json files exist, then return true, otherwise return false
			check = checkJson(JPath);
			//handles the exception
		} catch (FileExistsException e) {
			//invokes the method to handle the exception
			checkBUJson(BUPath);
		}
		//create .json files
		createJson();
		
		//assign the stream to out
		for (int i = 0; i < 3; i++) {
			try {
				out[i] = new PrintWriter(new FileOutputStream(Json[i]));
			} catch (FileNotFoundException e) {
				//if the exception is thrown, displays the prompt
				System.out.println("The file " + Json[i].getName() + " cannot be written");
				//deletes and closes all created output files
				deleteCloseOut();
				//closes all opened input files
				closeInput();
				//terminates the program
				System.exit(0);
			}
		}

		// creates an ArrayList to store the article information
		ArrayList<String> article = new ArrayList<String>();
		// creates an ArrayList to store String array which store every single item in one article with a specified author name
		//which mean one string array stores one article's each item with the specified name
		ArrayList<String[]> articleContents = new ArrayList<String[]>();
		// commaIndex and newArticle are to simply the String that contains the article
		// information
		int commaIndex = 0;
		String newAticle = "";
		// assigns the article information to article
		for (int i = 0; i < ins.length; i++) {
			while (ins[i].hasNext()) {
				// uses @ARTICLE as delimiter when reading input files
				ins[i].useDelimiter("@ARTICLE");
				// reads the file
				newAticle = ins[i].next();
				commaIndex = newAticle.indexOf(",") + 1;
				// adds article information to the ArrayList
				article.add(newAticle.substring(commaIndex).trim());
			}
		}

		// finds the article information with specified author name, split the
		// information wiht }, as a String array, then add
		// it to aritcleContents
		boolean flag = false;
		int count = 0;
		for (String s : article) {
			if (s.indexOf("author=") != -1 && s.indexOf(author) != -1) {
				articleContents.add(s.split("},"));
				count++;
			}
		}
		//if count is greater than 0; this is, the specified author name isn't found in all input files, assigns true to flag
		if (count > 0)
			flag = true;

		// deletes all the punctuation and key values to get the simply detail of each item
		String authorName = "";
		int authorNameIndex = 0;
		String title = "";
		int titleIndex = 0;
		String journal = "";
		int journalIndex = 0;
		String volume = "";
		int volumeIndex = 0;
		String number = "";
		int numberIndex = 0;
		String pages = "";
		int pagesIndex = 0;
		String month = "";
		int monthIndex = 0;
		String year = "";
		int yearIndex = 0;
		String doi = "";
		int doiIndex = 0;
		//iterates the articleContents to get every single string array
		for (int i = 0; i < articleContents.size(); i++) {
			//traverse the string array get each item of the article
			for (String s : articleContents.get(i)) {
				//get the totally compact content of the author names
				if (s.indexOf("author={") != -1) {
					authorNameIndex = s.indexOf("={") + 2;
					authorName = s.substring(authorNameIndex);
				}
				if (s.indexOf("title={") != -1) {
					titleIndex = s.indexOf("={") + 2;
					title = s.substring(titleIndex);
				}
				if (s.indexOf("journal={") != -1) {
					journalIndex = s.indexOf("={") + 2;
					journal = s.substring(journalIndex);
				}
				if (s.indexOf("volume={") != -1) {
					volumeIndex = s.indexOf("={") + 2;
					volume = s.substring(volumeIndex);
				}
				if (s.indexOf("number={") != -1) {
					numberIndex = s.indexOf("={") + 2;
					number = s.substring(numberIndex);
				}
				if (s.indexOf("pages={") != -1) {
					pagesIndex = s.indexOf("={") + 2;
					pages = s.substring(pagesIndex);
				}
				if (s.indexOf("month={") != -1) {
					monthIndex = s.indexOf("={") + 2;
					month = s.substring(monthIndex);
				}
				if (s.indexOf("year={") != -1) {
					yearIndex = s.indexOf("={") + 2;
					year = s.substring(yearIndex);
				}
				if (s.indexOf("doi={") != -1) {
					doiIndex = s.indexOf("={") + 2;
					doi = s.substring(doiIndex);
				}
			}
			// writes the IEEE format files with the specified author name
			out[0].println(authorName + ". \"" + title + "\", " + journal + ", vol. " + volume + ", no. " + number
					+ ", p. " + pages + ", " + month + " " + year + ".");
			out[0].println();
			out[0].flush();
			// gets the first name of the specified author
			int andIndex = 0;
			int lastNameIndex = authorName.indexOf(author);
			String firstName = authorName.substring(0, lastNameIndex);
			if (firstName.lastIndexOf("and") != -1) {
				andIndex = firstName.lastIndexOf("and");
				firstName = firstName.substring(andIndex + 4);
			}
			// writes the ACM format files
			out[1].println("[" + (i + 1) + "]\t" + firstName + author + " el al. " + year + ". " + title + ". "
					+ journal + ". " + volume + ", " + number + "(" + year + "), DOI:http://doi.org/" + doi + ".");
			out[1].println();
			out[1].flush();

			// gets the author name list connected with &
			String[] nameArray = authorName.split("and");
			String nameList = "";
			for (int j = 0; j < nameArray.length - 1; j++) {
				nameList += nameArray[j] + "&";
			}
			nameList += nameArray[nameArray.length - 1];
			// writes the NJ format files
			out[2].println(nameList + ". " + title + ". " + journal + ". " + volume + ", " + pages + "(" + year + ").");
			out[2].println();
			out[2].flush();
		}

		//prints the outcome
		//if the author name the user enter can be found in all given files
		if (flag) {
			//if the .josn files already exist
			if (check) {
				for (int i = 0; i < 3; i++) {
					System.out.println("A file already exists with the name: " + JPath[i]);
					System.out.println(
							"File will be renamed as: " + BUPath[i] + " and any old BUs will be deleted\n");
				}
			}
			System.out.println("A total of " + articleContents.size() + " were found for author(s) with name: " + author);
			String xx = "";
			for(int i = 0 ; i < 3 ; i++) {
				xx += Json[i].getName() + ", ";
			}
			System.out.println("Files " + xx + "have been created!");
			//if the author name the user input cannot be found
		}else {
			//deletes and closes all created output files
			deleteCloseOut();
			System.out.println("No records were found for author(s) with name: " + author);
			System.out.println("No files have been created");
		}

		//closes all input files
		closeInput();
		//closes all output files
		closeOutput();
		System.out.println("\n\nGoodeBye! Hope you have enjoyed creating the needed files using AuthorBibCreator.");
	}

	// prints the welcome banner
	/**
	 * prints welcome banner
	 */
	public static void printWel() {
		System.out.println("***************************************************");
		System.out.println("      Welcome to the Author Bib Creator!!!");
		System.out.println("***************************************************");
	}// end of printWel();

	// method that closes the input files
	/**
	 * close all opened input files
	 */
	public static void closeInput() {
		//crosses through the ins
		for (int i = 0; i < ins.length; i++) {
			// if the file is opened, closes it
			if (ins[i] != null) {
				ins[i].close();
				// System.out.println("No."+(i+1)+" is closed");
			}
		}
	}// end of closeInput()

	/**
	 * close all created output files
	 */
	public static void closeOutput() {
		// if the file is created, close it
		for (int i = 0; i < out.length; i++) {
			if (out[i] != null) {
				out[i].close();
			}
		}
	}//end of closeOutput

	// close and delete all output files
	/**
	 * delete and close all output files
	 */
	public static void deleteCloseOut() {
		//if the file is created, close and delete it
		for (int i = 0; i < 3; i++) {
			if (out[i] != null) {
				out[i].close();
				Json[i].delete();
			}
		}
	}// end of deletecloseout

	// method that read/open the file
	/**
	 * read the input files
	 */
	public static void readFile() {
		// creates a String array to store the file path name
		String[] BibPaths = new String[10];
		for (int i = 0; i < 10; i++) {
			// assigns the path names to the String array
			BibPaths[i] = "Latex" + (i + 1) + ".bib";
			try {
				// opens the file
				ins[i] = new Scanner(new FileInputStream(BibPaths[i]));
			} catch (FileNotFoundException e) {
				// if the file isn't found, displays the prompt
				System.out.println("Could not open input file " + BibPaths[i]
						+ " for reading. \n\nPlease check if file exists! Program will terminate after closing any opened file.");
				// closes all opened files
				closeInput();
				// terminates the program
				System.exit(0);
			}
		}
	}// end of readFile()

	// method that checks whether or not the .jsom files exist
	// if exists, throws exception
	/**
	 * check whether the .json files exist
	 * @param JPath the path names of the .json files
	 * @return if .json files exist, return true; otherwise, return false
	 * @throws FileExistsException throws the exception if the files doesn't exist
	 */
	public static boolean checkJson(String[] JPath) throws FileExistsException {
		boolean flag = true;
		// checks whether the .json file exists
		for (int i = 0; i < 3; i++) {
			// creates a File object
			Json[i] = new File(JPath[i]);
		}
		for (int i = 0; i < 3; i++) {
			// if the .json file already exists, throw the exception
			if (Json[i].exists()) {
				throw new FileExistsException();
			// if the file doesn't exist, assign false to flag
			}else {
				flag = false;
			}
		}
		return flag;
	}//end of checkJson

	// creates .json file
	/**
	 * ceate .json files
	 */
	public static void createJson() {
		
		for (int i = 0; i < 3; i++) {
			try {
				Json[i].createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}//end of crerateJson

	// checks whether the BU.json files exist
	// if exists, deletes it and rename the .josn to BU; otherwise rename the .json
	// to BU
	/**
	 * checke whether the BU.json files exist
	 * @param BUPath the path name of BU.json files
	 */
	public static void checkBUJson(String[] BUPath) {
		// initiates the BUJson
		BUJson = new File[3];
		//create File objects and store them to BUJson
		for (int i = 0; i < 3; i++) {
			BUJson[i] = new File(BUPath[i]);
			//if the BU.json file exists, delete it, and rename the existing .json file to BU.json
			if (BUJson[i].exists()) {
				BUJson[i].delete();
				Json[i].renameTo(new File(BUPath[i]));
				BUJson[i] = Json[i];
			//else rename the existing .json file to BU.json directly
			} else {
				Json[i].renameTo(new File(BUPath[i]));
				BUJson[i] = Json[i];
			}
		}
	}// end of CheckBUJson
}// end of class
