package org.perucchi.lib.afp.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Bytes {

	/**
	 * @param fileInput
	 */
	public static Byte readSingleByte(FileInputStream fileInput) {
		byte[] singleByteArray = new byte[1];
		try {
			fileInput.read(singleByteArray);
			return singleByteArray[0];
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * @param fileInput
	 */
	public static Integer readSingleInt(FileInputStream fileInput) {
		byte[] singleIntArray = new byte[2];
		int answer = 0;
		try {
			fileInput.read(singleIntArray);
			answer = getUnsignedByte(singleIntArray[1]) | getUnsignedByte(singleIntArray[0]) << 8;
			return answer;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * @param fileInput
	 */
	public static void writeSingleByte(FileOutputStream fileOutput, Byte singleByteArray) {
		try {
			fileOutput.write(singleByteArray);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * @param fileInput
	 */
	public static void writeSingleInt(FileOutputStream fileOutput, int integerToWrite) {
		byte[] singleIntArray = new byte[2];
		singleIntArray[0] = (byte) (integerToWrite >> 8);
		singleIntArray[1] = (byte) (integerToWrite & 0xff);
		try {
			fileOutput.write(singleIntArray);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * @param getBlock
	 * @return
	 */
	public static int getUnsignedByte(byte getBlock) {
		return getBlock & 0xff;
	}
	
	/**
	 * @param getBlock
	 * @return
	 */
	public static int getUnsignedInt(byte[] getBlock) {
		return (getUnsignedByte(getBlock[0]) << 8) | getUnsignedByte(getBlock[1]);
	}

}
