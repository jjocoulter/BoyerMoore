package stringSearcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class BoyerMooreSearcher extends StringSearcher {

    private Hashtable<Character, Integer[]> negativeShift = new Hashtable<>();
    private Integer[] goodSuffixLocations;

    public BoyerMooreSearcher(char[] string) {
        super(string);
    }

    public BoyerMooreSearcher(String string) {
        super(string);
    }

    public int occursIn(char[] superstring) throws NotFound {
        generateNegativeShiftTable();
        generateGoodSuffixTable();
        negativeShift.forEach((key, value) -> System.out.println("char: " + key + "list: " + Arrays.toString(value)));
        System.out.println("Good suffix: " + Arrays.toString(goodSuffixLocations));
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
                    int good = checkGoodSuffix(i);
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

    private void generateNegativeShiftTable() {
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

    private void generateGoodSuffixTable() {
        String string = new String(getString()); //convert substring char[] to string
        goodSuffixLocations = new Integer[string.length()];
        int bestMove = 0;
        int subString = string.length() - 1;
        for (int i = string.length() - 1; i >= 0; i--) {
            String current = string.substring(0, i); //current substring to check
            System.out.println("current: " + current);
            System.out.println("sub: " + string.substring(subString));
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

    private int checkNegativeShift(char c, int pos) {
        return negativeShift.get(c)[pos];
    }

    private int checkGoodSuffix(int pos) {
        return goodSuffixLocations[pos];
    }
}
