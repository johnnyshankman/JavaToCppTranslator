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
		classHeaderDeclaration.add(initializeVirtualTable); //need to write these methods
		classHeaderDeclaration.add(initializeDataLayout); //need to write these methods
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
		classTree.add(classNode); 
		classTree.add(array);
		//do i need to do integer?
	}
	
	//creates Object's virtual table, each method is a child of VTableDeclaration
	GNode initializeVirtualTable(){
		GNode virtualTable = GNode.create("VTableDeclaration");
		//0 add __isa
		//1 add __delete
		//2 add hashcode
		//3 add equals
		//4 add getClass
		//5 add toString
		//6 add constructor
	}
	
	//creates Object's data layout, each data field is a child of DataLayoutDeclaration
	GNode initializeDataLayout(){
		GNode dataLayout = GNode.create("DataLayoutDeclaration");
		//0 add vtable pointer
		//1 add node containing list of data fields
		//2 add node to hold constructors
		//3 add node containing list of methods
		//4 not sure what goes in this child
		
	}
	
	
	
	
	
	
	
	
	
	///////////************** FIELD HANDLING
	
	public void visitFieldDeclaration(GNode n){
		
		/* 
		 * create new a data layout for that field declaration 
		 * override to determine if field is in layout or not, override index...
		 * and all found fields into data layout
		 * just create DATA LAYOUT
        **/
		
		//static vars don't get initialized in the data layout, they are added to classStaticVars
		if ( isStatic( (GNode)n.getNode(0) ){		//if method is static, add to classStaticVars
			classStaticVars.add( (GNode)n.getNode(2) );
			n.getNode(2).getNode(2).set(2, null); //sets location to null 
		}
		
		//set overriddenMethodIndex 
		int overridenFieldIndex = indexOfOverridingField(n, (GNode)currentHeaderNode.getNode(1).getNode(1) );
		
		if ( overridenFieldIndex >= 0 ) {
			//if field needs to be overriden, use .set() to override field at specified index
			currentHeaderNode.getNode(1).getNode(1).set(overridenFieldIndex, n);
		}
		else {
			//if field won't be overriden, add as regular field using .add()
			currentHeaderNode.getNode(1).getNode(1).add(n);
		}
		
		boolean isStatic ( GNode currentNode ) {
			for( Object o : currentNode ) if ( ((GNode)o).get(0).equals("static")) return true;
			return false
		}
		
		int indexOfOverridingField ( GNode OverrideField, GNode CurrentDataLayout) {
			
			//initialize string for comparison
			String fieldName = OverrideField.getNode(2).getNode(0).get(0).toString();
			String comparefieldName;
			
			// Search layout to see if field should be overridden 

			for (int i = 0; i < CurrentDataLayout.size(); i++ ) {
				comparefieldName = CurrentDataLayout.getNode(i).getNode(2).getNode(0).get(0).toString();
				if ( fieldName.equals(comparefieldName) ) {
					return i;
				}
			}			
		
			//compare current field, with override things
			
			// If not overriden, return -1
            return -1;
			
		}
		//NOTES & COMMENTS 
		
		//add the data field to the classes' data layout declaration
		
	}
	
	
	
	
	
	
	
	
	
	
	///////////************** (VIRTUAL) METHOD HANDLING
	
	public void visitMethodDeclaration(GNode n){
		/*everytime we visit a declaration
		 * create new a virtual method signature
		 * (case of overriding)overwrite the old virtualMethodPointer with the new one
		 * (all other times) creat the virtualMethodPointer and append it to the VirtualTable
		 * also make sure to edit the vtables constructor accordingly
        **/
		
		
		//--Method Node Editing
		
		String methodName = n.get(3).getString(0);
		
		if(methodName == "main")
			return; //ignore main methods
		
		betterMethodName = appendParamsToMethodName(methodName); //write this method
		n.set(3, betterMethodName); //overwrite name with better name that includes params in the name itself
		
		//--Adding '__this' to PARAMATERS node as a 'FormalParameter' GNode
		GNode thisFormParam = GNode.create("FormalParameter");
		thisFormParam.add(null); //no modifiers to set
		thisFormParam.add(createTypeNode(className)); //set type of parameter
		thisFormParam.add(null); //nothing to set
		thisFormParam.add("__this"); //set name of parameter
		thisFormParam.add(null); //nothing to set
		n.set(4, GNode.ensureVariable((GNode)n.getNode(4)).add(0,thisFormParam) ); //go to PARAMETERS node, add the thisFormParam node we just made at 0th child
		
		
		//--Creating new VirtualMethodDeclaration node to put in VTable
		GNode vMethodSig = GNode.create("VirtualMethodDeclaration");
		vMethodSig.add(n.get(2)); //return type
		vMethodSig.add(betterMethodName); //method name
		vMethodSig.add((GNode)n.get(4)); //parameters   //in the other version they make a copy and then change the structure so that
														      //params is just a list of paramater TYPES (held at n.get(4).get(1).getNode(indexofParam).get(1)
		
		
		//--Assessing Override Status and Adding VirtualMethodDeclaration to VTable accordingly
		
		int overridenMethodIndex = indexOfOverridingMethod(n, (GNode)currentHeaderNode.getNode(0) ); //give it the MethodDeclaration node and current version of VTable
		
		//overrides inherited method
		if( overridenMethodIndex != (-1) ) 
		{
			currentHeaderNode.getNode(0).set(overridenMethodIndex, vMethodSig); // replace the inherited method sig with new one
			
			//adding method's pointer to the constructor
			
			int constructorSlot = currentHeaderNode.getNode(0).size()-1;
			GNode vtConstructorPtrList = (GNode)currentHeaderNode.getNode(0).getNode(constructorSlot).getNode(4); //4 is where the pointers are held
			GNode newPtr = GNode.create( "vtMethodPointer" ); //create a new vMethodPointer
			newPtr.add(betterName); //append name to pointer
			newPtr.add(createTypeNode( "__"+className)); //className is the caller class
			newPtr.add(GNode.create( "FormalParameters")); //append params
			vtConstructorPtrList.set(overridenMethodIndex, newPtr); //replace the inherited ptr with newPtr
			
		}
		//is a new method without overriding
		else 
		{
			int newMethodIndex = (currentHeaderNode.getNode(0).size()-1); //append new method to VTable just before the constructor (which is last aka index+1)
			currentHeaderNode.getNode(0).add(newMethodIndex, vMethodSig); //append to VTable
			
			//add new pointer to the constructor
			int constructorSlot = currentHeaderNode.getNode(0).size()-1;
			GNode vtConstructorPtrList = (GNode)currentHeaderNode.getNode(0).getNode(constructorSlot).getNode(4); //4 is where the pointers are held
			GNode newPtr = GNode.create( "vtMethodPointer" ); //create a new vMethodPointer
			newPtr.add(betterName); //append name to pointer
			newPtr.add(createTypeNode( "__"+className)); //className is the caller class
			newPtr.add(GNode.create( "FormalParameters")); //append params
			vtConstructorPtrList.add( newPtr ); //add new ptr at the end of ptr list
			
			//add new method to this class's data layout/list of methods
			GNode thisDataLayoutsMethodList = (GNode)currentHeaderNode.getNode(1).getNode(3);
			GNode method = GNode.create( "StaticMethodHeader" );
			method.add(n.get(2)); //append return type of method
			method.add(betterMethodName); //append method's name
			method.add(formalParameters); //append method's parameters
			thisDataLayoutsMethodList.add(method); //add this method to the method list
		}
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
	
    
    //n is the method declaration node
    //currentVTable is the current Vtable...
    //returns -1 if the method does not override any of the methods in currentVTable
    int indexOfOverridingMethod(GNode  n, GNode currentVTable) {
		String methodName = n.get(3).toString(); //get method name

		for(int i = 1; i < currentVTable.size()-1; i++)  //start at one to ignore __isa declaration... conclude at size-1 to ignore constructor declaration
		{ 
			if(methodName.equals(currentVTable.getNode(i).get(1).toString())) 
			{
				return i; //return index into VTable where method that needs to be overridden lies
			}
		}
		return -1; //method does not override anything in currentVTable
    }
	
    //same as method version
    int indexOfOverridenField(GNode newField, GNode currentDataLayout) {
		String fieldName = newField.getNode(2).getNode(0).get(0).toString();
		String existingField;
		for(int i = 0; i < currentDataLayout.size(); i++) //iterate through every field
		{
			if(fieldName.equals(currentDataLayout.getNode(i).getNode(2).getNode(0).get(0).toString())) //equals the current existing field
				return i; //return index into DataLayout where the field that need to be overriden lies
		}
		return -1; //field does not override an existing field in DataLayout
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