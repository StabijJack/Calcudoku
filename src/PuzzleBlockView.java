import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

class PuzzleBlockView {
    private final int size;
    private AnchorPane block;
    private Label solution;
    private Label possibilities;
    private Label formula;
    private boolean selected = false;
//    private boolean formulaBorderTop = false;
//    private boolean formulaBorderRight = false;
//    private boolean formulaBorderBottom = false;
//    private boolean formulaBorderLeft = false;

    PuzzleBlockView(int size) {
        this.size = size;
        setBlock();
    }

    AnchorPane getBlock() {
        return block;
    }

    private void setBlock() {
        block = new AnchorPane();
        block.getStyleClass().add("block");
        block.setId("block");
        int verticalGrid = size / 5;
        int fontSize = size / 10;

        formula = new Label();
        formula.setLayoutY(0);
        formula.setLayoutX(4);
        formula.setPrefHeight((verticalGrid) * 2);
        formula.setPrefWidth(size);
        formula.setFont(Font.font(fontSize * 2));
        formula.getStyleClass().add("formula");

        solution = new Label();
        solution.setLayoutY((verticalGrid) * 2);
        solution.setLayoutX(4);
        solution.setPrefHeight((verticalGrid) * 3);
        solution.setPrefWidth(size);
        solution.setFont(Font.font(fontSize * 6));
        solution.getStyleClass().add("solution");

        possibilities = new Label();
        possibilities.setLayoutY((verticalGrid) * 2);
        possibilities.setLayoutX(4);
        possibilities.setPrefHeight((verticalGrid) * 3);
        possibilities.setPrefWidth(size);
        possibilities.setFont(Font.font(fontSize * 2));
        possibilities.getStyleClass().add("possibilities");

        block.getChildren().addAll(formula, solution, possibilities);
        resetVisibilities();
    }

    private void resetVisibilities() {
        if (formula.getText().isBlank()) {
            formula.setVisible(false);
        } else {
            formula.setVisible(true);
        }
        if (solution.getText().isBlank()) {
            solution.setVisible(false);
            possibilities.setVisible(true);
        } else {
            solution.setVisible(true);
            possibilities.setVisible(false);
        }
    }

    private void resetSelected() {
        if (selected) {
            block.setId("blockSelected");
        } else {
            block.setId("");
        }
    }

    void setSelected(boolean select) {
        selected = select;
        resetSelected();
    }

    void setSolution(Integer s) {
        if (s != null)
            solution.setText(s.toString());
        else
            solution.setText("");
        resetVisibilities();
    }

    void SetSolutionError(Boolean error) {
        if (error) solution.getStyleClass().add("solution-error");
        else solution.getStyleClass().removeAll("solution-error");
    }

    void setPossibilities(String possibility) {
        possibilities.setText(possibility);
        resetVisibilities();
    }

    void setFormula(String s) {
        formula.setText(s);
        resetVisibilities();
    }

//    public void setFormulaBorderTop() {
//        formulaBorderTop = true;
//    }
//
//    public void setFormulaBorderRight() {
//        formulaBorderRight = true;
//    }
//
//    public void setFormulaBorderBottom() {
//        formulaBorderBottom = true;
//    }
//
//    public void setFormulaBorderLeft() {
//        formulaBorderLeft = true;
//    }
//
//    void resetFormulaBorders() {
//        formulaBorderTop = false;
//        formulaBorderRight = false;
//        formulaBorderBottom = false;
//        formulaBorderLeft = false;
//    }
//
//    void setBlockTopBorderColor(Color color) {
//        BorderStroke bs = block.getBorder().getStrokes().get(0);
//        block.setBorder(new Border(new BorderStroke(
//                color, bs.getRightStroke(), bs.getBottomStroke(), bs.getLeftStroke(),
////                bs.getTopStroke(), bs.getRightStroke(), bs.getBottomStroke(),bs.getLeftStroke(),
//                bs.getTopStyle(), bs.getRightStyle(), bs.getBottomStyle(), bs.getLeftStyle(),
//                bs.getRadii(), bs.getWidths(), bs.getInsets())));
//    }
//
//    void setBlockRightBorderColor(Color color) {
//        BorderStroke bs = block.getBorder().getStrokes().get(0);
//        block.setBorder(new Border(new BorderStroke(
//                bs.getTopStroke(), color, bs.getBottomStroke(), bs.getLeftStroke(),
////                bs.getTopStroke(), bs.getRightStroke(), bs.getBottomStroke(),bs.getLeftStroke(),
//                bs.getTopStyle(), bs.getRightStyle(), bs.getBottomStyle(), bs.getLeftStyle(),
//                bs.getRadii(), bs.getWidths(), bs.getInsets())));
//    }
//
//    void setBlockBottomBorderColor(Color color) {
//        BorderStroke bs = block.getBorder().getStrokes().get(0);
//        block.setBorder(new Border(new BorderStroke(
//                bs.getTopStroke(), bs.getRightStroke(), color, bs.getLeftStroke(),
////                bs.getTopStroke(), bs.getRightStroke(), bs.getBottomStroke(),bs.getLeftStroke(),
//                bs.getTopStyle(), bs.getRightStyle(), bs.getBottomStyle(), bs.getLeftStyle(),
//                bs.getRadii(), bs.getWidths(), bs.getInsets())));
//    }
//
//    void setBlockBottomLeftColor(Color color) {
//        BorderStroke bs = block.getBorder().getStrokes().get(0);
//        block.setBorder(new Border(new BorderStroke(
//                bs.getTopStroke(), bs.getRightStroke(), bs.getBottomStroke(), color,
////                bs.getTopStroke(), bs.getRightStroke(), bs.getBottomStroke(),bs.getLeftStroke(),
//                bs.getTopStyle(), bs.getRightStyle(), bs.getBottomStyle(), bs.getLeftStyle(),
//                bs.getRadii(), bs.getWidths(), bs.getInsets())));
//    }
}
