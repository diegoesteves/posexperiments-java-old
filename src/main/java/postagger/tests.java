package postagger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import com.clearnlp.nlp.engine.NLPDecode;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class tests {
	
	private static String taggers_stanford = System.getProperty("user.dir") + "/models/taggers/stanford-full-2014-10-26/models/";
	private static String text = "";
	private static String inputfile = "susanne.txt";
	
	 public static void main(String[] args) throws IOException,ClassNotFoundException {
		 //stanford
		 
		   String aux = taggers_stanford + "wsj-0-18-left3words-distsim.tagger";
		 
		    String op[] = new String[4] ;
		    
		    //= {"-model", aux.toString(), "-props","train/susanne-left3words-distsim.tagger.props", "-textFile", "train/susanne.text" };
		 	//String[] op = {"-model", model};
		
		    op[2] = "-model";
		    op[3] = "wsj-0-18-left3words-distsim.tagger"; //aux.toString();
		    op[0] = "-props";
		    op[1] = "models/taggers/stanford-full-2014-10-26/models/susanne-left3words-distsim.tagger.props";
		    
		    
		    
		    int size = op.length;
		    for (int i=0; i<size; i++){
		    	System.out.println(op[i]);}
		    
		    
			try {
				MaxentTagger.main(op);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
			/*
		 	MaxentTagger tagger = new MaxentTagger(taggers_stanford + "wsj-0-18-left3words-distsim.tagger");
			String text = "";
			String line = null;
		 	
			BufferedReader bufferedReader = new BufferedReader(new FileReader(inputfile));
			    while ((line = bufferedReader.readLine()) != null)
			    {
			        text+=line;
			    }
			   
			    // close the BufferedReader when we're done
			    bufferedReader.close();
			
			String tagged = tagger.tagString(text);
			System.out.println(tagged);	 //This_DT is_VBZ a_DT sample_NN text_NN 
			
			PrintWriter writer = new PrintWriter(inputfile + ".stanford.pos", "UTF-8");
			writer.println(tagged.toString());
			writer.close();
			
		 
		 //cnlp
			NLPDecode nlp = new NLPDecode();
			nlp.decode("config_en_pos.xml", inputfile, "", "cnlp.pos", "pos");
		 
		 //checking
		 */
			
	 }
	 
	 
	 
	 

}
