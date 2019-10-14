import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

class PuzzleFrame {
    private int maxNumber;
    private int startNumber;
    private BasicBlock[][] puzzleBlock;
    private int maxBlockNumber;


    PuzzleFrame(int maxNumber, int size){
        this(maxNumber,size,1);
    }
    PuzzleFrame(int maxNumber, int size,int startNumber){
        this.startNumber = startNumber;
        this.maxBlockNumber = maxNumber + 1 - startNumber;
        puzzleBlock = new BasicBlock[this.maxBlockNumber][this.maxBlockNumber];
        for (int x = 0; x < this.maxBlockNumber; x++) {
            for (int y = 0; y < this.maxBlockNumber; y++) {
                puzzleBlock[x][y] = new BasicBlock(size);
            }
        }

        puzzleBlock[0][0].setSolution("1");
        puzzleBlock[1][1].setPossibilities("1,2,3,4");
        puzzleBlock[0][0].setFormula("135X");

    }
    GridPane getFrame(){
        GridPane puzzleFrame = new GridPane();
        for (int x = 0; x < this.maxBlockNumber; x++) {
            for (int y = 0; y < this.maxBlockNumber; y++) {
                puzzleFrame.add(puzzleBlock[x][y].getBlock() , x, y);
            }
        }
        return puzzleFrame;
    }
}
