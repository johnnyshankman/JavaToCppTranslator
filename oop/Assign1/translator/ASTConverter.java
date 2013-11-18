package oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Printer;
import xtc.tree.Visitor;

import oop.InheritanceHandler;

import xtc.type.*;

import xtc.lang.JavaEntities;

import xtc.util.SymbolTable;
import xtc.util.Runtime;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Iterator;

/*
 * This class takes in the main java AST, the entire instance of InheritanceHandler, the global symbol table, and the program's printer
 * in order to iterate over the main java ast and dynamically create the C++ AST for the class.cc file as we iterate.
 * 
 * We use the visitor pattern extensively.
 * 
 * We use the getters of InheritanceHandler to access the vtables and datalayouts for all the classes. This is necessary
 * in order to correctly modify the CallExpression nodes so that the call the correct methods with all the correct
 * arguements etc etc.
 * 
 * TODO: We use the symbol table to ensure that all primaryIdentifiers (variables) within the class body refer to the correct
 * version of said symbol according to the scope we find ourselves inside of. 
 * We check local vs. class level variables and append a ThisExpression block where needed so that the
 * C++ printer will correctly print "this.FIELDNAME" when necessary instead of "FIELDNAME"
 * 
 * We translate all SYSTEM.OUT method calls into C++ equivalent calls (stdout:: and "<<" stuff)
 * 
 * We modify super expression calls to be more specific than in java. We append the explicit class name of the super classso 
 * that we may eventually printout "CLASSNAME::whatever" instead of what java produces, which wrongly translates roughly
 * to "SUPER::whatver" in C++
 * 
 * 
 */

public class ASTConverter extends Visitor {
		
	GNode javaTree; //input java AST
	GNode ccComplete; //the whole shebang
	GNode ccTree; //cc node for the class were currently working on
  	String currentClassName; //holds name of class we're currently working on
	GNode newClassExpression; //for when we visit "new ___" expressions and need to analyze their type (could be an exception, a custom class, an array, or one of the initialized given types)
	GNode currentExpressionStatement; //for holding the current expression statement for translating into CPP printable statements
	InheritanceHandler header; //holds the output of layout.getClassTree, otherwise known as the AST for the entire header.h file
	SymbolTable table; //the global symbol table
	String thisClass; //to globally hold the class name for explicit "this" statements (in visitCallExpression wayyyyy down in this file)
	Printer console; //for printing and what not
	
 
///////////************** CONSTRUCTOR ///////////************** ///////////************** CONSTRUCTOR ///////////************** ///////////************** CONSTRUCTOR ///////////**************
	
	public ASTConverter(GNode javaTree, InheritanceHandler header, SymbolTable table, Printer console) 
	{  
	  this.javaTree = javaTree;
	  this.header = header;
	  this.table = table;
	  this.console = console;
	} 
  
  
	
	
///////////************** THE MEAT ///////////************** ///////////************** THE MEAT ///////////************** ///////////************** THE MEAT ///////////**************
	
