import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Objects;

class PuzzleBlockDataGroup {
    final ArrayList<PuzzleBlockData> group;

    @Contract(pure = true)
    PuzzleBlockDataGroup() {
        group = new ArrayList<>();
    }

    void addPuzzleBlockData(PuzzleBlockData puzzleBlockData) {
        group.add(puzzleBlockData);
    }
    int size(){
        return group.size();
    }
    boolean allUnique() {
        boolean allUnique = true;
        group.removeIf(puzzleBlockData -> puzzleBlockData.getSolution() == null);
        for (int i = 0; i < group.size(); i++) {
            boolean unique = true;
            for (int i1 = i + 1; i1 < group.size(); i1++) {
                if (Objects.equals(group.get(i).getSolution(), group.get(i1).getSolution())) {
                    unique = false;
                    allUnique = false;
                    group.get(i1).setSolutionError(true);
                }
            }
            if (!unique) group.get(i).setSolutionError(true);
        }

        return allUnique;
    }

    void findNumberOfEqualsBlocksWithSamePossibilities(int nEquals) {
        ArrayList<PuzzleBlockData> puzzleBlocks = new ArrayList<>();
        group.removeIf(PuzzleBlockData::arePossibilitiesAllFalse);
        boolean found = true;
        while(found){
            found = false;
            for (int i = 0; i < group.size() - (nEquals - 1); i++) {//stop before nEquals becomes impossible
                puzzleBlocks = new ArrayList<>();
                if (group.get(i).getNumberOfPossibilities() == nEquals) {
                    puzzleBlocks.add(group.get(i));
                    for (int iSecond = i + 1; iSecond < group.size(); iSecond++) {
                        if (group.get(iSecond).getNumberOfPossibilities() == nEquals) {
                            if (group.get(i).arePossibilitiesEqual(group.get(iSecond))) {
                                puzzleBlocks.add(group.get(iSecond));
                            }
                        }
                    }
                }
                if (puzzleBlocks.size() == nEquals) {
                    found = true;
                    break;
                }
            }
            if (found){
                PuzzleBlockData foundCombination = puzzleBlocks.get(0);
                for (PuzzleBlockData puzzleBlockData : puzzleBlocks) {
                    group.remove(puzzleBlockData);
                }
                boolean[] block = foundCombination.getPossibilities();
                for (PuzzleBlockData puzzleBlockData : group) {
                    for (int i = 0; i < block.length; i++) {
                        if (block[i]){
                            puzzleBlockData.resetPossibilities(i);
                        }
                    }
                }
            }
        }
    }
}
