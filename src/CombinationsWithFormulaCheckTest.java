import org.junit.jupiter.api.Test;

class CombinationsWithFormulaCheckTest {
    @Test
    void test(){

        CombinationsWithFormulaCheck a = new CombinationsWithFormulaCheck(0,2);
        a.checkCombinations(2,operators.NONE,1,1);
        assert a.getCombinations().size() == 1;
        a.checkCombinations(2,operators.ADD,1,1);
        assert a.getCombinations().size() == 1;
        a.checkCombinations(2,operators.SUBTRACT,1,1);
        assert a.getCombinations().size() == 2;
        a.checkCombinations(2,operators.MULTIPLY,2,1);
        assert a.getCombinations().size() == 1;
        a.checkCombinations(2,operators.DIVIDE,2,1);
        assert a.getCombinations().size() == 1;

    }

}