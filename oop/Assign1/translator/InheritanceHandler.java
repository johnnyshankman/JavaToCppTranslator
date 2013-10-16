
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

//needs to be compartamentalized into
//initialization
//tree processing
//virtual method processing
//data layout processing
//constructor processing
//"internal" method processing
//overloading handling
//and we'll want to include getter methods

public class InheritanceHandler extends Visitor {
		
	GNode[] inheritanceTree; //will hold the whole thang
	GNode currentHeaderNode; //global, points to the current working header node (vtable and data layout)
    String className; //global current class name
	GNode classStaticVars; //this class's static variables
	
	public InheritanceHandler(Node[] astArray)
	{
		inheritanceTree = astArray;
		initializeGivenClasses(); //this will hardcode the Object, Class, String, and Array classes to be beginning of the tree (correctly)
									// every other class will have to extend one of these data layouts/classes (meaning they just inherit and add on to the existing data layout)
		
		for(int i = 0; i < astArray.length; i++)  //for each dependency AST, dispatch
		{
			if(astArray[i] != null) 
			{
				this.dispatch(astArray[i]); //will visit everything in the tree, now to override the visit____ methods fuck.
			}
		}
		
		//dispatchCallExpressionVisitor(astArray); //really have no idea what this does and why it's after everything else
		//ill have to analyze the reference code to understand why they needed this seemingly bizarre step

	}
	
	
	
	
	
	public void visit(Node n) 
	{
		for( Object o : n ) {
			if (o instanceof Node) dispatch((Node)o);
		}
	}

	
	
	
    public void visitClassDeclaration(GNode n) {
    	//techincally creating a childNode of whatever class this extends
		className = n.get(1).toString();
		GNode newChildNode = GNode.create("Class");
		newChildNode.setProperty("name", className);
		
		thisClassStaticVars = GNode.create("StaticVarsList"); //for use way later, remains blank atm
		
		//Now we need a super class that's either Object or something explicit
		GNode parent = null; 
		if(n.get(3) != null) { // if it extends something
			String extendedClass = (String)(n.getNode(3).getNode(0).getNode(0).get(0)); // String name of Parent Class
			parent = getClass(extendedClass); // Append new class as child of the Parent class, otherwise it gets Object as parent
		}
		else { 
			parent = getClass("Object"); // Doesn't extend, add to root (Object)
		}
		
		if(DEBUG) System.out.println( "Visiting new class: " + className + " extends " + parent.getProperty("name") );
		
		//Building the vtables and data layout via Parent
		currentHeaderNode = inheritHeader( (GNode)parent.getNode(0) ); //inherit parent header
		visit(n); // <-- VISITING ALL CHILDREN OF THIS CLASS (DATA FIELDS, METHODS, CONSTRUCTORS, ...)
		newChildNode.add(currentHeaderNode);
		newChildNode.setProperty( "parentClassNode", parent);
		parent.addNode(newChildNode);
    }
	
    
    
    
    
    
    
	//hardcodes the given base classes
	//Object, Class, String, and Array
    //need to provide the data layout for Object
	public void initializeGivenClasses(){
		GNode object = GNode.create("Class");
		object.setProperty("name", "Object");
		GNode classHeaderDeclaration = GNode.create("ClassHeaderDeclaration");
		classHeaderDeclaration.add( objectClassVirtualTable() );
		classHeaderDeclaration.add( objectClassDataLayout() );
		object.add(classHeaderDeclaration);

		GNode string = GNode.create("Class");
		string.setProperty("name", "String");
		string.setProperty("parentClassNode", objectNode);
		className = "String";
		string.add( inheritHeader( classHeaderDeclaration ) );

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
	}
	
	
	
	
	
	public void visitMethodDeclaration(GNode n){
		//adds virtual methods to vtable
	}
	
	
	public GNode getSuperclass(String sc) 
	{
		GNode child = getClass(sc);
		
		if(DEBUG) System.out.println("got class node for " + sc);
		// can't be passed primitive types

		if (child.getProperty("name").equals("Object")) return null;
			
		return (GNode)child.getProperty("parentClassNode");
	}
	
	
	public GNode getSuperclass(GNode n) 
	{
		return getSuperclass( n.getStringProperty("name") );
	}

	
	public boolean hasSuperclass(String sc) 
	{
		if (getSuperclass(sc) != null) 
		{
			return true;
		}
		else
			return false;
	}
	
	
	public String getClassName(GNode n) 
	{
		return n.getStringProperty("name");
    }
	
	
	//print classes using class tree to make sure shits working
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
