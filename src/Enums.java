enum operators {NONE(""), ADD("+"), SUBTRACT("-"), MULTIPLY("*"), DIVIDE("/");
    String name;
    operators(String s) {
        name=s;
    }
}
enum formulaResult { UNDECIDED, CORRECT, WRONG}
