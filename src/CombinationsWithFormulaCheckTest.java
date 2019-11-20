import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class CombinationsWithFormulaCheckTest {
    @Test
    void test(){
        CombinationsWithFormulaCheck a = new CombinationsWithFormulaCheck(0,2,2,operators.NONE,1);
        assert a.getCombinations().size() == 1;
        a = new CombinationsWithFormulaCheck(0,2,2,operators.ADD,1);
        assert a.getCombinations().size() == 1;
        a = new CombinationsWithFormulaCheck(0,2,2,operators.SUBTRACT,1);
        assert a.getCombinations().size() == 2;
        a = new CombinationsWithFormulaCheck(0,2,2,operators.MULTIPLY,2);
        assert a.getCombinations().size() == 1;
        a = new CombinationsWithFormulaCheck(0,2,2,operators.DIVIDE,2);
        assert a.getCombinations().size() == 1;

    }

}