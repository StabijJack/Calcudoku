class BlockPosition {
    private final int column;
    private final int row;

    public BlockPosition(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public boolean areNeighbors(BlockPosition blockPosition){
        if (Math.abs(this.column - blockPosition.column) <= 1 & this.row == blockPosition.row) return true;
        return Math.abs(this.row - blockPosition.row) <= 1 & this.column == blockPosition.column;
    }
    boolean isEqual(BlockPosition blockPosition){
        return column == blockPosition.column & row == blockPosition.row;
    }
}
