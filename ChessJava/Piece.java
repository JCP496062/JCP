public abstract class Piece {
    protected Player player;

    public Piece(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board);
    
    public abstract String toString(); // For displaying pieces on the board
}