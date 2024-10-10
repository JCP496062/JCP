public abstract class Piece {
    protected Player player;
    protected PieceType type; // Add a field for the type of piece

    public Piece(Player player, PieceType type) {
        this.player = player;
        this.type = type; // Initialize the type
    }

    public Player getPlayer() {
        return player;
    }

// Enum for piece types
public enum PieceType {
    PAWN,
    KNIGHT,
    BISHOP,
    ROOK,
    QUEEN,
    KING
}

    public PieceType getType() {
        return this.type; // Method to get the piece type
    }

    public abstract boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board);
    
    public abstract String toString(); // For displaying pieces on the board
}


