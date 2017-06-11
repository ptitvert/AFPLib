package org.perucchi.lib.afp.structuredFields.triplets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.perucchi.lib.afp.utils.Utils;

/**
 * @author Alessandro Perucchi
 */
public class FullyQualifiedName {
	public static final byte[]	TID				= Utils.hexStringToByteArray("02");
	private byte[]				type			= null;
	private byte[]				format			= null;
	private byte[]				name			= null;
	private String				convertedName	= null;

	private final static int	MIN_LENGTH		= 4;
	private int					length			= MIN_LENGTH;

	public static enum FORMAT {
		STRING("00"), //
		OID("10"), //
		URL("20");

		private final byte[] internalValue;

		FORMAT(String value) {
			this.internalValue = Utils.hexStringToByteArray(value);
		}

		/**
		 * @return the internalValue
		 */
		public byte[] getValue() {
			return internalValue;
		}

		public static FORMAT findEnum(byte value) {
			for (FORMAT type : FORMAT.values()) {
				if (value == type.getValue()[0]) {
					return type;
				}
			}
			return null;
		}
	}

	public static enum TYPE {

		REPLACE_FIRST_GID_NAME("01"), //
		FONT_FAMILY_NAME("07"),  //
		FONT_TYPEFACE_NAME("08"),  //
		MODCA_RES_HIERARCHY_REF("09"), // 
		BEGIN_RES_GROUP_REF("0A"),  //
		ATTRIBUTE_GID("0B"),  //
		PROCESS_ELEMENT_GID("0C"), // 
		BEGIN_PAGE_GROUP_REF("0D"),  //
		MEDIA_TYPE_REF("11"),  //
		MEDIA_DEST_REF("12"),  //
		CMR_REF("41"),  //
		DATAOBJ_FONT_BASE_FONT_ID("6E"), // 
		DATAOBJ_FONT_LINKED_FONT_ID("7E"),  //
		BEGIN_DOCUMENT_REF("83"),  //
		RES_OBJ_REF("84"),  //
		CODE_PAGE_NAME_REF("85"), // 
		FONT_CHARSET_NAME_REF("86"), //
		BEGIN_PAGE_REF("87"), //
		BEGIN_MEDIUM_MAP_REF("8D"), //
		CODED_FONT_NAME_REF("8E"), //
		BEGIN_DOCUMENT_INDEX_REF("98"), //
		BEGIN_OVERLAY_REF("B0"), //
		DATAOBJ_INTERNAL_RES_REF("BE"), //
		INDEX_ELEMENT_GID("CA"), //
		OTHER_OBJECT_DATA_REF("CE"), //
		DATAOBJ_EXTERNAL_RES_REF("DE");
		;

		private final byte[] internalValue;

		TYPE(String value) {
			this.internalValue = Utils.hexStringToByteArray(value);
		}

		/**
		 * @return the internalValue
		 */
		public byte[] getValue() {
			return internalValue;
		}

		public static TYPE findEnum(byte value) {
			for (TYPE type : TYPE.values()) {
				if (value == type.getValue()[0]) {
					return type;
				}
			}
			return null;
		}
	};

	public FullyQualifiedName() {
		super();
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return the type
	 */
	public byte[] getType() {
		return this.type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(TYPE type) {
		this.type = type.getValue();
	}

	/**
	 * @return the format
	 */
	public byte[] getFormat() {
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat(FORMAT format) {
		this.format = format.getValue();
	}

	/**
	 * @return the name
	 */
	public byte[] getName() {
		return name;
	}

	public String getConvertedName() {
		if (convertedName == null) {
			try {
				setConvertedName(new String(getName(), "Cp500"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return convertedName;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		try {
			this.name = name.getBytes("Cp500");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		setLength();
	}

	private void setConvertedName(String name) {
		this.convertedName = name;
	}

	private void setLength() {
		this.length = MIN_LENGTH + this.name.length;
	}

	public byte[] getTriplet() {
		byte[] answer = new byte[getLength()];
		ByteBuffer answerBuffer = ByteBuffer.wrap(answer);
		answerBuffer.put((byte) (getLength() & 0xff));
		answerBuffer.put(TID);
		answerBuffer.put(getType());
		answerBuffer.put(getFormat());
		answerBuffer.put(getName());

		return answer;
	}

	public void setTriplet(byte[] data) {
		if (data == null) {
			return;
		}
		if (data[0] == TID[0]) {
			setType(TYPE.findEnum(data[1]));
			setFormat(FORMAT.findEnum(data[2]));
			this.name = new byte[data.length - MIN_LENGTH + 1];
			for (int i = 0; i < data.length - MIN_LENGTH + 1; i++) {
				this.name[i] = data[i + MIN_LENGTH - 1];
			}
			setLength();
		}
	}
}
