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
		



	
	
	GNode originalTree;
  GNode classHierarchy;
  GNode translatedTree;
  
  GNode thisClassVTableStructDeclList;
    
  public ASTConverter(GNode layout, GNode javaAST) { 
    this.translatedTree = GNode.create("TranslationUnit");
    this.originalTree = javaAST;
    this.classHierarchy = layout;
        
  } 
  
  GNode createPrimaryIdentifier( String contents ) {
    
    return (GNode)GNode.create( "PrimaryIdentifier" ).add(contents);
  }
  
  GNode buildHeaderForClass(String className) {
    GNode objectTree = GNode.create("HeaderDeclaration"); 
    {
      GNode typedefDecl = GNode.create("Declaration"); 
      {
        typedefDecl.add(0, null);
        GNode typeDefDeclSpef = GNode.create("DeclarationSpecifiers");
        {
          typeDefDeclSpef.add( 0, GNode.create("TypedefSpecifier"));
          typeDefDeclSpef.add(1, createPrimaryIdentifier( "__" + className + "*" ) );
        }
        typedefDecl.add(1, typeDefDeclSpef);
        GNode initDeclList = GNode.create("InitializedDeclaratorList");
        {
          GNode initDecl = GNode.create("InitializedDeclarator");
          initDecl.add(0, null);
          initDecl.add(1, GNode.create("SimpleDeclarator").add(className));
          initDecl.add(2, null);
          initDecl.add(3, null);
          initDecl.add(4, null);
          initDeclList.add( initDecl );
        }
        typedefDecl.add(2, initDeclList);
      }
      objectTree.add(0, typedefDecl);
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
      objectTree.add(1, dataLayout);
      GNode vtableLayout = GNode.create("Declaration"); 
      {
        vtableLayout.add(0, null);
        GNode vtDeclSpef = GNode.create("DeclarationSpecifiers");
        {
          GNode vtStructDef = GNode.create("StructureTypeDefinition", 4);
          {
            vtStructDef.add(0, null);
            vtStructDef.add(1, "__" + className + "_VT");
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
      objectTree.add(2, vtableLayout);
    }
    return objectTree;
  }
  
  public void translateJavaToCPP() { dispatch(originalTree); 
    System.out.println("Dispatched.");}
    
  public GNode getTranslatedTree() { return translatedTree; }

  
  public void visit(Node n) {
    for( Object o : n ) if( o instanceof Node ) dispatch((Node)o);
  }
  
  public void visitClassDeclaration(GNode n) {
    translatedTree.add(buildHeaderForClass( n.get(1).toString() ));
    visit(n);
  }
  
  public void visitMethodDeclaration(GNode n) {
    thisClassVTableStructDeclList.add(0, createPrimaryIdentifier( (String)n.get(3) ));
    visit(n);
  }
	
	
	
  
}





