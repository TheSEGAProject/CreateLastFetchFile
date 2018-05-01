package edu.nau.rtisnl;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class CreateLastFetchFile {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		(new CreateLastFetchFile()).run();
	}
	
	public void run(){
		System.out.println("Time Now: " + sdf.format(new Date()));
		System.out.println("There are two ways to enter a timestamp for the last fetch file.\r\n" +
				"[1] Long - milliseconds since 1970\r\n" +
				"[2] String - date formatted as 'yyyy-MM-dd HH:mm:ss'\r\n" +
				"\r\n\r\n Please select an option to continue:");
		Scanner scan = new Scanner(System.in);
		String val = scan.nextLine();
		
		int option = -1;
		try{
			option = Integer.parseInt(val);
			
			if(option != 1 && option != 2){
				System.out.println("Unrecognized option entered (" + option + ")...exiting");
				System.exit(-1);
			}
			
		}catch(NumberFormatException e){
			System.out.println("Number format exception - please enter a valid integer option");
			System.exit(-1);
		}
		
		long timestamp = 0;
		
		
		
		if(option == 1){			
			try{
				System.out.println("You have selected option [1] Long - milliseconds since 1970\r\n\r\n" +
						"Please input a timestamp (ms since 1970) to create the last fetch file\r\n\r\n" +
						"Timestamp (ms since 1970): ");
				val = scan.nextLine();				
				timestamp = Long.parseLong(val);
			}catch(NumberFormatException e){
				System.out.println("You entered: '" + val + "' - Number format exception - please enter a valid timestamp (ms since 1970) of type 'long'");
				System.exit(-1);
			}
		}
		else if (option == 2){
			System.out.println("You have selected option [2] String - date formatted as 'yyyy-MM-dd HH:mm:ss'\r\n\r\n" +
					"Please input a date that adheres to the above format.\r\n\r\n" +
					"Timestamp ('yyyy-MM-dd HH:mm:ss'): ");
			val = scan.nextLine();
			try {
				timestamp = sdf.parse(val).getTime();
			} catch (ParseException e) {
				System.out.println("You entered: '" + val + "' - Invalid date format, please try again...exiting");
				System.exit(-1);
			}
		}
		System.out.println("You entered: '" + val + "'");
		System.out.println("Parsed timestamp: " + new Date(timestamp).toString() +  " - " + timestamp + ")");
		System.out.println("Is this the correct starting timestamp for the last fetch file?" +
				"\r\n\r\nContinue? [Y/n]: ");
		val = scan.nextLine();
		
		if(val.equals("Y")){
			System.out.println("To continue, specify a directory or hit <Enter> to write to the current directory.\r\n" + 
					"Directory (NOTE: must be unix style absolute path):\r\n");
			val=scan.nextLine();
			write_last_fetch_timestamp(timestamp,val);			
			scan.close();
		}
		else{
			System.out.println("User cancelled operation. Exiting...");
			scan.close();
			System.exit(-1);
		}
	}
	
	/**
	 * Write the last known fetch time to disk
	 * @param timestamp
	 */
	private void write_last_fetch_timestamp(long timestamp, String directory){
		DataOutputStream output;
		File last_fetch_file;
		if(directory.isEmpty()){
			directory = "./last_fetch_timestamp";
			last_fetch_file = new File("./last_fetch_timestamp");
		}
		else{
			directory = directory + "/last_fetch_timestamp";
			last_fetch_file = new File(directory + "/last_fetch_timestamp");
		}
		try {
			//If file doesn't exist, create it and init to zero
			if (!last_fetch_file.exists()){				
				last_fetch_file.createNewFile();
			}
			// Open the file
			output = new DataOutputStream(new FileOutputStream(last_fetch_file, false));
			// Write time to file
			output.writeLong(timestamp);
			// Close the file
			output.close();
			System.out.println("File written to " + directory + "... Done");
						
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException: Last fetch start time file was not created.");
		} catch (IOException e) {
			System.out.println("IOException: Unable to write to last fetch start time file.");
		}
	}
	
	

}
