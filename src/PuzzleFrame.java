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
    private int currentColumn = 0;
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
    void setPlayMode(boolean playMode) {
        this.playMode = playMode;
    }
    boolean isPlayMode() {

        return playMode;
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
        c -= 48;
        if (altDown){
            if (checkPossibility(c)) puzzleBlock[currentColumn][currentRow].addPossibilities( String.valueOf(c));
        }
        else{
            if (checkSolution(c)) puzzleBlock[currentColumn][currentRow].setSolution( String.valueOf(c));
        }
    }
    void manageLetters(KeyCode keyCode, boolean altDown) {
        int c = keyCode.getCode();
        c -= 55;
        if (altDown){
            if (checkPossibility(c)) puzzleBlock[currentColumn][currentRow].addPossibilities( String.valueOf(c));
        }
        else{
            if (checkSolution(c)) puzzleBlock[currentColumn][currentRow].setSolution( String.valueOf(c));
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
    void manageFormulaDigits(KeyCode keyCode) {
        int c = keyCode.getCode();
        if(c > 57) c -= 48;
        c -= 48;
        puzzleBlock[currentColumn][currentRow].addToFormula(String.valueOf(c));
    }
    void manageFormulaLetters(KeyCode keyCode) {
        char c = ' ';
        switch (keyCode){
            case ADD:
                c = '+';break;
            case SUBTRACT:
                c = '-';break;
            case MULTIPLY:
                c ='x';break;
            case DIVIDE:
                c = ':';break;
        }
        puzzleBlock[currentColumn][currentRow].addToFormula(" " + c);
    }
    void manageFormulaClearBlock(KeyCode keyCode) {
            puzzleBlock[currentColumn][currentRow].delFormula();
    }
    private boolean checkSolution(int solution){
        return solution <= maxNumber && solution >= startNumber;
    }
    private boolean checkPossibility(int possibility){
        return possibility <= maxNumber && possibility >= startNumber;
    }
}
