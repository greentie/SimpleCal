package org.gt.syntax.domain.node;

import java.util.Map;

import org.gt.syntax.domain.value.ValueObject;
import org.gt.syntax.domain.value.ValueOperationUtil;

public class ItemNode extends AbstractNode {

	AbstractNode left;
	AbstractNode right;

	boolean needCompute;

	char operationCode;

	public char getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(char operationCode) {
		this.operationCode = operationCode;
	}

	public AbstractNode getLeft() {
		return left;
	}

	public AbstractNode getRight() {
		return right;
	}

	public boolean isNeedCompute() {
		return needCompute;
	}

	public void setLeft(AbstractNode left) {
		this.left = left;
	}

	public void setRight(AbstractNode right) {
		this.right = right;
	}

	public void setNeedCompute(boolean needCompute) {
		this.needCompute = needCompute;
	}

	public ItemNode() {
		super();
		type = NodeType.item;
	}
	
	
	public ValueObject<?> getValue() {
		if(!needCompute) {
			return left.getValue();
		}else {
			if(operationCode=='*') {
				return ValueOperationUtil.multiply(left.getValue(), right.getValue());
			}else if(operationCode=='/') {
				return ValueOperationUtil.divid(left.getValue(), right.getValue());
			}else {
				throw new UnsupportedOperationException("not support operator of " +operationCode);
			}
		}
	}
	
	public ValueObject<?> getValue(Map<String,Object> valueMapping) {
		if(!needCompute) {
			return left.getValue(valueMapping);
		}else {
			if(operationCode=='*') {
				return ValueOperationUtil.multiply(left.getValue(valueMapping), right.getValue(valueMapping));
			}else if(operationCode=='/') {
				return ValueOperationUtil.divid(left.getValue(valueMapping), right.getValue(valueMapping));
			}else {
				throw new UnsupportedOperationException("not support operator of " +operationCode);
			}
		}
	}
}