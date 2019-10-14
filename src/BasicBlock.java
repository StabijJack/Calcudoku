import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderWidths;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BasicBlock {
    private AnchorPane block;
    private TextField solution;
    private TextField possibilities;
    private TextField formula;
    private int size = 100;
    private boolean selected = false;

    BasicBlock() {
        setBlock();
    }

    BasicBlock(int size) {
        this.size = size;
        setBlock();
    }

    private void setBlock() {
        block = new AnchorPane();
        block.setStyle("-fx-border-color: green; -fx-border-width: 1px 1px 1px 1px");
        int verticalGrid = size/5;
        int fontSize =  size/10;

        formula = new TextField();
        formula.setLayoutY(0);
        formula.setPrefHeight((verticalGrid) * 2);
        formula.setPrefWidth(size);
        formula.setFont(Font.font("System",2 * fontSize));

        solution = new TextField();
        solution.setLayoutY((verticalGrid) * 2);
        solution.setPrefHeight((verticalGrid) * 3);
        solution.setPrefWidth(size);
        solution.setFont(Font.font("System", FontWeight.findByName("bold"), 2 * fontSize));
        solution.setAlignment(Pos.CENTER);
        solution.setStyle("-fx-text-fill: green");

        possibilities = new TextField();
        possibilities.setLayoutY((verticalGrid) * 2 );
        possibilities.setPrefHeight((verticalGrid) * 3);
        possibilities.setPrefWidth(size);
        possibilities.setFont(Font.font("System", fontSize));

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
//        if (selected){
//
//        }
//        else{
//
//        }
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
    AnchorPane getBlock() {
        return this.block;
    }

}
