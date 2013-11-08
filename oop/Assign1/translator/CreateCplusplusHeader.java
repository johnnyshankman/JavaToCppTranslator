package oop;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import xtc.lang.JavaFiveParser;
import xtc.lang.CParser;
import xtc.lang.JavaPrinter;
import xtc.lang.CPrinter;

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

public class CreateCplusplusHeader extends xtc.util.Tool { 


        GNode createCplusplusHeader; 
	public CreateCplusplusHeader(GNode n) { 
		
        createCplusplusHeader  = n; 
        
        
        final PrintWriter p1; 
        
        File headerFile; 
        
        File newDirectory;
        
        try { 
            
            
            
            newDirectory = new File("cplusplusfiles"); 
            newDirectory.mkdir(); 
            
            headerFile = new File("cplusplusfiles", "Header.h"); 
            
            headerFile.createNewFile(); 
            
            p1 = new PrintWriter(headerFile); 
            
            p1.println("// Foward Declarations "); 
            p1.println();

            
            p1.println("struct __" + createCplusplusHeader.getNode(0).getNode(6).getNode(2).getNode(0).getString(1) + ";");
            p1.println();
            
            p1.println("struct __" + createCplusplusHeader.getNode(0).getNode(6).getNode(2).getNode(0).getString(1) + "_VT;");
            p1.println();

            p1.println("typedef " + "__" + createCplusplusHeader.getNode(0).getNode(6).getNode(2).getNode(0).getString(1) + "* " + createCplusplusHeader.getNode(0).getNode(6).getNode(2).getNode(0).getString(1) + ";");
            p1.println();

            p1.println("struct __" + createCplusplusHeader.getNode(0).getNode(6).getNode(2).getNode(0).getString(1) + " { " );
            p1.println();
            p1.println("    // The data layout for java.lang.plainClassName");  
            p1.println("      " + "__" + createCplusplusHeader.getNode(0).getNode(6).getNode(2).getNode(0).getString(1) + "_VT* __vptr;"); 
            p1.println();

            p1.println("    " + "String name;"); 
            p1.println();

            p1.println("    " + "Class parent;");
            p1.println();

            p1.println("     "   +  "// The Constructor"); 
            p1.println();

            p1.println("      " +  "__" + createCplusplusHeader.getNode(0).getNode(6).getNode(2).getNode(0).getString(1) + "(String name, Class parent); "); 
            p1.println();

            
            
            
            /* Find Instance Methods of the class Basically go through vtMethodPointersList and 
             go through its children and check if the qualified identifier is the same as the clas name **/
            
            final  List<String> names = new ArrayList<String>(); 
            
            List<String> types2 = new ArrayList<String>();
            
            final List<Integer> indexx = new ArrayList<Integer>();
            
            final String className = "__" + createCplusplusHeader.getNode(0).getNode(6).getNode(2).getNode(0).getString(1); 
            
            String plainClassName = createCplusplusHeader.getNode(0).getNode(6).getNode(2).getNode(0).getString(1);
            
            GNode vMethods = (GNode) createCplusplusHeader.getNode(0);
            
            // Lets find out which methods are unique 
            new Visitor() {
                public int counter = 0; 
                public void visitVTConstructorDeclaration(GNode n) {
                    visit(n);
                }
                
                public void visitvtMethodPointersList(GNode n) {
                    
                    visit(n);
                    
                }
                
                public void visitvtMethodPointer(GNode n) { 
                    
                    counter++;
                    
                    if( n.getNode(1).getString(1).equals(className) && !(n.getString(0).equals("main$String"))) { 
                        
                        //  p1.println(n.getString(0)); 
                        indexx.add(counter);
                        names.add(n.getString(0)); 
                    }   
                }
                
                public void visit(Node n) {
                    for (Object o : n) if (o instanceof Node) dispatch((Node) o);
                }
                
            }.dispatch(vMethods);
            
            // System.out.println("ARRAY CONTENTS" + names); 
            
            
            
            // Now lets get the type of the method and store it in Types arraylist
            
            // Visit the Method Declarations of the Java AST and store the types in order into an array. Then 
            // store the corresponding names of the methods in another array then do matching to determine the 
            // the types of the method 
            
            for ( int i = 0; i < indexx.size(); i++ ) { 
                
                
                types2.add(vMethods.getNode(indexx.get(i)).getNode(0).getNode(0).getString(0));
                
                
                
                
            }
            
            
            //p1.println(types2);
            
            // Now print the instance methods using the types and names 
            p1.println("    // The instance methods of java.lang.plainClassName"); 
            
            
            for ( int i = 0; i < types2.size(); i++) { 
                
                p1.println("    "  + "static " + types2.get(i) + " " + names.get(i) + "( " + plainClassName + ")" + ";" ); 
                
                p1.println();
                
                
            }
            p1.println("    // The Function returning the class Object representing java.lang.plainClassName " ); 
            p1.println("    static Class __class(); "); 
            p1.println();

            
            p1.println("    static __" + plainClassName + "_" + "VT " + "__vtable;"); 
            p1.println();

            
            p1.println(" };"); 
            p1.println();
            
            
            // Now print the Constructor taking into account which ones are implemented by the given class
            
            p1.println("struct __" + plainClassName + "_" + "VT" + "{");
            
            p1.println("    Class __isa;");
            
            
            // Introduce some logic to differentiate between new methods and predefined 
            List<String> arr1 = new ArrayList<String>(); 
            arr1.add("hashcode"); 
            arr1.add("equals"); 
            arr1.add("getClass"); 
            arr1.add("toString");
            arr1.add("getName");
            arr1.add("getSuperClass"); 
            arr1.add("isInstance"); 
            
            p1.println("    int32_t (*hashCode)(" + plainClassName + ");");
            p1.println("    bool (*equals)(" + plainClassName + "," + "Object);"); 
            p1.println("    Class (*getClass)(" + plainClassName + ");"); 
            p1.println("    String (*toString) (" + plainClassName + ");"); 
            p1.println("    String (*getName) (" + plainClassName + ");"); 
            p1.println("    Class (*getSuperClass) (" + plainClassName + ")"); 
            p1.println("    bool (*isInstance) (" + plainClassName + ", Object);"); 
           
            for ( int i = 0; i < names.size(); i++ ) { 
                
                if ( !(arr1.contains(names.get(i)))) {
                    
                    p1.println("    " + types2.get(i) + " (*" + names.get(i) + ") (" + plainClassName + ");"); 
                    
                    
                }
                
                
            } 
            p1.println(); 
            p1.println(); 
            
            // Now the constructor initilization inlined in the header 
            p1.println("    __" + plainClassName + "_VT()");
            p1.println("    : __isa(__Class:__class());");
            
            List<String> getImplementation = new ArrayList<String>();
            
            for ( int i = 0; i < arr1.size(); i++) { 
                
                if( names.contains(arr1.get(i))) { 
                    
                    getImplementation.add(plainClassName); 
                }   
                
                else {
                    
                    getImplementation.add("Object"); 
                }
                
                
            }
            
            // physically put it in 
            p1.println("      hashCode((int32_t(*)(" + plainClassName + "))" + "&__" + getImplementation.get(0) + "::hashcode),"); 
            p1.println("      equals((bool(*)(" + plainClassName + ",Object)) &__" + getImplementation.get(1) + "::equals), "); 
            p1.println("      getClass((Class(*)" + plainClassName + ")) &__" + getImplementation.get(2) + "::getClass), "); 
            p1.println("      toString(&__" + getImplementation.get(3) + "::toString),"); 
          
            
            // ADD Remaining Methods to implementation 
            for ( int i = 0; i < names.size(); i++) { 
                
                
                if(!(arr1.contains(names.get(i)))) { 
                    
                   
                        // Remember to Fix this later 
                        p1.println("    " + names.get(i) + "(&__" + plainClassName + "::" + names.get(i) + "){"); 
                        
                }
                
                
                
            }
            p1.println("    }");
             
            p1.println("};"); 
            
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
        

	
	
