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

/**
 * A translator from (a subset of) Java to (a subset of) C++.
 */
public class Translator extends Tool {

  /** Create a new translator. */
  public Translator() {
    // Nothing to do.
  }

  public String getName() {
    return "Java to C++ Translator";
  }

  public String getCopy() {
    return "(C) 2013 <Group Name>";
  }

  public void init() {
    super.init();

    // Declare command line arguments.
    runtime.
      bool("printJavaAST", "printJavaAST", false, "Print Java AST.").
      bool("printJavaCode", "printJavaCode", false, "Print Java code.").
      bool("countMethods", "countMethods", false, "Count all Java methods.");
      bool("translate", "translate", false, "Translate from Java to C++.");
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
      
      //curerntly just a blank space and a debug print line for each stage of the translation.
    if (runtime.test("translate")){
        runtime.console.pln("Begin translation...\n").flush();
          
        runtime.console.pln("Begin depedency finding and resolving.\n").flush();
          //will use an array of ASTs implementation to store dependencies (/classes too?)
          //so something like GNode dependencies[] = new GNode[x]
          //dependcies[0] = (GNode)node  <---- i think 'node' in process(Node node) is the compilation unit aka root node
          
        runtime.console.pln("Begin scoping/symbol table stuff.\n").flush();
          
        runtime.console.pln("Begin inheritance and data layout handling.\n").flush();
          //final InheritanceHandler dataLayout = new InheritanceHandler(dependenciesArray)
          
        runtime.console.pln("Begin creating a C++ AST for each Java AST.\n").flush();
          
        runtime.console.pln("Begin creating C++ files by using CppPrinter on each C++ AST and siphoning the output to output.cpp").flush();
          
        runtime.console.pln("... translation is now finished.\n").flush();
      }
      
    if (runtime.test("findDependencies")){
        runtime.console.pln("Finding dependencies...\n").flush();
          //find and resolve all dependencies
          //this may be unnecessary depending on implementation
          //if you go with AST implementation this can be super unneccessary and handled by another class.
      }
    }
  }

  /**
   * Run the translator with the specified command line arguments.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    new Translator().run(args);
  }

}