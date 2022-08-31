package org.gt.syntax.domain.value;

import java.util.Collection;

public class CollectionValue<T> extends ValueObject<Collection<T>> {

	ValueType rawValueType;
	
	public CollectionValue(Collection<T> collection) {
		value=collection;
		valueType=ValueType.collection;
		if(collection.isEmpty()) {
			rawValueType=ValueType.Null;
		}else {
			rawValueType=inferValueType(collection.iterator().next());
		}
		containerType=ContainerType.scalar;
	}
	
	
	private  ValueType inferValueType(T element) {
		
		Class<?> type=element.getClass();
		if(Number.class.isAssignableFrom(type)) return ValueType.number;
		if(String.class.isAssignableFrom(type)) return ValueType.str;
		if(Boolean.class.isAssignableFrom(type)) return ValueType.bool;
		if(ValueObject.class.isAssignableFrom(type)) {
			return ValueObject.class.cast(element).getValueType();
		}else {
			throw new RuntimeException("not support computeType");
		}
	}
	
	
}
