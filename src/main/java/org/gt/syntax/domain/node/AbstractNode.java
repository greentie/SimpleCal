package org.gt.syntax.domain.node;


import java.util.Map;

import org.gt.syntax.domain.value.NumberValue;
import org.gt.syntax.domain.value.ValueObject;
import org.gt.syntax.domain.value.ValueType;

public abstract class AbstractNode {

	protected NodeType type;

	protected ValueType vType;
	
	//protected ContainerType containerType;

	int start;

	int end;

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public NodeType getType() {
		return type;
	}

	public void setType(NodeType type) {
		this.type = type;
	}

	public ValueType getvType() {
		return vType;
	}

	public void setvType(ValueType vType) {
		this.vType = vType;
	}
	
//	public ContainerType getContainerType() {
//		return containerType;
//	}
//
//	public void setContainerType(ContainerType containerType) {
//		this.containerType = containerType;
//	}

	public ValueObject<?> getValue() {
		return getValue(null);
	}
	
	abstract public ValueObject<?> getValue(Map<String,Object> valueMapping) 
//	{
//		return new NumberValue(0);
//	}
	;

}