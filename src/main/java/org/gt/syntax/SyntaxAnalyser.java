package org.gt.syntax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.gt.syntax.domain.ASTree;
import org.gt.syntax.domain.node.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyntaxAnalyser {

	private static class ParseContext {
		private ASTree tree;
	}

	private static final Logger logger = LoggerFactory.getLogger(SyntaxAnalyser.class);

	private static final String extendChar = "[]{}<>()%$@&!|-";

	private static final char[] subExtendChars = extendChar.substring(0, 8).toCharArray();

	private static final char[] extendChars = extendChar.toCharArray();

	private static boolean isEmptyChar(char c) {
		return ParseUtils.isKindOfChar(c, ' ', '\t', '\r', '\n', '\u00A0', '\u0020', '\u3000');
	}

	private static boolean isMultiply(char c) {
		return ParseUtils.isKindOfChar(c, '/', '*');
	}

	private static boolean isPlusMinus(char c) {
		return ParseUtils.isKindOfChar(c, '+', '-');
	}

	private static boolean isNegetive(char c) {
		return ParseUtils.isKindOfChar(c, '-');
	}

	private static boolean isFunctionLead(char c) {
		return ParseUtils.isKindOfChar(c, '&');
	}

	private static boolean isReferenceLead(char c) {
		return ParseUtils.isKindOfChar(c, '$', '[');
	}

	private static boolean isCellRefLead(char c) {
		return ParseUtils.isKindOfChar(c, '$');
	}

	private static boolean isVariableLead(char c) {
		return ParseUtils.isKindOfChar(c, '@');
	}

	private static boolean isValidVariableLead(char c) {
		return c == '_' || ParseUtils.isAlphabet(c);
	}

	private static boolean isValidVariableChar(char c) {
		return c == '_' || ParseUtils.isAlphabet(c) || ParseUtils.isNumberChar(c);
	}

	private static boolean isValidString(char c) {
		return !isEmptyChar(c)
				&& (isValidVariableChar(c) || ParseUtils.isCJKChar(c) || ParseUtils.isKindOfChar(c, extendChars));
	}

	private static boolean isValidSheetNameChar(char c) {
		return !isEmptyChar(c) && (isValidVariableChar(c) || ParseUtils.isCJKChar(c)
				|| ParseUtils.isKindOfChar(c, '&', '-', '@') || ParseUtils.isKindOfChar(c, subExtendChars));
	}

	private static boolean isValidStringLeadChar(char c) {
		return !isEmptyChar(c) && (isValidVariableChar(c) || ParseUtils.isCJKChar(c)
				|| ParseUtils.isKindOfChar(c, '\'', '"') || ParseUtils.isKindOfChar(c, extendChars));
	}

	public static int ignoreWhiteSpace(char[] charArr, int ptr) {
		return movePtr(charArr, ptr, SyntaxAnalyser::isEmptyChar);
	}

	private static int movePtr(char[] charArr, int ptr, Predicate<Character> predicate) {
		if (ptr >= charArr.length) {
			return charArr.length;
		}
		while (ptr < charArr.length && predicate.test(charArr[ptr])) {
			ptr++;
		}
		return ptr;
	}

	/**
	 * the syntax should be as follows expr -> item +expr| item - expr| item <br>
	 * item -> pvar * item| pvar / item| pvar <br>
	 * pvar -> var|-var <br>
	 * var -> (expr)|ref|num|str|variable|func <br>
	 * 
	 * 
	 * variable -> @ varName <br>
	 * 
	 * func -> & funcName(args) <br>
	 * args -> * expr,args|expr|"" <br>
	 * 
	 * 
	 * num-> ?[0-9].[0-9]*|[1-9][0-9]* <br>
	 * str->[A-Z][a-z](_ckjchar)([A-Z][a-z][0-9](_ckjchar))* <br>
	 * 
	 * varName-> [A-Z][a-z][_](_[A-Z][a-z][0-9])* <br>
	 * funcName->[A-Z][a-z][_](_[A-Z][a-z][0-9])* <br>
	 * 
	 * ref-> datasourceRef!cellRef|cellRef <br>
	 * datasourceRef->[sourceName]sheetName <br>
	 * cellRef->$colName|$colName$num|$colName${expr}|$colName${varName} <br>
	 * sourceName->varName <br>
	 * sheetName->str <br>
	 * colName-> [A-Z]([A-Z])* <br>
	 **/
	public static ASTree parseExpression(String expression) {
		String trimedStr = expression.trim();
		char[] charArr = trimedStr.toCharArray();
		AbstractNode root;
		ASTree ret = new ASTree();
		if (charArr.length == 0) {
			StringNode strNode = new StringNode();
			strNode.setVal("");
			root = strNode;
		} else {
			ParseContext ctx = new ParseContext();
			ctx.tree = ret;
			AbstractNode retNode = parseExpression(ctx, charArr, 0);
			root = retNode;
			if (retNode.getEnd() != charArr.length) {
				logger.warn("some characters  following a valid expressiong will bbe ignored");
			}
		}
		ret.setTrimedExpressionStr(trimedStr);
		ret.setRoot(root);
		return ret;
	}

	private static ExpressionNode parseExpression(ParseContext ctx, char[] statment, int start) {
		start = ignoreWhiteSpace(statment, start);
		AbstractNode node = parseItem(ctx, statment, start);
		int end = node.getEnd();
		ExpressionNode ret = new ExpressionNode();
		if (end == statment.length) {
			/* nomal end */
			ret.setLeft(node);
			ret.setStart(node.getStart());
			ret.setEnd(node.getEnd());
			ret.setNeedCompute(false);
		} else {
			end = ignoreWhiteSpace(statment, end);// allow white space
			char nextToken = statment[end];
			if (isPlusMinus(nextToken) && end != statment.length) {
				ret.setOperationCode(nextToken);
				end++;// move
				end = ignoreWhiteSpace(statment, end); // allow white space after operation
				ExpressionNode restExpr = parseExpression(ctx, statment, end);
				ret.setLeft(node);
				ret.setRight(restExpr);
				ret.setStart(node.getStart());
				ret.setEnd(restExpr.getEnd());
				ret.setNeedCompute(true);
			} else {
				ret.setLeft(node);
				ret.setStart(node.getStart());
				ret.setEnd(node.getEnd());
				ret.setNeedCompute(false);
			}
		}
		return ret;
	}

	private static ItemNode parseItem(ParseContext ctx, char[] statment, int start) {
		start = ignoreWhiteSpace(statment, start);
		AbstractNode node = parsePvar(ctx, statment, start);
		int end = node.getEnd();
		ItemNode ret = new ItemNode();
		if (end == statment.length) {
			/* nomal end */
			ret.setLeft(node);
			ret.setStart(node.getStart());
			ret.setEnd(node.getEnd());
			ret.setNeedCompute(false);
		} else {
			end = ignoreWhiteSpace(statment, end);// allow white space
			char nextToken = statment[end];
			if (isMultiply(nextToken) && end != statment.length) {
				ret.setOperationCode(nextToken);
				end++;// move
				end = ignoreWhiteSpace(statment, end); // allow white space after operation
				ItemNode restItem = parseItem(ctx, statment, end);
				ret.setLeft(node);
				ret.setRight(restItem);
				ret.setStart(node.getStart());
				ret.setEnd(restItem.getEnd());
				ret.setNeedCompute(true);
			} else {
				ret.setLeft(node);
				ret.setStart(node.getStart());
				ret.setEnd(node.getEnd());
				ret.setNeedCompute(false);
			}
		}
		return ret;
	}

	private static PVarNode parsePvar(ParseContext ctx, char[] statment, int start) {
		int ptr = start;
		ptr = ignoreWhiteSpace(statment, ptr);
		PVarNode varNode = new PVarNode();
		varNode.setStart(ptr);
		if (isNegetive(statment[ptr])) {
			ptr++;// move
			varNode.setNeedCompute(true);
		}
		if (isEmptyChar(statment[ptr])) {
			throw new ParseException("not allowed a empty folloing negative symbol");
		}
		VarNode node = parseVar(ctx, statment, ptr);
		varNode.setInnerNode(node);
		varNode.setStart(node.getStart());
		varNode.setEnd(node.getEnd());
		return varNode;
	}

	private static VarNode parseVar(ParseContext ctx, char[] statment, int start) {
		int ptr = start;
		ptr = ignoreWhiteSpace(statment, ptr);
		char firstToken = statment[ptr];
		VarNode ret = new VarNode();
		ret.setStart(ptr);
		if (isVariableLead(firstToken)) {
			AbstractNode node = parseVarable(ctx, statment, ptr);
			ptr = node.getEnd();
			ret.setValueNode(node);
			ret.setEnd(ptr);
		} else if (isReferenceLead(firstToken)) {
			AbstractNode node = parseRef(ctx, statment, ptr);
			ptr = node.getEnd();
			ret.setValueNode(node);
			ret.setEnd(ptr);
		} else if (isFunctionLead(firstToken)) {
			AbstractNode node = parseFunction(ctx, statment, ptr);
			ptr = node.getEnd();
			ret.setValueNode(node);
			ret.setEnd(ptr);
		} else if (ParseUtils.isNumberChar(firstToken)) {
			AbstractNode node = parseNumber(statment, ptr);
			ptr = node.getEnd();
			ret.setValueNode(node);
			ret.setEnd(ptr);
		} else if (ParseUtils.isOpenParentheses(firstToken)) {
			ptr++;// move
			AbstractNode node = parseExpression(ctx, statment, ptr);
			ptr = node.getEnd();
			ptr = ignoreWhiteSpace(statment, ptr);// allow white space;
			if (ParseUtils.isCloseParentheses(statment[ptr])) {
				ptr++;
				ret.setValueNode(node);
				ret.setEnd(ptr);
			} else
				throw new ParseException("Unclosed  expression need ) ");
		} else if (isValidString(firstToken)) {
			AbstractNode node = parseString(statment, ptr);
			ptr = node.getEnd();
			ret.setValueNode(node);
			ret.setEnd(ptr);
		} else
			throw new ParseException("Unsupport variable char " + firstToken);
		return ret;
	}

	private static VariableNode parseVarable(ParseContext ctx, char[] statment, int start) {
		int ptr = start;
		VariableNode ret = new VariableNode();
		ret.setStart(start);
		ptr++;// move
		StringNode stringNode = parseInnerRefName(statment, ptr);
		ret.setVariableName(stringNode.getVal());
		ret.setEnd(stringNode.getEnd());
		ctx.tree.getVaribleTable().add(stringNode.getVal());// add to variable Table;
		return ret;
	}

	private static ReferenceNode parseRef(ParseContext ctx, char[] statment, int start) {

		int ptr = ignoreWhiteSpace(statment, start);
		ReferenceNode ret = new ReferenceNode();
		ret.setStart(ptr);
		String sourceNameString = "$defaultSource";
		String sheetNameString = "$defaultSheet";

		if (ParseUtils.isOpenBracket(statment[ptr])) {
			ptr = ignoreWhiteSpace(statment, ptr + 1);// allowed white space
			StringNode sourceName = parseSourceName(statment, ptr);
			sourceNameString = sourceName.getVal();
			ptr = sourceName.getEnd();
			ptr = ignoreWhiteSpace(statment, ptr);// allowed white space
			if (!ParseUtils.isCloseBracket(statment[ptr])) {
				throw new ParseException("Illegal datasource syntax should be a close ] but find a " + statment[ptr]);
			}
			ptr++;// move
			StringNode sheetName = parseSheetName(statment, ptr);
			sheetNameString = sheetName.getVal();
			ptr = sheetName.getEnd();
			ptr = ignoreWhiteSpace(statment, ptr);// allowed white space
			if (statment[ptr] != '!') {
				throw new ParseException("Illegal datasource syntax should be a close ] but find a " + statment[ptr]);
			}
			ptr++;// move
		} else if (!isCellRefLead(statment[ptr])) {
			throw new ParseException("Illegal cell ref lead char " + statment[ptr]);
		}

		if (!isCellRefLead(statment[ptr])) {
			throw new ParseException("Illegal cell reference syntax should be a $ but find a " + statment[ptr]);
		}
		ptr++;// move
		StringNode columnName = parseColumnName(statment, ptr);
		ptr = columnName.getEnd();
		String rowName = "";
		if (isCellRefLead(statment[ptr])) {
			ptr++;// move
			if (ParseUtils.isOpenBrace(statment[ptr])) {
				ptr++;// move;
				ptr = ignoreWhiteSpace(statment, ptr);// allowed white space
				StringNode rowRef = parseInnerRefName(statment, ptr);
				ptr = rowRef.getEnd();
				rowName = rowRef.getVal();
				ctx.tree.getVaribleTable().add(rowName);
				ptr = ignoreWhiteSpace(statment, ptr);// allowed white space
				if (!ParseUtils.isCloseBrace(statment[ptr])) {
					throw new ParseException(
							"Illegal cell reference syntax should be follow a } but find a " + statment[ptr]);
				}
				ptr++;// move
			} else {
				StringNode rowIdx = parseIntegerStr(statment, ptr);
				ptr = rowIdx.getEnd();
				rowName = rowIdx.getVal();
			}
		} else if (ParseUtils.isNumber(statment[ptr])) {
			StringNode rowIdx = parseIntegerStr(statment, ptr);
			ptr = rowIdx.getEnd();
			rowName = rowIdx.getVal();
		}

		ret.setSheetName(sheetNameString);
		ctx.tree.getDataSourceTable().add(sourceNameString);
		ret.setSourceName(sourceNameString);
		ret.setColumeName(columnName.getVal());
		ret.setRowName(rowName);
		ret.setEnd(ptr);
		ret.setRefString(String.copyValueOf(statment, ret.getStart(), ret.getEnd() - ret.getStart()));
		ctx.tree.getRefTable().add(ret.getRefString());// add to variable Table;
		return ret;
	}

	private static StringNode parseSourceName(char[] statment, int start) {
		return parseInnerRefName(statment, start);
	}

	private static StringNode parseSheetName(char[] statment, int start) {
		int ptr = start;
		StringNode ret = new StringNode();
		ret.setStart(ptr);
		ptr = movePtr(statment, ptr, SyntaxAnalyser::isValidSheetNameChar);
		ret.setEnd(ptr);
		ret.setVal(String.copyValueOf(statment, ret.getStart(), ret.getEnd() - ret.getStart()));
		return ret;
	}

	private static FunctionNode parseFunction(ParseContext ctx, char[] statment, int start) {
		int ptr = ignoreWhiteSpace(statment, start);
		FunctionNode ret = new FunctionNode();
		ret.setStart(ptr);
		ptr++;// move
		StringNode stringNode = parseInnerRefName(statment, ptr);
		ptr = stringNode.getEnd();// should be a (
		ptr = ignoreWhiteSpace(statment, ptr);
		if (!ParseUtils.isOpenParentheses(statment[ptr])) {
			throw new ParseException("Illegal express of an function must a ( follow the functionName");
		}
		String functionName = stringNode.getVal();
		ret.setFunctionName(functionName);
		List<ArgumentsNode> args = parseArguments(ctx, statment, ptr + 1);
		if (args.size() != 0) {
			ret.appendArgs(args);
			ptr = args.get(args.size() - 1).getEnd();
			ptr = ignoreWhiteSpace(statment, ptr);
		} else {
			ptr = ignoreWhiteSpace(statment, ptr + 1);
		}
		if (!ParseUtils.isCloseParentheses(statment[ptr])) {
			throw new ParseException("Illegal express of an function not close need )");
		}
		ret.setEnd(ptr + 1);
		return ret;
	}

	private static List<ArgumentsNode> parseArguments(ParseContext ctx, char[] statment, int start) {
		int ptr = ignoreWhiteSpace(statment, start);
		if (ParseUtils.isCloseParentheses(statment[ptr])) {
			return Collections.emptyList();
		}
		AbstractNode node = parseExpression(ctx, statment, ptr);
		ArgumentsNode arg1 = new ArgumentsNode();
		arg1.setStart(node.getStart());
		arg1.setEnd(node.getEnd());
		arg1.setValueNode(node);
		ptr = node.getEnd();
		ptr = ignoreWhiteSpace(statment, ptr);
		if (node.getEnd() == statment.length) {
			throw new ParseException("Illegal express of an function argument not close need )");
		} else {
			if (ParseUtils.isCloseParentheses(statment[ptr])) {
				List<ArgumentsNode> x = new ArrayList<>();
				x.add(arg1);
				return x;
			} else if (ParseUtils.isComma(statment[ptr])) {
				List<ArgumentsNode> restArg = parseArguments(ctx, statment, ptr + 1);
				if (restArg.size() == 0) {
					throw new ParseException("Illegal express of an function argument need more expr");
				}
				arg1.setEnd(restArg.get(0).getEnd());
				restArg.add(0, arg1);
				return restArg;
			} else {
				throw new ParseException("Illegal express of an function argument need ','");
			}
		}
	}

	private static NumberNode parseNumber(char[] statment, int start) {
		int ptr = ignoreWhiteSpace(statment, start);
		NumberNode ret = new NumberNode();
		ret.setStart(ptr);
		ptr = movePtr(statment, ptr, ParseUtils::isNumberChar);
//		while (ptr < statment.length&& ParseUtils.isNumberChar(statment[ptr]) ) {
//			ptr++;
//		}
		ret.setEnd(ptr);
		ret.setNumbStr(String.copyValueOf(statment, ret.getStart(), ret.getEnd() - ret.getStart()));
		ret.setVal(Double.parseDouble(ret.getNumbStr()));
		return ret;
	}

	private static StringNode parseColumnName(char[] statment, int start) {
		int ptr = start;
		StringNode ret = new StringNode();
		ret.setStart(ptr);
		ptr = movePtr(statment, ptr, ParseUtils::isCapitalChar);
//		while (ptr < statment.length&& ParseUtils.isCapitalChar(statment[ptr]) ) {
//			ptr++;
//		}
		ret.setEnd(ptr);
		ret.setVal(String.copyValueOf(statment, ret.getStart(), ret.getEnd() - ret.getStart()));
		return ret;
	}

	private static StringNode parseIntegerStr(char[] statment, int start) {
		int ptr = start;
		StringNode ret = new StringNode();
		ret.setStart(ptr);
		ptr = movePtr(statment, ptr, ParseUtils::isNumber);
		ret.setEnd(ptr);
		ret.setVal(String.copyValueOf(statment, ret.getStart(), ret.getEnd() - ret.getStart()));
		return ret;
	}

	private static StringNode parseString(char[] statment, int start) {
		int ptr = ignoreWhiteSpace(statment, start);
		StringNode ret = new StringNode();
		ret.setStart(ptr);
		ptr = movePtr(statment, ptr, SyntaxAnalyser::isValidString);
		ret.setEnd(ptr);
		ret.setVal(String.copyValueOf(statment, ret.getStart(), ret.getEnd() - ret.getStart()));
		return ret;
	}

	private static StringNode parseInnerRefName(char[] statment, int start) {
		int ptr = start;
		char firstChar = statment[ptr];
		if (!isValidVariableLead(firstChar)) {
			throw new ParseException("Illegal char '" + firstChar + "' at a variable head ");
		}
		StringNode ret = new StringNode();
		ret.setStart(ptr);
		ptr = movePtr(statment, ptr, SyntaxAnalyser::isValidVariableChar);
		ret.setEnd(ptr);
		ret.setVal(String.copyValueOf(statment, ret.getStart(), ret.getEnd() - ret.getStart()));
		return ret;
	}

	/**
	 * the legal syntax should be as follows
	 * 
	 * -123 123.5 0.12345 abc Abd09ab aGD09 $A3 $AA256 $abcABDD $A$4 $A${row} &ABC()
	 * &ABC(12,$AA256) &Func1(12,$AA256) &Func1(12,$AA256,&Func2(12,$AA256))
	 **/
	public static void main(String[] args) {

		String[] testStr = {
				"12+23/4","12 + 23 / 4",
				"-123", "123.5", "0.12345", "abc", "Abd09ab", "aGD09", "$A3", "$AA256", "@abcABDD",
				"(@a+@b+@c)/3+7.5/@c",
				"$A${row}", "&ABC()", "&ABC(12,$AA256)", "&ABC(12,$AA256)", "&Func1(12,$AA256)",
				
				"&Func1(12,$AA256,&Func2(12,$AA256,12+23))",
				
				"-[fileName]sheetName!$A$2+中国人民万岁",
				"@F_JSYZ_LDSCZZ/@F_JSYZ_BNPJCYRYRS",
				"" };

		Map<String, Object> valueMap = new HashMap<>();
		valueMap.put("abcABDD", 12.5);
		valueMap.put("a", 1);
		valueMap.put("b", 2);
		valueMap.put("c", 3);
		valueMap.put("F_JSYZ_LDSCZZ",5);
		valueMap.put("F_JSYZ_BNPJCYRYRS",2.5);

		for (String str : testStr) {
			System.out.println(str);
			ASTree ast = parseExpression(str);
			System.out.println(ast.getExprValue(valueMap));
			System.out.println();
		}

	}
}
