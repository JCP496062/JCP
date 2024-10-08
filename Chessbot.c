#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define BOARD_SIZE 8
#define MAX_DEPTH 3

typedef enum {
    EMPTY,
    WHITE_PAWN, WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING,
    BLACK_PAWN, BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING
} Piece;

typedef struct {
    Piece board[BOARD_SIZE][BOARD_SIZE];
    int whiteToMove; // 1 for white's turn, 0 for black's turn
    int castlingRights; // 1 = White can castle kingside, 2 = White can castle queenside
                         // 4 = Black can castle kingside, 8 = Black can castle queenside
} GameState;

// Function prototypes
void initializeBoard(GameState *game);
void printBoard(const GameState *game);
int minimax(GameState *game, int depth, int isMaximizingPlayer);
int evaluateBoard(const GameState *game);
int possibleMoves(GameState *game);
void makeMove(GameState *game, int fromRow, int fromCol, int toRow, int toCol);
void undoMove(GameState *game, int fromRow, int fromCol, int toRow, int toCol);
int isGameOver(const GameState *game);
int isCheck(const GameState *game);
int isCheckmate(const GameState *game);
int isStalemate(const GameState *game);
int canMoveTo(GameState *game, int fromRow, int fromCol, int toRow, int toCol);
void getKingPosition(const GameState *game, int *kingRow, int *kingCol);

// Main function
int main() {
    GameState game;
    initializeBoard(&game);

    while (1) {
        printBoard(&game);
        if (isGameOver(&game)) {
            printf("Game Over!\n");
            break;
        }

        // Call Minimax for AI move (this is simplified)
        // Here you would implement logic to choose the best move based on Minimax
        // For simplicity in this example we will just print the current state.
        
        // Handle player input here (you would get this from your JS front end)
        // For example: makeMove(&game, fromRow, fromCol, toRow, toCol);

        // Simulate a move for testing purposes (you can replace this with actual user input)
        makeMove(&game, 6, 4, 4, 4); // Move white pawn forward
    }

    return 0;
}

// Initialize the chess board
void initializeBoard(GameState *game) {
    memset(game->board, EMPTY, sizeof(game->board));
    game->whiteToMove = 1; // White starts first
    game->castlingRights = 3; // Both sides can castle

    // Place pieces on the board
    game->board[0][0] = game->board[0][7] = BLACK_ROOK;
    game->board[0][1] = game->board[0][6] = BLACK_KNIGHT;
    game->board[0][2] = game->board[0][5] = BLACK_BISHOP;
    game->board[0][3] = BLACK_QUEEN;
    game->board[0][4] = BLACK_KING;

    for (int i = 0; i < BOARD_SIZE; i++) {
        game->board[1][i] = BLACK_PAWN; // Black Pawns
        game->board[6][i] = WHITE_PAWN; // White Pawns
    }

    game->board[7][0] = game->board[7][7] = WHITE_ROOK;
    game->board[7][1] = game->board[7][6] = WHITE_KNIGHT;
    game->board[7][2] = game->board[7][5] = WHITE_BISHOP;
    game->board[7][3] = WHITE_QUEEN;
    game->board[7][4] = WHITE_KING;
}

// Print the chess board
void printBoard(const GameState *game) {
    for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
            switch (game->board[i][j]) {
                case WHITE_PAWN: printf("P "); break;
                case WHITE_ROOK: printf("R "); break;
                case WHITE_KNIGHT: printf("N "); break;
                case WHITE_BISHOP: printf("B "); break;
                case WHITE_QUEEN: printf("Q "); break;
                case WHITE_KING: printf("K "); break;
                case BLACK_PAWN: printf("p "); break;
                case BLACK_ROOK: printf("r "); break;
                case BLACK_KNIGHT: printf("n "); break;
                case BLACK_BISHOP: printf("b "); break;
                case BLACK_QUEEN: printf("q "); break;
                case BLACK_KING: printf("k "); break;
                default: printf(". ");
            }
        }
        printf("\n");
    }
}

