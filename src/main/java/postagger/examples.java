package postagger;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;
import com.hp.hpl.jena.vocabulary.VCARD;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class examples {
	//static Logger logger = Logger.getLogger(examples.class);
	static final String inputFileName = "susa.owl";

	public static void main(String args[]) {
		try {
			
			//https://jena.apache.org/tutorials/rdf_api.html
			//https://github.com/apache/jena/blob/master/jena-core/src-examples/jena/examples/rdf/Tutorial03.java
			//https://jena.apache.org/documentation/notes/typed-literals.html
			//http://staff.um.edu.mt/cabe2/lectures/webscience/docs/jena.pdf
			
			Model model = ModelFactory.createDefaultModel();
			InputStream in = FileManager.get().open( inputFileName );
			if (in == null) {
				throw new IllegalArgumentException("File: " + inputFileName + " not found");
			}
			
			model.read(in, "");
			Property p = model.getProperty("hasTag");;
			
			
			
			int i=0;
			StmtIterator iter2 = model.listStatements(); //get triples
			while (iter2.hasNext()) {
				Statement stmt      = iter2.nextStatement();  // get next statement
				Resource  subject   = stmt.getSubject();     // get the subject
				Property  predicate = stmt.getPredicate();   // get the predicate
				RDFNode   object    = stmt.getObject();      // get the object

				 if (predicate.getLocalName().toString().equals("hasTag")) {
					 System.out.println(object.asLiteral().getString());
					 i++;
				 }
			   
			}		
			
			System.out.println(i);			
			
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}