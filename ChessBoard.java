import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ChessBoard {
    private Piece[][] board;
    private Stack<Move> moveHistory;

    public ChessBoard() {
        this.board = new Piece[8][8];
        this.moveHistory = new Stack<>();
        initializeBoard();
    }

    private void initializeBoard() {
        // Initialize pieces for both players (white and black)
        board[0][0] = new Rook(Player.WHITE);
        board[0][7] = new Rook(Player.WHITE);
        
        board[7][0] = new Rook(Player.BLACK);
        board[7][7] = new Rook(Player.BLACK);

        // Place Knights
        board[0][1] = new Knight(Player.WHITE);
        board[0][6] = new Knight(Player.WHITE);
        
        board[7][1] = new Knight(Player.BLACK);
        board[7][6] = new Knight(Player.BLACK);

        // Place Bishops
        board[0][2] = new Bishop(Player.WHITE);
        board[0][5] = new Bishop(Player.WHITE);
        
        board[7][2] = new Bishop(Player.BLACK);
        board[7][5] = new Bishop(Player.BLACK);

        // Place Queens and Kings
        board[0][3] = new Queen(Player.WHITE);
        board[0][4] = new King(Player.WHITE);
        
        board[7][3] = new Queen(Player.BLACK);
        board[7][4] = new King(Player.BLACK);

        // Place Pawns
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(Player.WHITE);
            board[6][i] = new Pawn(Player.BLACK);
        }
    }

    public Piece getPieceAt(int row, int col) { 
        return row >= 0 && row < 8 && col >= 0 && col < 8 ? board[row][col] : null; 
    }

    public void movePiece(int startRow, int startCol, int targetRow, int targetCol) {
        Move move = new Move(startRow, startCol, targetRow, targetCol,
            board[startRow][startCol], board[targetRow][targetCol]);
        
        // Move the piece
        board[targetRow][targetCol] = board[startRow][startCol];
        board[startRow][startCol] = null;
        
        // Record the move in history
        moveHistory.push(move); 
    }

    public void undoLastMove() {
        if (!moveHistory.isEmpty()) {
            Move lastMove = moveHistory.pop();
            // Restore the moved piece and captured piece
            board[lastMove.getStartRow()][lastMove.getStartCol()] = lastMove.getMovedPiece();
            board[lastMove.getEndRow()][lastMove.getEndCol()] = lastMove.getCapturedPiece();
        }
    }

    public boolean canUndo() {
        return !moveHistory.isEmpty();
    }

    public List<Move> getPossibleMoves(Player player) {
        List<Move> moves = new ArrayList<>();
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.getPlayer() == player) {
                    for (int targetRow = 0; targetRow < 8; targetRow++) {
                        for (int targetCol = 0; targetCol < 8; targetCol++) {
                            if (piece.isValidMove(row, col, targetRow, targetCol, this)) {
                                moves.add(new Move(row, col, targetRow, targetCol, piece, getPieceAt(targetRow, targetCol)));
                            } 
                        } 
                    } 
                } 
            } 
        } 
        return moves; 
    }

    public boolean isGameOver() {
        // Implement checkmate/stalemate detection logic here...
        return false; 
    }

    public ChessBoard simulateMove(Move move) {
        ChessBoard simulatedBoard = new ChessBoard();
        
        // Logic to copy current state and apply the move...
        
        simulatedBoard.movePiece(move.getStartRow(), move.getStartCol(),
                                  move.getEndRow(), move.getEndCol());
        
        return simulatedBoard; 
    }

    // New method to get the current state of the chessboard
    public Piece[][] getBoard() {
        Piece[][] copyOfBoard = new Piece[8][8];
        
        for (int i = 0; i < 8; i++) {
            System.arraycopy(board[i], 0, copyOfBoard[i], 0, 8);
        }
        
        return copyOfBoard;
    }
}