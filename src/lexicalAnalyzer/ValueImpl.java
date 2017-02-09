package lexicalAnalyzer;

public class ValueImpl implements Value {

	private ValueType valueType = null;
	private String sValue = "";
	private int iValue = 0;
	private boolean bValue = false;
	
	public ValueImpl(String value) {
		this.sValue = value;
		this.valueType = ValueType.STRING;
	}

	public ValueImpl(int value) {
		this.iValue = value;
		this.valueType = ValueType.INTEGER;
	}

	public ValueImpl(boolean value) {
		this.bValue = value;
		this.valueType = ValueType.BOOL;
	}

	@Override
	public String getSValue() {
		return this.sValue;
	}

	@Override
	public int getIValue() {
		return this.iValue;
	}

	@Override
	public double getDValue() {
		return 0;
	}

	@Override
	public boolean getBValue() {
		return this.bValue;
	}

	@Override
	public ValueType getType() {
		return this.valueType;
	}

}
