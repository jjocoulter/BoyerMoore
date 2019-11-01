package stringSearcher;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Implements the Boyer Moore search algorithm.
 *
 * @author Josh Coulter
 * @version 31/10/2019
 */
public class BoyerMooreSearcher extends StringSearcher {

    private Hashtable<Character, Integer[]> negativeShift = new Hashtable<>();
    private Integer[] goodSuffixLocations;

    public BoyerMooreSearcher(char[] string) {
        super(string);
    }

    public BoyerMooreSearcher(String string) {
        super(string);
    }

    /**
     * Does the substring occur in the superstring?
     *
     * @param superstring the superstring to be searched
     * @return the index of the leftmost occurrence of the substring in the superstring, if there is such an occurrence
     * @throws NotFound if the substring does not occur in the superstring
     */
    public int occursIn(char[] superstring) throws NotFound {
        generateNegativeShiftTable();
        generateGoodSuffixTable();
        boolean found = false;
        int offset = 0;
        while (!found && offset <= (superstring.length - getString().length)) {
            for (int i = getString().length - 1; i >= 0; i--) {
                if (getString()[i] == superstring[i + offset] && i != 0) {
                } else if (getString()[i] != superstring[i + offset]) {
                    int neg;
                    if (negativeShift.containsKey(superstring[i + offset])) {
                        neg = checkNegativeShift(superstring[i + offset], i);
                    } else {
                        neg = negativeShift.get('0')[i];
                    }
                    int good = (i < getString().length-1) ? checkGoodSuffix(i+1) : 0;
                    offset += ((neg > good) ? neg : good);
                    break;
                } else {
                    found = true;
                }
            }
        }
        if (found) {
            return offset;
        } else {
            throw new NotFound();
        }

    }

    /**
     * Generate the negative shift hash table for all unique characters and the wildcard.
     */
    private void generateNegativeShiftTable() {
        /**
         * Find all unique characters in the substring and create a separate array in the hashtable based on where the
         * next element of this character is in the substring.
         * Create a wildcard element ('0') to account for the given character in the superstring not being present in
         * the substring.
         */
        List<Character> uniqueChars = new ArrayList<>();
        for (char c : getString()) { //generate list of all unique chars
            if (!uniqueChars.contains(c)) uniqueChars.add(c);
        }

        for (char c : uniqueChars) { //create goodSuffixLocations for each unique char
            Integer[] locations = new Integer[getString().length];
            for (int index = 0; index < getString().length; index++) {
                if (getString()[index] == c) {
                    locations[index] = 0;
                } else {
                    int firstIndex = 1 - getString().length;
                    for (int i = getString().length - 1; i >= 0; i--) {
                        if (getString()[i] == c) {
                            locations[index] = firstIndex + index;
                            break;
                        } else {
                            firstIndex++;
                        }
                    }
                }
            }
            negativeShift.put(c, locations);
        }
        Integer[] locations = new Integer[getString().length]; //create wildcard array
        for (int i = 0; i < getString().length; i++) {
            locations[i] = i + 1;
        }
        negativeShift.put('0', locations);
    }

    /**
     * Generate the good suffix table for the given substring.
     */
    private void generateGoodSuffixTable() {
        /**
         * Use the substring to calculate the best movement amounts from an increasing substring length (starting from
         * right hand element).
         */
        String string = new String(getString()); //convert substring char[] to string
        goodSuffixLocations = new Integer[string.length()];
        int bestMove = 0;
        int subString = string.length() - 1;
        for (int i = string.length() - 1; i >= 0; i--) {
            String current = string.substring(0, i); //current substring to check
            int lastIndexOf = current.lastIndexOf(string.substring(subString));
            if (lastIndexOf != -1) { //lastIndexOf returns -1 if the string doesn't contain the substring
                goodSuffixLocations[i] = subString - lastIndexOf;
                bestMove = goodSuffixLocations[i];
            } else {
                goodSuffixLocations[i] = bestMove;
            }
            subString--;
        }
    }

    /**
     * Check the movement from using the negative shift table.
     * @param c the current character in the superstring (or wildcard if it doesn't exist in substring)
     * @param pos the position being checked in the substring
     * @return the movement amount from the hashtable
     */
    private int checkNegativeShift(char c, int pos) {
        return negativeShift.get(c)[pos];
    }

    /**
     * Check the movement from using the good suffix table.
     * @param pos the current position being checked in the substring
     * @return the movement amount from the suffix table
     */
    private int checkGoodSuffix(int pos) {
        return goodSuffixLocations[pos];
    }
}
