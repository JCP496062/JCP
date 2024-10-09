public class Rook extends Piece {
    public Rook(Player player) {
        super(player);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board) {
        if (startX != endX && startY != endY) return false; // Must move in a straight line

        // Check for obstacles in the path of the move
        if (startX == endX) { // Horizontal move
            for (int i = Math.min(startY, endY) + 1; i < Math.max(startY, endY); i++) {
                if (board.getPieceAt(startX, i) != null) return false;
            }
        } else { // Vertical move
            for (int i = Math.min(startX, endX) + 1; i < Math.max(startX, endX); i++) {
                if (board.getPieceAt(i, startY) != null) return false;
            }
        }
        
        return true; // Valid move
    }

    @Override
    public String toString() {
        return (player == Player.WHITE) ? "R" : "r"; // Display 'R' for white and 'r' for black
    }
}