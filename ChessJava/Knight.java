public class Knight extends Piece {
    public Knight(Player player) {
        super(player);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board) {
        // Knights move in an L shape: two squares in one direction and one square perpendicular.
        if ((Math.abs(startX - endX) == 2 && Math.abs(startY - endY) == 1)
                || (Math.abs(startX - endX) == 1 && Math.abs(startY - endY) == 2)) {
            return board.getPieceAt(endX, endY) == null || board.getPieceAt(endX, endY).getPlayer() != player;
        }
        
        return false; // Invalid move
    }

    @Override
    public String toString() {
        return (player == Player.WHITE) ? "N" : "n"; // Display 'N' for white and 'n' for black
    }
}