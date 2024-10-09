public class Pawn extends Piece {
    public Pawn(Player player) {
        super(player);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board) {
        int direction = (player == Player.WHITE) ? -1 : 1; // White moves up, Black moves down

        // Move forward by one square
        if (startX + direction == endX && startY == endY && board.getPieceAt(endX, endY) == null) {
            return true;
        }
        
        // Capture diagonally
        if (startX + direction == endX && Math.abs(startY - endY) == 1 && board.getPieceAt(endX, endY) != null) {
            return true;
        }
        
        return false; // Invalid move
    }

    @Override
    public String toString() {
        return (player == Player.WHITE) ? "P" : "p"; // Display 'P' for white and 'p' for black
    }
}