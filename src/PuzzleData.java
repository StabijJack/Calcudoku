import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;


class PuzzleData {

    String puzzleName;
    final int startNumber;
    final int maxNumber;
    final int numberOfBlocks;
    private final PuzzleBlockData[][] puzzleBlockData;
    @Contract(pure = true)
    PuzzleData(int maxNumber, int startNumber, String puzzleName) {
        this.startNumber = startNumber;
        this.maxNumber = maxNumber;
        this.puzzleName = puzzleName;
        this.numberOfBlocks = maxNumber + 1 - startNumber;
        this.puzzleBlockData = new PuzzleBlockData[numberOfBlocks][numberOfBlocks];
        for (int i = 0; i < numberOfBlocks; i++)
            for (int j = 0; j < numberOfBlocks; j++) {
                this.puzzleBlockData[i][j] = new PuzzleBlockData(maxNumber, startNumber);
            }
    }

    public void setPuzzleName(String puzzleName) {
        this.puzzleName = puzzleName;
    }

    boolean setSolution(int column, int row, Integer solution) {
        if (solution == null) {
            puzzleBlockData[column][row].setSolution(null);
            return true;
        }
        else {
            if ((solution <= maxNumber && solution >= startNumber)) {
                puzzleBlockData[column][row].setSolution(solution);
                return true;
            } else {
                return false;
            }
        }
    }
    Integer getSolution(int column, int row){
        return puzzleBlockData[column][row].getSolution();
    }

    boolean togglePossibility(int column, int row, int possibility) {
        if (possibility <= maxNumber && possibility >= startNumber) {
            puzzleBlockData[column][row].togglePossibilities(possibility-startNumber);
            return true;
        } else return false;
    }
    void resetPossibility(int column, int row, int possibility) {
        puzzleBlockData[column][row].resetPossibilities(possibility-startNumber);
    }
    boolean getPossibility(int column, int row, int value) {
        return puzzleBlockData[column][row].getPossibilities()[value-startNumber];
    }
    String getPossibilityToString( int column, int row){
        StringBuilder s = new StringBuilder();
        for (int possibility = startNumber; possibility <= maxNumber; possibility++) {
            if (getPossibility(column, row, possibility))
                s.append(" ").append(possibility);
        }
        return s.toString();

    }

    void setFormulaNumber(int column, int row, Integer number) {
        BlockPosition blockPosition = new BlockPosition(column, row);
        if (number == null) {
            for (PuzzleBlockData[] block : puzzleBlockData) {
                for (PuzzleBlockData b : block) {
                    if (b.getParent() != null) {
                        if (b.getParent().isEqual(blockPosition)) {
                            b.setParent(null);
                        }
                    }
                }
            }
        }
        puzzleBlockData[column][row].setFormulaNumber(number);
    }
    Integer getFormulaNumber(int column, int row) {
        return puzzleBlockData[column][row].getFormulaNumber();
    }

    operators getFormulaOperator(int column, int row) {
        return puzzleBlockData[column][row].getFormulaOperator();
    }
    void setFormulaOperator(int column, int row, operators operator) {
        puzzleBlockData[column][row].setFormulaOperator(operator);
    }

    void setParent(int column, int row, int parentColumn, int parentRow){
        setParent(column, row, new BlockPosition(parentColumn,parentRow));
    }

    void setParent(int column, int row, BlockPosition parent){
        puzzleBlockData[column][row].setParent(parent);
    }

    BlockPosition getParent(int column, int row){
        return puzzleBlockData[column][row].getParent();
    }

    BlockPosition getFormulaParent(int column, int row){
        if (getFormulaNumber(column,row) != null){
            return new BlockPosition(column,row);
        }
        return getParent(column,row);
    }

    private boolean isFormulaSolutionFilled(@NotNull BlockPosition parent){
        if (puzzleBlockData[parent.getColumn()][parent.getRow()].getSolution() == null) return false;
        for (PuzzleBlockData[] blockDataRow : puzzleBlockData) {
            for (PuzzleBlockData blockData : blockDataRow) {
                if (blockData.getParent() != null)
                    if (blockData.getParent().isEqual(parent) && blockData.getSolution() == null) return false;
            }
        }
        return true;
    }

    @NotNull
    private ArrayList<Integer> getFormulaSolutions(@NotNull BlockPosition parent){
        ArrayList<Integer> solutions = new ArrayList<>();
        solutions.add(puzzleBlockData[parent.getColumn()][parent.getRow()].getSolution());
        for (PuzzleBlockData[] blockDataRow : puzzleBlockData) {
            for (PuzzleBlockData blockData : blockDataRow) {
                BlockPosition bp = blockData.getParent();
                if (bp != null && bp.isEqual(parent)) solutions.add(blockData.getSolution());
            }
        }
        return solutions;
    }

    formulaResult checkFormulaResult(int parentColumn, int parentRow){
        return checkFormulaResult(new BlockPosition(parentColumn,parentRow));
    }
    formulaResult checkFormulaResult(BlockPosition parent){
        if (!isFormulaSolutionFilled(parent)) return formulaResult.UNDECIDED;
        ArrayList<Integer> formulaValues = getFormulaSolutions(parent);
        switch (puzzleBlockData[parent.getColumn()][parent.getRow()].getFormulaOperator()) {
            case NONE:
                if (formulaValues.size() == 1 && formulaValues.get(0)
                        == puzzleBlockData[parent.getColumn()][parent.getRow()].getFormulaNumber())  return formulaResult.CORRECT;
                break;
            case ADD:
                if (formulaValues.stream().mapToInt(value -> value).sum()
                        == puzzleBlockData[parent.getColumn()][parent.getRow()].getFormulaNumber()) return formulaResult.CORRECT;
                break;
            case MULTIPLY:
                if (formulaValues.stream().mapToInt(v -> v).reduce(1, (a, b) -> a * b)
                        == puzzleBlockData[parent.getColumn()][parent.getRow()].getFormulaNumber()) return formulaResult.CORRECT;
                break;
            case SUBTRACT:
                formulaValues.sort(Collections.reverseOrder());
                if (formulaValues.stream().mapToInt(v -> v).reduce(formulaValues.get(0)*2, (a, b) -> a - b)
                        == puzzleBlockData[parent.getColumn()][parent.getRow()].getFormulaNumber()) return formulaResult.CORRECT;
                break;
            case DIVIDE:
                Collections.sort(formulaValues);
                if (formulaValues.get(0) == 0 && puzzleBlockData[parent.getColumn()][parent.getRow()].getFormulaNumber() == 0) return formulaResult.CORRECT;
                formulaValues.sort(Collections.reverseOrder());
                if (formulaValues.stream().mapToInt(v -> v).filter(v -> v!=0).reduce(formulaValues.get(0)*formulaValues.get(0), (a, b) -> a / b)
                        == puzzleBlockData[parent.getColumn()][parent.getRow()].getFormulaNumber()) return formulaResult.CORRECT;
                break;
        }
        return formulaResult.WRONG;
    }

    boolean isFormulaPuzzleReadyToPlay(){
        for (PuzzleBlockData[] puzzleBlockDataColumn : puzzleBlockData) {
            for (PuzzleBlockData blockData : puzzleBlockDataColumn) {
                if (blockData.getFormulaNumber() == null && blockData.getParent() == null ) return false;
            }
        }
        return true;
    }
}
