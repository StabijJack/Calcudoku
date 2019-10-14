import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

class BasicBlock {
    private AnchorPane block;
    private Label solution;
    private Label possibilities;
    private Label formula;
    private int size = 100;
    private boolean selected = false;
    private static String backgroundSelected = "-fx-background-color: lavender;";
    private static String backgroundNotSelected = "-fx-background-color: white;";
    private static String blockBorderSelected = "-fx-border-color: red; -fx-border-width: 3px";
    private static String blockBorderNotSelected = "-fx-border-color: green; -fx-border-width: 3px";
    private static String fontFamily = "System";
    private static String solutionFalse = "-fx-text-fill: red";
    private static String solutionTrue = "-fx-text-fill: green";



    BasicBlock() {
        setBlock();
    }

    BasicBlock(int size) {
        this.size = size;
        setBlock();
    }

    private void setBlock() {
        block = new AnchorPane();
        block.setStyle(backgroundNotSelected + blockBorderNotSelected);
        int verticalGrid = size/5;
        int fontSize =  size/10;

        formula = new Label();
        formula.setLayoutY(0);
        formula.setLayoutX(4);
        formula.setPrefHeight((verticalGrid) * 2);
        formula.setPrefWidth(size);
        formula.setFont(Font.font(fontFamily,2 * fontSize));

        solution = new Label();
        solution.setLayoutY((verticalGrid) * 2);
        solution.setLayoutX(4);
        solution.setPrefHeight((verticalGrid) * 3);
        solution.setPrefWidth(size);
        solution.setFont(Font.font(fontFamily, FontWeight.BOLD, 2 * fontSize));
        solution.setAlignment(Pos.CENTER);

        possibilities = new Label();
        possibilities.setLayoutY((verticalGrid) * 2 );
        possibilities.setLayoutX(4);
        possibilities.setPrefHeight((verticalGrid) * 3);
        possibilities.setPrefWidth(size);
        possibilities.setFont(Font.font(fontFamily, fontSize));

        block.getChildren().addAll(formula, solution, possibilities);
        resetVisibilities();
    }
    private void resetVisibilities(){
        if (formula.getText().isBlank()){
            formula.setVisible(false);
        }
        else{
            formula.setVisible(true);
        }
        if (solution.getText().isBlank()){
            solution.setVisible(false);
            possibilities.setVisible(true);
        }
        else{
            solution.setVisible(true);
            possibilities.setVisible(false);
        }
    }
    private void resetSelected(){
        if (selected){
            block.setStyle(backgroundSelected + blockBorderSelected);
            solution.setStyle(backgroundSelected);
            possibilities.setStyle(backgroundSelected);
        }
        else{
            block.setStyle(backgroundNotSelected + blockBorderNotSelected);
            solution.setStyle(backgroundNotSelected);
            possibilities.setStyle(backgroundNotSelected);
        }
    }
    void setFormula(String formula){
        this.formula.setText(formula);
        resetVisibilities();
    }
    void setSolution(String solution){
        this.solution.setText(solution);
        resetVisibilities();
    }
    void setPossibilities(String possibilities){
        this.possibilities.setText(possibilities);
        resetVisibilities();
    }
    void setSelected(boolean selected){
        this.selected = selected;
        resetSelected();
    }
    void setSolution(boolean correct){
        if (correct){
            solution.setStyle(solutionTrue);
        }
        else {
            solution.setStyle(solutionFalse);
        }
    }
    AnchorPane getBlock() {
        return this.block;
    }

}
