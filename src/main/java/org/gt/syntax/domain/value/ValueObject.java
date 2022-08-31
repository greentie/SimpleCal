package org.gt.syntax.domain.value;

public abstract class ValueObject<T> {

	ValueType valueType;

	ContainerType containerType;
	T value;

	public ContainerType getContainerType() {
		return containerType;
	}

	public void setContainerType(ContainerType containerType) {
		this.containerType = containerType;
	}

	public ValueType getValueType() {
		return valueType;
	}

	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return String.format("(valueType %s containerType %s)=%s", valueType,containerType,value.toString());
	}
}
