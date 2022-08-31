package org.gt.syntax;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComputeUtil {

	@InnerFunction("opposite")
	public static Number oppositeOf(Object num) {
		if (num instanceof Double) {
			return -(Double) num;
		}
		if (num instanceof Float) {
			return -(Float) num;
		}
		if (num instanceof Integer) {
			return -(Integer) num;
		}
		if (num instanceof Long) {
			return -(Long) num;
		}
		if (num instanceof Short) {
			return -(Short) num;
		}
		if (num instanceof BigInteger) {
			return BigInteger.valueOf(-1).multiply((BigInteger) num);
		}
		if (num instanceof BigDecimal) {
			return BigDecimal.valueOf(-1L).multiply((BigDecimal) num);
		} else {
			throw new UnsupportedOperationException("Not support to compute value of " + num.getClass());
		}
	}

	private static boolean isPlusableType(Class type) {
		return Number.class.isAssignableFrom(type) || String.class.isAssignableFrom(type)
				|| Collection.class.isAssignableFrom(type);
	}
	
	
	private static boolean isMinusableType(Class type) {
		return Number.class.isAssignableFrom(type);
	}
	

	private static boolean isMultiableType(Class type) {
		return Number.class.isAssignableFrom(type);
	}

	@SuppressWarnings("rawtypes")
	public static Object plus(Object A, Object B) {

		if (!isPlusableType(A.getClass()) || !isPlusableType(B.getClass())) {
			throw new UnsupportedOperationException(
					"Not support to compute the sume value of " + A.getClass() + " and " + B.getClass());
		}

		if (A instanceof String && B instanceof String) {
			return ((String) A) + ((String) B);
		} else if (A instanceof Double && B instanceof Double) {
			return ((Double) A) + ((Double) B);
		} else if (A instanceof Long && B instanceof Long) {
			return ((Long) A) + ((Long) B);
		} else if (A instanceof List && B instanceof List) {
			List x = new ArrayList();
			x.addAll((List) A);
			x.addAll((List) B);
			return x;
		} else if (A instanceof Set && B instanceof Set) {
			Set x = new HashSet();
			x.addAll((Set) A);
			x.addAll((Set) B);
			return x;
		} else
			throw new UnsupportedOperationException(
					"Not support to compute the sume value of " + A.getClass() + " and " + B.getClass());
	}
	
	
	@SuppressWarnings("rawtypes")
	public static Object multiply(Object A, Object B) {

		if (!isMultiableType(A.getClass()) || !isMultiableType(B.getClass())) {
			throw new UnsupportedOperationException(
					"Not support to compute the value of " + A.toString() + "*" + B.toString());
		}
		if (A instanceof Double && B instanceof Double) {
			return ((Double) A) * ((Double) B);
		} else if (A instanceof Long && B instanceof Long) {
			return ((Long) A) * ((Long) B);
		} else 
			throw new UnsupportedOperationException(
					"Not support to compute the value of " + A.toString() + "*" + B.toString());
	}
	
	@SuppressWarnings("rawtypes")
	public static Object divid(Object A, Object B) {

		if (!isMultiableType(A.getClass()) || !isMultiableType(B.getClass())) {
			throw new UnsupportedOperationException(
					"Not support to compute the value of " + A.toString() + "/" + B.toString());
		}
		if (A instanceof Double && B instanceof Double) {
			return ((Double) A) / ((Double) B);
		} else if (A instanceof Long && B instanceof Long) {
			return ((Long) A) / ((Long) B);
		} else 
			throw new UnsupportedOperationException(
					"Not support to compute the value of " + A.toString() + "/" + B.toString());
	}
	
	@SuppressWarnings("rawtypes")
	public static Object minus(Object A, Object B) {

		if (!isMinusableType(A.getClass()) || !isMinusableType(B.getClass())) {
			throw new UnsupportedOperationException(
					"Not support to compute the value of " + A.toString() + "-" + B.toString());
		}
		if (A instanceof Double && B instanceof Double) {
			return ((Double) A) - ((Double) B);
		} else if (A instanceof Long && B instanceof Long) {
			return ((Long) A) - ((Long) B);
		} else 
			throw new UnsupportedOperationException(
					"Not support to compute the value of " + A.toString() + "-" + B.toString());
	}
	

//	public static Object sum(Object A,Object B) {
//		if(num instanceof Double) {
//			return -(Double)num;
//		}
//		if(num instanceof Float) {
//			return -(Float)num;
//		}
//		if(num instanceof Integer) {
//			return -(Integer)num;
//		}
//		if(num instanceof Long) {
//			return -(Long)num;
//		}
//		if(num instanceof Short) {
//			return -(Short)num;
//		}
//		if(num instanceof BigInteger) {
//			return BigInteger.valueOf(-1).multiply((BigInteger)num);
//		}
//		if(num instanceof BigDecimal) {
//			return BigDecimal.valueOf(-1L).multiply((BigDecimal)num);
//		}
//		else {
//			throw new UnsupportedOperationException("Not support to compute value of "+num.getClass());
//		}
//	}
//	
	
	public static void main(String[] args) {
		
	}

}
