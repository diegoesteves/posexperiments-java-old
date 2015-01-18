package postagger;

import com.clearnlp.nlp.engine.NLPDecode;

public class cnlptests {
	
	/**
	 * http://clearnlp.wikispaces.com/dataFormat
	 * @author esteves
	 *
	 */
	public interface DataFormat {
		String RAW = "raw";
		String LINE = "lin";
		String TOKEN = "tok";
	    String POS = "pos";
	    String MORPH = "morph";
	    String DEP = "dep";  
	    String SRL = "srl";
	}
	
	public interface Model_Type {
	    int SPECIFIC = 0;
	    int GENERALIZED = 1;  
	    int DOMAINSELECTION = 2;
	}

	public static void main(String[] args)
	{
		String conf = "config_en_pos.xml";
		String inputFile = "iphone5.txt";
		String mode = DataFormat.POS;
		String outputExt = "cnlp." + mode;
	
		try
		{
			String[] tst = new String[1];
			tst[0] = "iphone5.txt";
			
			clearnlpDecode(conf,inputFile, "", outputExt, mode);
			//clearnlpTrain("config_en_pos.xml", "feature_en_pos.xml", "cnlp_train/", "sample-pos-out.jar" );
			System.out.println("done!");
		}
		catch (Exception e) {e.printStackTrace();}
	}
	
	private static void clearnlpDecode(String conf, String inputFile, String inputExt, String outputExt, String mode) throws Exception{
		NLPDecode nlp = new NLPDecode();
		nlp.decode(conf, inputFile, inputExt, outputExt, mode);
	}
	
	
	/**
	 * 
	 * @param conf 					- configuration file (required)
	 * @param feat					- feature template file (required)
	 * @param input_train			- input directory containg training files (required)
	 * @param model_output			- model file (output; required)
	 * @param model_type			- model type - 0|1|2 (default: 1)
	 * 									0: train only a domain-specific model
     *             						1: train only a generalized model
     *             						2: train both models using dynamic model selection
	 * @param threshold_dynamic		- similarity threshold (default: -1)
	 */
	private static void clearnlpTrain(String conf, String[] feat, String input_train, String model_output, int model_type, double threshold_dynamic){
		
	}
	
}
