public class Minimax {
    private static final int MAX_DEPTH = 3;
    private static final String PAWN = null;
    private static final String KNIGHT = null;
    private static final String ROOK = null;
    private static final String BISHOP = null;
    private static final String KING = null;
    private static final String QUEEN = null;
    int bestValue = Integer.MIN_VALUE;

    public Move findBestMove(ChessBoard chessBoard, Player player) {
        Move bestMove = null;

        for (Move move : chessBoard.getPossibleMoves(player)) {
            ChessBoard simulatedBoard = chessBoard.simulateMove(move);
            int value = minimax(simulatedBoard, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false, player);

            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            } 
        } 
        return bestMove; 
    }

    private int minimax(ChessBoard chessBoard, int depth, int alpha, int beta, boolean maximizingPlayer, Player player) {
        if (depth == 0 || chessBoard.isGameOver()) {
            return evaluate(chessBoard); 
        } 

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;

            for (Move move : chessBoard.getPossibleMoves(player)) {
                ChessBoard simulatedBoard = chessBoard.simulateMove(move);
                maxEval = Math.max(maxEval, minimax(simulatedBoard, depth - 1, alpha, beta, false, player));
                alpha = Math.max(alpha, maxEval);

                if (beta <= alpha) { break; } 
            } 
            return maxEval; 
        } else {
            int minEval = Integer.MAX_VALUE;

            for (Move move : chessBoard.getPossibleMoves(player.getOpponent())) {
                ChessBoard simulatedBoard = chessBoard.simulateMove(move);
                minEval = Math.min(minEval, minimax(simulatedBoard, depth - 1, alpha, beta, true, player));
                beta = Math.min(beta, minEval);

                if (beta <= alpha) { break; } 
            } 
            return minEval; 
        } 
    } 

    private int evaluate(ChessBoard chessBoard) {
        int score = 0;
        for (Piece[] row : chessBoard.getBoard()) {
            for (Piece piece : row) {
                if (piece != null) {
                    score += pieceValue(piece);
                }
            }
        }
        return score;
    }

    private int pieceValue(Piece piece) {
        switch (PieceType.getType()) {
            case PAWN: return 10;
            case KNIGHT: return 30;
            case BISHOP: return 30;
            case ROOK: return 50;
            case QUEEN: return 90;
            case KING: return Integer.MAX_VALUE; // Highest value for King
            default: return 0;
        }
    }
}