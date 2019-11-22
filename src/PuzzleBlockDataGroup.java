import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Iterator;

public class PuzzleBlockDataGroup {
    ArrayList<PuzzleBlockData> group;

    @Contract(pure = true)
    public PuzzleBlockDataGroup() {
        group = new ArrayList<>();
    }
    void addPuzzleBlockData(PuzzleBlockData puzzleBlockData){
        group.add(puzzleBlockData);
    }
    boolean allUnique(){
        boolean allUnique = true;
        group.removeIf(puzzleBlockData -> puzzleBlockData.getSolution() == null);
        for (int i = 0; i < group.size(); i++) {
            boolean unique = true;
            for (int i1 = i+1; i1 < group.size(); i1++) {
                if (group.get(i).getSolution() == group.get(i1).getSolution()){
                    unique = false;
                    allUnique = false;
                    group.get(i1).setSolutionError(true);
                }
            }
            if (!unique) group.get(i).setSolutionError(true);
        }

        return allUnique;
    }
}
