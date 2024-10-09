public class King extends Piece {
    public King(Player player){
        super(player);
    }
 
    @Override 
    public boolean isValidMove(int startX,int startY,int endX,int endY,
                               ChessBoard board){
        if(Math.abs(startX-endX)<=1 && Math.abs(startY-endY)<=1){
            return board.getPieceAt(endX,endY)==null || 
                   board.getPieceAt(endX,endY).getPlayer()!=player;
        }
        return false; 
    }
 
    @Override 
    public String toString(){
        return (player==Player.WHITE)? "K": "k"; 
    }
 }