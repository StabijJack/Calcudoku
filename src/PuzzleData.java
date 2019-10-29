import org.jetbrains.annotations.Contract;


class PuzzleData {
    final int numberOfBlocks;
    private final PuzzleBlockData[][] puzzleBlockData;
    private final int maxNumber;
    private final int startNumber;
    @Contract(pure = true)
    PuzzleData(int maxNumber, int startNumber) {
        this.startNumber = startNumber;
        this.maxNumber = maxNumber;
        this.numberOfBlocks = maxNumber + 1 - startNumber;
        this.puzzleBlockData = new PuzzleBlockData[numberOfBlocks][numberOfBlocks];
        for (int i = 0; i < numberOfBlocks; i++)
            for (int j = 0; j < numberOfBlocks; j++) {
                this.puzzleBlockData[i][j] = new PuzzleBlockData(maxNumber, startNumber);
            }
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
            puzzleBlockData[column][row].togglePossibilities(possibility);
            return true;
        } else return false;
    }

    void resetPossibility(int column, int row, int posibility) {
        puzzleBlockData[column][row].resetPossibilities(posibility);
    }

    boolean getPossibility(int column, int row, int value) {
        return puzzleBlockData[column][row].getPossibilities()[value];
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

    String getFormulaOperator(int column, int row) {
        return puzzleBlockData[column][row].getFormulaOperator();
    }

    void setFormulaOperator(int column, int row, operators operator) {
        puzzleBlockData[column][row].setFormulaOperator(operator);
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
    void exportData(){
    }
}
