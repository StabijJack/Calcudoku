import org.junit.jupiter.api.Test;

class PuzzleBlockDataTest {

    @Test
    void getSolution() {
        PuzzleBlockData pbd = new PuzzleBlockData(5,1);
        pbd.setSolution(1);
        assert pbd.getSolution() == 1;
    }

    @Test
    void setSolution() {
//        getSolution();
    }

    @Test
    void getPossibilities() {
        PuzzleBlockData pbd = new PuzzleBlockData(5,1);
        pbd.togglePossibilities(4);
        assert pbd.getPossibilities()[4];
        assert !pbd.getPossibilities()[1];
        pbd.resetPossibilities(4);
        assert !pbd.getPossibilities()[4];
        assert !pbd.getPossibilities()[1];
    }

    @Test
    void resetPossibilities() {
//        getPossibilities();
    }

    @Test
    void togglePossibilities() {
//        getPossibilities();
    }

    @Test
    void getFormulaNumber() {
        PuzzleBlockData pbd = new PuzzleBlockData(5,1);
        pbd.setFormulaNumber(12);
        assert pbd.getFormulaNumber() == 12;
    }

    @Test
    void setFormulaNumber() {
//        getFormulaNumber();
    }

    @Test
    void getFormulaOperator() {
        PuzzleBlockData pbd = new PuzzleBlockData(5,1);
        pbd.setFormulaOperator(operators.ADD);
        assert pbd.getFormulaOperator() == operators.ADD;
    }

    @Test
    void setFormulaOperator() {
//        getFormulaOperator();
    }

    @Test
    void getParent() {
        PuzzleBlockData pbd = new PuzzleBlockData(5,1);
        pbd.setParent(new BlockPosition(4,4));
        assert pbd.getParent().isEqual(new BlockPosition(4,4));
    }

    @Test
    void setParent() {
//        getParent();
    }

    @Test
    void arePossibilitiesAllFalse() {
        PuzzleBlockData pbd = new PuzzleBlockData(5,1);
        assert pbd.arePossibilitiesAllFalse();

    }
    @Test
    void arePossibilitiesEqual(){
        PuzzleBlockData pbd1 = new PuzzleBlockData(5,1);
        PuzzleBlockData pbd2 = new PuzzleBlockData(5,1);
        assert pbd1.arePossibilitiesEqual(pbd2);
        pbd1.togglePossibilities(1);
        assert !pbd1.arePossibilitiesEqual(pbd2);
    }
}