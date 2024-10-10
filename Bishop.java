public class Bishop extends Piece {
    public Bishop(Player player) {
        super(player);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board) {
        if (Math.abs(startX - endX) != Math.abs(startY - endY)) return false; // Must move diagonally

        // Check for obstacles in the path of the move
        int dx = (endX - startX > 0) ? 1 : -1;
        int dy = (endY - startY > 0) ? 1 : -1;

        for (int i = 1; i < Math.abs(endX - startX); i++) {
            if (board.getPieceAt(startX + i * dx, startY + i * dy) != null)
                return false;
        }
        
        return true; // Valid move
    }

    @Override
    public String toString() {
        return (player == Player.WHITE) ? "B" : "b"; // Display 'B' for white and 'b' for black
    }
}