package org.gt.syntax.domain;

import java.util.HashSet;
import java.util.Map;

import org.gt.syntax.domain.node.AbstractNode;
import org.gt.syntax.domain.value.ValueObject;

/*
 * The Abstarct Syntax Tree from the script language
 * */
public class ASTree {

	public String getTrimedExpressionStr() {
		return trimedExpressionStr;
	}

	public void setTrimedExpressionStr(String trimedExpressionStr) {
		this.trimedExpressionStr = trimedExpressionStr;
	}

	/**
	 * root node
	 */
	AbstractNode root;

	String trimedExpressionStr;

	HashSet<String> varibleTable;

	HashSet<String> functionTable;

	HashSet<String> refTable;
	
	HashSet<String> dataSourceTable;

	

	public ASTree(AbstractNode root) {
		this.root = root;
		varibleTable = new HashSet<>();
		functionTable = new HashSet<>();
		refTable = new HashSet<>();
		dataSourceTable=new HashSet<>();
	}

	public ASTree() {
		this(null);
	}

	public void setRoot(AbstractNode root) {
		this.root = root;
	}

	public AbstractNode getRoot() {
		return this.root;
	}

	public HashSet<String> getVaribleTable() {
		return varibleTable;
	}

	public void setVaribleTable(HashSet<String> varibleTable) {
		this.varibleTable = varibleTable;
	}

	public HashSet<String> getFunctionTable() {
		return functionTable;
	}

	public void setFunctionTable(HashSet<String> functionTable) {
		this.functionTable = functionTable;
	}

	public  HashSet<String> getRefTable() {
		return refTable;
	}

	public  void setRefTable(HashSet<String> refTable) {
		this.refTable = refTable;
	}
	
	
	public HashSet<String> getDataSourceTable() {
		return dataSourceTable;
	}

	public void setDataSourceTable(HashSet<String> dataSourceTable) {
		this.dataSourceTable = dataSourceTable;
	}
	
	public void compile() {
		
	}
	
	public Object getExprValue() {
		return root.getValue().getValue();
	}
	
	
	public Object getExprValue(Map<String,Object> valueContext) {
		return root.getValue(valueContext).getValue();
	}
	
	

	public String getStringOfNode(AbstractNode node) {
		if (node == null)
			return null;
		if (trimedExpressionStr.length() == 0)
			return trimedExpressionStr;
		return trimedExpressionStr.substring(node.getStart(), node.getEnd());
	}

}
