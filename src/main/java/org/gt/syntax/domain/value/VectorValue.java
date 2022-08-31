package org.gt.syntax.domain.value;

import java.util.LinkedList;

public class VectorValue<T> extends ValueObject<LinkedList<T>> {

	ValueType rawValueType;
	
	public VectorValue(LinkedList<T> vector) {
		value=vector;
		valueType=ValueType.vector;
		if(vector.size()==0) {
			rawValueType=ValueType.Null;
		}else {
			rawValueType=inferValueType(vector.iterator().next());
		}
		containerType=ContainerType.scalar;
	}
	
		private  ValueType inferValueType(T element) {
		
		Class<?> type=element.getClass();
		if(Number.class.isAssignableFrom(type)) return ValueType.number;
		if(String.class.isAssignableFrom(type)) return ValueType.str;
		if(Boolean.class.isAssignableFrom(type)) return ValueType.bool;
		
		throw new RuntimeException("not support computeType");
		
	}
	
}
