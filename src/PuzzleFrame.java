import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

class PuzzleFrame {
    private final int maxNumber;
    private final int startNumber;
    @NotNull
    private final BasicBlock[][] puzzleBlock;
    private final int numberOfBlocks;
    private final int maxIndex;
    private int currentRow = 0;
    private int currentColumn = 0;
    private boolean playMode;

    private final Integer[][] solution;
    private final boolean[][][] possibilities;
    private final Integer[][] numberFormula;
    private final char[][] operatorFormula;


    PuzzleFrame(int maxNumber, int size, int startNumber) {
        this.startNumber = startNumber;
        this.maxNumber = maxNumber;
        this.numberOfBlocks = maxNumber + 1 - startNumber;
        this.maxIndex = numberOfBlocks - 1;
        this.solution = new Integer [numberOfBlocks][numberOfBlocks];
        this.possibilities = new boolean[numberOfBlocks][numberOfBlocks][maxNumber];
        this.numberFormula = new Integer[numberOfBlocks][numberOfBlocks];
        this.operatorFormula = new char[numberOfBlocks][numberOfBlocks];

        puzzleBlock = new BasicBlock[this.numberOfBlocks][this.numberOfBlocks];
        for (int x = 0; x < this.numberOfBlocks; x++) {
            for (int y = 0; y < this.numberOfBlocks; y++) {
                puzzleBlock[x][y] = new BasicBlock(size);
            }
        }
        puzzleBlock[currentColumn][currentRow].setSelected(true);
    }

    @NotNull GridPane getFrame() {
        GridPane puzzleFrame = new GridPane();
        for (int x = 0; x < this.numberOfBlocks; x++) {
            for (int y = 0; y < this.numberOfBlocks; y++) {
                puzzleFrame.add(puzzleBlock[x][y].getBlock(), x, y);
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
                currentColumn = maxIndex;
                break;
            case PAGE_UP:
                currentRow = 0;
                break;
            case PAGE_DOWN:
                currentRow = maxIndex;
                break;
        }

        currentRow = (currentRow + numberOfBlocks) % numberOfBlocks;
        currentColumn = (currentColumn + numberOfBlocks) % numberOfBlocks;
        puzzleBlock[currentColumn][currentRow].setSelected(true);

    }

    void manageMouse(double mouseX, double mouseY) {
        puzzleBlock[currentColumn][currentRow].setSelected(false);
        for (currentColumn = 0; currentColumn <= maxIndex; currentColumn++) {
            if (mouseX < puzzleBlock[currentColumn][0].getBlock().getLayoutX() + puzzleBlock[0][currentRow].getBlock().getWidth()) {
                break;
            }
        }
        for (currentRow = 0; currentRow <= maxIndex; currentRow++) {
            if (mouseY < puzzleBlock[0][currentRow].getBlock().getLayoutY() + puzzleBlock[0][currentRow].getBlock().getHeight()) {
                break;
            }
        }
        puzzleBlock[currentColumn][currentRow].setSelected(true);


    }

    void manageInputBlock(@NotNull KeyCode keyCode, boolean shiftDown) {
        int c = keyCode.getCode();
        if (keyCode.isDigitKey()){
            if (c > 57) c -= 48;
            c -= 48;
        }
        else{ //isLetterKey()
            c -= 55;
        }
        if (shiftDown) {
            if (checkPossibility(c)) {
                possibilities[currentColumn][currentRow][c]=!possibilities[currentColumn][currentRow][c];
                StringBuilder s = new StringBuilder();
                for (int p = 0; p < numberOfBlocks; p++) {
                    if(possibilities[currentColumn][currentRow][p]) s.append(" ").append(p);
                }
                puzzleBlock[currentColumn][currentRow].setPossibilities(s.toString());
            }
        } else {
            if (checkSolution(c)) {
                solution[currentColumn][currentRow] = c;
                puzzleBlock[currentColumn][currentRow].setSolution(String.valueOf(solution[currentColumn][currentRow]));
            }
        }


    }

    void manageClearBlock() {
        if (solution[currentColumn][currentRow] == null) {
            for (int v = 0; v < numberOfBlocks; v++) {
                possibilities[currentColumn][currentRow][v]=false;
            }
            puzzleBlock[currentColumn][currentRow].setPossibilities("");
        } else {
            solution[currentColumn][currentRow]= null;
            puzzleBlock[currentColumn][currentRow].setSolution("");

        }
    }

    void manageFormulaDigits(@NotNull KeyCode keyCode) {
        int c = keyCode.getCode();
        if (c > 57) c -= 48;
        c -= 48;
        if (numberFormula[currentColumn][currentRow] == null){
            numberFormula[currentColumn][currentRow] = c;
        }
        else{
            numberFormula[currentColumn][currentRow] = numberFormula[currentColumn][currentRow]*10 + c;
        }
        puzzleBlock[currentColumn][currentRow].setFormula(numberFormula[currentColumn][currentRow].toString() + operatorFormula[currentColumn][currentRow]);
    }

    void manageFormulaLetters(@NotNull KeyCode keyCode) {
        char c = ' ';
        switch (keyCode) {
            case ADD:
                c = '+';
                break;
            case SUBTRACT:
                c = '-';
                break;
            case MULTIPLY:
                c = 'x';
                break;
            case DIVIDE:
                c = ':';
                break;
        }
        operatorFormula[currentColumn][currentRow] = c;
        puzzleBlock[currentColumn][currentRow].setFormula(numberFormula[currentColumn][currentRow].toString() + operatorFormula[currentColumn][currentRow]);
    }

    void manageFormulaClearBlock() {
        numberFormula[currentColumn][currentRow] = null;
        operatorFormula[currentColumn][currentRow]= ' ';
        puzzleBlock[currentColumn][currentRow].setFormula("");
    }

    @Contract(pure = true)
    private boolean checkSolution(int solution) {
        return solution <= maxNumber && solution >= startNumber;
    }

    @Contract(pure = true)
    private boolean checkPossibility(int possibility) {
        return possibility <= maxNumber && possibility >= startNumber;
    }
}
