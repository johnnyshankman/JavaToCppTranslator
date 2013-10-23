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
		



        
  String thisClassName;
        GNode thisClassVTableStructDeclList;
        GNode thisClassImplementation;
        
	
	
	GNode originalTree;
  GNode classHierarchy;
  GNode translatedTree;
  GNode thisExpressionStatement;
  
 
    // pass Layout>??? 
  public ASTConverter(GNode layout, GNode javaAST) { 
    this.translatedTree = GNode.create("TranslationUnit");
    this.originalTree = javaAST;
    this.classHierarchy = layout;
        
  } 
	
// call to begin translation
  public void translateJavaToCPP() { dispatch(originalTree); 
    System.out.println("Dispatched.");}
    
 // Vist ALl nodes starting right here/ 
  public void visit(Node n) {
    for( Object o : n ) if( o instanceof Node ) dispatch((Node)o);
  }

//return the translated tree 
  public GNode getTranslatedTree() { return translatedTree; }


  
  
//OBjectree 
// 0 -> Declaration Node (with, specifiers, types and list of initialized declarator) 
// 1 -> DataLayout (has Structs in it with it's type.) - Special to C++
// 2 -> 
// creates a skeleton class structure for C++  tree and returns the tree, 
GNode buildHeaderForClass() {
        String className = thisClassName;
       GNode objectTree = GNode.create("HeaderDeclaration"); 
    {
      GNode Decl = GNode.create("Declaration"); 
      {
        Decl.add(0, null);
        GNode DeclSpef = GNode.create("DeclarationSpecifiers");
        {
          DeclSpef.add( 0, GNode.create("TypedefSpecifier"));
          DeclSpef.add(1, createPrimaryIdentifier( "__" + className + "*" ) );
        }
        Decl.add(1, DeclSpef);
        GNode initDeclList = GNode.create("InitializedDeclaratorList");
        {
          GNode initDecl = GNode.create("InitializedDeclarator");
          initDecl.add(0, null);
	 // hm? 
          initDecl.add(1, GNode.create("SimpleDeclarator").add(className));
          initDecl.add(2, null);
          initDecl.add(3, null);
          initDecl.add(4, null);
          initDeclList.add( initDecl );
        }
        Decl.add(2, initDeclList);
      }
	// first node to OBJ
      objectTree.add(0, Decl);
	//start making Second 
      GNode dataLayout = GNode.create("Declaration"); 
      {
        dataLayout.add(0, null);
        GNode dlDeclSpef = GNode.create("DeclarationSpecifiers");
        {
          GNode structDef = GNode.create("StructureTypeDefinition", 4);
          {
            structDef.add(0, null);
            structDef.add(1, "__" + className);
            structDef.add(2, GNode.create("StructureDeclarationList"));
            structDef.add(3, null);
          }
          dlDeclSpef.add(structDef);
        }
        dataLayout.add(1, dlDeclSpef);
        dataLayout.add(2, null);
      }
	// second node to OBJ 
      objectTree.add(1, dataLayout);
	//Vtable
	//Start making third  
	// Only difference between Second node and third node is that Second node has __VT following Classname 
	// And that "Structure DeclarationList has a null node in it.
      GNode vtableLayout = GNode.create("Declaration"); 
      {
        vtableLayout.add(0, null);
        GNode vtDeclSpef = GNode.create("DeclarationSpecifiers");
        {
          GNode vtStructDef = GNode.create("StructureTypeDefinition", 4);
          {
            vtStructDef.add(0, null);
            vtStructDef.add(1, "__" + className + "_VT");
	// SETS IT TO GLOBAL VARIABLE, KEEPS TRACK OF WHICH VTABLE 
            thisClassVTableStructDeclList = GNode.create("StructureDeclarationList");
            {
              thisClassVTableStructDeclList.add(0, null);
            }
            vtStructDef.add(2, thisClassVTableStructDeclList);
            vtStructDef.add(3, null);
          }
          vtDeclSpef.add(vtStructDef);
        }
        vtableLayout.add(1, vtDeclSpef);
        vtableLayout.add(2, null);
      }
	// Third Node to OBJ 
      objectTree.add(2, vtableLayout);
    }
    return objectTree;
    }
 
