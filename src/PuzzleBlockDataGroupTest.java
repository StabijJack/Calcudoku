import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PuzzleBlockDataGroupTest {
    PuzzleBlockDataGroup  g = new PuzzleBlockDataGroup();
    PuzzleBlockData p0 = new PuzzleBlockData(5,1);
    PuzzleBlockData p1 = new PuzzleBlockData(5,1);
    PuzzleBlockData p2 = new PuzzleBlockData(5,1);

    @Test
    void addPuzzleBlockData() {
        g.addPuzzleBlockData(p0);
        g.addPuzzleBlockData(p1);
        g.addPuzzleBlockData(p2);
        assert g.group.size() == 3;
    }

    @Test
    void allUnique() {
        addPuzzleBlockData();
        p0.setSolution(1);
        p1.setSolution(1);
        assert g.allUnique() == false;
        assert g.group.size() == 2;
        assert p0.isSolutionError() == true;
        assert p1.isSolutionError() == true;
        g.addPuzzleBlockData(p2);
        p2.setSolution(2);
        p0.setSolutionError(false);
        p1.setSolutionError(false);
        p2.setSolutionError(false);
        assert g.allUnique() == false;
        assert g.group.size() == 3;
        assert p0.isSolutionError() == true;
        assert p1.isSolutionError() == true;
        assert p2.isSolutionError() == false;
        p0.setSolutionError(false);
        p1.setSolutionError(false);
        p2.setSolutionError(false);
        p1.setSolution(3);
        assert g.allUnique() == true;
        assert g.group.size() == 3;
        assert p0.isSolutionError() == false;
        assert p1.isSolutionError() == false;
        assert p2.isSolutionError() == false;


    }
}