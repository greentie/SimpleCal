package org.gt.syntax.domain.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.gt.syntax.domain.value.ValueObject;
import org.gt.syntax.domain.value.ValueOperationUtil;

public class FunctionNode extends AbstractNode {

	String functionName;

	List<AbstractNode> args = new ArrayList<>();

	public String getFunctionName() {
		return functionName;
	}

	public void appendArgs(AbstractNode arg) {
		this.args.add(arg);
	}

	public void appendArgs(List<? extends AbstractNode> args) {
		this.args.addAll(args);
	}

	public List<AbstractNode> getArgs() {
		return args;
	}

	public void setArgs(List<AbstractNode> args) {
		this.args = args;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public FunctionNode() {
		super();
		type = NodeType.func;
	}
	
	public ValueObject<?> getValue(Map<String,Object> valueMapping){
		return ValueOperationUtil.executeFunction(functionName,args.stream().map(arg->arg.getValue(valueMapping)).collect(Collectors.toList()).toArray(new ValueObject<?>[0]));
	} 
	
}