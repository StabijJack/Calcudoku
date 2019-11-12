import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

class PuzzleBlockView {
    private BorderPane blockSelectBorder;
    private AnchorPane block;
    private Label solution;
    private Label possibilities;
    private Label formula;
    private boolean selected = false;

    PuzzleBlockView() {
        setBlock();
    }

    Pane getBlock() {
        return blockSelectBorder;
    }

    private void setBlock() {

        formula = new Label();
        formula.setLayoutY(0);
        formula.setLayoutX(Style.blockSelectedBorderWidth);
        formula.setPrefHeight((Style.blockRowHeight) * 2);
        formula.setPrefWidth(Style.blockSize);
        formula.setFont(Style.formulaFont);
        formula.setUnderline(Style.formulaUnderline);

        solution = new Label();
        solution.setLayoutY((Style.blockRowHeight) * 2);
        solution.setLayoutX(Style.blockSelectedBorderWidth);
        solution.setPrefHeight((Style.blockRowHeight) * 3);
        solution.setPrefWidth(Style.blockSize);
        solution.setFont(Style.solutionFont);
        solution.setAlignment(Style.solutionAlignment);

        possibilities = new Label();
        possibilities.setLayoutY((Style.blockRowHeight) * 2);
        possibilities.setLayoutX(Style.blockSelectedBorderWidth);
        possibilities.setPrefHeight((Style.blockRowHeight) * 3);
        possibilities.setPrefWidth(Style.blockSize);
        possibilities.setFont(Style.possibilitiesFont);
        possibilities.setWrapText(Style.possibilitiesTextWrap);

        block = new AnchorPane();
        block.setBorder(Style.blockBorder);
        block.getChildren().addAll(formula, solution, possibilities);

        blockSelectBorder = new BorderPane(block);
        blockSelectBorder.setBorder(Style.blockNotSelectedBorder);
        blockSelectBorder.setBackground(Style.blockNotSelectedBackground);

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
            blockSelectBorder.setBackground(Style.blockSelectedBackground);
            blockSelectBorder.setBorder(Style.blockSelectedBorder);
            blockSelectBorder.requestFocus();
        } else {
            blockSelectBorder.setBackground(Style.blockNotSelectedBackground);
            blockSelectBorder.setBorder(Style.blockNotSelectedBorder);
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
        if (error)
            solution.setTextFill(Style.solutionErrorFontColor);
        else solution.setTextFill(Style.solutionFontColor);
    }

    void setFormula(String s) {
        formula.setText(s);
        resetVisibilities();
    }

    void setBlockTopBorderColor(Color color) {
        BorderStroke bs = block.getBorder().getStrokes().get(0);
        block.setBorder(new Border(new BorderStroke(
                color, bs.getRightStroke(), bs.getBottomStroke(), bs.getLeftStroke(),
//                bs.getTopStroke(), bs.getRightStroke(), bs.getBottomStroke(),bs.getLeftStroke(),
                bs.getTopStyle(), bs.getRightStyle(), bs.getBottomStyle(), bs.getLeftStyle(),
                bs.getRadii(), bs.getWidths(), bs.getInsets())));
    }

    void setBlockRightBorderColor(Color color) {
        BorderStroke bs = block.getBorder().getStrokes().get(0);
        block.setBorder(new Border(new BorderStroke(
                bs.getTopStroke(), color, bs.getBottomStroke(), bs.getLeftStroke(),
//                bs.getTopStroke(), bs.getRightStroke(), bs.getBottomStroke(),bs.getLeftStroke(),
                bs.getTopStyle(), bs.getRightStyle(), bs.getBottomStyle(), bs.getLeftStyle(),
                bs.getRadii(), bs.getWidths(), bs.getInsets())));
    }

    void setBlockBottomBorderColor(Color color) {
        BorderStroke bs = block.getBorder().getStrokes().get(0);
        block.setBorder(new Border(new BorderStroke(
                bs.getTopStroke(), bs.getRightStroke(), color, bs.getLeftStroke(),
//                bs.getTopStroke(), bs.getRightStroke(), bs.getBottomStroke(),bs.getLeftStroke(),
                bs.getTopStyle(), bs.getRightStyle(), bs.getBottomStyle(), bs.getLeftStyle(),
                bs.getRadii(), bs.getWidths(), bs.getInsets())));
    }

    void setBlockLeftBorderColor(Color color) {
        BorderStroke bs = block.getBorder().getStrokes().get(0);
        block.setBorder(new Border(new BorderStroke(
                bs.getTopStroke(), bs.getRightStroke(), bs.getBottomStroke(), color,
//                bs.getTopStroke(), bs.getRightStroke(), bs.getBottomStroke(),bs.getLeftStroke(),
                bs.getTopStyle(), bs.getRightStyle(), bs.getBottomStyle(), bs.getLeftStyle(),
                bs.getRadii(), bs.getWidths(), bs.getInsets())));
    }

    public String getPossibilities() {
        return possibilities.getText();
    }

    void setPossibilities(String possibility) {
        possibilities.setText(possibility);
        resetVisibilities();
    }
}
