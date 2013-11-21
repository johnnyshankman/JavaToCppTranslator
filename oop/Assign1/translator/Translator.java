package oop;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import xtc.lang.JavaFiveParser;
import xtc.lang.CParser;
import xtc.lang.JavaPrinter;
import xtc.lang.CPrinter;
import xtc.lang.JavaAstSimplifier;

import xtc.parser.ParseException;
import xtc.parser.Result;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import java.util.*; 
import xtc.util.Tool;
import xtc.util.SymbolTable;

import oop.GetDependencies;
import oop.InheritanceHandler;
import oop.SymbolTableHandler;
import oop.ASTConverter;
import oop.CPPrinter;
import java.io.*; 

/**
 * A translator from (a subset of) Java to (a subset of) C++.
 */
public class Translator extends xtc.util.Tool {

  /** Create a new translator. */
  public Translator() {
    // Nothing to do.
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

      
      // Create a hash map
     final  HashMap hm = new HashMap();
      // Put elements to the map  
      
      
    if (runtime.test("countMethods")) {
      new Visitor() {
        private int count = 0;

        public void visitCompilationUnit(GNode n) {
          visit(n);
          runtime.console().p("Number of methods: ").p(count).pln().flush();
        }

        public void visitMethodDeclaration(GNode n) {
          runtime.console().p("Name of node: ").p(n.getName()).pln();
          runtime.console().p("Name of method: ").p(n.getString(3)).pln();
          visit(n);
          count++;
        }

        public void visit(Node n) {
          for (Object o : n) if (o instanceof Node) dispatch((Node) o);
        }

      }.dispatch(node);
    }
      
 
    if (runtime.test("translate")){
        runtime.console().pln("Begin translation...\n").flush();
        runtime.console().pln("Finding and resolving dependencies...\n").flush();
        
        GNode[] astArray = new GNode[50]; //arbitrary size
        astArray[0] = (GNode) node; //0th spot is the original java file's AST
        GetDependencies dependencyHandler = new GetDependencies();
        try{
		astArray = dependencyHandler.getFile(); //store the array of java ASTs
        }
        catch (IOException e){
        	
        }
        catch (ParseException e){
        	
        }
		astArray[0] = (GNode)node; //just to ensure we have the original java ast in slot 0 still
          
		  
		
		
		
		
		
		
        runtime.console().pln("Building symbol table...\n").flush();
        
        
        new JavaAstSimplifier().dispatch((GNode)node);
        final SymbolTable table = new SymbolTable(); //empty symbol table
        new SymbolTableHandler(runtime, table).dispatch((GNode)node); //create the table
        table.current().dump( runtime.console() ); //print dump the table into console
        runtime.console().pln().pln().pln().flush();
          
        
        
        
        
        
        runtime.console().pln("Building vtables and data-layouts for C++ ASTs...\n").flush();
        
        
        InheritanceHandler layout = new InheritanceHandler(astArray, runtime.console());
        //done
        
        runtime.console().format(layout.getClassTree()).pln().pln().pln().pln().flush();
        
        
        
        
     
        

          
        runtime.console().pln("Creating header file...\n").flush();
        
        GNode createCplusplusHeader = layout.getClassTree();
        CreateCplusplusHeader getHeader = new CreateCplusplusHeader(createCplusplusHeader); 
        
        runtime.console().pln("Header file can now be found in output directory.\n").pln().pln().pln().flush();
        
        
        
        
        
        runtime.console().pln("Translating body...\n").pln().pln().pln().flush();
        
        GNode [] ccAstArray = new GNode [50];
        
        runtime.console().pln("Translating body...\n").pln().pln().pln().flush();  
        
        for(int i=0 ; i<astArray.length ; i++)
        {
        	if(astArray[i]!=null)
        	{
        		ASTConverter ccConverter = new ASTConverter(astArray[i], layout, table, runtime.console() );
        		ccAstArray[i] = ccConverter.createCCTree();
        		//ccAstArray[i] = ccConverter.getCCTree();
        		runtime.console().format(ccAstArray[i]).pln().flush();
        	}
        }
        
        
        new JavaPrinter(runtime.console()).dispatch(node);
       
        runtime.console().flush();
        
        runtime.console().pln("Siphoning output to .cc files...").pln().pln().pln().flush();
          
        
        
        
        
        
        runtime.console().pln("... the translation is now finished! Please check src/oop/output for your translated files. \n").flush();

       
      
      }


  }

  /**
   * Run the translator with the specified command line arguments.
   *
   * @param args The command line arguments.
   */
  public static void main (String [] args)
  {
	  Translator translator = new Translator();
	  translator.run(args);
  }
}