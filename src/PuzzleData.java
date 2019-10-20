import org.jetbrains.annotations.Contract;

public class PuzzleData {
    enum operators  {A, S, M, D }

    private final int maxNumber;
    private final int startNumber;
    final int numberOfBlocks;
    final Integer[][] solution;
    private final boolean[][][] possibilities;
    private final Integer[][] numberFormula;
    private final operators[][] operatorFormula;

    @Contract(pure = true)
    PuzzleData(int maxNumber, int startNumber) {
        this.startNumber = startNumber;
        this.maxNumber = maxNumber;
        this.numberOfBlocks = maxNumber + 1 - startNumber;
        this.solution = new Integer [numberOfBlocks][numberOfBlocks];
        this.possibilities = new boolean[numberOfBlocks][numberOfBlocks][numberOfBlocks];
        this.numberFormula = new Integer[numberOfBlocks][numberOfBlocks];
        this.operatorFormula = new operators[numberOfBlocks][numberOfBlocks];

    }
    void flipPossibility(int column, int row, int value){
        possibilities[column][row][value]=!possibilities[column][row][value];
    }
    void resetPossibility(int column, int row, int value){
        possibilities[column][row][value] = false;
    }
    boolean getPossibility(int column, int row, int value){
        return possibilities[column][row][value];
    }
    void setSolution(int column, int row, Integer value){
        solution[column][row] = value;
    }
    void setNumberFormula(int column, int row, Integer value){
        if(value == null){
            numberFormula[column][row] = null;
        }
        else{
            if(numberFormula[column][row] == null) {
                numberFormula[column][row] = value;
            }
            else{
                numberFormula[column][row] = numberFormula[column][row]*10 + value;
            }
        }
    }

    Integer getNumberFormula(int column, int row){
        return numberFormula[column][row];
    }
    operators getOperatorFormula(int column, int row){
        return operatorFormula[column][row];
    }
    void setOperatorFormula(int column, int row, operators operator){
        operatorFormula[column][row] = operator;
    }

    boolean checkSolution(int solution) {
        return solution <= maxNumber && solution >= startNumber;
    }

    boolean checkPossibility(int possibility) {
        return possibility <= maxNumber && possibility >= startNumber;
    }

}
