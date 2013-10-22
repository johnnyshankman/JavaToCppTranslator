package xtc.oop;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import xtc.lang.JavaFiveParser;
import xtc.lang.JavaPrinter;

import xtc.parser.ParseException;
import xtc.parser.Result;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.util.Tool;

import xtc.oop.GetDependencies;
import xtc.oop.InheritanceHandler;

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

  public void process(Node node) {
    if (runtime.test("printJavaAST")) {
      runtime.console().format(node).pln().flush();
    }

    if (runtime.test("printJavaCode")) {
      new JavaPrinter(runtime.console()).dispatch(node);
      runtime.console().flush();
    }

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
        runtime.console().pln("Begin depedency finding and resolving.\n").flush();
        
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
          
         
          
        runtime.console().pln("Begin inheritance and data layout handling.\n").flush();
        
        InheritanceHandler layout = new InheritanceHandler(astArray);
        //done
        
        
        
        runtime.console().pln("Begin scoping/symbol table stuff.\n").flush();
          
        runtime.console().pln("Begin creating a C++ AST for each Java AST.\n").flush();
          
        runtime.console().pln("Begin creating C++ files by using CppPrinter on each C++ AST and siphoning the output to output.cpp").flush();
          
        runtime.console().pln("... translation is now finished.\n").flush();
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