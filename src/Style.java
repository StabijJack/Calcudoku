import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

class Style {
    private static final String fontFamily = "Courier New";

    private static final double rows = 5;
    private static final double fontScale = 10;
    private static final double formulaScale = 2;
    private static final double solutionScale = 6;
    private static final double possibilitiesScale = 1.5;


    static final double blockSelectedBorderWidth = 1;
    private static final double blockBorderWidth = 3;

    private static final Color blockSelectedBorderColor = Color.RED;
    private static final Color blockNotSelectedBorderColor = Color.GREEN;
    private static final Color blockSelectedBorderBackgroundColor = Color.LAVENDER;
    private static final Color blockNotSelectedBorderBackgroundColor = Color.WHITE;

    static final Color blockBorderColor = Color.TRANSPARENT;
    static final Color blockFormulaBorderColor = Color.BLUE;

    static final Color solutionFontColor = Color.GREEN;
    static final Color solutionErrorFontColor = Color.RED;
    private static final FontWeight solutionFontWeight = FontWeight.BOLD;
    static final Pos solutionAlignment = Pos.CENTER;

    private static final FontWeight formulaFontWeight = FontWeight.BOLD;
    static final boolean formulaUnderline = true;

    private static final FontWeight possibilitiesFontWeight = FontWeight.NORMAL;
    static final boolean possibilitiesTextWrap = true;

    static double blockSize;
    static Border blockBorder;
    static double blockRowHeight;
    static Border blockSelectedBorder;
    static Border blockNotSelectedBorder;
    static Background blockSelectedBackground;
    static Background blockNotSelectedBackground;
    static Font solutionFont;
    static Font formulaFont;
    static Font possibilitiesFont;


    public Style(double size) {
        blockSize = size;
        blockRowHeight = size / rows;
        double fontReferenceSize = size / fontScale;
        double formulaFontSize = fontReferenceSize * formulaScale;
        double solutionFontSize = fontReferenceSize * solutionScale;
        double possibilitiesFontSize = fontReferenceSize * possibilitiesScale;
        blockBorder = new Border(new BorderStroke(
                Style.blockBorderColor,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(blockBorderWidth)));
        blockSelectedBorder = new Border(new BorderStroke(
                blockSelectedBorderColor,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(blockSelectedBorderWidth)));
        blockNotSelectedBorder = new Border(new BorderStroke(
                blockNotSelectedBorderColor,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(blockSelectedBorderWidth)));
        blockSelectedBackground = new Background(
                new BackgroundFill(
                        Style.blockSelectedBorderBackgroundColor,
                        CornerRadii.EMPTY,
                        Insets.EMPTY));
        blockNotSelectedBackground = new Background(
                new BackgroundFill(
                        Style.blockNotSelectedBorderBackgroundColor,
                        CornerRadii.EMPTY,
                        Insets.EMPTY));
        solutionFont = Font.font(fontFamily, solutionFontWeight, solutionFontSize);
        formulaFont = Font.font(fontFamily, formulaFontWeight, formulaFontSize);
        possibilitiesFont = Font.font(fontFamily, possibilitiesFontWeight, possibilitiesFontSize);
    }
}

