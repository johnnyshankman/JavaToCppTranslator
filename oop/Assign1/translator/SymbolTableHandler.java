package oop;

import xtc.util.SymbolTable;
import xtc.util.Runtime;

import xtc.tree.GNode;
import xtc.tree.Node;


public class SymbolTableHandler extends xtc.lang.JavaExternalAnalyzer {
	
	//run dispatch on an instance of JavaExternalAnalyzer to create the symbol table
	public SymbolTableHandler(Runtime runtime, SymbolTable table)
	{
		super(runtime, table);
	}
	
	//added in order to visit method bodies
	public void visitBlock(GNode n) 
	{
		_table.enter(_table.freshName("block"));
		_table.mark(n);
		visit(n);
		_table.exit();
	}
	
	public void visitForStatement(GNode n) 
	{
		_table.enter(_table.freshName("forStatement"));
		_table.mark(n);
		visit(n);
		_table.exit();
	}
	
	public void visit(Node n) 
	{
		for (Object o : n) if (o instanceof Node) dispatch((Node) o);
	}
		
}