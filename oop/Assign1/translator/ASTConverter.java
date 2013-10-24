package oop;

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

public class ASTConverter extends Visitor {
		
  	String currentClassName;
        GNode currentVTableStructDeclarations;
        GNode currentClassImplementation;
	GNode javaTree;
	GNode classHierarchy;
	GNode cppTree;
	GNode currentExpressionStatement;
	  
 
	//Constructor
  public ASTConverter(GNode inheritanceTree, GNode javaAST) { 
    this.cppTree = GNode.create("TranslationUnit");
    this.javaTree = javaAST;
    this.classHierarchy = inheritanceTree;   
  } 
	
// call to begin translation
  public void translateJavaToCPP() { dispatch(javaTree); 
    System.out.println("Dispatched.");}
    
 // Vist ALl nodes starting right here/ 
  public void visit(Node n) {
    for( Object o : n ) if( o instanceof Node ) dispatch((Node)o);
  }

//return the translated tree 
  public GNode getTranslatedTree() { return cppTree; }



//Class Tree 
// 0 -> Declaration Node (with, specifiers, types and list of initialized declarator) 
// 1 -> DataLayout (has Structs in it with it's type.) - Special to C++
// 2 -> 
// creates a skeleton class structure for C++  tree and returns the tree, 
GNode buildHeaderForClass() 
{
	String className = currentClassName;
    GNode classTree = GNode.create("HeaderDeclaration"); 
	{
    	GNode declaration = GNode.create("Declaration"); 
      	{
        	declaration.add(0, null);
        	GNode declarationSpecs = GNode.create("declarationSpecs");
        	{
          		declarationSpecs.add( 0, GNode.create("TypedefSpecifier"));
          		declarationSpecs.add(1, createPrimaryIdentifier( "__" + className + "*" ) );
        	}
        declaration.add(1, declarationSpecs);
        GNode initializedDeclaratorList = GNode.create("InitializedDeclaratorList");
        {
          GNode initializedDeclarator = GNode.create("InitializedDeclarator");
          initializedDeclarator.add(0, null);
	 // hm? 
          initializedDeclarator.add(1, GNode.create("SimpleDeclarator").add(className));
          initializedDeclarator.add(2, null);
          initializedDeclarator.add(3, null);
          initializedDeclarator.add(4, null);
          initializedDeclaratorList.add( initializedDeclarator );
        }
        declaration.add(2, initializedDeclaratorList);
      }  
	// first node to classTree
      classTree.add(0, declaration);
	//start making Second 
      GNode dataLayout = GNode.create("Declaration"); 
      {
        dataLayout.add(0, null);
        GNode dataLayoutDeclarationSpecs = GNode.create("declarationSpecs");
        {
          GNode structTypeDef = GNode.create("StructureTypeDefinition", 4);
          {
            structTypeDef.add(0, null);
            structTypeDef.add(1, "__" + className);
            structTypeDef.add(2, GNode.create("StructureDeclarationList"));
            structTypeDef.add(3, null);
          }
          dataLayoutDeclarationSpecs.add(structTypeDef);
        }
        dataLayout.add(1, dataLayoutDeclarationSpecs);
        dataLayout.add(2, null);
      }
	// second node to OBJ 
      classTree.add(1, dataLayout);
	//Vtable
	//Start making third  
	// Only difference between Second node and third node is that Second node has __VT following Classname 
	// And that "Structure DeclarationList has a null node in it.
      GNode vTable = GNode.create("Declaration"); 
      {
        vTable.add(0, null);
        GNode vTableDeclarationSpecs = GNode.create("declarationSpecs");
        {
          GNode vTableStructTypeDef = GNode.create("StructureTypeDefinition", 4);
          {
            vTableStructTypeDef.add(0, null);
            vTableStructTypeDef.add(1, "__" + className + "_VT");
	// SETS IT TO GLOBAL VARIABLE, KEEPS TRACK OF WHICH VTABLE 
            currentVTableStructDeclarations = GNode.create("StructureDeclarationList");
            {
              currentVTableStructDeclarations.add(0, null);
            }
            vTableStructTypeDef.add(2, currentVTableStructDeclarations);
            vTableStructTypeDef.add(3, null);
          }
          vTableDeclarationSpecs.add(vTableStructTypeDef);
        }
        vTable.add(1, vTableDeclarationSpecs);
        vTable.add(2, null);
      }
	// Third Node to OBJ 
      classTree.add(2, vTable);
    }
    return classTree;
    }
 
// class translation
//takes a class node and adds it to translated tree. 
//calls BUILDIMPLEMENTATION FOR CLASS -> 
//then visits further. 

