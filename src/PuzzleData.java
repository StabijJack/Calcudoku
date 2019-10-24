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

//    BlockPosition getFormulaOwner(BlockPosition point){
//        for (int column = 0; column < numberOfBlocks; column++) {
//            for (int row = 0; row < numberOfBlocks; row++) {
//                for (BlockPosition member : formulaMembers[column][row]) {
//                    if (point.areNeighbors(member)){
//                        return new BlockPosition(column,row);
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    boolean addFormulaMember(int column, int row, BlockPosition point){
//        if (formulaMembers[column][row].size() == 0 & numberFormula[column][row] != null) {
//            formulaMembers[column][row].add(point);
//            return true;
//        }
//        else{
//            boolean areNeighbors = false;
//            for (BlockPosition member : formulaMembers[column][row]) {
//                if (areNeighbors || point.areNeighbors(member)){
//                    areNeighbors = true;
//                }
//            }
//            if (areNeighbors) {
//                formulaMembers[column][row].add(point);
//                return true;
//            }
//        }
//        return false;
//    }
//
//    HashSet getFormulaMembers(BlockPosition formulaOwner){
//        return formulaMembers[formulaOwner.getColumn()][formulaOwner.getRow()];
//    }
}
