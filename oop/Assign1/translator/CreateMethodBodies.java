package oop;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import xtc.lang.JavaFiveParser;
import xtc.lang.CParser;
import xtc.lang.JavaPrinter;
import xtc.lang.CPrinter;
import java.util.regex.*; 
import xtc.parser.ParseException;
import xtc.parser.Result;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import java.util.*; 
import xtc.util.Tool;

import oop.GetDependencies;
import oop.InheritanceHandler;
import oop.ASTConverter;
import oop.CPPrinter;
import java.io.*; 



/* Created by Ankit Goel**/
public class CreateMethodBodies extends xtc.util.Tool { 
    
    
    
    public CreateMethodBodies(GNode n) { 
	
	final PrintWriter p1; 
        File headerFile; 
        
        File newDirectory;
        
        try { 
            
            newDirectory = new File("cplusplusfiles"); 
            newDirectory.mkdir(); 
            headerFile = new File("cplusplusfiles", "Method_Bodies.cc"); 
            headerFile.createNewFile(); 
            p1 = new PrintWriter(headerFile); 
            
              
            
            p1.println("#include \"java_lang.h\" ");
            p1.println("#include \"Header.h\" ");
           // p1.println("#include sstream
            p1.println(" namespace java { ");
            p1.println(" namespace lang { "); 
            
            // Iterate over the Classes and basically add these nodes to GNode            
            final GNode stackOfClasses = GNode.create("Holder"); 
            
            new Visitor() {
                int counter22 = 0; 
                
                public void visitClasses(GNode n ) { 
                                        
                    
                    visit(n); 
                }
                
                public void visitClassDeclaration(GNode n) { 
                    
                    stackOfClasses.add(n);
                    
                }
                
                public void visit(Node n) {
                    for (Object o : n) if (o instanceof Node) dispatch((Node) o);
                }
                
            }.dispatch(n);
          
            
            /** Debug Info **/ 
                //p1.println(n.size());
            
            
            // Iterate through Class Declarations
            for ( int bba = 0; bba < stackOfClasses.size(); bba++) { 
            
                // Constructor AND VTable stuff 
                p1.println();
                p1.println("     " + "__" + stackOfClasses.getNode(bba).getString(1) + "::" + "__" + stackOfClasses.getNode(bba).getString(1) + "() " + ":" + " __vptr(&__vtable) {} ");
                
                p1.println();
                
                p1.println("     " + "__" +  stackOfClasses.getNode(bba).getString(1) + "_VT" + " __" + stackOfClasses.getNode(bba).getString(1) + "::" + "__vtable" + ";"); 
                p1.println();
                p1.println();
                p1.println();
                
                
		final String className = "__" + stackOfClasses.getNode(bba).getString(1); 
                
		// Move on to method Declaration stuff 
                    
                        // Formal Paramters should be internalized 
          
                new Visitor() {
                    
                    String methodName; 
                    public void visitMethodDeclaration(GNode n ) { 
                        
                        methodName = n.getString(3); 
			//p1.println(methodName);
                        visit(n); 
                    }
                   

		
		  public void visitType(GNode n) { 


			
				p1.println("void " + className + "::" + methodName); 
			
			//else
				//p1.println( n.getNode(2).getNode(0).getString(0) + className + "::" + methodName + "(" + className + "__this");
		  }

		

		   public void visitBlock( GNode n) { 		
			
			visit(n);
	           }	

		   public void visitExpressionStatement (GNode n) { 


			p1.println(" { ");
			visit(n);
			p1.println(" } "); 

		  }	

	
                    public void visit(Node n) {
                        for (Object o : n) if (o instanceof Node) dispatch((Node) o);
                    }
                    
                }.dispatch(stackOfClasses.getNode(bba));
                
               
                

            }
            
            p1.println("}");
            p1.println("}");
            p1.flush();
            p1.close();
            
            
            
        } 
        catch ( Exception e) { 
        }
    }

    
    public String getName() {
        return "Java to C++ Translator";
    }
    
    public String getCopy() {
        return "(C) 2013 Group Group";
    }
    
    public void init() {
        super.init();
        
        // Declare command line arguments.
        runtime.
        bool("printJavaAST", "printJavaAST", false, "Print Java AST.").
        bool("printJavaCode", "printJavaCode", false, "Print Java code.").
        bool("countMethods", "countMethods", false, "Count all Java methods.").
        bool("translate", "translate", false, "Translate from Java to C++.").
        bool("findDependencies", "findDependencies", false, "Find all Dependencies of given Java file.");
    }
    public void prepare() {
        super.prepare();
        
        // Perform consistency checks on command line arguments.
    }
    
    public File locate(String name) throws IOException {
        File file = super.locate(name);
        if (Integer.MAX_VALUE < file.length()) {
            throw new IllegalArgumentException(file + ": file too large");
        }
        return file;
    }
    
    public Node parse(Reader in, File file) throws IOException, ParseException {
        JavaFiveParser parser =
        new JavaFiveParser(in, file.toString(), (int)file.length());
        Result result = parser.pCompilationUnit(0);
        return (Node)parser.value(result);
    }
    
    public Node Cparse(Reader in, File file) throws IOException, ParseException {
        CParser parser =
        new CParser(in, file.toString(), (int)file.length());
        Result result = parser.pTranslationUnit(0);
        return (Node)parser.value(result);
    }
    
    public void process(Node node) {
        if (runtime.test("printJavaAST")) {
            runtime.console().format(node).pln().flush();
        }
        
        if (runtime.test("printJavaCode")) {
            new CPrinter(runtime.console()).dispatch(node);
            runtime.console().flush();
        }
        
    }
}



