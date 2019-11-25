import org.junit.jupiter.api.Test;

class PuzzleBlockDataGroupTest {
    private final PuzzleBlockData p0 = new PuzzleBlockData(6,0);
    private final PuzzleBlockData p1 = new PuzzleBlockData(6,0);
    private final PuzzleBlockData p2 = new PuzzleBlockData(6,0);
    private final PuzzleBlockData p3 = new PuzzleBlockData(6,0);
    private final PuzzleBlockData p4 = new PuzzleBlockData(6,0);
    private final PuzzleBlockData p5 = new PuzzleBlockData(6,0);

    @Test
    void addPuzzleBlockData() {
        PuzzleBlockDataGroup  g = new PuzzleBlockDataGroup();
        g.addPuzzleBlockData(p0);
        g.addPuzzleBlockData(p1);
        g.addPuzzleBlockData(p2);
        assert g.size() == 3;
    }

    @Test
    void allUnique() {
        PuzzleBlockDataGroup  g = new PuzzleBlockDataGroup();
        g.addPuzzleBlockData(p0);
        g.addPuzzleBlockData(p1);
        g.addPuzzleBlockData(p2);
        p0.setSolution(1);
        p1.setSolution(1);
        assert !g.allUnique();
        assert g.size() == 2;
        assert p0.isSolutionError();
        assert p1.isSolutionError();
        g.addPuzzleBlockData(p2);
        p2.setSolution(2);
        p0.setSolutionError(false);
        p1.setSolutionError(false);
        p2.setSolutionError(false);
        assert !g.allUnique();
        assert g.size() == 3;
        assert p0.isSolutionError();
        assert p1.isSolutionError();
        assert !p2.isSolutionError();
        p0.setSolutionError(false);
        p1.setSolutionError(false);
        p2.setSolutionError(false);
        p1.setSolution(3);
        assert g.allUnique();
        assert g.size() == 3;
        assert !p0.isSolutionError();
        assert !p1.isSolutionError();
        assert !p2.isSolutionError();


    }

    @Test
    void find2BlocksWithSamePossibilities() {
        PuzzleBlockDataGroup  g = new PuzzleBlockDataGroup();
        g.addPuzzleBlockData(p0);
        g.addPuzzleBlockData(p1);
        g.addPuzzleBlockData(p2);
        p0.setPossibilities(1);
        p0.setPossibilities(2);
        p1.setPossibilities(1);
        p1.setPossibilities(2);
        p2.setPossibilities(3);
        p2.setPossibilities(2);
        g.findNumberOfEqualsBlocksWithSamePossibilities(2);

        assert p0.getPossibilities()[1];
        assert p0.getPossibilities()[2];
        assert p1.getPossibilities()[1];
        assert p1.getPossibilities()[2];
        assert p2.getPossibilities()[3];
        assert !p2.getPossibilities()[2];
        assert g.size()==1;
        g = new PuzzleBlockDataGroup();
        g.addPuzzleBlockData(p0);
        g.addPuzzleBlockData(p1);
        g.addPuzzleBlockData(p2);
        g.addPuzzleBlockData(p3);
        g.addPuzzleBlockData(p4);
        g.addPuzzleBlockData(p5);
        p0.setPossibilities(1);
        p0.setPossibilities(2);
        p1.setPossibilities(1);
        p1.setPossibilities(2);
        p2.setPossibilities(3);
        p2.setPossibilities(2);
        p3.setPossibilities(4);
        p3.setPossibilities(3);
        p4.setPossibilities(3);
        p4.setPossibilities(5);
        p5.setPossibilities(4);
        p5.setPossibilities(3);
        g.findNumberOfEqualsBlocksWithSamePossibilities(2);
        assert p0.getPossibilities()[1];
        assert p0.getPossibilities()[2];
        assert p1.getPossibilities()[1];
        assert p1.getPossibilities()[2];
        assert !p2.getPossibilities()[3];
        assert !p2.getPossibilities()[2];
        assert p3.getPossibilities()[4];
        assert p3.getPossibilities()[3];
        assert p4.getPossibilities()[5];
        assert !p4.getPossibilities()[3];
        assert p5.getPossibilities()[4];
        assert p5.getPossibilities()[3];
        assert g.size()==2;
    }

    @Test
    void findNumberOfEqualsBlocksWithSamePossibilities() {
        PuzzleBlockDataGroup  g = new PuzzleBlockDataGroup();
        g.addPuzzleBlockData(p0);
        g.addPuzzleBlockData(p1);
        g.addPuzzleBlockData(p2);
        p0.setPossibilities(1);
        p0.setPossibilities(2);
        p1.setPossibilities(1);
        p1.setPossibilities(2);
        p2.setPossibilities(3);
        p2.setPossibilities(2);
        g.findNumberOfEqualsBlocksWithSamePossibilities(2);

        assert p0.getPossibilities()[1];
        assert p0.getPossibilities()[2];
        assert p1.getPossibilities()[1];
        assert p1.getPossibilities()[2];
        assert p2.getPossibilities()[3];
        assert !p2.getPossibilities()[2];
        assert g.size()==1;
        g = new PuzzleBlockDataGroup();
        g.addPuzzleBlockData(p0);
        g.addPuzzleBlockData(p1);
        g.addPuzzleBlockData(p2);
        g.addPuzzleBlockData(p3);
        g.addPuzzleBlockData(p4);
        g.addPuzzleBlockData(p5);
        p0.setPossibilities(1);
        p0.setPossibilities(2);
        p1.setPossibilities(1);
        p1.setPossibilities(2);
        p2.setPossibilities(3);
        p2.setPossibilities(2);
        p3.setPossibilities(4);
        p3.setPossibilities(3);
        p4.setPossibilities(3);
        p4.setPossibilities(5);
        p5.setPossibilities(4);
        p5.setPossibilities(3);
        g.findNumberOfEqualsBlocksWithSamePossibilities(2);
        assert p0.getPossibilities()[1];
        assert p0.getPossibilities()[2];
        assert p1.getPossibilities()[1];
        assert p1.getPossibilities()[2];
        assert !p2.getPossibilities()[3];
        assert !p2.getPossibilities()[2];
        assert p3.getPossibilities()[4];
        assert p3.getPossibilities()[3];
        assert p4.getPossibilities()[5];
        assert !p4.getPossibilities()[3];
        assert p5.getPossibilities()[4];
        assert p5.getPossibilities()[3];
        assert g.size()==2;

        g = new PuzzleBlockDataGroup();
        g.addPuzzleBlockData(p0);
        g.addPuzzleBlockData(p1);
        g.addPuzzleBlockData(p2);
        g.addPuzzleBlockData(p3);
        p0.setPossibilities(0);
        p1.setPossibilities(0);
        p2.setPossibilities(0);
        p0.setPossibilities(1);
        p1.setPossibilities(1);
        p2.setPossibilities(1);
        p0.setPossibilities(2);
        p1.setPossibilities(2);
        p2.setPossibilities(2);
        p3.setPossibilities(0);
        p3.setPossibilities(1);
        p3.setPossibilities(2);
        p3.setPossibilities(3);
        g.findNumberOfEqualsBlocksWithSamePossibilities(3);
        assert g.size()==1;
        assert !p3.getPossibilities()[0];
        assert !p3.getPossibilities()[1];
        assert !p3.getPossibilities()[2];
        assert p3.getPossibilities()[3];

    }
}