package org.gt.syntax.domain.value;

public class BooleanValue extends ValueObject<Boolean> {

	public BooleanValue(Boolean bool) {
		value = bool;
		valueType = ValueType.bool;
		containerType = ContainerType.scalar;
	}
}
