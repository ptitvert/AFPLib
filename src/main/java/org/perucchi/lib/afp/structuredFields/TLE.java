package org.perucchi.lib.afp.structuredFields;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.perucchi.lib.Utils;
import org.perucchi.lib.afp.Constant;
import org.perucchi.lib.afp.structuredFields.triplets.AttributeValue;
import org.perucchi.lib.afp.structuredFields.triplets.FullyQualifiedName;

/**
 * @author Alessandro Perucchi
 *
 */
public class TLE {
	public final static byte[] TRIPLET = Utils.hexStringToByteArray("D3A090");
	public final static String name = "TLE";
	public final static byte[] FLAGS = Utils.hexStringToByteArray("00");
	public final static byte[] RESERVEDFIELDS = Utils.hexStringToByteArray("0000");
	private final static int MIN_LENGTH = 8;

	private String variableName = null;
	private String variableValue = null;

	private FullyQualifiedName tripletName;
	private AttributeValue tripletValue;
	private int length;

	public TLE(String name, String value) {
		this();
		setVariableName(name);
		setVariableValue(value);
	}

	public TLE() {
		super();
	}

	/**
	 * @return the variableName
	 */
	public String getVariableName() {
		if (variableName==null) {
			variableName=tripletName.getConvertedName();
		}
		return variableName;
	}

	/**
	 * @param variableName
	 *            the variableName to set
	 */
	public void setVariableName(String variableName) {
		this.variableName = variableName;
		tripletName = new FullyQualifiedName();
		tripletName.setType(FullyQualifiedName.TYPE.ATTRIBUTE_GID);
		tripletName.setFormat(FullyQualifiedName.FORMAT.STRING);
		tripletName.setName(variableName);
		setLength();
	}

	/**
	 * @return the variableValue
	 */
	public String getVariableValue() {
		if (variableValue==null) {
			variableValue=tripletValue.getConvertedValue();
		}
		return variableValue;
	}

	/**
	 * @param variableValue
	 *            the variableValue to set
	 */
	public void setVariableValue(String variableValue) {
		this.variableValue = variableValue;
		tripletValue = new AttributeValue(variableValue);
		setLength();
	}

	public byte[] getStructuredField() {
		byte[] answer = new byte[getLength() + 1];
		ByteBuffer answerBuffer = ByteBuffer.wrap(answer);
		answerBuffer.put(Constant.MAIN_PREFIX);
		answerBuffer.put(getByteArrayLength());
		answerBuffer.put(TRIPLET);
		answerBuffer.put(FLAGS);
		answerBuffer.put(RESERVEDFIELDS);
		answerBuffer.put(tripletName.getTriplet());
		answerBuffer.put(tripletValue.getTriplet());

		return answer;
	}

	/**
	 * @param data
	 */
	public void setStructuredField(byte[] data) {

		byte[] temp = Arrays.copyOfRange(data, 0, 3);
		if (Arrays.equals(temp, TRIPLET)) {
			int nameLength = data[6] + 6;
			temp = Arrays.copyOfRange(data, 7, nameLength);
			tripletName = new FullyQualifiedName();
			tripletName.setTriplet(temp);
			temp = Arrays.copyOfRange(data, nameLength + 1, data.length);
			tripletValue = new AttributeValue();
			tripletValue.setTriplet(temp);
			
			setLength();

//			for (byte singleByte : getStructuredField()) {
//				System.out.print(String.format("%02X_", singleByte));
//			}
//			System.out.println();
		}
	}

	public static void main(String[] args) {
		byte[] getBlock = new TLE("LIST_ART_CD", "A-Post").getStructuredField();
		byte counting = -3;
		for (@SuppressWarnings("unused")
		byte myByte : getBlock) {
			System.out.print(String.format("%02d ", counting++));
		}
		System.out.println();
		for (byte myByte : getBlock) {
			System.out.print(String.format("%02X ", myByte));
		}
		System.out.println();
	}

	private byte[] getByteArrayLength() {
		byte[] answer = new byte[2];
		answer[0] = (byte) ((getLength() >> 8) & 0xff);
		answer[1] = (byte) (getLength() & 0xff);
		return answer;
	}

	public int getLength() {
		return this.length;
	}

	private void setLength() {
		this.length = MIN_LENGTH;
		if (tripletName != null) {
			this.length += tripletName.getLength();
		}
		if (tripletValue != null) {
			this.length += tripletValue.getLength();
		}
	}
}
