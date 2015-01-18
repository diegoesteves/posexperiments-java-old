import java.util.*;
import java.io.*;

public class Diff2CoNLLAlign {
	public static void main(String[] argv) throws Exception {
		boolean DEBUG = false;
		System.err.println("args: file1.conll file2.conll, read diff from stdin");
		BufferedReader conll1 = new BufferedReader(new FileReader(argv[0]));
		BufferedReader conll2 = new BufferedReader(new FileReader(argv[1]));
		BufferedReader diff= new BufferedReader(new InputStreamReader(System.in));
		
		DEBUG = argv[2].toLowerCase().contains("debug");
		
		String dLine = "";
		String c1Line = conll1.readLine();
		int c1Pos = 1;
		String c2Line = conll2.readLine();
		int c2Pos = 1;
		int c1Start = 0;
		int c1End = 0;
		int c2Start = 0;
		int c2End= 0;
		while(c1Line!=null || c2Line!=null) {
			if(dLine!=null && c1Pos>c1End) dLine=diff.readLine();
				while(dLine!=null && !dLine.matches("^[0-9,]+[cad][0-9,]+$")) dLine=diff.readLine();
				if(dLine!=null) {
					c1Start=Integer.parseInt(dLine.replaceFirst("[^0-9].*",""));
					c1End=Integer.parseInt(dLine.replaceFirst("[cad].*","").replaceFirst(".*,",""));
					c2Start=Integer.parseInt(dLine.replaceFirst(".*[cad]","").replaceFirst(",.*",""));
					c2End=Integer.parseInt(dLine.replaceFirst(".*[^0-9]",""));
					if(DEBUG) System.err.println(dLine);
				}
			
			
			if(c1Pos<c1Start || (dLine.contains("c") && c1Start==c1Pos && c1Start==c1End && c2Start==c2End)) { // matches
				if(c1Line.trim().equals("") && c2Line.trim().equals("")) 
					if(DEBUG) System.out.println(c1Pos+": "+c1Line);			// do not duplicate empty lines
					else System.out.println(c1Line);
				else 
					if(DEBUG) System.out.println(c1Pos+": "+c1Line+"\t| "+c2Pos+": "+c2Line);
					else System.out.println(c1Line+"\t| "+c2Line);
				c1Pos++;
				c2Pos++;
				c1Line=conll1.readLine();
				c2Line=conll2.readLine();
			} else {
				while(c1Pos<=c1End && c1Line!=null) {
					if(DEBUG)
						System.out.println(c1Pos+": "+c1Line+"\t>");
					else 
						System.out.println(c1Line+"\t>");
					c1Pos++;
					c1Line=conll1.readLine();
				}
				while(c2Pos<=c2End && c2Line!=null) {
					if(DEBUG)
						System.out.println("\t\t\t< "+c2Pos+": "+c2Line);
					else 
						System.out.println("\t\t\t< "+c2Line);
					c2Pos++;
					c2Line=conll2.readLine();
				}
			}
		}
		
	}
}