package xtc.oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Printer;
import xtc.tree.Visitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Iterator;

//initialization of inheritance tree w/ java.lang
//tree processing
//method processing
//data layout processing
//constructor processing
//getter/helper methods


public class InheritanceHandler extends Visitor {
		
	GNode inheritanceTree; //will hold the whole thang
	GNode currentHeaderNode; //global GNode holding the current version of the header (data layout + vtable)
    GNode classStaticVars; //this class's static variables
    String className; //global reference to the name of the class we're currently dispatching through
	
	
	
	
	///////////************** CONSTRUCTOR
	
	public InheritanceHandler(Node[] astArray)
	{
		initializeGivenClasses(); //this will hardcode the Object, Class, String, and Array classes to be beginning of the tree (correctly)
							      // every other class will have to extend one of these data layouts/classes
		for(int i = 0; i < astArray.length; i++)  //for each dependency AST, dispatch
		{
			if(astArray[i] != null)  //don't care about null ASTs
			{
				this.dispatch(astArray[i]); //will visit everything in the tree, now to override the visit____ methods fuck.
			}
		}
		
		//dispatchCallExpressionVisitor(astArray); //in the other code this is where they handled how we do method calls, like which one gets called when we dispatch etc
        //printClassTree();
	}
	
	
	
	///////////************** TREE/CLASS PROCESSING
	
	public void visit(Node n) 
	{
		for( Object o : n ) {
			if (o instanceof Node) dispatch((Node)o);
		}
	}

	
	
	
    public void visitClassDeclaration(GNode n) {
		className = n.get(1).toString();  //for the record, get(#) goes down the tree according to the # index and then returns that node
		GNode newClassNode = GNode.create("Class"); //create Class node
		newClassNode.setProperty("name", className); //set the name
        GNode parent = null; 
		thisClassStaticVars = GNode.create("StaticVarsList"); //this will get set in visitFieldDeclaration when we visit(n) later
		
		if(n.get(3) != null) { // if this class extends another class
			//String extendedClass = (String)(n.getNode(3).getNode(0).getNode(0).get(0)); // String name of Parent Class
            String parentClass = n.getNode(3).getNode(0).getNode(0).getString(0);  //check to make sure this does the same thing as the previous line
			parent = getClass(parentClass); //define the parent node
		}
		else { 
			parent = getClass("Object"); // Doesn't extend define parent as Object
		}
		//if(DEBUG) System.out.println( "Visiting new class: " + className + " extends " + parent.getProperty("name") );
		//Building the vtables and data layout via Parents layout
		currentHeaderNode = inheritParentHeader((GNode)parent.getNode(0)); //inherit parent header
		visit(n); //visit every child of this class this will fill in the rest of the header
		newClassNode.add(currentHeaderNode); //add the header to the newClassNode
		newClassNode.setProperty( "parentClassNode", parent); //set property 'parentClassNode' to the parent GNode from before, can get list of each nodes properties at any time, it's in the Node class's methods
		parent.addNode(newClassNode); //add child as the child of the parent node 
    }
	
    
    
    
    
    
	///////////************** INITIALIZERS
    
  
	//hardcodes the given base classes
	//Object, Class, String, and Array
    //need to provide the data layout for Object, and need to finish Templates for ARRAY
	public void initializeGivenClasses(){
		GNode object = GNode.create("Class");
		object.setProperty("name", "Object");
		GNode classHeaderDeclaration = GNode.create("ClassHeaderDeclaration");
		classHeaderDeclaration.add( objectClassVirtualTable() ); //need to write these methods
		classHeaderDeclaration.add( objectClassDataLayout() ); //need to write these methods
		object.add(classHeaderDeclaration);

		GNode string = GNode.create("Class");
		string.setProperty("name", "String");
		string.setProperty("parentClassNode", objectNode);
		className = "String";
		string.add( inheritHeader( classHeaderDeclaration ) ); //need to write this method as well

		GNode classNode = GNode.create("Class");
		class.setProperty("name", "Class");
		class.setProperty("parentClassNode", objectNode);
		className = "Class";
		class.add( inheritHeader( classHeaderDeclaration ) );

		GNode array = GNode.create("Class");
		array.setProperty("name", "Array");
		array.setProperty("parentClassNode", objectNode);
		className = "Array";
		array.add( inheritHeader( classHeaderDeclaration ) );
		
		//actually add the nodes to our class tree after all the info
		//has been filled in correctly
		classTree = object;
		classTree.add(string);
		classTree.add(class); 
		classTree.add(array);
		//do i need to do integer?
	}
	
	///////////************** FIELD HANDLING
	
	public void visitFieldDeclaration(GNode n){
		//create data layout
		//adds fields to it
	}
	
	
	
	///////////************** (VIRTUAL) METHOD HANDLING
	
	public void visitMethodDeclaration(GNode n){
		//everytime we visit a declaration either:
        //(case of overriding)overwrite the methods virtualMethodPointer to the new one it should now point to
        //(all other times) creat the virtualMethodPointer and append it to the VirtualTable
        //make sure to thoroughly create the methods signature
	}
	
	
	
	///////////************** CONSTRUCTOR HANDLING
	
	public void visitConstructorDeclaration(GNode n){
		//literally no idea what this needs to do
	}
	
	
	
	
	
	
	
	///////////************** GETTERS
	
	/*given the subclass as a string, give me the super class's GNode*/
	public GNode getSuperclass(String sc) 
	{
		GNode child = getClass(sc);
		
		//if(DEBUG) System.out.println("got class node for " + sc);
		// can't be passed primitive type
		if (child.getProperty("name").equals("Object")) return null;
			
		return (GNode)child.getProperty("parentClassNode");
	}
	
	/*given the subclasses node, get it's name, and then make call to other getSuperclass method, return the Superclass's GNode*/
	public GNode getSuperclass(GNode n) 
	{
		return getSuperclass( n.getStringProperty("name") );
	}

	/*given the subclass as a string, return if it has a superclass*/
	public boolean hasSuperclass(String sc) 
	{
		if (getSuperclass(sc) != null) 
		{
			return true;
		}
		else
			return false;
	}
	
	//given a node give me the class name
	public String getClassName(GNode n) 
	{
		return n.getStringProperty("name");
    }
	
	
	///////////************** HELPER METHODS
    
    GNode inheritParentsHeader(GNode parentHeader){
        //write (the global variable) className's header as a copy of its parentHeader's header
    }
	
    GNode createTypeNode(String type){
        //creates/returns a "Type" node, followed by null node, followed by either "QualifiedIdentifier" or "PrimitiveType" node, followed by the string (not a node)
        //remember that we always make ASTs like this backwards
        //
        //we add the string on to the typeSpecifier
        //then the typeSpecifier on to the Type node
        //then return the Type node
        //just saves a dickload of code cause we do this a fewtimes when creating vtable stuff
    }
	
	
	
	
	///////////************** DEBUG
	
	
    /*someday this will print out the whole class tree once it's been made*/
	public void printClassTree() 
	{

		System.out.println("Class Tree:");
		
		new Visitor () 
		{
			public void visit(GNode n) 
			{
				for( Object o : n) 
				{
					if (o instanceof Node) dispatch((GNode)o);
				}
			}

			public void visitClass(GNode n) 
			{
				System.out.print("Class " + n.getStringProperty("name"));
				String superClass = getSuperclassName(n.getStringProperty("name"));
				System.out.println( " extends " + superClass/*(superClass==null?superClass:"")*/);
				visit(n);
			}
		}.dispatch(classTree);
		System.out.println();
    }
}