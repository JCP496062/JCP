import javax.swing.*;

import MiniMax.java.Minimax;
import Move.java.Move;
import Piece.java.Pawn;
import Piece.java.Piece;
import Player.java.Player;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// Player enumeration
enum Player {
    WHITE, BLACK;

    public Player getOpponent() {
        return this == WHITE ? BLACK : WHITE;
    }
}

// Abstract Piece class
abstract class Piece {
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

// Specific piece classes
class Pawn extends Piece {
    public Pawn(Player player) {
        super(player);
    }

    public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board) {
        int direction = (player == Player.WHITE) ? -1 : 1;
        if (startX + direction == endX && startY == endY && board.getPieceAt(endX, endY) == null) {
            return true; // Move forward
        }
        if (startX + direction == endX && Math.abs(startY - endY) == 1 && board.getPieceAt(endX, endY) != null) {
            return true; // Capture diagonally
        }
        return false;
    }

    public String toString() {
        return (player == Player.WHITE) ? "P" : "p";
    }
}

class Rook extends Piece {
    public Rook(Player player) {
        super(player);
    }

    public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board) {
        if (startX != endX && startY != endY) return false;
        if (startX == endX) { // Horizontal move
            for (int i = Math.min(startY, endY) + 1; i < Math.max(startY, endY); i++) {
                if (board.getPieceAt(startX, i) != null) return false;
            }
        } else { // Vertical move
            for (int i = Math.min(startX, endX) + 1; i < Math.max(startX, endX); i++) {
                if (board.getPieceAt(i, startY) != null) return false;
            }
        }
        return true;
    }

    public String toString() {
        return (player == Player.WHITE) ? "R" : "r";
    }
}

class Knight extends Piece {
    public Knight(Player player) {
        super(player);
    }

    public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board) {
        if ((Math.abs(startX - endX) == 2 && Math.abs(startY - endY) == 1)
                || (Math.abs(startX - endX) == 1 && Math.abs(startY - endY) == 2)) {
            return board.getPieceAt(endX, endY) == null || board.getPieceAt(endX, endY).getPlayer() != player;
        }
        return false;
    }

    public String toString() {
        return (player == Player.WHITE) ? "N" : "n";
    }
}

class Bishop extends Piece {
    public Bishop(Player player) {
        super(player);
    }

    public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board) {
        if (Math.abs(startX - endX) != Math.abs(startY - endY)) return false;

        // Ensure no pieces are in between
        int dx = (endX - startX > 0) ? 1 : -1;
        int dy = (endY - startY > 0) ? 1 : -1;

        for (int i = 1; i < Math.abs(endX - startX); i++) {
            if (board.getPieceAt(startX + i * dx, startY + i * dy) != null)
                return false;
        }
        
        return true;
    }

    public String toString() {
        return (player == Player.WHITE) ? "B" : "b";
    }
}

class Queen extends Piece {
    public Queen(Player player) {
        super(player);
    }

    public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board) {
        Rook rook = new Rook(player);
        Bishop bishop = new Bishop(player);
        
        return rook.isValidMove(startX, startY, endX, endY, board)
                || bishop.isValidMove(startX, startY, endX, endY, board);
    }

    public String toString() {
        return (player == Player.WHITE) ? "Q" : "q";
    }
}

class King extends Piece {
    public King(Player player) {
        super(player);
    }

    public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board) {
        if (Math.abs(startX - endX) <= 1 && Math.abs(startY - endY) <= 1)
            return board.getPieceAt(endX, endY) == null || board.getPieceAt(endX, endY).getPlayer() != player;

        return false;
    }

    public String toString() {
        return (player == Player.WHITE) ? "K" : "k";
    }
}

// Move class to handle moves
class Move {
    private final int startRow, startCol, endRow, endCol;
    private final Piece movedPiece;
    private final Piece capturedPiece;

    public Move(int startRow, int startCol, int endRow, int endCol,
                Piece movedPiece, Piece capturedPiece) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
    }

   // Getters for move details
   public int getStartRow() { return startRow; }
   public int getStartCol() { return startCol; }
   public int getEndRow() { return endRow; }
   public int getEndCol() { return endCol; }
   public Piece getMovedPiece() { return movedPiece; }
   public Piece getCapturedPiece() { return capturedPiece; }
}

// ChessBoard class to manage the game state
class ChessBoard {
   private final Piece[][] board;
   private final Stack<Move> moveHistory;

   public ChessBoard() {
       this.board = new Piece[8][8];
       this.moveHistory = new Stack<>();
       initializeBoard();
   }

