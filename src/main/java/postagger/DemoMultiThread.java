package postagger;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.clearnlp.component.AbstractComponent;
import com.clearnlp.dependency.DEPTree;
import com.clearnlp.nlp.NLPGetter;
import com.clearnlp.nlp.NLPMode;
import com.clearnlp.reader.AbstractReader;
import com.clearnlp.segmentation.AbstractSegmenter;
import com.clearnlp.tokenization.AbstractTokenizer;
import com.clearnlp.util.UTInput;
import com.clearnlp.util.UTOutput;
import com.google.common.collect.Lists;
/**
 * @since 1.1.0
 * @author Jinho D. Choi ({@code jdchoi77@gmail.com})
 */
public class DemoMultiThread
{
	final String language = AbstractReader.LANG_EN;
	AbstractComponent[] c_components;
	public DemoMultiThread(String modelType, String inputFile, String outputFile, int numThreads) throws Exception
	{
		AbstractTokenizer tokenizer = NLPGetter.getTokenizer(language);
		AbstractComponent tagger = NLPGetter.getComponent(modelType, language, NLPMode.MODE_POS);
		AbstractComponent parser = NLPGetter.getComponent(modelType, language, NLPMode.MODE_DEP);
		AbstractComponent identifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_PRED);
		AbstractComponent classifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_ROLE);
		AbstractComponent labeler = NLPGetter.getComponent(modelType, language, NLPMode.MODE_SRL);
		c_components = new AbstractComponent[]{tagger, parser, identifier, classifier, labeler};
		process(tokenizer, UTInput.createBufferedFileReader(inputFile), UTOutput.createPrintBufferedFileStream(outputFile), numThreads);
	}
	public void process(AbstractTokenizer tokenizer, BufferedReader reader, PrintStream fout, int numThreads)
	{
		AbstractSegmenter segmenter = NLPGetter.getSegmenter(language, tokenizer);
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		List<DEPTree> trees = Lists.newArrayList();
		DEPTree tree;
		for (List<String> tokens : segmenter.getSentences(reader))
		{
			tree = NLPGetter.toDEPTree(tokens);
			trees.add(tree);
			executor.execute(new DecodeTask(tree));
		}
		executor.shutdown();
		try
		{
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		}
		catch (InterruptedException e) {e.printStackTrace();}
		for (DEPTree t : trees)
			fout.println(t.toStringSRL()+"\n");
		fout.close();
	}
	class DecodeTask implements Runnable
	{
		DEPTree d_tree;
		public DecodeTask(DEPTree tree)
		{
			d_tree = tree;
		}
		public void run()
		{
			for (AbstractComponent component : c_components)
				component.process(d_tree);
		}
	}
	public static void main(String[] args)
	{
		String modelType =  "general-en"; // args[0]; // "general-en" or "medical-en"
		String inputFile = "iphone5.txt";//args[1];
		String outputFile = "iphone5.txt.thr";//args[2];
		int numThreads = Integer.parseInt(args[3]);
		try
		{
			new DemoMultiThread(modelType, inputFile, outputFile, numThreads);
		}
		catch (Exception e) {e.printStackTrace();}
	}
}