package org.gt.syntax.domain.value;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;

import org.gt.syntax.ComputeUtil;
import org.gt.syntax.ExecutionException;
import org.gt.syntax.domain.node.ReferenceNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValueOperationUtil {
	
	private final static Logger logger=LoggerFactory.getLogger(ValueOperationUtil.class);
	
	
	public static ValueObject<?> plus(ValueObject<?> a, ValueObject<?> b) {
		logger.info("[plus] input A={}",a);
		logger.info("[plus] input B={}",b);
		
		if(a.getValueType().equals(ValueType.number)&&b.getValueType().equals(ValueType.number)) {
			return new NumberValue(Double.valueOf(a.getValue().toString())+Double.valueOf(b.getValue().toString()));
		}else {
			throw new ExecutionException("Not support "+a.getValueType()+" add "+b.getValueType());
		}
		
	}

	public static ValueObject<?> minus(ValueObject<?> a, ValueObject<?> b) {
		logger.info("[minus] input A={}",a);
		logger.info("[minus] input B={}",b);
		if(a.getValueType().equals(ValueType.number)&&b.getValueType().equals(ValueType.number)) {
			return new NumberValue(Double.valueOf(a.getValue().toString())-Double.valueOf(b.getValue().toString()));
		}else {
			throw new ExecutionException("Not support "+a.getValueType()+" sub "+b.getValueType());
		}
		
	}
	
	private static Class<? extends Number> getType(Number a,Number b){
		
		Class ret=getSuprior(a.getClass(),b.getClass(),BigDecimal.class);
		if(ret!=null) {
			return ret;
		}
		ret=getSuprior(a.getClass(),b.getClass(),BigInteger.class);
		if(ret!=null) {
			return ret;
		}
		ret=getSuprior(a.getClass(),b.getClass(),Double.class);
		if(ret!=null) {
			return ret;
		}
		ret=getSuprior(a.getClass(),b.getClass(),Float.class);
		if(ret!=null) {
			return ret;
		}
		ret=getSuprior(a.getClass(),b.getClass(),Long.class);
		if(ret!=null) {
			return ret;
		}
		ret=getSuprior(a.getClass(),b.getClass(),Integer.class);
		if(ret!=null) {
			return ret;
		}
		ret=getSuprior(a.getClass(),b.getClass(),Short.class);
		if(ret!=null) {
			return ret;
		}
		ret=getSuprior(a.getClass(),b.getClass(),Byte.class);
		if(ret!=null) {
			return ret;
		}
		return null;
		
	}
	
	private static Class<?> getSuprior(Class<?> a,Class<?> b,Class<?> check){
		Class<?> ret=null;
		if(check.isAssignableFrom(a)||check.isAssignableFrom(b)) {
			ret=check;
		}
		return ret;
	}
	
	

	public static ValueObject<?> multiply(ValueObject<?> a, ValueObject<?> b) {
		logger.info("[multiply] input A={}",a);
		logger.info("[multiply] input B={}",b);
		if(a.getValueType().equals(ValueType.number)&&b.getValueType().equals(ValueType.number)) {
			return new NumberValue(Double.valueOf(a.getValue().toString())*Double.valueOf(b.getValue().toString()));
		}else {
			throw new ExecutionException("Not support "+a.getValueType()+" multiply "+b.getValueType());
		}
	}

	public static ValueObject<?> divid(ValueObject<?> a, ValueObject<?> b) {
		logger.info("[divid] input A={}",a);
		logger.info("[divid] input B={}",b);		
		if(a.getValueType().equals(ValueType.number)&&b.getValueType().equals(ValueType.number)) {
			return new NumberValue(Double.valueOf(a.getValue().toString())/Double.valueOf(b.getValue().toString()));
		}else {
			throw new ExecutionException("Not support "+a.getValueType()+" divid "+b.getValueType());
		}
	}

	public static ValueObject<?> oppositeOf(ValueObject<?> val) {
		logger.info("[oppositeOf] input val={}",val);
		if (val.getValueType() == ValueType.number) {
			return new NumberValue(ComputeUtil.oppositeOf(val.getValue()));
		} else {
			throw new ExecutionException("Not support to compute value of " + val.getValueType());
		}
	}
	
	public static ValueObject<?> executeFunction(String functionName,ValueObject<?>[] args) {
		logger.info("[executeFunction] {}({})",functionName,Arrays.asList(args));
		return new NumberValue(0);
	}
	
	
	public static ValueObject<?> getNamedVaribleValue(String variableName,Map<String,Object> valueMapping) {
		logger.info("[getNamedVaribleValue] {}",variableName);
		if(valueMapping.containsKey(variableName)) {
			Object t=valueMapping.get(variableName);
			if(t!=null) {
				logger.info("[getNamedVaribleValue] {}={}",variableName,t);
				if(t.getClass().equals(String.class)) {
					return new StringValue(String.valueOf(t));
				}else if(Number.class.isAssignableFrom(t.getClass())) {
					return new NumberValue(Number.class.cast(t));
				}else if(Boolean.class.isAssignableFrom(t.getClass())) {
					return new BooleanValue(Boolean.class.cast(t));
				}
			}
		}
		return new NullValue();
	}

	public static ValueObject<?> getReferencValue(ReferenceNode referenceNode) {
		logger.info("[getReferencValue] {}",referenceNode.getRefString());
		
		return new NumberValue(0);
	}

}
