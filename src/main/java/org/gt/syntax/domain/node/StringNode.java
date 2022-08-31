package org.gt.syntax.domain.node;

import java.util.Map;
import java.util.Optional;

import org.gt.syntax.domain.value.StringValue;

public class StringNode extends AbstractNode {

	String val;

	public StringNode() {
		super();
		type = NodeType.str;
	}

	
	public StringValue getValue(Map<String,Object> valueMapping) {
		return new StringValue(val);
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}
}