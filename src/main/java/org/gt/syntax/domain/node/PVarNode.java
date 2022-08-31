package org.gt.syntax.domain.node;

import java.util.Map;

import org.gt.syntax.domain.value.ValueObject;
import org.gt.syntax.domain.value.ValueOperationUtil;

public class PVarNode extends AbstractNode {

	boolean needCompute;

	AbstractNode innerNode;

	public boolean isNeedCompute() {
		return needCompute;
	}

	public void setNeedCompute(boolean needCompute) {
		this.needCompute = needCompute;
	}

	public AbstractNode getInnerNode() {
		return innerNode;
	}

	public void setInnerNode(AbstractNode innerNode) {
		this.innerNode = innerNode;
	}

	public ValueObject<?> getValue(Map<String,Object> valueMapping) {
		ValueObject<?> val = innerNode.getValue(valueMapping);
		if (!needCompute) {
			return val;
		} else {
			return ValueOperationUtil.oppositeOf(val);
		}
	}

	public PVarNode() {
		super();
		type = NodeType.pvar;
	}
}