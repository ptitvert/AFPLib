/**
 * 
 */
package org.perucchi.lib;

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
