import org.jetbrains.annotations.Contract;

class PuzzleBlockData {
    private Integer solution;
    private final boolean[] possibilities;
    private Integer formulaNumber;
    private operators formulaOperator;
    private BlockPosition parent;
    private boolean solutionError;

// --Commented out by Inspection START (24-11-2019 17:20):
//    @Contract(pure = true)
//    PuzzleBlockData(){
//        new PuzzleBlockData(1,1);
//    }
// --Commented out by Inspection STOP (24-11-2019 17:20)
    @Contract(pure = true)
    PuzzleBlockData(int maxNumber, int startNumber) {
        solution = null;
        possibilities = new boolean[maxNumber + 1 - startNumber];
        formulaNumber = null;
        formulaOperator = operators.NONE;
        solutionError = false;
    }

    Integer getSolution() {
        return solution;
    }
    void setSolution(Integer solution) {
        this.solution = solution;
    }

    boolean[] getPossibilities() {
        return possibilities;
    }
    void resetPossibilities(int possibility) {
        possibilities[possibility] = false;
    }
    void setPossibilities(int possibility) {
        possibilities[possibility] = true;
    }
    void togglePossibilities(int possibility) {
        this.possibilities[possibility] = !this.possibilities[possibility];
    }
    @Contract(pure = true)
    int getNumberOfPossibilities(){
        int number = 0 ;
        for (boolean possibility : possibilities) {
            if (possibility) number++;
        }
        return number;
    }
    boolean arePossibilitiesEqual(PuzzleBlockData other){
        for (int i = 0; i < possibilities.length; i++) {
            if (this.possibilities[i] != other.possibilities[i])
                return false;
        }
        return true;
    }
    boolean arePossibilitiesAllFalse(){
        for (boolean possibility : possibilities) {
            if (possibility)
                return false;
        }
        return true;

    }

    Integer getFormulaNumber() {
        return formulaNumber;
    }
    void setFormulaNumber(Integer number) {
        if (parent == null) {
            if (number == null) {
                formulaNumber = null;
            } else {
                if (formulaNumber == null) {
                    formulaNumber = number;
                } else {
                    formulaNumber = formulaNumber * 10 + number;
                }
            }
        }
    }

    operators getFormulaOperator() {
        return formulaOperator;
    }
    void setFormulaOperator(operators formulaOperator) {
        this.formulaOperator = formulaOperator;
    }

    BlockPosition getParent() {
        return parent;
    }
    void setParent(BlockPosition parent) {
        this.parent = parent;
    }

    boolean isSolutionError() {
        return solutionError;
    }

    void setSolutionError(boolean solutionError) {
        this.solutionError = solutionError;
    }
}
