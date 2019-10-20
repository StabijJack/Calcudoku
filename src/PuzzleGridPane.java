import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;

class PuzzleGridPane extends PuzzleData{
    private final BasicBlock[][] puzzleBlock;
    private int currentRow = 0;
    private int currentColumn = 0;
    private boolean playMode;


    PuzzleGridPane(int maxNumber, int size, int startNumber) {
        super(maxNumber,startNumber);
        puzzleBlock = new BasicBlock[this.numberOfBlocks][this.numberOfBlocks];
        for (int column = 0; column < this.numberOfBlocks; column++) {
            for (int row = 0; row < this.numberOfBlocks; row++) {
                puzzleBlock[column][row] = new BasicBlock(size);
            }
        }
        puzzleBlock[currentColumn][currentRow].setSelected(true);
    }

    @NotNull GridPane getFrame() {
        GridPane puzzleFrame = new GridPane();
//        to get Initial KeyEvent within GridPane
        TextField t = new TextField();
        puzzleFrame.add(t, 0,numberOfBlocks +1, numberOfBlocks, 1);
//        to get Initial KeyEvent within GridPane
        for (int column = 0; column < this.numberOfBlocks; column++) {
            for (int row = 0; row < this.numberOfBlocks; row++) {
                puzzleFrame.add(puzzleBlock[column][row].getBlock(), column, row);
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

    void manageCursorKeys(@NotNull KeyCode keyCode) {
        puzzleBlock[currentColumn][currentRow].setSelected(false);
        switch (keyCode) {
            case DOWN:
                ++currentRow;
                break;
            case UP:
                --currentRow;
                break;
            case RIGHT:
                ++currentColumn;
                break;
            case LEFT:
                --currentColumn;
                break;
            case HOME:
                currentColumn = 0;
                break;
            case END:
                currentColumn = numberOfBlocks - 1;
                break;
            case PAGE_UP:
                currentRow = 0;
                break;
            case PAGE_DOWN:
                currentRow = numberOfBlocks - 1;
                break;
        }

        currentRow = (currentRow + numberOfBlocks) % numberOfBlocks;
        currentColumn = (currentColumn + numberOfBlocks) % numberOfBlocks;
        puzzleBlock[currentColumn][currentRow].setSelected(true);

    }

    void manageMouse(double mouseX, double mouseY) {
        puzzleBlock[currentColumn][currentRow].setSelected(false);
        for (currentColumn = 0; currentColumn < numberOfBlocks; currentColumn++) {
            if (mouseX < puzzleBlock[currentColumn][0].getBlock().getLayoutX() + puzzleBlock[0][currentRow].getBlock().getWidth()) {
                break;
            }
        }
        for (currentRow = 0; currentRow < numberOfBlocks; currentRow++) {
            if (mouseY < puzzleBlock[0][currentRow].getBlock().getLayoutY() + puzzleBlock[0][currentRow].getBlock().getHeight()) {
                break;
            }
        }
        puzzleBlock[currentColumn][currentRow].setSelected(true);


    }

    void manageInputBlock(@NotNull KeyCode keyCode, boolean shiftDown) {
        int value = keyCode.getCode();
        if (keyCode.isDigitKey()){
            if (value > 57) value -= 48;
            value -= 48;
        }
        else{ //isLetterKey()
            value -= 55;
        }
        if (shiftDown) {
            if (checkPossibility(value)) {
                flipPossibility(currentColumn,currentRow,value);
                StringBuilder s = new StringBuilder();
                for (int possibility = 0; possibility < numberOfBlocks; possibility++) {
                    if(getPossibility(currentColumn, currentRow, possibility)) s.append(" ").append(possibility);
                }
                puzzleBlock[currentColumn][currentRow].setPossibilities(s.toString());
            }
        } else {
            if (checkSolution(value)) {
                setSolution(currentColumn, currentRow,value);
                puzzleBlock[currentColumn][currentRow].setSolution(String.valueOf(solution[currentColumn][currentRow]));
            }
        }
    }

    void manageClearBlock() {
        if (solution[currentColumn][currentRow] == null) {
            for (int value = 0; value < numberOfBlocks; value++) {
                resetPossibility(currentColumn, currentRow, value);
            }
            puzzleBlock[currentColumn][currentRow].setPossibilities("");
        } else {
            setSolution(currentColumn, currentRow, null);
            puzzleBlock[currentColumn][currentRow].setSolution("");
        }
    }

    void manageFormulaDigits(@NotNull KeyCode keyCode) {
        int value = keyCode.getCode();
        if (value > 57) value -= 48;
        value -= 48;
        setNumberFormula(currentColumn,currentRow,value);
        puzzleBlock[currentColumn][currentRow].setFormula(getNumberFormula(currentColumn,currentRow).toString() + getOperatorFormula(currentColumn, currentRow));
    }

    void manageFormulaLetters(@NotNull KeyCode keyCode) {
        switch (keyCode) {
            case ADD:
                setOperatorFormula(currentColumn, currentRow, operators.A);
                break;
            case SUBTRACT:
                setOperatorFormula(currentColumn, currentRow, operators.S);
                break;
            case MULTIPLY:
                setOperatorFormula(currentColumn, currentRow, operators.M);
                break;
            case DIVIDE:
                setOperatorFormula(currentColumn, currentRow, operators.D);
                break;
        }
        puzzleBlock[currentColumn][currentRow].setFormula(getNumberFormula(currentColumn,currentRow).toString() + getOperatorFormula(currentColumn, currentRow));
    }

    void manageFormulaClearBlock() {
        setNumberFormula(currentColumn, currentRow, null);
        setOperatorFormula(currentColumn, currentRow, null);
        puzzleBlock[currentColumn][currentRow].setFormula("");
    }

}
