package postagger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
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

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.process.WordToSentenceProcessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;




/*
 * author: dnes85
 * date: 10-dez-2014
 * version: 0.1
 * label: 
 * 
 * notes when running POS taggers: For memory problems (quoting Akash’s comment below):
 * It turns out that the problem is that eclipse allocates on 256MB of memory by default. 
 * RightClick on the Project->Run as->Run Configurations->Go to the arguments tab-> 
 * under VM arguments type -Xmx2048m This will set the allocated memory to 2GB and 
 * all the tagger files should run now.
 */


public class acl {
	
	private static Boolean debug = true;
	
	//Susanne Parameters
	private final static File susanne_db = new File("data/susanne/");
	private final static String susanne_regex = "^[A-Za-z][0-9]*$";
	private final static int susanne_index_tag = 2;
	private final static int susanne_index_word = 3;
	
	private final static String bsscript = "sh scripts/auxcorpus.sh ";
	
	//Stanford Parameters
	//private final static String stanf_language = AbstractReader.LANG_EN;
	
	
	
	//private static String taggers = "/home/esteves/workspace/aauxiliar/models/stanford-postagger-full-2014-10-26/models/";
	
	//private static String modelType = "general-en"; //"general-en" or "medical-en"
	//private static String stanfordTagger = "wsj-0-18-left3words-distsim.tagger";
	//private static String propsFile = "/train/susanne-left3words-distsim.tagger.props";
		
	public acl(String[] args) throws IOException,ClassNotFoundException {
		
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
		
		System.out.print("getting the tagset list...");
		List<String> distinctTagset  = getTagsetList(arraySusanneFilesToProcess);
		System.out.println("done! " + distinctTagset.size() + " distinct tags");
		System.out.println(distinctTagset.toString());
		System.out.println("");
		
		System.out.print("getting the tagset from OLiA...");
		oliaHelper olia = new oliaHelper();
		
		List<String> tagsetOLiA  = olia.getSusanneTagsetListFromOlia("susa.owl");
		System.out.println("done! " + tagsetOLiA.size() + " tags from OLiA");
		System.out.println(tagsetOLiA.toString());
		System.out.println("");
		
		
		//execBash2("sh scripts/ptb2conll.sh scripts/data/ca01.pos.ptb -nocomments > scripts/data/ca01.pos.ptb.conll");
		//execBash2("sh scripts/susa2conll.sh scripts/data/A01.susa > scripts/data/A01.susa.conll");
		//execBash2("sh scripts/conll-wordalign-tsv.sh scripts/data/ca01.pos.ptb.conll scripts/data/A01.susa.conll");
		//String bs[] = new String[]{"bash", "scripts/test.sh"};
		//execBash(bs);
	
		 
		//train(propsFile);
	
		/*String sentence = "This is a sample text";
		
		AbstractTokenizer tokenizer = NLPGetter.getTokenizer(language); 
		AbstractComponent tagger = NLPGetter.getComponent(modelType, language, NLPMode.MODE_POS);
		
		//tokens
		List<String> tokens = tokenizer.getTokens(sentence);
		
				
		MaxentTagger taggerMax = new MaxentTagger(taggers + stanfordTagger);

		String tagged2 = taggerMax.tagTokenizedString(sentence);
		String tagged = taggerMax.tagString(sentence); 
		 
		System.out.println(tagged);	 //This_DT is_VBZ a_DT sample_NN text_NN
		System.out.println(tagged2);	 
		
		//WordToSentenceProcessor
		
	
		demo(taggers + stanfordTagger, "iphone5.txt");*/
	}
	
	//http://www.java-tips.org/java-se-tips/java.util/from-runtime.exec-to-processbuilder.html
	private static void execBash(String bash) {
		try {
		    Runtime r = Runtime.getRuntime();                    

		    Process p = r.exec(bash);

		    BufferedReader in =
		        new BufferedReader(new InputStreamReader(p.getInputStream()));
		    String inputLine;
		    while ((inputLine = in.readLine()) != null) {
		        System.out.println(inputLine);
		    }
		    in.close();

		} catch (IOException e) {
		    System.out.println(e);
		}
		
		
	}
	
	public static void execBash(String[] bashCommand) {
		  try {
			  	//Process p1 = Runtime.getRuntime().exec(new String[]{"bash", bashCommand, pp1, p2, p3, p4});
			  	//p1.waitFor();
			  	
			   
			    System.out.println("starting script: " + Arrays.toString(bashCommand));
			  	Process p1 = Runtime.getRuntime().exec(bashCommand);
			  	p1.waitFor();
			 
			  
			  
			  	
			  /*
			  final ProcessBuilder pb = new ProcessBuilder(System.getProperty("user.dir"), "bash test.sh", "whatever",
					    "arguments", "go", "here");
					pb.directory(new File(System.getProperty("user.dir")));
					// redirect stdout, stderr, etc
					final Process p = pb.start();
					
					final ProcessBuilder pb = new ProcessBuilder("/bin/sh", "script.sh", "whatever",
						    "arguments", "go", "here");
						pb.directory(new File("/path/to/directory"));
						// redirect stdout, stderr, etc
						final Process p = pb.start();
					*/	
					
					
			   System.out.println("Working Directory = " +
			              System.getProperty("user.dir"));
			   
			  /*
		    ProcessBuilder pb = new ProcessBuilder(bashCommand);
		    if (dir != "") {pb.directory(new File(dir));}
		    Process p = pb.start();
		    if (runsSync){
		    	p.waitFor();	
		    }
		    */
			   
		    System.out.println("Script executed successfully");
		  } catch (Exception e) {
		    e.printStackTrace();
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
	
	private static void train(String props) {
		//MaxentTagger taggerMax = new MaxentTagger(taggers + stanfordTagger);
		String param[];
		param = new String[] {"-props" + props};
		
		try {
			MaxentTagger.main(param);
			System.out.println("model has been trained");
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}
	
	private static void demo(String modelFile, String fileToTag) throws UnsupportedEncodingException, FileNotFoundException{
		
		 MaxentTagger tagger = new MaxentTagger(modelFile);
		    TokenizerFactory<CoreLabel> ptbTokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(),
											   "untokenizable=noneKeep");
		    BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(fileToTag), "utf-8"));
		    PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out, "utf-8"));
		    DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor(r);
		    documentPreprocessor.setTokenizerFactory(ptbTokenizerFactory);
		    for (List<HasWord> sentence : documentPreprocessor) {
		      List<TaggedWord> tSentence = tagger.tagSentence(sentence);
		      pw.println(Sentence.listToString(tSentence, false));
		    }

		    // print the adjectives in one more sentence. This shows how to get at words and tags in a tagged sentence.
		    List<HasWord> sent = Sentence.toWordList("The", "slimy", "slug", "crawled", "over", "the", "long", ",", "green", "grass", ".");
		    List<TaggedWord> taggedSent = tagger.tagSentence(sent);
		    for (TaggedWord tw : taggedSent) {
		      if (tw.tag().startsWith("JJ")) {
		        pw.println(tw.word());
		      }
		    }

		    pw.close();
		  }

}


