package org.perucchi.lib.afp.structuredFields.triplets;

public class TestLong {

	public static void main(String[] args) {
		long a=0x7fff9988;
		System.out.println((a&0xFF));
		System.out.println((a&0xFF00)>>8);
		System.out.println((a&0xFF0000)>>16);
		System.out.println((a&0xFF000000)>>24);
	}

}
