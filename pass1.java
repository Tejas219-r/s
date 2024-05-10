package Assembler;
import java.io.BufferedReader;
import java.io.*;
import java.io.IOException;
import java.util.*;

public class pass1 {
	public static void main(String[] args) {

		BufferedReader br = null;
		FileReader fr = null;

		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			String inputfilename = "C:/Users/91985/Documents/LP1/SPOS/Assembler/Input.txt";     //Used absolute file address instead of only "Input.txt" used in ubuntu
			fr = new FileReader(inputfilename);
			br = new BufferedReader(fr);

			String OUTPUTFILENAME = "IC.txt";
			fw = new FileWriter(OUTPUTFILENAME);
			bw = new BufferedWriter(fw);

			Hashtable<String, String> is = new Hashtable<String, String>();
			is.put("STOP", "00");
			is.put("ADD", "01");
			is.put("SUB", "02");
			is.put("MULT", "03");
			is.put("MOVER", "04");
			is.put("MOVEM", "05");
			is.put("COMP", "06");
			is.put("BC", "07");
			is.put("DIV", "08");
			is.put("READ", "09");
			is.put("PRINT", "10");

			Hashtable<String, String> dl = new Hashtable<String, String>();
			dl.put("DC", "01");
			dl.put("DS", "02");

			Hashtable<String, String> ad = new Hashtable<String, String>();
			ad.put("START", "01");
			ad.put("END", "02");
			ad.put("ORIGIN", "03");
			ad.put("EQU", "04");
			ad.put("LTORG", "05");

			Hashtable<String, String> symtab = new Hashtable<String, String>();
			Hashtable<String, String> littab = new Hashtable<String, String>();

			String sCurrentLine;
			int locptr = 0;
			int litptr = 1;
			int symptr = 1;
			
			sCurrentLine = br.readLine();

			String s1 = sCurrentLine.split(" ")[1];
			if (s1.equals("START")) {
				bw.write("AD \t 01 \t");
				String s2 = sCurrentLine.split(" ")[2];
				bw.write("C \t" + s2 + "\n");
				locptr = Integer.parseInt(s2);
			}
			else{
			System.out.println("\nI/P must start with START ,add START in I/P ");
			}

			while ((sCurrentLine = br.readLine()) != null) {
				int mind_the_LC = 0;
				String type = null;

				int flag2 = 0;		//checks whether addr is assigned to current symbol
				
				String s = sCurrentLine.split(" |\\,")[0];	//consider the first word in the line
			                                                                                                                                        	
				for (Map.Entry m : symtab.entrySet()) {				//allocating addr to arrived symbols
					if (s.equals(m.getKey())) {						//that are already present
						m.setValue(locptr);
						flag2 = 1;
					}
				}
				
				if (s.length() != 0 && flag2 == 0) {		//if current string is not " " or addr is not assigned, then the current string must be a new symbol.
					symtab.put(s, String.valueOf(locptr));
					symptr++;
				}

				int isOpcode = 0;		//checks whether current word is an opcode or not
				
				s = sCurrentLine.split(" |\\,")[1];			//consider the second word in the line
				
				for (Map.Entry m : is.entrySet()) {
					if (s.equals(m.getKey())) {
						bw.write("IS\t" + m.getValue() + "\t");		//if match found in imperative stmt
						type = "is";
						isOpcode = 1;
					}
				}
				
				
				for (Map.Entry m : ad.entrySet()) {
					if (s.equals(m.getKey())) {
						bw.write("AD\t" + m.getValue() + "\t");		//if match found in Assembler Directive
						type = "ad";
						isOpcode = 1;
					}
				}
				for (Map.Entry m : dl.entrySet()) {
					if (s.equals(m.getKey())) {
						bw.write("DL\t" + m.getValue() + "\t");		//if match found in declarative stmt
						type = "dl";
						isOpcode = 1;
					}
				}
				
				
				if (s.equals("END")) {
					for (Map.Entry m : littab.entrySet()) {
						if (m.getValue() == "") {
							m.setValue(locptr);
							locptr++;
							mind_the_LC = 1;
						}
					}
				}
				
				
				if(s.equals("EQU")){
					symtab.put("equ", String.valueOf(locptr));
				}
				
				
				if (sCurrentLine.split(" |\\,").length > 2) {		//if there are 3 words
					s = sCurrentLine.split(" |\\,")[2];				//consider the 3rd word
																	
																	//this is our first operand.
																	//it must be either a Register/Declaration/Symbol
					if (s.equals("AREG")) {
						bw.write("1\t");
						isOpcode = 1;
					} else if (s.equals("BREG")) {
						bw.write("2\t");
						isOpcode = 1;
					} else if (s.equals("CREG")) {
						bw.write("3\t");
						isOpcode = 1;
					} else if (s.equals("DREG")) {
						bw.write("4\t");
						isOpcode = 1;
					} else if (type == "dl") {
						bw.write("C\t" + s + "\t");
					} else {
						symtab.put(s, "");				//forward referenced symbol
					}
				}
				

				if (sCurrentLine.split(" |\\,").length > 3) {		//if there are 4 words
					
					s = sCurrentLine.split(" |\\,")[3];			//consider 4th word.
																//this is our 2nd operand
																//it is either a literal, or a symbol
					if (s.contains("=")) {
						littab.put(s, "");
						bw.write("L\t" + litptr + "\t");
						isOpcode = 1;
						litptr++;
					} 
					// code does not skip symbols that are already present; instead, it updates their location pointers or adds them to the symbol table if they are encountered for the first time.
					else 				
					{
						flag2 = 0;		//checks whether addr is assigned to current symbol
						int ind=1;       // ptr for tracking already present symbols                          
						for (Map.Entry m : symtab.entrySet()) {				//allocating addr to arrived symbols
							if (s.equals(m.getKey())) {						//that are already present
								m.setValue(locptr);
								flag2 = 1;
								bw.write("S\t" + ind + "\t");//
								ind++;
							}
						}
						
						if (s.length() != 0 && flag2 == 0) {		//if current string is not " " or addr is not assigned,
																	//then the current string must be a new symbol.			
							bw.write("S\t" + symptr + "\t");		
							symtab.put(s, String.valueOf(locptr));
							symptr++;
						}
					}
				}

				bw.write("\n");		//done with a line.

				if (mind_the_LC == 0)
					locptr++;
			}

			String f1 = "SYMTAB.txt";
			FileWriter fw1 = new FileWriter(f1);
			BufferedWriter bw1 = new BufferedWriter(fw1);
			for (Map.Entry m : symtab.entrySet()) {
				bw1.write(m.getKey() + "\t" + m.getValue()+"\n");				
				System.out.println(m.getKey() + " " + m.getValue());
			}

			String f2 = "LITTAB.txt";
			FileWriter fw2 = new FileWriter(f2);
			BufferedWriter bw2 = new BufferedWriter(fw2);
			for (Map.Entry m : littab.entrySet()) {
				bw2.write(m.getKey() + "\t" + m.getValue()+"\n");
				System.out.println(m.getKey() + " " + m.getValue());
			}
			

			bw.close();
			bw1.close();
			bw2.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