	public void createCCTree() 
	{
		ccComplete = GNode.create("Classes");
		new Visitor()
		{
			/*
			 * calls the method which translates our class body
			 */
			public void visitClassDeclaration(GNode n)
			{
				ccTree = n;
				currentClassName = n.get(1).toString();
				translateClassBody(n);
				ccComplete.add(ccTree);
			}
			
			
			/*
			 * Standard visit method, no changes made
			 * @param n GNode Any GNode
			 */
			public void visit(GNode n)
			{
				for( Object o : n) 
				{
				    if (o instanceof Node) dispatch((GNode)o);
				}
			}
			
		}.dispatch(javaTree);
	}
	
	
	/*
	 * Takes the classDeclaration node and calls all other methods that are needed in order to translate it into (what will be) a .cc file
	 * @param n GNode classDeclaration node for the class body we want to translate
	 * 
	 */
	public void translateClassBody(GNode n)
	{
		GNode ccTree = n;
		//HELP: do i need to add the import declaration on to the top of the CC tree (right here) so that the first line of the .cc file is the import header.h statement?
		buildImplementation(ccTree); //this will create all the code to create one gigantic .cc file (with one import statement for the gigantic header.h file we've already made i think)
	}
	
	
	/*
	 * takes in the java classDeclaration node, builds and returns the cc tree (ready for turning into a class.cc file)
	 * @param n GNode a copy of the java classDeclarationNode for turning into a cc node/tree
	 */
	public GNode buildImplementation(GNode n)
	{
		
		new Visitor()
		{	
			/*
			 * Standard overridden visitor for visiting ambiguous nodes of type GNode
			 * @param n Gnode any ambiguous GNode
			 */
			public void visit(GNode n) 
			{
				// Need to override visit to work for GNodes
				for( Object o : n) {
				    if (o instanceof Node) dispatch((GNode)o);
				}
			}
			
			/*
			 * Simply here to globally grab the explicit "this" parameter for use later
			 * @param n GNode a classDeclaration node
			 */
			public void visitClassDeclaration(GNode n) 
			{
				thisClass = n.getString(1);
				visit(n);
			}
			
			/*
			 * Simply here to globally grab the expression statement for use from within callExpression
			 */
			public void visitExpressionStatement(GNode n) 
			{
				// Set the global variable for separate tree traversal: 
				currentExpressionStatement = n;
				visit(n);
			}	
			
			/*
			 * This is a monster... it chooses which methods get called at any given invocation and also translates System.out methods into C++ stdout stuff
			 */
			public void visitCallExpression(GNode n)
			{
				String primaryIdentifier = null; //holds the calling the class's name
				
				//self explanatory
				if(n.getNode(0) == null ||  n.getNode(0).hasName("ThisExpression")) 
				{
					primaryIdentifier = thisClass; //set the thisClass global variable rull quick
				}
				
				//SELECTRION EXPRESSION HANDLER
				else if(n.getNode(0).hasName("SelectionExpression")) {
					
				    primaryIdentifier = n.getNode(0).getNode(0).getString(0);

				    // HANDLER FOR "SYSTEM"
				    if("System".equals(primaryIdentifier)) 
				    {
				    	// Changes any "+" into a "<<"
				    	new Visitor () {
						
				    		public void visitAdditiveExpression(GNode n) 
				    		{
				    			if("+".equals(n.getString(1))) {
				    				n.set(1, "<<");
				    			}
				    			// Of course, this doesn't add niumbers etc. 
				    			// in the print statemetn, but neither does 
				    			// grimm's test cases!
				    			visit(n);
				    		}
									
					    public void visit(GNode n) 
					    {
					    	for( Object o : n) 
					    	{
					    		if (o instanceof Node) dispatch((GNode)o);
					    	}
					    }
									
					}.dispatch(n); // end Visitor
					
						//HANDLER FOR "SYSTEM.OUT.PRINTLN"
						if("println".equals(n.getString(2))) 
						{
							GNode strOut = GNode.create("StreamOutputList");
							strOut.add(0, GNode.create("PrimaryIdentifier").add(0, "std::cout"));
							// Add all arguments to System.out.println
							for(int i = 0; i < n.getNode(3).size(); i++) 
							{
								// HACK : check if primaryidentifer.get(0) == null
								if(GNode.test(n.getNode(3).get(i)) && null == n.getNode(3).getNode(i).get(0)) 
								{
									
								} 
								else 
								{
									// standard behavior
									// removed addindex 1
									strOut.add(n.getNode(3).get(i) ); 
								}				
							}
						    
						    // removed add index 2
						    strOut.add(GNode.create( "PrimaryIdentifier" ).add(0, "std::endl") );
										
						    currentExpressionStatement.set(0, strOut);
						}
						
						
						//HANDLER FOR "SYSTEM.OUT.PRINT"
						else if("print".equals(n.getString(2))) 
						{
						    GNode strOut = GNode.create("StreamOutputList");
						    strOut.add(0, GNode.create( "PrimaryIdentifier" ).add(0, "std::cout") );
						    // Add all arguments to System.out.print
						    for(int i = 0; i < n.getNode(3).size(); i++) 
						    {
						    	strOut.add(1, n.getNode(3).get(i) ); 
						    }
						    currentExpressionStatement.set(0, strOut);
						} 
									
				    }// end if "System"
				} // end if "SelectionExpression"
				//PRIM IDENTIFIER HANDLER
				else if(n.getNode(0).hasName("PrimaryIdentifier"))
				{
				    primaryIdentifier = n.getNode(0).getString(0);	
				}
				//SUPER EXPRESSION HANDLER
				else if(n.getNode(0).hasName("SuperExpression")) 
				{							
					// Replace Java keyword super with actual class
				    GNode primID = GNode.create("PrimaryIdentifier");			
				    GNode vtList = header.getVTable(thisClass); // NOTE: make sure these getters actually exist and work correctly!
				    GNode superNode = header.getSuperclass(currentClassName); // NOTE: make sure these getters actually exist and work correctly!
				    String superName = header.getName(superNode);   // NOTE: make sure these getters actually exist and work correctly!
				    primID.add(0, superName); //add super's name to 0th child of the primitveIdentifier node
				    n.set(0, primID); //overwrite the super expression with the custom made primitiveIdentifer node
				}
				
				//DEFAULT/CATCH-ALL HANDLER
				else 
				{ 
				    visit(n);
				}
				
				if(n.size() >= 4 && n.getNode(3).hasName("Arguments")) 
				{
				    // Time to append arguments
				    GNode vtable = header.getVTable(thisClass); // for use in getVTMethod
				    String methodName = n.getString(2); //for use in getVTMethod
				    GNode vtm = header.getVTMethod(vtable, methodName); //FIXME: WRITE THIS GETTER METHOD + MAKE SURE IT WORKS!!!!

				    if(null != vtm) 
				    {
				    	if(n.getNode(3).size() != vtm.getNode(2).size()) 
				    	{
				    		// Being extra safe
				    		if(n.getNode(3).hasName("Arguments")) 
				    		{
					    	// Finally appending target
					    	GNode arg = header.deepCopy((GNode)n.getNode(3));

					    	// Child should be PrimaryIdentifier node
					    	GNode tmp = header.deepCopy((GNode)n.getNode(0));
					    	arg.add(tmp);
					    	
					    	n.set(3, arg);
				    		}
					    
				    	}

				    }
				    // Keep on visiting arguments?

				}
				
				//THIS IS THE MONSTER AHH, DECIDES WHAT GETS CALLED WHEN
			}
			
			
			public void visitFieldDeclaration(GNode n)
			{
				visit(n);
			}
			
			/*
			 * This visits the throw statement and translates it into something that our printer / java_lang.h supports
			 * It finds the type of the throw statement, analyzes whether its a given type or not
			 * If it is, we throw the simplified version of the exception found in java_lang.h
			 * Otherwise we throw a standard exception of type "Exception"
			 * This method at some point collapses the node structure of the exception because we need so little information from the orignal java AST for the C++ translation
			 * @param n GNode a throwStatement node
			 */
			public void visitThrowStatement(GNode n)
			{
				if(n.getNode(0).hasName("NewClassExpression")) {
				    
				    String throwType = n.getNode(0).getNode(2).getString(0); //find string containing the thrown exception's type
				    
				    if(!isPreDefinedException(throwType)) //if the exception isn't of a predefined type...
				    {
				    	throwType = "Exception"; //just give it the general umbrella name "Exception" w/ no args
				    }
				    
				    /*
				     *  These lines collapse the node structure entirely b/c said structure is superfluous to our needs.
				     *  NOTE: This removes any arguments to the Exception BUT this is all good because we don't support arguments. (java_lang.h never asks for parameters)
				     */
				    
				    GNode tmp = GNode.create("QualifiedIdentifier");
				    tmp.add(throwType); //only child of QualifiedIdentifier is the throw type
				    n.set(0, tmp); //set the 0th child to the new collapsed version of the structure
				}
				visit(n);
			}
			
			public void visitBlock(GNode n){
				visit(n);
			}
			
			public void visitExpression(GNode n){
				visit(n);
			}
			
		}.dispatch(n);
		
		
		
		return n; //return the ccTree!!!! YAY!!
	}
	

	
	
	
	
	
///////////************** HELPER METHODS ///////////************** ///////////************** HELPER METHODS ///////////************** ///////////************** HELPER METHODS ///////////**************
	
