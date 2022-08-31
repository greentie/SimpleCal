package org.gt.syntax;

public class ParseUtils {
	
	
	private static final String capital = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static final String small = "abcdefghijklmnopqrstuvwxyz";
	
	private static final String numberchar = "0123456789";
	
	public static int findFirstNext(char[] charArr,char key,int ptr) {
		for(int i=ptr;i<charArr.length;i++) {
			if(charArr[i]==key) {
				return i;
			}
		}
		return -1;
	}
	
	
	public static boolean isOpenParentheses(char c) {
		return c == '(';
	}

	public static boolean isCloseParentheses(char c) {
		return c == ')';
	}
	
	public static boolean isOpenBracket(char c) {
		return c=='[';
	}
	
	public static boolean isCloseBracket(char c) {
		return c==']';
	}
	
	
	public static boolean isOpenBrace(char c) {
		return c=='{';
	}
	
	public static boolean isCloseBrace(char c) {
		return c=='}';
	}
	
	public static boolean isDot(char c) {
		return c=='.';
	}
	
	public static boolean isExclamation(char c) {
		return c=='!';
	}

	public static boolean isCJKChar(char c) {
		boolean value = false;
		value |= c >= '\u4E00' && c <= '\u9FFF';
		value |= c >= '\u3400' && c <= '\u4DBF';
		value |= c >= '\uF900' && c <= '\uFAFF';
		return value;
	}


	public static boolean isCapitalChar(char c) {
		return c >= 'A' && c <= 'Z';
	}

	public static boolean isSmallLetter(char c) {
		return c >= 'a' && c <= 'z';
	}
	
	public static boolean isAlphabet(char c) {
		return isCapitalChar(c)||isSmallLetter(c);
	}

	public static boolean isNumberChar(char c) {
		return isNumber(c) || c == '.';
	}
	
	public static boolean isNumber(char c) {
		return c >= '0' && c <= '9' ;
	}

	public static boolean isKindOfChar(char c, char... samples) {
		for (char s : samples) {
			if (c == s)
				return true;
		}
		return false;
	}


	public static boolean isComma(char c) {
		return c==',';
	}
}