// Minimax algorithm implementation
int minimax(GameState *game, int depth, int isMaximizingPlayer) {
    if (depth == MAX_DEPTH || isGameOver(game)) return evaluateBoard(game);

    if (isMaximizingPlayer) {
        int bestValue = -10000;

        for (int fromRow = 0; fromRow < BOARD_SIZE; fromRow++) {
            for (int fromCol = 0; fromCol < BOARD_SIZE; fromCol++) {
                if ((game->whiteToMove && game->board[fromRow][fromCol] >= WHITE_PAWN && 
                     game->board[fromRow][fromCol] <= WHITE_KING) ||
                    (!game->whiteToMove && game->board[fromRow][fromCol] >= BLACK_PAWN && 
                     game->board[fromRow][fromCol] <= BLACK_KING)) {

                    for (int toRow = 0; toRow < BOARD_SIZE; toRow++) {
                        for (int toCol = 0; toCol < BOARD_SIZE; toCol++) {
                            if (canMoveTo(game, fromRow, fromCol, toRow, toCol)) {
                                makeMove(game, fromRow, fromCol, toRow, toCol);
                                bestValue = max(bestValue, minimax(game, depth + 1, 0));
                                undoMove(game, fromRow, fromCol, toRow, toCol);
                            }
                        }
                    }
                }
            }
        }
        return bestValue;
    } else {
        int bestValue = 10000;

        for (int fromRow = 0; fromRow < BOARD_SIZE; fromRow++) {
            for (int fromCol = 0; fromCol < BOARD_SIZE; fromCol++) {
                if ((!game->whiteToMove && game->board[fromRow][fromCol] >= WHITE_PAWN && 
                     game->board[fromRow][fromCol] <= WHITE_KING) ||
                    (game->whiteToMove && game->board[fromRow][fromCol] >= BLACK_PAWN && 
                     game->board[fromRow][fromCol] <= BLACK_KING)) {

                    for (int toRow = 0; toRow < BOARD_SIZE; toRow++) {
                        for (int toCol = 0; toCol < BOARD_SIZE; toCol++) {
                            if (canMoveTo(game, fromRow, fromCol, toRow, toCol)) {
                                makeMove(game, fromRow, fromCol, toRow, toCol);
                                bestValue = min(bestValue, minimax(game, depth + 1, 1));
                                undoMove(game, fromRow, fromCol, toRow, toCol);
                            }
                        }
                    }
                }
            }
        }
        return bestValue;
    }
}

// Evaluate the board position
int evaluateBoard(const GameState *game) {
    int score = 0;

    for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
            switch (game->board[i][j]) {
                case WHITE_PAWN: score += 10; break;
                case WHITE_ROOK: score += 50; break;
                case WHITE_KNIGHT: score += 30; break;
                case WHITE_BISHOP: score += 30; break;
                case WHITE_QUEEN: score += 90; break;
                case WHITE_KING: score += 900; break;

                case BLACK_PAWN: score -= 10; break;
                case BLACK_ROOK: score -= 50; break;
                case BLACK_KNIGHT: score -= 30; break;
                case BLACK_BISHOP: score -= 30; break;
                case BLACK_QUEEN: score -= 90; break;
                case BLACK_KING: score -= 900; break;

                default: break;
            }
        }
    }

    return score;
}

// Check if the game is over
int isGameOver(const GameState *game) {
    return isCheckmate(game) || isStalemate(game);
}

// Check if the current player is in check
int isCheck(const GameState *game) {
   int kingRow = -1;
   int kingCol = -1;

   getKingPosition(game,(kingRow,&kingCol));

   // Check if any opponent piece can attack the king's position
   for(int row=0 ; row<BOARD_SIZE ; row++){
       for(int col=0 ; col<BOARD_SIZE ; col++){
           Piece piece= game->board[row][col];

           if((piece >= BLACK_PAWN && piece <= BLACK_KING && !game->whiteToMove) || 
              (piece >= WHITE_PAWN && piece <= WHITE_KING && game->whiteToMove)){
               continue ; // Skip own pieces
           }

           if(canMoveTo(game,row,col , kingRow , kingCol)){
               return 1 ; // King is in check
           }
       }
   }

   return 0 ; // King is not in check
}

// Check if the current player is in checkmate
int isCheckmate(const GameState *game) {
   return isCheck(game) && !possibleMoves(game); 
}

// Check if the current player is in stalemate
int isStalemate(const GameState *game) {
   return !isCheck(game) && !possibleMoves(game); 
}

