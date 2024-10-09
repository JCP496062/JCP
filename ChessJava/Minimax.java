public class Minimax {
    private static final int MAX_DEPTH = 3;
    int bestValue = Integer.MIN_VALUE;

    public Move findBestMove(ChessBoard chessBoard ,Player player){
        Move bestMove=null;

        for(Move move:chessBoard.getPossibleMoves(player)){
            ChessBoard simulatedBoard=chessBoard.simulateMove(move);
            int value=minimax(simulatedBoard ,MAX_DEPTH,Integer.MIN_VALUE,
                              Integer.MAX_VALUE,false ,player);

            if(value>bestValue){
                bestValue=value;
                bestMove=move;
            } 
        } 
        return bestMove; 
    }

    private int minimax(ChessBoard chessBoard ,int depth ,int alpha ,int beta,
                        boolean maximizingPlayer ,Player player){
        if(depth==0 || chessBoard.isGameOver()){
            return chessBoard.evaluate(player); 
        } 

        if(maximizingPlayer){
            int maxEval=Integer.MIN_VALUE;

            for(Move move:chessBoard.getPossibleMoves(player)){
                ChessBoard simulatedBoard=chessBoard.simulateMove(move);
                maxEval=Math.max(maxEval,minimax(simulatedBoard ,depth-1,
                                                   alpha,beta,false ,player));
                alpha=Math.max(alpha,maxEval);

                if(beta<=alpha){break;} 
            } 
            return maxEval; 
        }else{
            int minEval=Integer.MAX_VALUE;

            for(Move move:chessBoard.getPossibleMoves(player.getOpponent())){
                ChessBoard simulatedBoard=chessBoard.simulateMove(move);
                minEval=Math.min(minEval,minimax(simulatedBoard ,depth-1,
                                                   alpha,beta,true ,player));
                beta=Math.min(beta,minEval);

                if(beta<=alpha){break;} 
            } 
            return minEval; 
        } 
    } 

    private static final double INFINITY=Double.POSITIVE_INFINITY;

    private double minimax(ChessBoard board,int depth,double alpha,double beta,
                           boolean maximizingPlayer){
        if(depth==0 || game.isGameOver()){
            return evaluate(game);  
        } 

        if(maximizingPlayer){
            double maxEval=-INFINITY;

            for(Move move:game.getPossibleMoves()){
                game.makeMove(move);  
                double eval=minimax(game ,depth-1 ,alpha,beta,false);  
                game.undo();  
                maxEval=Math.max(maxEval ,eval);  
                alpha=Math.max(alpha,maxEval); 

                if(beta<=alpha){break;}  
            } 

            return maxEval;  
        }else{
            double minEval=INFINITY;

            for(Move move:game.getPossibleMoves()){
                game.makeMove(move);  
                double eval=minimax(game ,depth-1 ,alpha,beta,true);  
                game.undo();  
                minEval=Math.min(minEval ,eval);  
                beta=Math.min(beta,minEval); 

                if(beta<=alpha){break;}  
            } 

            return minEval;  
        } 
    } 

    private double evaluate(ChessGame game){
        double score=0.0;

        for(Piece piece:game.getPieces()){
            score+=pieceValue(piece);  
        } 

        return score;  
    } 

    private double pieceValue(Piece piece){
        switch(piece.getType()){
            case PAWN:return 10.0;
            case KNIGHT:return 30.0;
            case BISHOP:return 30.0;
            case ROOK:return 50.0;
            case QUEEN:return 90.0;
            case KING:return INFINITY;
            default:return 0.0;
        } 
    }     
}