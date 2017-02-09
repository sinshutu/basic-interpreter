package interpreter;

public class ValueImpl implements Value {

	private ValueType valueType = null;
	private String sValue = "";
	private int iValue = 0;
	private double dValue = 0;
	private boolean bValue = false;

	public ValueImpl() {
		this.valueType = ValueType.VOID;
	}

	public ValueImpl(String value) {
		this.sValue = value;
		this.valueType = ValueType.STRING;
	}

	public ValueImpl(int value) {
		this.iValue = value;
		this.valueType = ValueType.INTEGER;
	}

	public ValueImpl(double value) {
		this.dValue = value;
		this.valueType = ValueType.DOUBLE;
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
		return this.dValue;
	}

	@Override
	public boolean getBValue() {
		return this.bValue;
	}

	@Override
	public ValueType getType() {
		return this.valueType;
	}

	@Override
	public String toString() {
		switch (this.valueType) {
		case VOID:
			return null;
		case STRING:
			return this.getSValue();
		case BOOL:
			return this.getBValue() + "";
		case INTEGER:
			return this.getIValue() + "";
		case DOUBLE:
			return this.getDValue() + "";
		default:
			System.out.println("undefined value type");
			return null;
		}
	}

}
