package com.kaizen.plant; 

import java.util.Set;

import xtc.lang.JavaFiveParser;
import xtc.lang.JavaPrinter;

import xtc.parser.ParseException;
import xtc.parser.Result;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.util.Tool;

/**
 * Finds where scopes begin and end.
 */
public class GetDependencies extends Tool {
    
    
    public GetDependencies() {
        // Nothing to do.
    }
    
    public String getName() {
        return "Find Scope of Java Components";
    }
    
    public String getCopy() {
        return "(C) 2013 Tara Wilson";
    }
    
    public void init() {
        super.init();
        
      
    }
    
    public void prepare() {
        super.prepare();
        
        // Perform consistency checks on command line arguments.
    }
    
    public File locate(String name) {
        File file = super.locate(name);
        if (Integer.MAX_VALUE < file.length()) {
            throw new IllegalArgumentException(file + ": file too large");
        }
        return file;
    }
    
    public Node parse(Reader in, File file)  {
        JavaFiveParser parser =
        new JavaFiveParser(in, file.toString(), (int)file.length());
        Result result = parser.pCompilationUnit(0);
        return (Node)parser.value(result);
    }



    
    public void process(Node node) {
        
       
            new Visitor() {
                
                
              
                
                public void visitImportDeclaration(GNode n) {
					// runtime.console().p("This is an import statement.").p(n.getNode(1).toString()).pln().flush();
					String[] files = new String[100];
					int startfiles = 0;
					String fileToImport = "";
					for (int i = 0; i < n.getNode(1).size(); i++){
						fileToImport += "/" + ((String)(n.getNode(1).get(i)));
						
					}
					fileToImport += ".java";
					files[startfiles] = fileToImport;
					
					runtime.console().p(files[startfiles]).pln().flush();
					startfiles++;
                    visit(n);
					
                }

				public void visitPackageDeclaration(GNode n) {
					runtime.console().p("This is a package statement.").pln().flush();
	                visit(n);

	            }
					
								                
				               public void visit(Node n) {
				                  for (Object o : n) if (o instanceof Node) dispatch((Node) o);
				               }
                
            }.dispatch(node);
        }
    
    
 
    public static void main(String[] args) {
       
        new GetDependencies().run(args);
    }
    
}
