import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

class PuzzleFrame {
    private int maxNumber;
    private int startNumber;
    private BasicBlock[][] puzzleBlock;
    private GridPane puzzleFrame;
    private int numberOfBlocks;
    private int maxIndex;
    private int currentRow = 0;
    private int currentColumn = 1;
    private boolean playMode;
    PuzzleFrame(int maxNumber, int size){
        this(maxNumber,size, 1);
    }
    PuzzleFrame(int maxNumber, int size, int startNumber){

        this.startNumber = startNumber;
        this.maxNumber = maxNumber;
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
        puzzleFrame = new GridPane();
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
    void manageMouse(double mouseX, double mouseY){
        puzzleBlock[currentColumn][currentRow].setSelected(false);
        for (currentColumn = 0; currentColumn <= maxIndex ; currentColumn++) {
            if (mouseX < puzzleBlock[currentColumn][0].getBlock().getLayoutX() + puzzleBlock[0][currentRow].getBlock().getWidth()){
                break;
            }
        }
        for (currentRow = 0; currentRow <= maxIndex ; currentRow++) {
            if (mouseY < puzzleBlock[0][currentRow].getBlock().getLayoutY() + puzzleBlock[0][currentRow].getBlock().getHeight()){
                break;
            }
        }
        puzzleBlock[currentColumn][currentRow].setSelected(true);


    }
    void manageDigits(KeyCode keyCode, boolean altDown) {
        int c = keyCode.getCode();
        if(c > 57) c -= 48;
        if (altDown){
            puzzleBlock[currentColumn][currentRow].addPossibilities( String.valueOf((char) c));
        }
        else{
            puzzleBlock[currentColumn][currentRow].setSolution( String.valueOf((char) c));
        }
    }

    void manageLetters(KeyCode keyCode, boolean altDown) {
        int c = keyCode.getCode();
        c -= 55;
        if (altDown){
            puzzleBlock[currentColumn][currentRow].addPossibilities( String.valueOf((char) c));
        }
        else{
            puzzleBlock[currentColumn][currentRow].setSolution(String.valueOf((c)));
        }
    }

    void manageClearBlock(KeyCode keyCode) {
        if (puzzleBlock[currentColumn][currentRow].getSolution().isBlank()){
            if (keyCode == KeyCode.BACK_SPACE){
                puzzleBlock[currentColumn][currentRow].delLastPossibility();
            }
            else{
                puzzleBlock[currentColumn][currentRow].setPossibilities("");
            }
        }
        else{
            puzzleBlock[currentColumn][currentRow].setSolution("");
        }
    }
}
