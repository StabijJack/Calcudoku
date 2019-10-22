import org.jetbrains.annotations.Contract;
import java.util.HashSet;

public class PuzzleData {
    enum operators  {NONE ,ADD, SUBTRACT, MULTIPLY, DIVIDE }

    private final int maxNumber;
    private final int startNumber;
    final int numberOfBlocks;
    final Integer[][] solution;
    private final boolean[][][] possibilities;
    private final Integer[][] numberFormula;
    private final operators[][] operatorFormula;
    private final HashSet<BlockPosition>[][] formulaMembers;


    @Contract(pure = true)
    PuzzleData(int maxNumber, int startNumber) {
        this.startNumber = startNumber;
        this.maxNumber = maxNumber;
        this.numberOfBlocks = maxNumber + 1 - startNumber;
        this.solution = new Integer [numberOfBlocks][numberOfBlocks];
        this.possibilities = new boolean[numberOfBlocks][numberOfBlocks][numberOfBlocks];
        this.numberFormula = new Integer[numberOfBlocks][numberOfBlocks];
        this.operatorFormula = new operators[numberOfBlocks][numberOfBlocks];
        this.formulaMembers = new HashSet[numberOfBlocks][numberOfBlocks];
        for (int i = 0; i <numberOfBlocks; i++)
            for (int j = 0; j < numberOfBlocks; j++){
                this.operatorFormula[i][j] = operators.NONE;
                this.formulaMembers[i][j] = new HashSet<BlockPosition>();
            }
    }
    boolean togglePossibility(int column, int row, int value){
        if (checkPossibilityRange(value)) {
            possibilities[column][row][value] = !possibilities[column][row][value];
            return true;
        }
        else return false;
    }

    void resetPossibility(int column, int row, int value){
        possibilities[column][row][value] = false;
    }

    boolean getPossibility(int column, int row, int value){
        return possibilities[column][row][value];
    }

    boolean setSolution(int column, int row, Integer value){
        if (checkSolutionRange(value)) {
            solution[column][row] = value;
            return true;
        }
        else return false;
    }

    void setNumberFormula(int column, int row, Integer value){
        if(value == null){
            numberFormula[column][row] = null;
            formulaMembers[column][row].clear();
        }
        else{
            if(numberFormula[column][row] == null) {
                numberFormula[column][row] = value;
            }
            else{
                numberFormula[column][row] = numberFormula[column][row]*10 + value;
            }
            if (formulaMembers[column][row].size() == 0){
                addFormulaMember(column,row, new BlockPosition(column,row));
            }
        }
    }

    Integer getNumberFormula(int column, int row){
        return numberFormula[column][row];
    }

    String getOperatorFormula(int column, int row){

        switch(operatorFormula[column][row]) {
            case ADD:
                return "+";
            case SUBTRACT:
                return "-";
            case MULTIPLY:
                return "*";
            case DIVIDE:
                return "/";
            case NONE:
            default:
                return "";
        }
    }
    void setOperatorFormula(int column, int row, operators operator){
        operatorFormula[column][row] = operator;
    }
    BlockPosition getFormulaOwner(BlockPosition point){
        for (int column = 0; column < numberOfBlocks; column++) {
            for (int row = 0; row < numberOfBlocks; row++) {
                for (BlockPosition member : formulaMembers[column][row]) {
                    if (point.areNeighbors(member)){
                        return new BlockPosition(column,row);
                    }
                }
            }
        }
        return null;
    }

    boolean addFormulaMember(int column, int row, BlockPosition point){
        if (formulaMembers[column][row].size() == 0 & numberFormula[column][row] != null) {
            formulaMembers[column][row].add(point);
            return true;
        }
        else{
            boolean areNeighbors = false;
            for (BlockPosition member : formulaMembers[column][row]) {
                if (areNeighbors || point.areNeighbors(member)){
                    areNeighbors = true;
                }
            }
            if (areNeighbors) {
                formulaMembers[column][row].add(point);
                return true;
            }
        }
        return false;
    }

    HashSet getFormulaMembers(BlockPosition formulaOwner){
        return formulaMembers[formulaOwner.getColumn()][formulaOwner.getRow()];
    }


    private boolean checkSolutionRange(int solution) {
        return solution <= maxNumber && solution >= startNumber;
    }

    private boolean checkPossibilityRange(int possibility) {
        return possibility <= maxNumber && possibility >= startNumber;
    }

}
