package com.nosto.fun.wael;

import com.nosto.fun.game1.ArenaPosition;
import com.nosto.fun.game1.Piece;
import com.nosto.fun.game1.Player;

public class ComputerOpponent implements Player, Cloneable {

    private Piece myPiece;

    private String name;

    private ArenaPosition myLastPosition;

    // multipliers used to check each of the four axes : 2 diagonal , horizontal , vertical
    private static final int[][] multipliers = {{1, 1}, {1, 0}, {1, -1}, {0,-1}};

    /**
     * Creates a new instance of ComputerOpponent
     */
    public ComputerOpponent(String name) {
        this.name = name;
    }

    public void setSide(Piece p) {
        myPiece = p;
    }

    public Piece getSide() {
        return myPiece;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public ArenaPosition move(Piece[][] board, ArenaPosition lastPosition) {
            this.myLastPosition = checkMyNextMove(board, this.myLastPosition,lastPosition,this.myPiece, board.length);
            return  this.myLastPosition;
    }

    public String toString() {
        return getName();
    }


    private ArenaPosition checkWinningCondition(Piece[][] board, ArenaPosition lastPosition, Piece opponentSide, int size ,int numLined) {
        int lastPosRow = lastPosition.getRow();
        int lastPosCol = lastPosition.getColumn();
        int currentRowPos ;
        int currentColPos ;
        for (int m = 0; m < 4; m++) {
            int xMultiplier = multipliers[m][0];
            int yMultiplier = multipliers[m][1];
            int sameLineCounter = 0;
            for (int i = -numLined; i < numLined+1; i++) {
                currentRowPos = lastPosRow + xMultiplier * i;
                currentColPos = lastPosCol + yMultiplier * i;
                if (inBoardLimit(currentRowPos,currentColPos,size)
                        && opponentSide.equals(board[currentRowPos][currentColPos])) {
                    sameLineCounter++;
                } else {
                    sameLineCounter = 0;
                }
                if (sameLineCounter == numLined) {
                    if (inBoardLimit(currentRowPos + xMultiplier, currentColPos + yMultiplier, size) &&
                            board[currentRowPos + xMultiplier][currentColPos + yMultiplier] == null) {
                        return new ArenaPosition(currentRowPos + xMultiplier, currentColPos + yMultiplier);
                    } else if (inBoardLimit(currentRowPos + xMultiplier * (-numLined), currentColPos + yMultiplier * (-numLined), size)
                            && board[currentRowPos + xMultiplier * (-numLined)][currentColPos + yMultiplier * (-numLined)] == null) {
                        return new ArenaPosition(currentRowPos + xMultiplier * (-numLined), currentColPos + yMultiplier * (-numLined));
                    }
                }
            }
        }
        return null;
    }

    private ArenaPosition checkMyNextMove(Piece[][] board, ArenaPosition lastPosition, ArenaPosition opponentPosition, Piece mySide, int size){
        //First piece
        if(lastPosition == null && board[size/2][size/2] == null) return new ArenaPosition(size/2,size/2);
        else if (lastPosition == null) return new ArenaPosition(size/2-1,size/2);
        Piece opponentSide = (mySide.equals(Piece.ROUND)) ? Piece.CROSS : Piece.ROUND;
        int numlined = 4; //numlined : number of same piece lined up
        boolean checkOpponent = false;
        ArenaPosition possibleNextPos;
        do{
            if(!checkOpponent && numlined==2) {
                //Defending if opponent going for the win
                possibleNextPos = checkWinningCondition(board, opponentPosition, opponentSide , board.length,numlined+1);
                checkOpponent = true;
            }else {
                //Connecting my pieces
                possibleNextPos = checkWinningCondition(board, lastPosition, mySide, size, numlined);
                numlined--;
            }
        }while(possibleNextPos==null && numlined!=0);

        //if computer didnt defend nor found pieces to connect
        if(possibleNextPos == null) return makeNewMove(board, lastPosition,size);
        else return possibleNextPos;
    }

    private ArenaPosition makeNewMove(Piece[][] board, ArenaPosition lastPosition, int size){
        int lastPosRow = lastPosition.getRow();
        int lastPosCol = lastPosition.getColumn();
        for(int x = lastPosRow-1; x<lastPosRow+2; x++){
            for(int y = lastPosCol-1 ; y<lastPosRow+2; y++){
                if(x==lastPosRow && y==lastPosCol) continue;
                if(inBoardLimit(x, y, size)
                        && board[x][y] == null) return new ArenaPosition(x,y);
            }
        }
        while (true) {
            int x = (int) (Math.random() * board.length);
            int y = (int) (Math.random() * board.length);
            if (board[x][y] == null) {
                return new ArenaPosition(x, y);
            }
        }
    }

    private boolean inBoardLimit(int xPosition, int yPosition, int size) {
        return 0 <= xPosition && xPosition < size - 1 && 0 <= yPosition && yPosition < size - 1;
    }

}
