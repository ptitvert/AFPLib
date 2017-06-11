package org.perucchi.lib.afp.structuredFields.triplets;

import java.nio.ByteBuffer;

import org.perucchi.lib.afp.utils.Utils;

/**
 * @author Alessandro Perucchi
 *
 */
public class AttributeQualifier {
	public static final byte[] TID = Utils.hexStringToByteArray("80");
	private static final int LENGTH = 10;

	private long seqNum = 0;
	private long levNum = 0;

	public AttributeQualifier() {
		super();
	}

	public AttributeQualifier(long seqNum, long levNum) {
		this();
		setSeqNum(seqNum);
		setLevNum(levNum);
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return LENGTH;
	}

	public byte[] getTriplet() {
		byte[] answer = new byte[getLength()];
		ByteBuffer answerBuffer = ByteBuffer.wrap(answer);
		answerBuffer.put((byte) (getLength() & 0xff));
		answerBuffer.put(TID);
		answerBuffer.put((byte) ((getSeqNum()&0xFF000000)>>24));
		answerBuffer.put((byte) ((getSeqNum()&0x00FF0000)>>16));
		answerBuffer.put((byte) ((getSeqNum()&0x0000FF00)>>8));
		answerBuffer.put((byte) ((getSeqNum()&0x000000FF)));
		
		answerBuffer.put((byte) ((getLevNum()&0xFF000000)>>24));
		answerBuffer.put((byte) ((getLevNum()&0x00FF0000)>>16));
		answerBuffer.put((byte) ((getLevNum()&0x0000FF00)>>8));
		answerBuffer.put((byte) ((getLevNum()&0x000000FF)));

		return answer;
	}

	/**
	 * @return the seqNum
	 */
	public long getSeqNum() {
		return seqNum;
	}

	/**
	 * @param seqNum the seqNum to set
	 */
	public void setSeqNum(long seqNum) {
		this.seqNum = seqNum;
	}

	/**
	 * @return the levNum
	 */
	public long getLevNum() {
		return levNum;
	}

	/**
	 * @param levNum the levNum to set
	 */
	public void setLevNum(long levNum) {
		this.levNum = levNum;
	}
}
