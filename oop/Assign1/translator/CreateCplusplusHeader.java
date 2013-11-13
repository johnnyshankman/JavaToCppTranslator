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

public class CreateCplusplusHeader extends xtc.util.Tool { 

    GNode createCplusplusHeader; 
    
    GNode ORIGINALJAVAAST; 
        
    public CreateCplusplusHeader(GNode n, GNode f ) { 
		
        createCplusplusHeader  = n; 
        
        ORIGINALJAVAAST = (GNode)f; 
        
        final PrintWriter p1; 
        
        File headerFile; 
        
        File newDirectory;
        
        try { 
            newDirectory = new File("cplusplusfiles"); 
            newDirectory.mkdir(); 
            headerFile = new File("cplusplusfiles", "Header.h"); 
            headerFile.createNewFile(); 
            p1 = new PrintWriter(headerFile); 
            p1.println("#pragma once");
            p1.println();
            p1.println("#include <stdint.h>");
            p1.println("#include <string>"); 
            p1.println(); 
            p1.println("namespace java {"); 
            p1.println("namespace lang {");
            p1.println("// Foward Declarations "); 
            p1.println();
            GNode vMethods = (GNode) createCplusplusHeader.getNode(0);
            
            
            // We need a mapping of methodName to accessibility
           final HashMap<String,String> mNAccess = new HashMap<String,String>(); 
            
            
            
            // We need a mapping to methodName to whetherItsStatic
           
            
            new Visitor() {
                
                
                public void visitCompiliationUnit(GNode n) { 
                    
                    visit(n); 
                }
                
                public void visitClassDeclaration(GNode n) { 
                    
                    
                    visit(n); 
                    
                }
                
                
                public void visitMethodDeclaration(GNode n ) { 
                    
                    
                        // We need to figure out whether this method is static it should be excluded from VTable 
                    
                    if ( n.getNode(0).size() > 1 && !(n.getString(3).equals("main$string")) ) { 
                        
                        mNAccess.put(n.getString(3), n.getNode(0).getNode(1).getString(0)); 
                    }
                    visit(n); 
                }
                
                public void visit(Node n) {
                    for (Object o : n) if (o instanceof Node) dispatch((Node) o);
                }
                
            }.dispatch(ORIGINALJAVAAST);
            
           // p1.println(mNAccess);
           // p1.println(mNAccess.get("goel")); 
            
            // Find out when the virtual method declarations ends 
            final ArrayList<Integer> getCounter = new ArrayList<Integer>(); 
            new Visitor() {
                int counter2 = 0; 
                public void visitVirtualMethodDeclaration(GNode n ) { 
                    counter2++; 
                    getCounter.add(counter2); 
                    
                }
                
                public void visit(Node n) {
                    for (Object o : n) if (o instanceof Node) dispatch((Node) o);
                }
                
            }.dispatch(vMethods);
            
            int startHere = getCounter.get(getCounter.size()-1) + 1; 
           // p1.println(startHere);
            String className = createCplusplusHeader.getNode(0).getNode(startHere).getNode(4).getNode(0).getString(0); 
            
            String plainClassName = className.substring(2, className.length()); 
            // p1.println(plainClassName); 
            //p1.flush();
            //p1.close();
            // p1.println(getCounter); 
            p1.println("struct __" + plainClassName + ";");
            p1.println();
            
            p1.println("struct __" + plainClassName + "_VT;");
            p1.println();

            p1.println("typedef " + "__" + plainClassName + "* " + plainClassName + ";");
            p1.println();

            p1.println("struct __" + plainClassName + " { " );
            p1.println();
            p1.println("    // The data layout for java.lang.plainClassName");  
            p1.println("      " + "__" + plainClassName + "_VT* __vptr;"); 
            p1.println();
            p1.println("     "   +  "// The Constructor"); 
            p1.println();

            p1.println("      " +  "__" + plainClassName + "(); "); 
            p1.println();
            //Find Instance Methods of the class Basically go through vtMethodPointersList and 
            //  go through its children and check if the qualified identifier is the same as the clas name 
            
            // Store the names in a Arraylist type
            final  List<String> names = new ArrayList<String>(); 
            
            List<String> types2 = new ArrayList<String>();
            
            final List<Integer> indexx = new ArrayList<Integer>();
            
            final HashMap<Integer, String> checkForOtherSuperClass = new HashMap<Integer, String>(); 
            
            
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
                    
                    if( !(n.getNode(1).getString(1).equals("__Object")) && !(n.getString(0).equals("main$String"))) { 
                        
                        //  p1.println(n.getString(0)); 
                        indexx.add(counter);
                        names.add(n.getString(0)); 
                        
                        
                        // There needs to be a check for the other than __Object && __SuperClass 
                        
                    checkForOtherSuperClass.put(counter, n.getNode(1).getString(1)); 
                        
                    }   
                    
                    else 
                        checkForOtherSuperClass.put(counter, n.getNode(1).getString(1)); 
                    
                }
                
                public void visit(Node n) {
                    for (Object o : n) if (o instanceof Node) dispatch((Node) o);
                }
                
            }.dispatch(vMethods);
            
