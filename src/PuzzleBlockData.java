class PuzzleBlockData {
    private Integer solution;
    private final boolean[] possibilities;
    private Integer formulaNumber;
    private operators formulaOperator;
    private BlockPosition parent;

    PuzzleBlockData(int maxNumber, int startNumber) {
        solution = null;
        possibilities = new boolean[maxNumber + 1 - startNumber];
        formulaNumber = null;
        formulaOperator = operators.NONE;
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
    void togglePossibilities(int possibility) {
        this.possibilities[possibility] = !this.possibilities[possibility];
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

}