// Check if a piece can move to a specific position based on its type and current state of the board.
int canMoveTo(GameState *game,int fromRow,int fromCol,int toRow,int toCol){
   Piece piece= game -> board[fromRow][fromCol];

   switch(piece){
       case WHITE_PAWN:
           if(fromRow-1 ==toRow && fromCol ==toCol && game -> board[toRow][toCol]==EMPTY){
               return 1 ; // Normal move forward
           }else if(fromRow==6 && fromRow-2==toRow &&fromCol==toCol&& 
                     game -> board[toRow-1][toCol]==EMPTY&& 
                     game -> board[toRow][toCol]==EMPTY){
               return 1 ; // Initial double move forward
           }else if(fromRow-1==toRow&&abs(fromCol-toCol)==1&& 
                     game -> board[toRow][toCol]>=BLACK_PAWN){
               return 1 ; // Capture move diagonally
           }break;

       case BLACK_PAWN:
           if(fromRow+1 ==toRow && fromCol ==toCol && 
              game -> board[toRow][toCol]==EMPTY){
               return 1 ; // Normal move forward
           }else if(fromRow==1&&fromRow+2==toRow&&fromCol==toCol&& 
                     game -> board[toRow+1][toCol]==EMPTY&& 
                     game -> board[toRow][toCol]==EMPTY){
               return 1 ; // Initial double move forward
           }else if(fromRow+1==toRow&&abs(fromCol-toCol)==1&& 
                     game -> board[toRow][toCol]<=WHITE_KING){
               return 1 ; // Capture move diagonally
           }break;

       case WHITE_ROOK:
       case BLACK_ROOK:
           if(fromColumn==toColumn){ // Vertical movement
               for(int row=min(fromColumn,toColumn)+1 ; row<max(fromColumn,toColumn);row++){
                   if(game -> board[row ][col]!=EMPTY){
                       return false ;
                   }
               }
               return true ;
           }else if(fromColumn==toColumn){ // Horizontal movement 
               for(int col=min(fromColumn,toColumn)+1 ; col<max(fromColumn,toColumn);col++){
                   if(game -> board[row ][col]!=EMPTY){
                       return false ;
                   }
               }
               return true ;
           }break;

       case WHITE_BISHOP:
       case BLACK_BISHOP:
           if(abs(toColumn-fromColumn)==abs(toColumn-fromColumn)){
               int rowStep=(toColumn>fromColumn?+1:-1);
               int colStep=(toColumn>fromColumn?+1:-1);

               for(int step=1 ; step<abs(toColumn-fromColumn);step++){
                   if(game -> board[fromColumn+step*rowStep ][fromColumn+step*colStep ]!=EMPTY){
                       return false ;
                   }
               }

               return true ;
           }break;

       case WHITE_KNIGHT:
       case BLACK_KNIGHT:
           if((abs(toColumn-fromColumn)==2&&abs(to-row-from-row)==1)|| 
              (abs(to-row-from-row)==2&&abs(to-column-from-column)==1)){
              return true ;
           }break;

       case WHITE_QUEEN:
       case BLACK_QUEEN:
           if(abs(to-row-from-row)==abs(to-column-from-column)){// Diagonal movement 
              int rowStep=(to-row>from-row?+1:-1);
              int colStep=(to-column>from-column?+1:-1);

              for(int step=1 ; step<abs(to-row-from-row);step++){
                  if(game -> board[from-row+step*rowStep ][from-column+step*colStep ]!=EMPTY){
                      return false ;
                  }
              }

              return true ;
          }else{
              if(from-column==to-column){ // Vertical movement 
                  for(int row=min(from-row,to-row)+step ; row<max(from-row,to-row);row++){
                      if(game -> board[row ][column]!=EMPTY){
                          return false ;
                      }
                  }

                  return true ;
              }else{
                  for(int col=min(from-column,to-column)+step ; col<max(from-column,to-column);col++){
                      if(game -> board[row ][col]!=EMPTY){
                          return false ;
                      }
                  }

                  return true ;
              }break ;
          }

       case WHITE_KING:
       case BLACK_KING:
          if(abs(to-row-from-row)<=1&&abs(to-column-from-column)<=1){
              return true ;
          }else{
              // Handle castling logic here.
          }break ;

       default:
          return false ;
   }

   return false ;
}

void getKingPosition(const GameState *game,int* king_row,int* king_col){
   Piece king_piece=game -> whiteToMove ?WHITE_KING :BLACK_KING ;

   for(int row=0 ; row<BOARD_SIZE ;row++){
      for(int col=0 ; col<BOARD_SIZE ;col++){
         if(game -> board[row ][col]==king_piece){
             *king_row=row ;
             *king_col=col ;
             return ;
         }
      }
   }

}

// Placeholder functions for making and undoing moves 
void makeMove(GameState *game,int from_row,int from_col,int to_row,int to_col){
   Piece movedPiece=game -> board[from_row ][from_col ];
   Piece targetPiece=EMPTY ;

   targetPiece=targetPiece != EMPTY ?targetPiece :targetPiece ;

   // Move piece on board
   game -> board[to_row ][to_col]=movedPiece ;
   game -> board[from_row ][from_col]=EMPTY ;

   // Update castling rights if necessary

}

void undoMove(GameState *game,int from_row,int from_col,int to_row,int to_col){
   Piece movedPiece=targetPiece != EMPTY ?targetPiece :targetPiece ;

   targetPiece=targetPiece != EMPTY ?targetPiece :targetPiece ;

   // Undo move on board
   game -> board[from_row ][from_col]=movedPiece ;
   game -> board[to_row ][to_col]=targetPiece ;

}
