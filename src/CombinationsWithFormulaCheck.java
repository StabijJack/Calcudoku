import java.util.ArrayList;

class CombinationsWithFormulaCheck extends Combinations{
    private final operators formulaOperator;
    private final int formulaNumber;
    public CombinationsWithFormulaCheck(int startNumber, int endNumber, int lengthOfCombination, operators formulaOperator, int formulaNumber) {
        super(startNumber, endNumber, lengthOfCombination);
        this.formulaOperator = formulaOperator;
        this.formulaNumber = formulaNumber;
        checkCombinations();
    }

    public CombinationsWithFormulaCheck(int startNumber, int endNumber, int lengthOfCombination, operators formulaOperator, int formulaNumber, int maxOccurrenceOfNumbers) {
        super(startNumber, endNumber, lengthOfCombination, maxOccurrenceOfNumbers);
        this.formulaOperator = formulaOperator;
        this.formulaNumber = formulaNumber;
        checkCombinations();
    }

    private void checkCombinations() {
        for (int i = combinations.size() - 1; i >= 0; i--) {
            ArrayList<Integer> combination = combinations.get(i);
            switch (formulaOperator) {
                case NONE:
                    if (combination.get(0) != formulaNumber) combinations.remove(i);
                    break;
                case ADD:
                    if (combination.stream().mapToInt(value -> value).sum() != formulaNumber) combinations.remove(i);
                    break;
                case SUBTRACT:
                    if (combination.stream().mapToInt(v -> v).reduce(combination.get(0)*2, (a, b) -> a - b)!= formulaNumber) combinations.remove(i);
                    break;
                case MULTIPLY:
                    if (combination.stream().mapToInt(v -> v).reduce(1, (a, b) -> a * b) != formulaNumber) combinations.remove(i);
                    break;
                case DIVIDE:
                    if (combination.get(combination.size()-1) == 0) {
                        combinations.remove(i);
                        break;
                    }
                    if (combination.stream().mapToInt(v -> v).reduce(combination.get(0)*combination.get(0), (a, b) -> a / b) != formulaNumber) combinations.remove(i);
                    break;
            }

        }
    }
}