            //p1.println(checkForOtherSuperClass);
            //p1.println(names);
            // System.out.println("ARRAY CONTENTS" + names); 
            // Now lets get the type of the method and store it in Types arraylist
            
            // Visit the Method Declarations of the Java AST and store the types in order into an array. Then 
            // store the corresponding names of the methods in another array then do matching to determine the 
            // the types of the method 
           // p1.println(checkForOtherSuperClass);
            for ( int i = 0; i < indexx.size(); i++ ) { 
                
                
                if(vMethods.getNode(indexx.get(i)).getNode(0).getName().equals("Type"))
                    types2.add(vMethods.getNode(indexx.get(i)).getNode(0).getNode(0).getString(0));
                else 
                    types2.add("void"); 
                
                
            }
            
            // params are appended to the methods name 
            List<String> specialnames = new ArrayList<String>(); 
            
            // A single method name which is a string could essential map to however many Strings 
            Map<String, String> parameters = new  HashMap<String,String>(); 
            
            // Remove anything after $ in the method these are the parameters that are appended to it 
            for ( int i = 0; i < names.size(); i++ ) { 
                
                Pattern p = Pattern.compile("\\$");    
                
                Matcher m = p.matcher(names.get(i)); 
                
                if(m.find()) { 
                   // p1.println("FOUND"); 
                   // p1.println(m.start());
                    specialnames.add(names.get(i).substring(0,m.start()));   
                    parameters.put(specialnames.get(i), names.get(i).substring(m.start(),names.get(i).length())); 
                    
                }
                
                else {
                    
                    specialnames.add(names.get(i)); 
                    // The hashmap needs to be consistent 
                    parameters.put(names.get(i), "ZeroParams"); 
                }
                
            } 
           // p1.println(parameters);
            //p1.println(types2);
            
            // Now print the instance methods using the types and names 
            p1.println("    // The instance methods of java.lang.plainClassName"); 
            
