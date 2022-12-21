package cs321.create;

/**
 * Utility methods dealing with DNA sequences and its compact representation as long variables.
 */
public class SequenceUtils
{

	/**
	 * Converts a String to a long value based on a binary representation where
	 * 'A' = 00, 'T' = 11, 'C' = 01, and 'G' = 10. Note: this method does not care
	 * if input string is uppercase or lowercase or a mix.
	 * @param DNAString	String formatted with only characters A, T, C, or G (or lowercase equivalent)
	 * @return long value converted from binary as described above
	 */
	public static long DNAStringToLong(String DNAString)  {
		String binaryString = "";
		for (int i = 0; i < DNAString.length(); i++) {
			switch(DNAString.charAt(i)){
				case 'a':
				case 'A':
					binaryString += "00";
					break;
				case 't':
				case 'T':
					binaryString += "11";
					break;
				case 'c':
				case 'C':
					binaryString += "01";
					break;
				case 'g':
				case 'G':
					binaryString += "10";
					break;
			}
		}
		long sequence = Long.parseLong(binaryString,2);
		return sequence;
	}

	/**
	 * Takes a long value representing a DNA sequence of length seqLength. First converts
	 * sequence to its binary representation using Long.toBinaryString(). Then adds leading
	 * zeroes as necessary, which is required to properly include any 'A' bases at the start
	 * of the sequence. Then parses this binary string and builds a String using the following:
	 * "00" = 'a', "11" = 't', "01" = 'c', and "10" = 'g'
	 * @param sequence long representation of DNA sequence
	 * @param seqLength int number of chars in DNA sequence
	 * @return String representation of DNA sequence
	 */
	public static String longToDNAString(long sequence, int seqLength) {
		String binaryString = Long.toBinaryString(sequence);
		if (binaryString.length() / 2 != seqLength) {
			int leadingZeroes = (seqLength*2) - binaryString.length();
			binaryString = "";
			for (int i = 0; i < leadingZeroes; i++) {
				binaryString += "0";
			}
			binaryString += Long.toBinaryString(sequence);
		}
		String DNAString = "";
		for (int i = 0; i < binaryString.length(); i += 2) {
			switch(binaryString.substring(i,i+2)) {
				case "00":
					DNAString += "a";
					break;
				case "11":
					DNAString += "t";
					break;
				case "01":
					DNAString += "c";
					break;
				case "10":
					DNAString += "g";
					break;
			}
		}
		return DNAString;
	}

	/**
	 * Generates the complement of a DNA sequence, given in its long representation as a parameter.
	 * DNA complement is such that "A" and "T" swap, and "C" and "G" swap. Because the binary
	 * representation of these bases was chosen so that complements are also binary complements, we
	 * can easily find the complement by flipping all bits that are included in the sequence, which
	 * is calculated by seqLength*2. This procedure is equivalent to subtracting sequence from the
	 * maximum value possible given seqLength*2 bits, or (2^(seqLength*2))-1. Thus, the final
	 * formula used is (((2^(seqLength*2))-1)-sequence).
	 * Example: sequence = 299, seqLength = 5. Sequence length 5 means 10 bits are used. The max
	 * value achievable with 10 bits is 1023. 1023 - 299 = 724, which is the correct complement.
	 * (299 --> caggt  &  724 --> gtcca)
	 *
	 * @param sequence long representation of DNA sequence
	 * @param seqLength integer value representing length of DNA sequence in chars
	 * @return long value of sequence's complement
	 */
	public static long getComplement(long sequence, int seqLength) {
		long complement = ( (long) Math.pow( 2,(seqLength*2) ) - 1) - sequence;
		return complement;
	}

}