   private void initializeBoard() {
       // Place Rooks
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

   // Move piece and store the move in history stack
   public void movePiece(int startX,int startY,int endX,int endY){
      Move move=new Move(startX,startY,endX,endY,
         board[startX][startY],board[endX][endY]);
      board[endX][endY]=board[startX][startY];
      board[startX][startY]=null;
      moveHistory.push(move); 
   }

   // Undo last move logic
   public void undoLastMove(){
      if(!moveHistory.isEmpty()){
         Move lastMove=moveHistory.pop();
         board[lastMove.getStartRow()][lastMove.getStartCol()]=lastMove.getMovedPiece();
         board[lastMove.getEndRow()][lastMove.getEndCol()]=lastMove.getCapturedPiece();
      }
   }

   // Check if a move is valid based on the player's turn
   public boolean isMoveValid(int startRow,int startCol,int targetRow,int targetCol,
                              Player player){
      Piece piece=board[startRow][startCol];
      if(piece==null || piece.getPlayer()!=player)return false;
      return piece.isValidMove(startRow,startCol,targetRow,targetCol,this); 
   }

   // Check for game over conditions like checkmate or stalemate
   public boolean isGameOver(){
      // Implement checkmate/stalemate detection logic here...
      return false; 
   }

   // Get list of possible moves for a given player
   public List<Move> getPossibleMoves(Player player){
      List<Move> moves=new ArrayList<>();
      for(int row=0;row<8;row++){
         for(int col=0;col<8;col++){
            Piece piece=board[row][col];
            if(piece!=null && piece.getPlayer()==player){
               for(int targetRow=0;targetRow<8;targetRow++){
                  for(int targetCol=0;targetCol<8;targetCol++){
                     if(piece.isValidMove(row,col,targetRow,targetCol,this)){
                        moves.add(new Move(row,col,targetRow,targetCol));
                     } 
                  } 
               } 
            } 
         } 
      } 
      return moves; 
   }

   // Simulate a move on the chessboard without modifying the actual state
   public ChessBoard simulateMove(Move move){
      ChessBoard simulatedBoard=new ChessBoard();
      // Logic to copy current state and apply the move...
      simulatedBoard.movePiece(move.getStartRow(),move.getStartCol(),
                                move.getEndRow(),move.getEndCol());
      return simulatedBoard; 
   }

   // Evaluation function for AI decision making based on material count etc.
   public int evaluate(Player player){
      // Basic evaluation function: count material values...
      return 0; 
   }
}

// Minimax class for AI logic
class Minimax{
   private static final int MAX_DEPTH=3;

   // Find best move using Minimax algorithm with alpha-beta pruning
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
}

// GUI class to manage user interface
public class ChessGUIth extends JFrame{
   private final ChessBoard chessBoard;
   private final JButton[][] squares;

   public ChessGUIth(){
      chessBoard=new ChessBoard();
      squares=new JButton[8][8];
      
      initializeUI(); 
      
      setupGUI(); 
      
      updateDisplay(); 
      
     setVisible(true);  
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
     pack();  
     setLocationRelativeTo(null);  
     
     updateDisplay();  
     
     setVisible(true);  
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
     pack();  
     setLocationRelativeTo(null);  
     
     updateDisplay();  
     
     setVisible(true);  
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
     pack();  
     setLocationRelativeTo(null);  
     
     updateDisplay();  
     
     setVisible(true);  
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
     pack();  
     setLocationRelativeTo(null);  
     
     updateDisplay();  
     
     setVisible(true);  
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
     pack();  
     setLocationRelativeTo(null);  
     
     updateDisplay();  
     
     setVisible(true);  
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
     pack();  
     setLocationRelativeTo(null);  
     
     updateDisplay();  
     
     setVisible(true);  
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
     pack();  
     setLocationRelativeTo(null);  

}

private void initializeUI(){
for(int row=0;row<8;row++){
for(int col=0;col<8;col++){
JButton button=new JButton();
button.setPreferredSize(new Dimension(60 ,60));
squares[row][col]=button;

final int r=row,c=col;

button.addActionListener(e->handlePlayerMove(r,c));}}}

private void setupGUI(){
JPanel panel=new JPanel(new GridLayout(8 ,8));
for(int row=0;row<8;row++){
for(int col=0;col<8;col++){
panel.add(squares[row][col]);}}add(panel);}
private void handlePlayerMove(int row,int col){
if(chessBoard.isMoveValid(currentStartRow,currentStartCol,row,col,currentPlayer)){
chessBoard.movePiece(currentStartRow,currentStartCol,row,col);

updateDisplay();

currentPlayer=currentPlayer.getOpponent();

botTurn();
}
}
private void botTurn(){
Minimax minimax=new Minimax();
Move bestMove=minimax.findBestMove(chessBoard,currentPlayer.getOpponent());

if(bestMove!=null){
chessBoard.movePiece(bestMove.getStartRow(),bestMove.getStartCol(),
bestMove.getEndRow(),bestMove.getEndCol());
updateDisplay();
currentPlayer=currentPlayer.getOpponent();
}
}
private void updateDisplay(){
for(int row=0 ;row<8 ;row++){
for(int col=0 ;col<8 ;col++){
Piece piece=chessBoard.getPieceAt(row,col);

if(piece!=null){
squares[row][col].setText(piece.toString());
}else{
squares[row][col].setText("");
}}}}
public static void main(String[] args){
SwingUtilities.invokeLater(ChessGUIth::new);}
}