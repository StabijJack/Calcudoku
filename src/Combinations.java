import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

class Combinations {
    private final int startNumber;
    private final int endNumber;
    private final int lengthOfCombination;
    private final int maxOccurrenceOfNumbers;
    private ArrayList<ArrayList<Integer>> combinations;
    private int[] possibleNumbers;
    private int[] combinationPositions;

    public Combinations(int startNumber, int endNumber, int lengthOfCombination) {
        this.startNumber = startNumber;
        this.endNumber = endNumber;
        this.lengthOfCombination = lengthOfCombination;
        this.maxOccurrenceOfNumbers = 1;
        createCombinations();
    }

    public Combinations(int startNumber, int endNumber, int lengthOfCombination, int maxOccurrenceOfNumbers) {
        this.startNumber = startNumber;
        this.endNumber = endNumber;
        this.lengthOfCombination = lengthOfCombination;
        this.maxOccurrenceOfNumbers = maxOccurrenceOfNumbers;
        createCombinations();
    }

    private void createCombinations() {
        combinations = new ArrayList<>();
        createPossibleNumbers();
        initCombinationPositionProgress();
        do {
            ArrayList<Integer> combination = new ArrayList<>();
            for (int i = 0; i < lengthOfCombination; i++) {
                combination.add(possibleNumbers[combinationPositions[i]]);
            }
            combination.sort(Collections.reverseOrder());
            if (maxOccurrenceOfNumbersIsOke(combination) && combinationIsUnique(combination)) {
                combinations.add(combination);
            }
            getNextCombinationPosition();
        }
        while (!endCombinationPositionProgress());
        combinations.sort(compareByAllElements());
    }

    private void createPossibleNumbers() {
        int possibleNumbersLength = endNumber - startNumber + 1;
        possibleNumbers = new int[possibleNumbersLength];
        for (int i = 0; i < possibleNumbersLength; i++) {
            possibleNumbers[i] = i + startNumber;
        }
    }

    private void initCombinationPositionProgress() {
        combinationPositions = new int[lengthOfCombination];
    }

    private void getNextCombinationPosition() {
        combinationPositions[lengthOfCombination - 1] += 1;
        for (int i = lengthOfCombination - 1; i > 0; i--) {
            if (combinationPositions[i] >= possibleNumbers.length) {
                combinationPositions[i] = 0;
                combinationPositions[i - 1] += 1;
            } else break;
        }
    }

    @Contract(pure = true)
    private boolean endCombinationPositionProgress() {
        return combinationPositions[0] >= possibleNumbers.length;
    }

    private boolean maxOccurrenceOfNumbersIsOke(ArrayList<Integer> newCombination) {
        for (int possibleNumber : possibleNumbers) {
            int occurrenceOfNumber = 0;
            for (Integer i : newCombination) {
                if (i == possibleNumber) occurrenceOfNumber += 1;
                if (occurrenceOfNumber > maxOccurrenceOfNumbers) return false;
            }
        }
        return true;
    }

    private boolean combinationIsUnique(ArrayList newCombination) {
        for (ArrayList combination : combinations) {
            boolean matches = true;
            for (int i = 0; i < lengthOfCombination; i++) {
                if (combination.get(i) != newCombination.get(i)) {
                    matches = false;
                    break;
                }
            }
            if (matches) return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("StartNumber         = ");
        s.append(startNumber);
        s.append(System.getProperty("line.separator"));
        s.append("EndNumber           = ");
        s.append(endNumber);
        s.append(System.getProperty("line.separator"));
        s.append("LengthOfCombination = ");
        s.append(lengthOfCombination);
        s.append(System.getProperty("line.separator"));
        s.append("maxOccurrenceOfNumbers= ");
        s.append(maxOccurrenceOfNumbers);
        s.append(System.getProperty("line.separator"));
        s.append("PossibleNumbers     = ");
        s.append(Arrays.toString(possibleNumbers));
        s.append(System.getProperty("line.separator"));
        s.append("Combinations        = ");
        s.append(System.getProperty("line.separator"));
        for (ArrayList<Integer> combination : combinations) {
            s.append(combination.toString());
            s.append(System.getProperty("line.separator"));
        }
        return s.toString();
    }

    public int[] getPossibleNumbers() {
        return possibleNumbers;
    }

    public ArrayList<ArrayList<Integer>> getCombinations() {
        return combinations;
    }

    private Comparator<ArrayList<Integer>> compareByAllElements() {
        return (s1, s2) -> {
            for (int i = 0; i < s1.size(); i++) {
                if (!s1.get(i).equals(s2.get(i)))
                    return s1.get(i).compareTo(s2.get(i));
            }
            return s1.get(0).compareTo(s2.get(0));
        };
    }
//    private Comparator<ArrayList<Integer>> compareByAllElements() { identical as above
//        return new Comparator<>() {
//            @Override
//            public int compare(ArrayList<Integer> s1, ArrayList<Integer> s2) {
//                for (int i = 0; i < s1.size(); i++) {
//                    if (!s1.get(i).equals(s2.get(i)))
//                        return s1.get(i).compareTo(s2.get(i));
//                }
//                return s1.get(0).compareTo(s2.get(0));
//            }
//        };
//    }

}
