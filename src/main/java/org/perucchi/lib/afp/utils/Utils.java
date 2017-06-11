/**
 * 
 */
package org.perucchi.lib.afp.utils;

import javax.xml.bind.DatatypeConverter;

/**
 * @author Alessandro Perucchi
 *
 */
public class Utils {

	public static byte[] hexStringToByteArray(String string) {
		return DatatypeConverter.parseHexBinary(string);
	}

}
