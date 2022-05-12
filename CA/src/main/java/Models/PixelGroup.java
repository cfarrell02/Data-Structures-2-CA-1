package Models;

/**
 * A class containing details on individual pixel groups
 */
public class PixelGroup {
    private int size,topRow,bottomRow,leftColumn,rightColumn,root;

    public PixelGroup(int size, int root) {
        this.size = size;
        this.root = root;

    }

    public void setBounds(int topRow,int bottomRow,int leftColumn,int rightColumn){
        this.topRow = topRow;
        this.bottomRow = bottomRow;
        this.leftColumn = leftColumn;
        this.rightColumn = rightColumn;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTopRow() {
        return topRow;
    }

    public void setTopRow(int topRow) {
        this.topRow = topRow;
    }

    public int getBottomRow() {
        return bottomRow;
    }

    public void setBottomRow(int bottomRow) {
        this.bottomRow = bottomRow;
    }

    public int getLeftColumn() {
        return leftColumn;
    }

    public void setLeftColumn(int leftColumn) {
        this.leftColumn = leftColumn;
    }

    public int getRightColumn() {
        return rightColumn;
    }

    public void setRightColumn(int rightColumn) {
        this.rightColumn = rightColumn;
    }

    public int getRoot() {
        return root;
    }

    public void setRoot(int root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return "PixelGroup{" +
                ", size=" + size +
                ", topRow=" + topRow +
                ", bottomRow=" + bottomRow +
                ", leftColumn=" + leftColumn +
                ", rightColumn=" + rightColumn +
                ", root=" + root +
                '}';
    }
}
