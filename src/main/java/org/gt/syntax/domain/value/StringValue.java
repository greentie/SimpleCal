package org.gt.syntax.domain.value;

public class StringValue extends ValueObject<String> {

	public StringValue(String str) {
		value = str;
		valueType = ValueType.str;
		containerType = ContainerType.scalar;
	}
}
