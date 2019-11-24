import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Objects;

class PuzzleBlockDataGroup {
    final ArrayList<PuzzleBlockData> group;

    @Contract(pure = true)
    public PuzzleBlockDataGroup() {
        group = new ArrayList<>();
    }

    void addPuzzleBlockData(PuzzleBlockData puzzleBlockData) {
        group.add(puzzleBlockData);
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

    public void find2BlocksWithSamePossibilities() {
        PuzzleBlockData firstBlock =new PuzzleBlockData(1,1);
        PuzzleBlockData secondBlock = new PuzzleBlockData(1,1);
        boolean found = true;
        while(found){
            found = false;
            for (int i = 0; i < group.size() - 1; i++) {//until -1 else no compare
                if (group.get(i).getNumberOfPossibilities() == 2) {
                    firstBlock = group.get(i);
                    for (int iSecond = i + 1; iSecond < group.size(); iSecond++) {
                        if (group.get(iSecond).getNumberOfPossibilities() == 2) {
                            secondBlock = group.get(iSecond);
                            if (group.get(i).arePossibilitiesEqual(group.get(iSecond))) {
                                found = true;
                                break;
                            }
                        }
                    }
                    if (found) break;
                }
            }
            if (found){
                group.remove(firstBlock);
                group.remove(secondBlock);
                boolean[] block = firstBlock.getPossibilities();
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

    public void findNumberOfEqualsBlocksWithSamePossibilities(int nEquals) {
        ArrayList<PuzzleBlockData> puzzleBlockDatas = new ArrayList<>();
        group.removeIf(PuzzleBlockData::arePossibilitiesAllFalse);
        boolean found = true;
        while(found){
            found = false;
            for (int i = 0; i < group.size() - (nEquals - 1); i++) {//stop before nEquals becomes impossible
                puzzleBlockDatas = new ArrayList<>();
                if (group.get(i).getNumberOfPossibilities() == nEquals) {
                    puzzleBlockDatas.add(group.get(i));
                    for (int iSecond = i + 1; iSecond < group.size(); iSecond++) {
                        if (group.get(iSecond).getNumberOfPossibilities() == nEquals) {
                            if (group.get(i).arePossibilitiesEqual(group.get(iSecond))) {
                                puzzleBlockDatas.add(group.get(iSecond));
                            }
                        }
                    }
                }
                if (puzzleBlockDatas.size() == nEquals) {
                    found = true;
                    break;
                }
            }
            if (found){
                PuzzleBlockData foundCombination = puzzleBlockDatas.get(0);
                for (PuzzleBlockData puzzleBlockData : puzzleBlockDatas) {
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
