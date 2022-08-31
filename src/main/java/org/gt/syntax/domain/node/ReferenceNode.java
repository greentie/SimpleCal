package org.gt.syntax.domain.node;

import java.util.Map;

import org.gt.syntax.domain.value.ValueObject;
import org.gt.syntax.domain.value.ValueOperationUtil;

public class ReferenceNode extends AbstractNode {

	private String refString;

	private String sourceName;
	private String sheetName;
	private String columeName;
	private String rowName;

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getColumeName() {
		return columeName;
	}

	public void setColumeName(String columeName) {
		this.columeName = columeName;
	}

	public String getRowName() {
		return rowName;
	}

	public void setRowName(String rowName) {
		this.rowName = rowName;
	}

	public String getRefString() {
		return refString;
	}

	public void setRefString(String refString) {
		this.refString = refString;
	}

	public ReferenceNode() {
		super();
		type = NodeType.ref;
	}
	

	public ValueObject<?> getValue(Map<String,Object> valueMapping) {
		return ValueOperationUtil.getReferencValue(this);
	}
	
}