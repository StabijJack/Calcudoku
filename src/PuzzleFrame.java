import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

class PuzzleFrame {
    private int maxNumber;
    private int startNumber;
    private BasicBlock[][] puzzleBlock;
    private int numberOfBlocks;
    private int maxIndex;
    private int currentRow = 0;
    private int currentColumn = 1;
    private boolean playMode;
    PuzzleFrame(int maxNumber, int size){
        this(maxNumber,size, 1);
    }
    PuzzleFrame(int maxNumber, int size,int startNumber){

        this.startNumber = startNumber;
        this.numberOfBlocks = maxNumber + 1 - startNumber;
        this.maxIndex = numberOfBlocks -1;
        puzzleBlock = new BasicBlock[this.numberOfBlocks][this.numberOfBlocks];
        for (int x = 0; x < this.numberOfBlocks; x++) {
            for (int y = 0; y < this.numberOfBlocks; y++) {
                puzzleBlock[x][y] = new BasicBlock(size);
            }
        }
        puzzleBlock[currentColumn][currentRow].setSelected(true);
    }
    GridPane getFrame(){
        GridPane puzzleFrame = new GridPane();
        for (int x = 0; x < this.numberOfBlocks; x++) {
            for (int y = 0; y < this.numberOfBlocks; y++) {
                puzzleFrame.add(puzzleBlock[x][y].getBlock() , x, y);
            }
        }
        return puzzleFrame;
    }
    boolean isPlayMode() {

        return playMode;
    }
    void setPlayMode(boolean playMode) {
        this.playMode = playMode;
    }
    void manageCursorKeys(KeyCode keyCode){
        puzzleBlock[currentColumn][currentRow].setSelected(false);
        switch (keyCode){
            case DOWN:
                ++currentRow;break;
            case UP:
                --currentRow;break;
            case RIGHT:
                ++currentColumn;break;
            case LEFT:
                --currentColumn;break;
            case HOME:
                currentColumn = 0;break;
            case END:
                currentColumn = maxIndex;break;
            case PAGE_UP:
                currentRow = 0;break;
            case PAGE_DOWN:
                currentRow = maxIndex;break;
        }

        currentRow = (currentRow + numberOfBlocks) % numberOfBlocks;
        currentColumn = (currentColumn + numberOfBlocks) % numberOfBlocks;
        puzzleBlock[currentColumn][currentRow].setSelected(true);

    }
}
