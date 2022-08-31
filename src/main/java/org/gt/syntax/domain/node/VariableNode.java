package org.gt.syntax.domain.node;

import java.util.Map;

import org.gt.syntax.domain.value.ValueObject;
import org.gt.syntax.domain.value.ValueOperationUtil;

public class VariableNode extends AbstractNode {

	private String variableName;

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public VariableNode() {
		super();
		type = NodeType.variable;
	}
	
	public ValueObject<?> getValue(Map<String,Object> valueMapping) {
		return ValueOperationUtil.getNamedVaribleValue(variableName,valueMapping);
	}
}