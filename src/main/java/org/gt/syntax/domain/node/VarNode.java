package org.gt.syntax.domain.node;

import java.util.Map;

import org.gt.syntax.domain.value.ValueObject;

public class VarNode extends AbstractNode {

	public VarNode() {
		super();
		type = NodeType.var;
	}

	AbstractNode valueNode;

	public AbstractNode getValueNode() {
		return valueNode;
	}

	public void setValueNode(AbstractNode valueNode) {
		this.valueNode = valueNode;
	}

	public ValueObject<?> getValue(Map<String,Object> valueMapping) {
		return valueNode.getValue(valueMapping);
	}
}