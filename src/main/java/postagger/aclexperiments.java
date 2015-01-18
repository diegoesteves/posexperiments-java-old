package postagger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clearnlp.component.AbstractComponent;
import com.clearnlp.nlp.NLPGetter;
import com.clearnlp.nlp.NLPMode;
import com.clearnlp.reader.AbstractReader;
import com.clearnlp.tokenization.AbstractTokenizer;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class aclexperiments {
	
	//Susanne Parameters
	private final static File susanne_db 		= new File("data/susanne/");
	private final static String susanne_regex 	= "^[A-Za-z][0-9]*$";
	private final static int susanne_index_tag 	= 2;
	private final static int susanne_index_word = 3;
	//Scripts
	private final static String bsscript = "sh scripts/auxcorpus.sh ";
	
	//POS - Stanford
	private final static String stanford_language 		= AbstractReader.LANG_EN;
	private final static String stanford_taggers_folder = "models/taggers/stanford-full-2014-10-26/models/";
	private final static String stanford_modelType 		= "general-en"; //"general-en" or "medical-en"
	private final static String stanford_tagger 		= "wsj-0-18-left3words-distsim.tagger";
	private final static String stanford_propsFile 		= "train/susanne-left3words-distsim.tagger.props";
	
	public interface StanfordType {
	    int TRAIN = 0;
	    int TEST = 1;  
	    int TAG = 2;
	}
	public static void main(String[] args) throws IOException, InterruptedException {
		
		System.out.println("reading the files to be processed [" + susanne_db + "]");
		List<String> arraySusanneFilesToProcess = addSusanneFilesToArray(susanne_db);
		Collections.sort(arraySusanneFilesToProcess);
		System.out.println("done! total files to be processed: " + arraySusanneFilesToProcess.size());
		System.out.println("");
		
		System.out.print("converting the files to Stanford POS format...");
		Iterator<String> iterator = arraySusanneFilesToProcess.iterator();
		while (iterator.hasNext()) {
			execBash(bsscript + iterator.next());
		}
		System.out.println("done! Tks Christian! ;-)");
		System.out.println("");
		
		System.out.println("getting the tagset from OLiA Ontology...");
		oliaHelper olia = new oliaHelper();		
		List<String> tagsetOLiA  = olia.getSusanneTagsetListFromOlia("susa.owl");
		System.out.println("");
		System.out.println("done! " + tagsetOLiA.size() + " tags from OLiA");
		System.out.println(tagsetOLiA.toString());
		System.out.println("");
		
		System.out.println("getting the current tagset list from Corpus...");
		List<String> distinctTagset  = getTagsetList(arraySusanneFilesToProcess);
		System.out.println("done! " + distinctTagset.size() + " distinct tags");
		System.out.println(distinctTagset.toString());
		System.out.println("");
		
		//checking targets
		Iterator<String> it = distinctTagset.iterator();
		while (it.hasNext()) {
		    if (!tagsetOLiA.contains(it.next())) {
		    	System.out.println("error: tagset " + it.next().toString() + " isnt on OLiA tagset");
		    }
		}
		
		//tag operation test
		System.out.println("");
		System.out.println("initializing Stanford Tagger..");
		Boolean ret = stanfordOperation(StanfordType.TAG, stanford_propsFile, "", stanford_taggers_folder + stanford_tagger, "iphone5.txt");
		if (ret){
			System.out.println("The text has been tagged!");
		}
		
		

	}
	
	/**
	 * execute a bash script 
	 * @param bash
	 * @throws InterruptedException
	 */
	private static void execBash(String bash) throws InterruptedException {
		try {
		    Runtime r = Runtime.getRuntime();                    
		    Process p = r.exec(bash);
		    p.waitFor();
		    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		    String inputLine;
		    while ((inputLine = in.readLine()) != null) {
		        System.out.println(inputLine);
		    }
		    in.close();

		} catch (IOException e) {
		    System.out.println(e);
		}
		
		
	}
	
	/** 
	 * returns a list of distinct tagsets considering all files
	 * @param files
	 * @return list of distinct tagsets
	 * @throws IOException
	 */
	private static ArrayList<String> getTagsetList(List<String> files) throws IOException{
		Iterator<String> iterator = files.iterator();
		List<String> tagsetList = new ArrayList<String>();
		
		while (iterator.hasNext()) {
		    String filename = iterator.next();
			FileReader fr = new FileReader(filename);
			BufferedReader bf = new BufferedReader(fr);
			String ln = null;
			while ((ln = bf.readLine()) != null){
				String susannesplited[] = ln.split("\t");
				if (!tagsetList.contains(susannesplited[susanne_index_tag])){
					tagsetList.add(susannesplited[susanne_index_tag]);	
				}
			}
			bf.close();
		}
		
		return (ArrayList<String>) tagsetList;
	}
	
	/**
	* transform from susanne corpus format to stanford POS tagger corpus format and return list of all tokens
	 * @param files (list of files to be converted)
	 * @param outputfile (name of output file)
	 * @return list of distinct tagsets existing on files 
	 * @throws IOException
	 */
	private static ArrayList<String> transformFromSusanneToStanfordFormatAndGetTokenList(List<String> files, String outputfile) throws IOException{
		
		File fout = new File(outputfile);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		List<String> tagsetList = new ArrayList<String>();
		
		//FileWriter fw = new FileWriter(outputfile, false);
		Iterator<String> iterator = files.iterator();
		System.out.println("starting...");
		
		while (iterator.hasNext()) {
			    String filename = iterator.next();
				FileReader fr = new FileReader(filename);
				BufferedReader bf = new BufferedReader(fr);
				String ln = null;
				String paragraph = null;
				while ((ln = bf.readLine()) != null){
					String susannesplited[] = ln.split("\t");
					
					if (susannesplited[susanne_index_tag].equals("YF")) {
						paragraph += " ._.";
						bw.write(paragraph);
						bw.newLine();
						
						paragraph = "";
					}
					else
					{
						paragraph += susannesplited[susanne_index_word] + "_" + susannesplited[susanne_index_tag] + " ";
					}
					
					if (!tagsetList.contains(susannesplited[susanne_index_tag])){
						tagsetList.add(susannesplited[susanne_index_tag]);	
					}
					
				}
				bw.flush();
				bf.close();
				//System.out.println("filename: " + filename);
		}
		
		bw.close();
		return (ArrayList<String>) tagsetList;
		
	}
	
	/**
	 * look at susanne's folder and add all files to array
	 * @param folder
	 * @return array of files to be processed
	 */
	private static List<String> addSusanneFilesToArray(final File folder) {
		 List<String> susanneFiles = new ArrayList<String>();;
		 
		 for (final File fileEntry : folder.listFiles()) {
	         if (fileEntry.isDirectory()) {
	        	 addSusanneFilesToArray(fileEntry);
	         } else {
	             Pattern r = Pattern.compile(susanne_regex);
	             Matcher m = r.matcher(fileEntry.getName());
	             if (m.find()) {
	            	 susanneFiles.add(fileEntry.getAbsolutePath());
	             }
	         }
	     }
	     return susanneFiles;
	 }
	
	/**
	 * note: running on Java 8 (requeriment of new Stanford POS Tagger)
	 * @param type
	 * @param props
	 * @param corpus
	 * @param model
	 * @param text
	 * @return
	 */
	private static Boolean stanfordOperation(int type, String props, String corpus, String model, String text) {
		
		String param[] = null;
		
		switch(type)
		{
		case StanfordType.TRAIN:
			param = new String[] {"-props", props, "-trainFile", corpus, "-model", model};	//output model
			break;
		case StanfordType.TEST:
			param = new String[] {"-props", props, "-testFile", corpus, "-model", model};	//input model
			break;
		case StanfordType.TAG:
			param = new String[] {"-props", props, "-textFile", text, "-model",  model};	
			break;
		default:
			System.out.println("unknown parameter = " + type);
			return false;
		}
		
		
		try {
			MaxentTagger.main(param);
			System.out.println("model has been trained");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
}
