public class Move {
    private final int startRow;
    private final int startCol;
    private final int endRow;
    private final int endCol;
    private final Piece movedPiece;
    private final Piece capturedPiece;
 
    public Move(int startRow,int startCol,int endRow,int endCol,
                 Piece movedPiece,Piece capturedPiece) {
 
       this.startRow=startRow;
       this.startCol=startCol;
       this.endRow=endRow;
       this.endCol=endCol;
       this.movedPiece=movedPiece;
       this.capturedPiece=capturedPiece; 
    }
 
    public int getStartRow() { return startRow; }
    public int getStartCol() { return startCol; }
    public int getEndRow() { return endRow; }
    public int getEndCol() { return endCol; }
    
    public Piece getMovedPiece() { return movedPiece; }
    
    public Piece getCapturedPiece() { return capturedPiece; }
 }
