#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#define SIZE 8
#define MAX_MOVES 256

// Define piece types
#define EMPTY ' '
#define WHITE_PAWN 'P'
#define WHITE_ROOK 'R'
#define WHITE_KNIGHT 'N'
#define WHITE_BISHOP 'B'
#define WHITE_QUEEN 'Q'
#define WHITE_KING 'K'
#define BLACK_PAWN 'p'
#define BLACK_ROOK 'r'
#define BLACK_KNIGHT 'n'
#define BLACK_BISHOP 'b'
#define BLACK_QUEEN 'q'
#define BLACK_KING 'k'

// Board representation
char board[SIZE][SIZE] = {
    {BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK},
    {BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN},
    {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
    {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
    {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
    {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
    {WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN},
    {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK}
};

// Square names
const char *squareNames[SIZE][SIZE] = {
    {"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"},
    {"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7"},
    {"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6"},
    {"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5"},
    {"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4"},
    {"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3"},
    {"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2"},
    {"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"}
};

// Function prototypes
void printBoard();
int isMoveValid(int startX,int startY,int endX,int endY);
void generateMoves(int xStart,int yStart);
int evaluateBoard();
int minimax(int depth,int alpha,int beta,int maximizingPlayer);
void makeMove(int startRow,int startCol,int targetRow,int targetCol);
void undoMove(int targetRow,int targetCol,int originalRow,int originalCol,char originalPiece);
int isInCheck(char color);
int isCheckmate(char color);
void parseInput(char *input);

// Max and Min functions
int max(int a, int b) {
    return (a > b) ? a : b;
}

int min(int a, int b) {
    return (a < b) ? a : b;
}

// Print the board
void printBoard() {
    printf("  a b c d e f g h\n");
    for (int i = 0; i < SIZE; i++) {
        printf("%d ", SIZE - i);
        for (int j = 0; j < SIZE; j++) {
            printf("%c ", board[i][j]);
        }
        printf("\n");
    }
}

// Check if a move is valid (simplified)
int isMoveValid(int startX,int startY,int endX,int endY) {
    // Add basic validation logic here (bounds checking and piece movement rules)
    if (endX < 0 || endX >= SIZE || endY < 0 || endY >= SIZE) return 0;
    
    char piece = board[startX][startY];
    
    // Basic movement rules for each piece
    if (piece == WHITE_PAWN) {
        if (startX - 1 == endX && startY == endY && board[endX][endY] == EMPTY) return 1; // Move forward
        if (startX - 1 == endX && abs(startY - endY) == 1 && board[endX][endY] >= 'a' && board[endX][endY] <= 'z') return 1; // Capture
        if (startX == 6 && startX - 2 == endX && startY == endY && board[endX][endY] == EMPTY) return 1; // Double move from starting position
    } else if (piece == BLACK_PAWN) {
        if (startX + 1 == endX && startY == endY && board[endX][endY] == EMPTY) return 1; // Move forward
        if (startX + 1 == endX && abs(startY - endY) == 1 && board[endX][endY] >= 'A' && board[endX][endY] <= 'Z') return 1; // Capture
        if (startX == 1 && startX + 2 == endX && startY == endY && board[endX][endY] == EMPTY) return 1; // Double move from starting position
    } else if (piece == WHITE_ROOK || piece == BLACK_ROOK) {
        if (startX == endX || startY == endY) {
            // Check for obstacles in the path
            int stepX = (endX - startX) ? (endX - startX) / abs(endX - startX) : 0;
            int stepY = (endY - startY) ? (endY - startY) / abs(endY - startY) : 0;
            int x = startX + stepX;
            int y = startY + stepY;
            while (x != endX || y != endY) {
                if (board[x][y] != EMPTY) return 0; // Blocked by another piece
                x += stepX;
                y += stepY;
            }
            return 1;
        }
    } else if (piece == WHITE_KNIGHT || piece == BLACK_KNIGHT) {
        if ((abs(startX - endX) == 2 && abs(startY - endY) == 1) || 
            (abs(startX - endX) == 1 && abs(startY - endY) == 2)) {
            return 1; // Knight move
        }
    } else if (piece == WHITE_BISHOP || piece == BLACK_BISHOP) {
        if (abs(startX - endX) == abs(startY - endY)) {
            // Check for obstacles in the path
            int stepX = (endX - startX) / abs(endX - startX);
            int stepY = (endY - startY) / abs(endY - startY);
            int x = startX + stepX;
            int y = startY + stepY;
            while (x != endX || y != endY) {
                if (board[x][y] != EMPTY) return 0; // Blocked by another piece
                x += stepX;
                y += stepY;
            }
            return 1;
        }
    } else if (piece == WHITE_QUEEN || piece == BLACK_QUEEN) {
        if ((startX == endX || startY == endY) || 
            (abs(startX - endX) == abs(startY - endY))) {
            // Check for obstacles in the path
            int stepX = (endX - startX) ? (endX - startX) / abs(endX - startX) : 0;
            int stepY = (endY - startY) ? (endY - startY) / abs(endY - startY) : 0;
            int x = startX + stepX;
            int y = startY + stepY;
            while (x != endX || y != endY) {
                if (board[x][y] != EMPTY) return 0; // Blocked by another piece
                x += stepX;
                y += stepY;
            }
            return 1;
        }
    } else if (piece == WHITE_KING || piece == BLACK_KING) {
        if ((abs(startX - endX) <= 1 && abs(startY - endY) <= 1)) {
            return 1; // King move
        }
    }

    return 0; // Invalid move
}

// Generate moves for a piece at position (xStart,yStart)
void generateMoves(int xStart,int yStart){
   printf("Possible moves for %c at %s:\n",
          board[xStart][yStart],
          squareNames[xStart][yStart]);
   for(int xEnd=0;xEnd<SIZE;xEnd++){
       for(int yEnd=0;yEnd<SIZE;yEnd++){
           if(isMoveValid(xStart,yStart,xEnd,yEnd)){
               printf("Move from %s to %s\n",
                      squareNames[xStart][yStart],
                      squareNames[xEnd][yEnd]);
           }
       }
   }
}

// Make a move on the board
void makeMove(int startRow,int startCol,int targetRow,int targetCol){
   char movedPiece = board[startRow][startCol];
   board[targetRow][targetCol] = movedPiece;
   board[startRow][startCol] = EMPTY;

   // Handle special cases like promotion or en passant here...
}

// Undo a move on the board
void undoMove(int targetRow,int targetCol,int originalRow,int originalCol,char originalPiece){
   board[originalRow][originalCol] = originalPiece;
   board[targetRow][targetCol] = EMPTY;

   // Handle special cases here...
}

// Find the position of the king for a given color
int* findKingPosition(char kingPiece){
   static int position[2]; // To hold the x and y coordinates
   
   for(int x=0;x<SIZE;x++){
       for(int y=0;y<SIZE;y++){
           if(board[x][y]==kingPiece){
               position[0]=x;
               position[1]=y;
               return position; // Return coordinates of king's position.
           }
       }
   }

   return NULL; // King not found.
}

// Check if there are any valid moves for the current player.
int anyValidMoves(char color){
   for(int x=0;x<SIZE;x++){
       for(int y=0;y<SIZE;y++){
           char piece=board[x][y];
           // Check if it's player's piece.
           if((color=='W' && piece>='A' && piece<='Z') ||
              (color=='B' && piece>='a' && piece<='z')){
               // Generate moves for this piece.
               for(int target_x=0;target_x<SIZE;target_x++){
                   for(int target_y=0;target_y<SIZE;target_y++){
                       if(isMoveValid(x,y,target_x,target_y)){
                           return 1; // Found at least one valid move.
                       }
                   }
               }
           }
       }
   }

   return 0; // No valid moves found.
}

// Check for check condition on the king's color.
int isInCheck(char color){
   int king_x=color=='W' ? findKingPosition(WHITE_KING)[0] : findKingPosition(BLACK_KING)[0];
   int king_y=color=='W' ? findKingPosition(WHITE_KING)[1] : findKingPosition(BLACK_KING)[1];

   for(int x=0;x<SIZE;x++){
       for(int y=0;y<SIZE;y++){
           char piece=board[x][y];
           if((color=='W' && piece>='a' && piece<='z') ||
              (color=='B' && piece>='A' && piece<='Z')){
               if(isMoveValid(x,y ,king_x ,king_y)){
                   return 1; // In check!
               }
           }
       }
   }

   return 0; // Not in check.
}

// Check for checkmate condition on the king's color.
int isCheckmate(char color){
   if(!isInCheck(color)) return 0; // Not checkmate unless in check.

   return !anyValidMoves(color); // If no valid moves left then checkmate.
}

// A simple evaluation function for demonstration purposes
int evaluateBoard() {
    int score = 0;
    for (int x = 0; x < SIZE; x++) {
        for (int y = 0; y < SIZE; y++) {
            char piece = board[x][y];
            switch(piece) {
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
            }
        }
    }
    return score;
}

// Minimax algorithm with alpha-beta pruning
int minimax(int depth,int alpha,int beta,int maximizingPlayer){
   if(depth==0){
       return evaluateBoard(); // Return evaluation of the board
   }

   if(maximizingPlayer){
       int maxEval=-10000;

       for(int x=0;x<SIZE;x++){
           for(int y=0;y<SIZE;y++){
               if(board[x][y]>='A'&&board[x][y]<='Z'){ // White pieces
                   for(int target_x=0;target_x<SIZE;target_x++){
                       for(int target_y=0;target_y<SIZE;target_y++){
                           if(isMoveValid(x,y,target_x,target_y)){
                               char originalPiece=board[target_x][target_y];
                               makeMove(x,y,target_x,target_y); // Make the move

                               int eval=minimax(depth-1 ,alpha,beta ,0);
                               undoMove(target_x,target_y,x,y ,originalPiece); // Undo the move

                               maxEval=max(maxEval ,eval);
                               alpha=max(alpha ,eval);
                               if(beta<=alpha){ 
                                   break; // Beta cutoff 
                               }
                           }
                       }
                   }
               }
           }
       }

       return maxEval;

   }else{
       int minEval=10000;

       for(int x=0;x<SIZE;x++){
           for(int y=0;y<SIZE;y++){
               if(board[x][y]>='a'&&board[x][y]<='z'){ // Black pieces
                   for(int target_x=0;target_x<SIZE;target_x++){
                       for(int target_y=0;target_y<SIZE;target_y++){
                           if(isMoveValid(x,y,target_x,target_y)){
                               char originalPiece=board[target_x][target_y];
                               makeMove(x,y,target_x,target_y); // Make the move

                               int eval=minimax(depth-1 ,alpha,beta ,1);
                               undoMove(target_x,target_y,x,y ,originalPiece); // Undo the move

                               minEval=min(minEval ,eval);
                               beta=min(beta ,eval);
                               if(beta<=alpha){ 
                                   break;// Alpha cutoff 
                               }
                           }
                       }
                   }
               }
           }
       }

       return minEval;

   }

}

// Function to convert user input into coordinates on the board.
// Function to convert user input into coordinates on the board.
void parseInput(char *input){
   // Validate input length and format before processing to avoid segmentation faults.
   if(strlen(input) != 5 || input[0] != ' ' || input[3] != ' ') {
      printf("Invalid input format. Please use format: e2 e4\n");
      return;
   }

   int from_x=SIZE-((input[2]-'0'));
   int from_y=input[1]-'a';
   
   int to_x=SIZE-((input[5]-'0'));
   int to_y=input[4]-'a';

   // Validate calculated indices before making a move.
   if(from_x < 0 || from_x >= SIZE || from_y < 0 || from_y >= SIZE ||
      to_x < 0 || to_x >= SIZE || to_y < 0 || to_y >= SIZE ) {
      printf("Invalid move. Please try again.\n");
      return;
   }

   makeMove(from_x ,from_y ,to_x ,to_y); 
}
int main() {
   printf("Initial Board:\n");
   printBoard();

   while(1){
       char input[10];
       printf("Your move: ");
       fgets(input,sizeof(input),stdin);
       
       parseInput(input); 
       
       printBoard();
       
       // Bot's turn using minimax algorithm here...
       printf("Bot's turn...\n");
       minimax(3,-10000,+10000 ,0); 
       
       printBoard();
       
       // Check for checkmate or stalemate conditions here...
       if(isCheckmate('W')){ 
           printf("Black wins!\n");
           break;
       }else if(isCheckmate('B')){
           printf("White wins!\n");
           break;
       }
   }

   return 0;
}
