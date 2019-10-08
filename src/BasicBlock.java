import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
        block.setPrefWidth(this.size);
        block.setPrefHeight(this.size);

        formula = new TextField();
        formula.setLayoutY(0);
        formula.setPrefHeight(size / 5);
        formula.setPrefWidth(this.size);
        formula.setFont(Font.font("System", FontWeight.findByName("bold"),12 * this.size / 100));

        solution = new TextField();
        solution.setLayoutY(this.size / 5);
        solution.setPrefHeight(this.size / 5 * 4);
        solution.setPrefWidth(this.size);
        solution.setPromptText("Solution");
        solution.setFont(Font.font("System", FontWeight.findByName("bold"), 32 * this.size / 100));
        solution.setAlignment(Pos.CENTER);
        solution.setStyle("-fx-text-fill: green");

        possibilities = new TextField();
        possibilities.setLayoutY(this.size / 5 );
        possibilities.setPrefHeight(this.size / 5 * 4);
        possibilities.setPrefWidth(this.size);
        possibilities.setPromptText("Suggestion");
        possibilities.setFont(Font.font("System", 12 * this.size / 100));

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

        }
        else{

        }
    }
    public void setFormula(String formula){
        this.formula.setText(formula);
        resetVisibilities();
    }
    public void setSolution(String solution){
        this.solution.setText(solution);
        resetVisibilities();
    }
    public void setPossibilities(String possibilities){
        this.possibilities.setText(possibilities);
        resetVisibilities();
    }
    public void setSelected(boolean selected){
        this.selected = selected;
        resetSelected();
    }
    public AnchorPane getBlock() {
        return this.block;
    }

}
