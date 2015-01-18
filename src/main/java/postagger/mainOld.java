package postagger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.Tree;

/* *
 * http://nlp.stanford.edu/software/corenlp.shtml
 * Stanford CoreNLP integrates many of our NLP tools, including:
 * 		part-of-speech (POS) tagger, 
 * 		named entity recognizer (NER), 
 * 		parser, 
 * 		coreference resolution system, 
 * 		sentiment analysis, 
 * 		bootstrapped pattern learning tools. 
 * */

public class mainOld {
	
	private static String _models = "/home/esteves/workspace/aauxiliar/models/stanford-postagger-full-2014-10-26/models/";
	private static String _models2 = "/home/esteves/workspace/aauxiliar/models/stanford-corenlp-caseless-2014-02-25-models/edu/stanford/nlp/models/lexparser/";
	

	public static void main(String[] args){
		System.out.println("start");
		test();
		test2();
		System.out.println("end");
		
		//sample s = new sample();
		//s.ex();
	}
		
	private static final Pattern TAG_REGEX = Pattern.compile("<PERSON>(.+?)</PERSON>");
	
	/*
	 * private static void test3(){
		String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";


        AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier
                .getClassifierNoExceptions(serializedClassifier);

        		String s1 = "Good afternoon Rahul Kulhari, how are you today?";

		       s1 = s1.replaceAll("\\s+", " ");
		       String t = classifier.classifyWithInlineXML(s1);
		    System.out.println(Arrays.toString(getTagValues(t).toArray()));
		
	}
	*/
	
	private static Set<String> getTagValues(final String str) {
	    final Set<String> tagValues = new HashSet<String>();
	    //final Set<String> tagValues = new TreeSet();
	    final Matcher matcher = TAG_REGEX.matcher(str);
	    while (matcher.find()) {
	        tagValues.add(matcher.group(1));
	    }

	    return tagValues;
	}
	
	
	private static void test2(){
		String a = "I like watching movies. What about you?";
		MaxentTagger maxTagger =  new MaxentTagger(_models + "english-left3words-distsim.tagger");
		String tagged = maxTagger.tagString(a);
		System.out.println(tagged);
	}
	
	private static void test(){
		String[] sent = { "This", "is", "an", "easy", "sentence", "." };
		LexicalizedParser lp = LexicalizedParser.loadModel(_models2 + "englishPCFG.caseless.ser.gz");
		List<CoreLabel> rawWords = Sentence.toCoreLabelList(sent);
		Tree parse = lp.apply(rawWords);
		ArrayList ar=parse.taggedYield();
		System.out.println(ar.toString()); 
	}

}
