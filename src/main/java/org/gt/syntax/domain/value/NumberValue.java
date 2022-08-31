package org.gt.syntax.domain.value;

public class NumberValue extends ValueObject<Number> {

	public NumberValue(Number num) {
		value=num;
		valueType=ValueType.number;
		containerType=ContainerType.scalar;
	}
}
