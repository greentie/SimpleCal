package org.gt.syntax.domain.value;

public class NullValue  extends ValueObject<Void>{

	public NullValue() {
		value = null;
		valueType = ValueType.Null;
	}
}
