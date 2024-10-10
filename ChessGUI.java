import javax.swing.*;
import java.awt.*;

public class ChessGUI extends JFrame {
     private ChessBoard chessBoard;
     private JButton[][] squares;
     private Player currentPlayer;

     public ChessGUI() {
          chessBoard=new ChessBoard();
          squares=new JButton[8][8];
          currentPlayer=Player.WHITE;

          initializeUI();   
          setupGUI();   
          updateDisplay();   
          setVisible(true);   
          setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
          pack();   
          setLocationRelativeTo(null);   
     }

     private void initializeUI(){
          for(int row=0 ;row<8 ;row++){
              for(int col=0 ;col<8 ;col++){
                  JButton button=new JButton();
                  button.setPreferredSize(new Dimension(60 ,60));
                  squares[row][col]=button;

                  final int r=row,c=col;

                  button.addActionListener(e->handlePlayerMove(r,c));
              }
          }
     }

     private void setupGUI(){
          JPanel panel=new JPanel(new GridLayout(8 ,8));
          for(int row=0 ;row<8 ;row++){
              for(int col=0 ;col<8 ;col++){
                  panel.add(squares[row][col]);
              }
          }
          add(panel);   
          pack();   
          setVisible(true);   
     }

     private void handlePlayerMove(int row,int col){
          // Implement logic to handle player's selection of a piece and its movement.
          
          botTurn(); // After player's turn let bot make its turn.
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

                  squares[row][col].setText(piece!=null?piece.toString():"");
              }
          }
     }

     public static void main(String[] args){
          SwingUtilities.invokeLater(ChessGUI::new);  
     }
}