            for ( int i = 0; i < types2.size(); i++) { 
                
                p1.println("    "  + "static " + types2.get(i) + " " + specialnames.get(i) + "( " + plainClassName + ")" + ";" ); 
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
            
           // Basically iterate through map and add any methods that have a type not equal to the 
            
            // You need to add the inherited types 
            p1.println("    int32_t (*hashCode)(" + plainClassName + ");");
            p1.println("    bool (*equals)(" + plainClassName + "," + "Object);"); 
            p1.println("    Class (*getClass)(" + plainClassName + ");"); 
            p1.println("    String (*toString) (" + plainClassName + ");"); 
            
            for ( int i = 0; i < names.size(); i++ ) { 
                
                if ( !(arr1.contains(names.get(i)))) {
                    
                    boolean turnOn = false;
                    boolean turnDoubleOn = false; 
                    if ( mNAccess.containsKey(names.get(i))) { 
                        turnOn = true; 
                    }
                        
                        if ( turnOn) { 
                            
                            if ( (mNAccess.get(names.get(i)).equals("static")))  
                                turnDoubleOn=true;
                            
                        }
                        
                            if ( turnDoubleOn == false )    
                                p1.println("    " + types2.get(i) + " (*" + specialnames.get(i) + ") (" + plainClassName  + ");");
                                                            
                            
                    else { 
                        turnOn = false; 
                        turnDoubleOn = false; 
                    }
                            
                            
                    /*
                    
                    if ( !(parameters.get(specialnames.get(i)).equals("ZeroParams"))) {
                        
                        Arraylist<Integer> g1 = new ArrayList<Integer>(); 
                        
                        Pattern p = Pattern.compile("\\$");    
                        
                        Matcher m = p.matcher(names.get(i)); 
                        
                        while(m.find()) { 
                           
                            g1.add(m.start()); 
                            
                        }
                        int getCash=0; 
                        
                        for ( int b = 0; b < g1.size(); b++) {
                            
                                p1.print(", " + names.get(i).substring(g1.get(b), g1.get(b+1))
                        }
                        
                    }
                       
                    **/
                }
            } 
            p1.println(); 
            p1.println(); 
            
            // Now the constructor initilization inlined in the header 
            p1.println("    __" + plainClassName + "_VT()");
            p1.println("    : __isa(__" + plainClassName + "::__class()),");
            
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
            if ( getImplementation.get(0).equals("Object")) 
                p1.println("      hashCode((int32_t(*)(" + plainClassName + "))" + "&__" + getImplementation.get(0) + "::hashCode),"); 
            else 
                p1.println("      hashCode(&__" + plainClassName + "::hashCode),"); 
            
            
            if (getImplementation.get(1).equals("Object")) 
                p1.println("      equals((bool(*)(" + plainClassName + ",Object)) &__" + getImplementation.get(1) + "::equals), "); 
            
            else 
                p1.println("      equals(&__" + plainClassName + "::equals"); 
            
            if (getImplementation.get(2).equals("Object"))
                p1.println("      getClass((Class(*)(" + plainClassName + ")) &__" + getImplementation.get(2) + "::getClass), "); 
            
            else 
                p1.println("      getClass(&__" + plainClassName + ")");
            
            // Remember to Take care of the comma issue 
            if (getImplementation.get(3).equals("Object"))
                p1.println("      toString((String(*)(" + plainClassName + ")) &__" + getImplementation.get(3) + "::toString), ");
            
            else 
                p1.println("      toString(&__" + getImplementation.get(3) + "::toString),"); 
            
            /*
            for ( Map.Entry entry: checkForSuperClass.entrySet()) { 
                
                if(!(entry.getValue().equals("__Object")) && !(entry.getValue().equals(className) )) { 
                    
                    p1.println("     " + entry.getKey() + "(("+types
            }
            }
            **/
            // ADD Remaining Methods to implementation 
          
            for ( int i = 0; i < names.size(); i++) { 
                
                boolean turnOn = false;
                boolean turnDoubleOn = false; 
                if ( mNAccess.containsKey(names.get(i))) { 
                    turnOn = true; 
                }
                
                if ( turnOn) { 
                    
                    if ( (mNAccess.get(names.get(i)).equals("static")))  
                        turnDoubleOn=true;
                    
                }
                
                if ( turnDoubleOn == false )    {
                    
                    
                    
                    if(!(arr1.contains(specialnames.get(i))) && checkForOtherSuperClass.get(i+6).equals(className) && !(mNAccess.get(names.get(i)).equals("static"))) { 
                        // Remember to Fix this later 
                        p1.println("      " + specialnames.get(i) + "(&__" + plainClassName + "::" + specialnames.get(i) + "){"); 
                    }
                    else 
                        p1.println("      " + specialnames.get(i) + "((" + types2.get(i) + "(*)" + "(" + plainClassName + "))" + "&" + checkForOtherSuperClass.get(i+6) + "::"  + specialnames.get(i) + "),"); 
                }
                    
                    else { 
                    turnOn = false; 
                    turnDoubleOn = false; 
                }

                
            }
             
            p1.println("    }");
            p1.println("};"); 
            p1.println();
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
        

	
	
