package org.perucchi.lib.afp.structuredFields.triplets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.perucchi.lib.Utils;

/**
 * @author Alessandro Perucchi
 *
 */
public class AttributeValue {
	public static final byte[] TID = Utils.hexStringToByteArray("36");
	private static final byte[] RESERVED = Utils.hexStringToByteArray("0000");
	private static final int MIN_LENGTH = 4;
	private int length = MIN_LENGTH;

	private byte[] value = null;
	private String convertedValue = null;

	public AttributeValue() {
		super();
	}

	public AttributeValue(String variableValue) {
		this();
		setValue(variableValue);
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return the name
	 */
	public byte[] getValue() {
		return this.value;
	}

	public String getConvertedValue() {
		if (this.convertedValue == null) {
			try {
				setConvertedValue(new String(getValue(), "Cp500"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return this.convertedValue;
	}

	private void setConvertedValue(String string) {
		this.convertedValue = string;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setValue(String value) {
		try {
			this.value = value.getBytes("Cp500");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		setLength();
	}

	private void setLength() {
		this.length = MIN_LENGTH + this.value.length;
	}

	public byte[] getTriplet() {
		byte[] answer = new byte[getLength()];
		ByteBuffer answerBuffer = ByteBuffer.wrap(answer);
		answerBuffer.put((byte) (getLength() & 0xff));
		answerBuffer.put(TID);
		answerBuffer.put(RESERVED);
		answerBuffer.put(getValue());

		return answer;
	}

	public void setTriplet(byte[] data) {
		if (data == null) {
			return;
		}
		if (data[0] == TID[0] && data[1] == 0 && data[2] == 0) {
			this.value = new byte[data.length - MIN_LENGTH + 1];
			for (int i = 0; i < data.length - MIN_LENGTH + 1; i++) {
				this.value[i] = data[i + MIN_LENGTH - 1];
			}
			setLength();
		}
	}
}