// class translation
//takes a class node and adds it to translated tree. 
//calls BUILDIMPLEMENTATION FOR CLASS -> 
//then visits further. 

  public void visitClassDeclaration(GNode n) {
        thisClassName = n.get(1).toString();
	// uses the classname to create a header for class 
        translatedTree.add(buildHeaderForClass());
	// returns a tree with "ImplementationDelaration" node with property set to classname to the header.
	//sets it to global variable to keep track of which class it's translating. 
        thisClassImplementation = buildImplementationForClass();
	// adds the "ImplementationDelaration" node with property set to classname to the header.
        translatedTree.add(thisClassImplementation);
        visit(n);
    }

    public GNode buildImplementationForClass() {
        GNode mainTree = GNode.create("ImplementationDeclaration");
        mainTree.setProperty("className", thisClassName);
        return mainTree;
    }
    
 // going into methods. 
    public void visitMethodDeclaration(GNode n) {
        addToVTable(n);
        addMethodImplementation(n);
        visit(n);
    }
 
	// adds it to vtable global variable it's keeping track of. 
    public void addToVTable(GNode n) {
	// n.get(3) should be method name. 
        thisClassVTableStructDeclList.add(0, createPrimaryIdentifier( "__" + thisClassName + "::" + (String)n.get(3) ));
    }
   
	// i dont get why this is necessary to visit again using this method. 
    public void addMethodImplementation(GNode n) {
        thisClassImplementation.add(functionDefForMethDecl(n));
        visit(n);
    }

	GNode functionDefForMethDecl(GNode n) {
		//Java:MethodDeclaration() -> CPP:FunctionDefinition()
		// Function name:
		// Return type:
		// Parameters: 
		GNode fncDef = GNode.create("FunctionDefinition");
		{
		    fncDef.add(0, null);
		    GNode declSpef = GNode.create("DeclarationSpecifiers");
		    {
		        declSpef.add( n.get(2) ); //add return type (java type)
		    }
		    fncDef.add(1, declSpef);
		    GNode fncDeclarator = GNode.create("FunctionDeclarator");
		    {
			System.out.println() 
		        GNode simpDecl = (GNode)GNode.create("SimpleDeclarator").add(n.get(3)); //method name
		        fncDeclarator.add(0, simpDecl);
		        fncDeclarator.add(1, null);
		    }
		    fncDef.add(2, fncDeclarator);
		    fncDef.add(3, null);
		    fncDef.add(4, n.get(7));  
		    // ^ NOTE: we are adding a java code block instead of a C compound statement
		}
		return fncDef;
	    }
	
// end of method translation.	
    
//sets the expression to global variable. 
// store it and visit
    public void visitExpressionStatement(GNode n) {
        thisExpressionStatement = n;
        visit(n);
    }
    
    public void visitCallExpression(GNode n) {
        if( n.size() >= 3 && "println".equals((String)n.get(2)) ) {
            GNode strOut = GNode.create("StreamOutputList");
            strOut.add(0, (GNode)GNode.create( "PrimaryIdentifier" ).add(0, "std::cout") );
            strOut.add(1, n.getNode(3).get(0) ); //FIXME: only adds the first argument
            strOut.add(2, (GNode)GNode.create( "PrimaryIdentifier" ).add(0, "std::endl") );
            /*
              n.set(2, "cout");
              GNode strLiteral = (GNode)GNode.create( kPrimID ).add(0, "std");
              n.set(0, strLiteral);
            */
            thisExpressionStatement.set(0, strOut);
        }
        else if( n.size() >= 3 && "print".equals((String)n.get(2)) ) {
            GNode strOut = GNode.create("StreamOutputList");
            strOut.add(0, (GNode)GNode.create( "PrimaryIdentifier" ).add(0, "std::cout") );
            strOut.add(1, n.getNode(3).get(0) ); //FIXME: only adds the first argument
            thisExpressionStatement.set(0, strOut);
        }
    }
	
  // helper method to return a create a node with Primary Identitifier. 
  GNode createPrimaryIdentifier( String contents ) {
    
    return (GNode)GNode.create( "PrimaryIdentifier" ).add(contents);
  }

}





