enum operators {NONE(""), ADD("+"), SUBTRACT("-"), MULTIPLY("*"), DIVIDE("/");
    final String name;
    operators(String s) {
        name=s;
    }
}
enum formulaResult { UNDECIDED, CORRECT, WRONG}