  public void visitClassDeclaration(GNode n) {
        currentClassName = n.get(1).toString();
	// uses the classname to create a header for class 
        cppTree.add(buildHeaderForClass());
	// returns a tree with "Implementation" node with property set to classname to the header.
	//sets it to global variable to keep track of which class it's translating. 
        currentClassImplementation = buildImplementationForClass();
	// adds the "Implementation" node with property set to classname to the header.
        cppTree.add(currentClassImplementation);
        visit(n);
    }

    public GNode buildImplementationForClass() {
        GNode implementation = GNode.create("Implementation");
        implementation.setProperty("className", currentClassName);
        return implementation;
    }
    
 // going into methods. 
    public void visitMethodDeclaration(GNode n) {
        addToVTable(n);
        addMethodImplementation(n);
        visit(n);
    }
 
	// adds it to vtable global variable it's keeping track of. 
    public void addToVTable(GNode n) {
	// n.(3) should be method name. 
        currentVTableStructDeclarations.add(0, createPrimaryIdentifier( "__" + currentClassName + "::" + (String)n.get(3) ));
    }
   
	// i dont get why this is necessary to visit again using this method. 
    public void addMethodImplementation(GNode n) {
        currentClassImplementation.add(methodDeclarationFunctionDef(n));
        visit(n);
    }

	GNode methodDeclarationFunctionDef(GNode n) {
		//Java:MethodDeclaration() -> CPP:FunctionDefinition()
		// Function name:
		// Return type:
		// Parameters: 
		GNode functionDef = GNode.create("FunctionDefinition");
		{
		    functionDef.add(0, null);
		    GNode declarationSpecs = GNode.create("declarationSpecs");
		    {
		        declarationSpecs.add( n.get(2) ); //add return type (java type)
		    }
		    functionDef.add(1, declarationSpecs);
		    GNode functionDeclarator = GNode.create("FunctionDeclarator");
		    {
			System.out.println();
		        GNode simpleDeclarator = (GNode)GNode.create("SimpleDeclarator").add(n.get(3)); //method name
		        functionDeclarator.add(0, simpleDeclarator);
		        functionDeclarator.add(1, null);
		    }
		    functionDef.add(2, functionDeclarator);
		    functionDef.add(3, null);
		    functionDef.add(4, n.get(7));  
		    // ^ NOTE: we are adding a java code block instead of a C compound statement
		}
		return functionDef;
	    }
	
// end of method translation.	
    
//sets the expression to global variable. 
// store it and visit
    public void visitExpressionStatement(GNode n) {
        currentExpressionStatement = n;
        visit(n);
    }
    public void visitExpression(GNode n){
	if("PrimaryIdentifier".equals(n.getNode(0).getName())){  
		if("+".equals(n.getNode(2).getString(1))) {
         	     n.getNode(2).set(1, "<<");
                              }
} 
	} 
    public void visitCallExpression(GNode n) { 
        if( n.size() >= 3 && "println".equals((String)n.get(2)) ) {
            GNode streamOutput = GNode.create("StreamOutputList");
            streamOutput.add(0, (GNode)GNode.create( "PrimaryIdentifier" ).add(0, "std::cout") );
            streamOutput.add(1, n.getNode(3).get(0) ); //FIXME: only adds the first argument
            streamOutput.add(2, (GNode)GNode.create( "PrimaryIdentifier" ).add(0, "std::endl") );
            /*
              n.set(2, "cout");
              GNode strLiteral = (GNode)GNode.create( kPrimID ).add(0, "std");
              n.set(0, strLiteral);
            */
            currentExpressionStatement.set(0, streamOutput);
        }
        else if( n.size() >= 3 && "print".equals((String)n.get(2)) ) {
            GNode streamOutput = GNode.create("StreamOutputList");
            streamOutput.add(0, (GNode)GNode.create( "PrimaryIdentifier" ).add(0, "std::cout") );
            streamOutput.add(1, n.getNode(3).get(0) ); //FIXME: only adds the first argument
            currentExpressionStatement.set(0, streamOutput);
        }
    }
	
  // helper method to return a node with Primary Identitifier. 
  GNode createPrimaryIdentifier( String contents ) {
    
    return (GNode)GNode.create( "PrimaryIdentifier" ).add(contents);
  }

}





