package org.perucchi.lib.afp.structuredFields;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.perucchi.lib.afp.Constant;
import org.perucchi.lib.afp.structuredFields.triplets.AttributeQualifier;
import org.perucchi.lib.afp.structuredFields.triplets.AttributeValue;
import org.perucchi.lib.afp.structuredFields.triplets.FullyQualifiedName;
import org.perucchi.lib.afp.utils.Utils;

/**
 * @author Alessandro Perucchi
 */

/*
 * 3 TLE Tag Logical Element 0058 D3A090 TLE Fully Qualified Name Triplet (02) TLE 0B Attribute Name
 * TLE Name = 'aa' TLE Attribute Value Triplet (36) TLE Value = ' OPERAZIONI FUORI BORSA' TLE
 * Attribute Qualifier Triplet (80) TLE sequence number = 0 (0) TLE level number = 2147483647
 * (7FFFFFFF)
 */
public class TLE {
	public final static byte[]	TRIPLET			= Utils.hexStringToByteArray("D3A090");
	public final static String	name			= "TLE";
	public final static byte[]	FLAGS			= Utils.hexStringToByteArray("00");
	public final static byte[]	RESERVEDFIELDS	= Utils.hexStringToByteArray("0000");
	private final static int	MIN_LENGTH		= 8;

	private String				variableName	= null;
	private String				variableValue	= null;
	private long				sequenceNumber	= 0;
	private long				levelNumber		= 0;

	private FullyQualifiedName	tripletName;
	private AttributeValue		tripletValue;
	private AttributeQualifier	tripletAttQua;
	private int					length;

	public TLE(String name, String value, long sequenceNumber, long levelNumber) {
		this(name, value);
		setSequenceNumber(sequenceNumber);
		setLevelNumber(levelNumber);
		setAttributeQualifier();
	}

	public TLE(String name, String value) {
		this();
		setVariableName(name);
		setVariableValue(value);
	}

	public TLE() {
		super();
	}

	public void setAttributeQualifier() {
		tripletAttQua = new AttributeQualifier();
		tripletAttQua.setSeqNum(getSequenceNumber());
		tripletAttQua.setLevNum(getLevelNumber());
		setLength();
	}

	/**
	 * @return the variableName
	 */
	public String getVariableName() {
		if (variableName == null) {
			variableName = tripletName.getConvertedName();
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
		if (variableValue == null) {
			variableValue = tripletValue.getConvertedValue();
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
		if (hasAttributeQualifier()) {
			answerBuffer.put(tripletAttQua.getTriplet());
		}
		return answer;
	}

	public boolean hasAttributeQualifier() {
		return tripletAttQua != null;
	}

	/**
	 * @param data
	 */
	public void setStructuredField(byte[] data) {

		byte[] temp = Arrays.copyOfRange(data, 0, 3);
		if (Arrays.equals(temp, TRIPLET)) {
			int dataLength = data.length;
			int nameLength = data[6] + 6;
			temp = Arrays.copyOfRange(data, 7, nameLength);
			tripletName = new FullyQualifiedName();
			tripletName.setTriplet(temp);
			if (data[dataLength - 10] == (byte) 0x0a && data[dataLength - 9] == (byte) 0x80) {
				temp = Arrays.copyOfRange(data, nameLength + 1, data.length - 10);

				long segNum = data[dataLength - 5];
//				System.out.println("seg 5=" + ((long) segNum));
				segNum += (data[dataLength - 6] << 8);
//				System.out.println("seg 6=" + ((long) segNum));
				segNum += (data[dataLength - 7] << 16);
//				System.out.println("seg 7=" + ((long) segNum));
				segNum += (data[dataLength - 8] << 24);
//				System.out.println("seg 8=" + ((long) segNum));
				setSequenceNumber(segNum);

				long levNum = data[dataLength - 1];
//				System.out.println("lev 1=" + ((long) levNum));
				levNum += (data[dataLength - 2] << 8);
//				System.out.println("lev 2=" + ((long) levNum));
				levNum += (data[dataLength - 3] << 16);
//				System.out.println("lev 3=" + ((long) levNum));
				levNum += (data[dataLength - 4] << 24);
//				System.out.println("lev 4=" + ((long) levNum));
				setLevelNumber(levNum);

				setAttributeQualifier();
			} else {
				temp = Arrays.copyOfRange(data, nameLength + 1, data.length);
			}
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
		if (tripletAttQua != null) {
			this.length += tripletAttQua.getLength();
		}
	}

	/**
	 * @return the sequenceNumber
	 */
	public long getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber
	 *            the sequenceNumber to set
	 */
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @return the levelNumber
	 */
	public long getLevelNumber() {
		return levelNumber;
	}

	/**
	 * @param levelNumber
	 *            the levelNumber to set
	 */
	public void setLevelNumber(long levelNumber) {
		this.levelNumber = levelNumber;
	}
}
