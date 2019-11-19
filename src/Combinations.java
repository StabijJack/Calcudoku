import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;

class Combinations {
    private final int startNumber;
    private final int endNumber;
    private final int lengthOfCombination;
    private final int maxOccurensOfNumbers;
    ArrayList<int[]> combinations;
    private int[] possibleNumbers;
    private int[] combinationPositions;

    public Combinations(int startNumber, int endNumber, int lengthOfCombination) {
        this.startNumber = startNumber;
        this.endNumber = endNumber;
        this.lengthOfCombination = lengthOfCombination;
        this.maxOccurensOfNumbers = 1;
        createCombinations();
    }

    public Combinations(int startNumber, int endNumber, int lengthOfCombination, int maxOccurensOfNumbers) {
        this.startNumber = startNumber;
        this.endNumber = endNumber;
        this.lengthOfCombination = lengthOfCombination;
        this.maxOccurensOfNumbers = maxOccurensOfNumbers;
        createCombinations();
    }

    private void createCombinations() {
        combinations = new ArrayList<>();
        createPossibleNumbers();
        initCombinationPositionProgress();
        do {
            int[] combination = new int[lengthOfCombination];
            for (int i = 0; i < lengthOfCombination; i++) {
                combination[i] = possibleNumbers[combinationPositions[i]];
            }
            Arrays.sort(combination);
            if (maxDubbleNumbersIsOke(combination) && combinationIsUnique(combination)) {
                combinations.add(combination);
            }
            getNextCombinationPosition();
        }
        while (!endCombinationPositionProgress());
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
        for (int progress : combinationPositions) {
            progress = 0;
        }
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

    private boolean maxDubbleNumbersIsOke(int[] newCombination) {
        for (int possibleNumber : possibleNumbers) {
            int occurensOfNumber = 0;
            for (int i : newCombination) {
                if (i == possibleNumber) occurensOfNumber += 1;
                if (occurensOfNumber > maxOccurensOfNumbers) return false;
            }
        }
        return true;
    }

    private boolean combinationIsUnique(int[] newCombination) {
        for (int[] combination : combinations) {
            boolean matches = true;
            for (int i = 0; i < lengthOfCombination; i++) {
                if (combination[i] != newCombination[i]) {
                    matches = false;
                    break;
                }
            }
            if (matches == true) return false;
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
        s.append("maxOccurensOfNumbers= ");
        s.append(maxOccurensOfNumbers);
        s.append(System.getProperty("line.separator"));
        s.append("PossibleNumbers     = ");
        s.append(Arrays.toString(possibleNumbers));
        s.append(System.getProperty("line.separator"));
        s.append("Combinations        = ");
        s.append(System.getProperty("line.separator"));
        for (int[] combination : combinations) {
            s.append(Arrays.toString(combination));
            s.append(System.getProperty("line.separator"));
        }
        return s.toString();
    }

    public int[] getPossibleNumbers() {
        return possibleNumbers;
    }

    public ArrayList<int[]> getCombinations() {
        return combinations;
    }
}
