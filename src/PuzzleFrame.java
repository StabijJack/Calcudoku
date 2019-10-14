import javafx.scene.layout.GridPane;

class PuzzleFrame {
    private int maxNumber;
    private int startNumber;
    private GridPane puzzleFrame;


    PuzzleFrame(int maxNumber, int size){
        this(maxNumber,size,1);
    }
    PuzzleFrame(int maxNumber, int size,int startNumber){
        this.startNumber = startNumber;
        this.puzzleFrame = new GridPane();
        for (int i = 0; i < maxNumber + 1 - startNumber; i++) {
            for (int j = 0; j < maxNumber + 1 - startNumber; j++) {
                puzzleFrame.add(new BasicBlock(size).getBlock(), i, j);
            }
        }
    }
    GridPane getFrame(){
        return puzzleFrame;
    }
}
