package postagger;


import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.List;

import com.clearnlp.component.AbstractComponent;
import com.clearnlp.component.dep.AbstractDEPParser;
import com.clearnlp.dependency.DEPTree;
import com.clearnlp.nlp.NLPGetter;
import com.clearnlp.nlp.NLPMode;
import com.clearnlp.reader.AbstractReader;
import com.clearnlp.segmentation.AbstractSegmenter;
import com.clearnlp.tokenization.AbstractTokenizer;
import com.clearnlp.util.UTInput;
import com.clearnlp.util.UTOutput;
import com.clearnlp.util.pair.ObjectDoublePair;
/**
 * @since 1.1.0
 * @author Jinho D. Choi ({@code jdchoi77@gmail.com})
 */
public class DemoMultiParse
{
	final String language = AbstractReader.LANG_EN;
	public DemoMultiParse(String modelType, String inputFile, String outputFile) throws Exception
	{
		AbstractTokenizer tokenizer = NLPGetter.getTokenizer(language);
		
		AbstractComponent tagger = NLPGetter.getComponent(modelType, language, NLPMode.MODE_POS);
		AbstractComponent parser = NLPGetter.getComponent(modelType, language, NLPMode.MODE_DEP);
		AbstractComponent identifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_PRED);
		AbstractComponent classifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_ROLE);
		AbstractComponent labeler = NLPGetter.getComponent(modelType, language, NLPMode.MODE_SRL);
		
		AbstractComponent[] preComponents = {tagger}; // components used before parsing
		AbstractComponent[] postComponents = {identifier, classifier, labeler}; // components used after parsing
		
		String sentence = "I know you know who I know.";
		
		process(tokenizer, (AbstractDEPParser)parser, preComponents, postComponents, sentence);
		process(tokenizer, (AbstractDEPParser)parser, preComponents, postComponents, UTInput.createBufferedFileReader(inputFile), UTOutput.createPrintBufferedFileStream(outputFile));
	}
	public void process(AbstractTokenizer tokenizer, AbstractDEPParser parser, AbstractComponent[] preComponents, AbstractComponent[] postComponents, String sentence)
	{
		DEPTree tree = NLPGetter.toDEPTree(tokenizer.getTokens(sentence));
		List<ObjectDoublePair<DEPTree>> trees = getParses(parser, preComponents, postComponents, tree);
		for (ObjectDoublePair<DEPTree> p : trees)
		{
			tree = (DEPTree)p.o;
			System.out.println("Score: "+p.d);
			System.out.println(tree.toStringSRL()+"\n");
		}
	}
	public void process(AbstractTokenizer tokenizer, AbstractDEPParser parser, AbstractComponent[] preComponents, AbstractComponent[] postComponents, BufferedReader reader, PrintStream fout)
	{
		AbstractSegmenter segmenter = NLPGetter.getSegmenter(language, tokenizer);
		List<ObjectDoublePair<DEPTree>> trees;
		DEPTree tree;
		for (List<String> tokens : segmenter.getSentences(reader))
		{
			tree = NLPGetter.toDEPTree(tokens);
			trees = getParses(parser, preComponents, postComponents, tree);
			for (ObjectDoublePair<DEPTree> p : trees)
			{
				tree = (DEPTree)p.o;
				fout.println("Score: "+p.d);
				fout.println(tree.toStringSRL()+"\n");
			}
		}
		fout.close();
	}
	private List<ObjectDoublePair<DEPTree>> getParses(AbstractDEPParser parser, AbstractComponent[] preComponents, AbstractComponent[] postComponents, DEPTree tree)
	{
		List<ObjectDoublePair<DEPTree>> trees;
		boolean uniqueOnly = true; // return only unique trees given a sentence
		for (AbstractComponent component : preComponents)
			component.process(tree);
		trees = parser.getParsedTrees(tree, uniqueOnly);
		// parses are already sorted by their scores in descending order
		for (ObjectDoublePair<DEPTree> p : trees)
		{
			tree = (DEPTree)p.o;
			for (AbstractComponent component : postComponents)
				component.process(tree);
		}
		return trees;
	}
	public static void main(String[] args)
	{
		String modelType =  "general-en"; // args[0]; // "general-en" or "medical-en"
		String inputFile = "iphone5.txt";//args[1];
		String outputFile = "iphone5.txt.mul";//args[2];
		try
		{
			new DemoMultiParse(modelType, inputFile, outputFile);
		}
		catch (Exception e) {e.printStackTrace();}
	}
}