	/*
	 * Checks if the name (string) held in "Type" node is Object, String, or Class
	 * @param s String A String holding the type
	 * @return b boolean false when the name is "String", "Object" or "Class"
	 */
	public boolean isCustomName(String s) 
	{
		if("String".equals(s) || "Object".equals(s) || "Class".equals(s)) 
		{
			return false;
		}
		
		return true;
	}
	
	/*
	 * Checks to see if Exception is defined in java_lang.h
	 * @param s String Exception name
	 * @return b boolean true when the name held in String s is the same as one of the predefined exceptions in java_lang.h
	 */
	public boolean isPreDefinedException(String s) 
	{
		if( "Exception".equals(s) ||
				"RuntimeException".equals(s) ||
				"NullPointerException".equals(s) ||
				"NegativeArraySizeException".equals(s) ||
				"ArrayStoreException".equals(s) ||
				"ClassCastException".equals(s) ||
				"IndexOutOfBoundsException".equals(s) ||
				"ArrayIndexOutOfBoundsException".equals(s) ) 
		{
			return true;
		}
		return false;
	}
	
	/*
	 * Checks whether the type of a certain node is custom (!= to Object, Class, String, etc)
	 * @param n GNode a classDeclaration node
	 * @param s String a string contianing the name of the primary identifier
	 *
	 * FIXME: Source of lots of bugs due to variable nature of Declarator node.
	 * 
	 */
	boolean isCustomType(GNode n, String s) {
		
		final String p = s; //p = primary identifier
		
		GNode isCT = (GNode) (new Visitor() 
		{
			public GNode visitDeclarator(GNode n) 
			{
			    if( p.equals(n.getString(0)) ) // We found where the primary identifier is declared to get Type
			    {
			    	String type;
			    	if(n.getNode(2).hasName("Type")) 
			    	{
			    		type = n.getNode(2).getNode(2).getString(0);
			    	}
					else if(n.getNode(2).hasName("NewClassExpression")) 
					{
					    type = n.getNode(2).getNode(2).getString(0);
					}
					else if(n.getNode(2).hasName("NewArrayExpression")) 
					{
					    type = n.getNode(2).getNode(0).getString(0);
					}
					else //we still haven't found the goddamn type!!!
					{
					    // Going down one more level should return Type node if we've gotten this far and haven't found it yet
					    type = n.getNode(2).getNode(0).getNode(0).getString(0);
					}
			    	
			    	//quickly check whether the name of the type we've found is technically custom
			    	//if it is then we just return that node immediately, otherwise we'll exit, hit the visit, and keep searching until we do find one
			    	if(isCustomName(type))
			    	{
				    return n;
			    	}
			    }
			    
			    // Keep Searching
			    for( Object o : n) 
			    {
			    	if (o instanceof Node) 
			    	{
			    		GNode returnValue = (GNode)dispatch((GNode)o);
			    		if( returnValue != null ) //if we've hit the isCustomName conditional and it has passed, then we return that custom type node
			    			return returnValue; //returns the custom type node
			    	}
			    }
			    return null; //otherwise there is no custom type, return value is null
			}


			public GNode visit(GNode n) { // override visit for GNodes
			    
			    // Keep Searching
			    for( Object o : n) {
				if (o instanceof Node) {
				    GNode returnValue = (GNode)dispatch((GNode)o);
				    if( returnValue != null ) return returnValue;
				}
			    }
			    
			    return null;
			    
			}
			
		    }.dispatch(n));

		if(isCT != null) return true;
		return false;
		

	    }
	
	
///////////************** GETTER METHODS ///////////************** ///////////************** GETTER METHODS ///////////************** ///////////************** GETTER METHODS ///////////**************
	
	/*
	 * Returns the AST tree containing all information needed in order to print the .cc file
	 */
	public GNode getCCTree() 
	{ 
		return ccComplete; 
	}


}