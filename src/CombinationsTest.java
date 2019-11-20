class CombinationsTest {
    @org.junit.jupiter.api.Test
    void createCombinations() {
        Combinations combinations = new Combinations(1,1);
        combinations.getCombinations(2,1);
        assert combinations.getPossibleNumbers().length == 1;
        combinations = new Combinations(1,1);
        combinations.getCombinations(3,1);
        assert combinations.getPossibleNumbers().length == 1;
        combinations = new Combinations(1,1);
        combinations.getCombinations(3,2);
        assert combinations.getPossibleNumbers().length == 1;
        combinations = new Combinations(0,1);
        combinations.getCombinations(3,1);
        assert combinations.getPossibleNumbers().length == 2;
        combinations = new Combinations(0,1);
        combinations.getCombinations(3,1);
        assert combinations.getPossibleNumbers().length == 2;
        combinations = new Combinations(0,1);
        combinations.getCombinations(3,2);
        assert combinations.getPossibleNumbers().length == 2;
        combinations = new Combinations(0,2);
        combinations.getCombinations(3,1);
        assert combinations.getPossibleNumbers().length == 3;
        assert combinations.getCombinations().size() == 1;
        combinations = new Combinations(1,4);
        combinations.getCombinations(2,1);
        assert combinations.getPossibleNumbers().length == 4;
        assert combinations.getCombinations().size() == 6;
        combinations = new Combinations(1,9);
        combinations.getCombinations(2,1);
        assert combinations.getPossibleNumbers().length == 9;
        assert combinations.getCombinations().size() == 36;
        combinations.getCombinations(3,1);
        assert combinations.getPossibleNumbers().length == 9;
        assert combinations.getCombinations().size() == 84;
        combinations.getCombinations(3,1);
        assert combinations.getPossibleNumbers().length == 9;
        assert combinations.getCombinations().size() == 84;
//        System.out.println(combinations.toString());

//        ArrayList a = new Combinations(1,9,3,1).getCombinations();


    }
}