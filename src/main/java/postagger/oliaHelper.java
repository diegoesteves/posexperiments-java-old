package postagger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

public class oliaHelper {
	
	public oliaHelper(){
		
	}
	
	public List<String> getSusanneTagsetListFromOlia(String owlfile) {
	
		List<String> tagsetList = new ArrayList<String>();
	
		try{
			
			Model model = ModelFactory.createDefaultModel();
			InputStream in = FileManager.get().open( owlfile );
			if (in == null) {
				throw new IllegalArgumentException("File: " + owlfile + " not found");
			}
			model.read(in, "");
			
			int i=0;
			StmtIterator iter2 = model.listStatements(); //get triples
			while (iter2.hasNext()) {
				Statement stmt      = iter2.nextStatement();  // get next statement
				Resource  subject   = stmt.getSubject();     // get the subject
				Property  predicate = stmt.getPredicate();   // get the predicate
				RDFNode   object    = stmt.getObject();      // get the object
	
				 if (predicate.getLocalName().toString().equals("hasTag")) {
					 tagsetList.add(object.asLiteral().getString());
				}
			   
			}		
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
		return tagsetList;
	}

}
