package org.gt.syntax.domain.node;

import java.util.Map;

import org.gt.syntax.domain.value.NumberValue;
import org.gt.syntax.domain.value.ValueObject;

public class NumberNode extends AbstractNode {

	Number val;
	
	String numbStr;

	public Number getVal() {
		return val;
	}

	public String getNumbStr() {
		return numbStr;
	}

	public void setNumbStr(String numbStr) {
		this.numbStr = numbStr;
	}

	public void setVal(Number val) {
		this.val = val;
	}

	public NumberNode() {
		super();
		type = NodeType.num;
	}

	public NumberValue getValue() {
		return new NumberValue(val);
	}
	
	
	public ValueObject<?> getValue(Map<String,Object> valueMapping) {
		return new NumberValue(val);
	}
}