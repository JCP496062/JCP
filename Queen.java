public class Queen extends Piece {
    public Queen(Player player) {
        super(player);
    }
 
    @Override 
    public boolean isValidMove(int startX,int startY,int endX,int endY,
                               ChessBoard board){
        Rook rook = new Rook(player);
        Bishop bishop = new Bishop(player);
        
        // A queen can move like both a rook and a bishop.
        return rook.isValidMove(startX,startY,endX,endY,board)
                || bishop.isValidMove(startX,startY,endX,endY,board);
    }
 
    @Override 
    public String toString(){
        return (player==Player.WHITE)? "Q": "q"; 
    }
 }