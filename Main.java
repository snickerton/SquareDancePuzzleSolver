package com.company;

import java.awt.*;
import java.util.Arrays;

public class Main {

    private static int[][] base = new int[][]{
            { 0, 0, 0, 0, 1 },
            { 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1 },
            { 0, 0, 0, 1, 1 }
    };
    private static int[][] player = new int[][]{
            { 2, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0 },
            { 1, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0 }
    };
    private static int[][] a = new int[5][5];

    private static int yPos,xPos;
    private static int yEnd,xEnd;
    private static int yStart,xStart;

    public static void main(String[] args) {
        copyArrays(a, base);
        boolean solved = false;
        int[] cmds = {2,2,3,2,2,2,4,1,1,2,3,3,3,4,3,3,2,1,2,2,1,1,1,2,3,3,3,4,4,1,1,2,4,4};

        int iterationCount = 0;

        //find green and set equal to position
        for (int i = 0; i < player.length; i++) {
            for (int j = 0; j < player[0].length; j++) {
                //check for start
                if (player[i][j] == 1) {
                    yStart = i;
                    xStart = j;
                }
                //check for end
                if(player[i][j] == 2){
                    yEnd = i;
                    xEnd = j;
                }
            }
        }


            //choose box placements
            masterLoop:
            for (int i = 0; i < 22; i++) {
                jLoop:
                for (int j = i+1; j < 23; j++) {
                    kLoop:
                    for (int k = j+1; k < 24; k++) {
                        lLoop:
                        for (int l = k+1; l < 25; l++) {

                            //clear the box/board
                            //reset player position
                            copyArrays(a, base);
                            xPos = xStart;
                            yPos = yStart;

                            //do not allow boxes to spawn in holes, start positions, or end positions
                            if(base[i/5][i%5] == 1 || player[i/5][i%5] == 1 || player[i/5][i%5] == 2){
                                break jLoop;
                            }
                            if(base[j/5][j%5] == 1 || player[j/5][j%5] == 1 || player[j/5][j%5] == 2){
                                break kLoop;
                            }
                            if(base[k/5][k%5] == 1 || player[k/5][k%5] == 1 || player[k/5][k%5] == 2){
                                break lLoop;
                            }
                            if(base[l/5][l%5] == 1 || player[l/5][l%5] == 1 || player[l/5][l%5] == 2){
                                continue;
                            }

                            //populate 'a' with boxes
                            a[i/5][i%5] = -1;
                            a[j/5][j%5] = -1;
                            a[k/5][k%5] = -1;
                            a[l/5][l%5] = -1;

                            System.out.println("Iteration Number: "+iterationCount);
                            System.out.println("Box Positions Generated.");
                            System.out.println("Worksheet Setup: \n" + Arrays.deepToString(a));
                            System.out.println("Beginning Command Sequence...");
                            System.out.println("...");

                            //run command sequence
                            cmdLoop:
                            for(int cmdCnt = 0; cmdCnt<cmds.length; cmdCnt++ ){
                                switch(cmds[cmdCnt]){
                                    case 1:
                                        boolean worked = moveNorth();
                                        if(!worked){
                                            break cmdLoop;
                                        }
                                        break;
                                    case 2:
                                        worked = moveEast();
                                        if(!worked){
                                            break cmdLoop;
                                        }
                                        break;
                                    case 3:
                                        worked = moveSouth();
                                        if(!worked){
                                            break cmdLoop;
                                        }
                                        break;
                                    case 4:
                                        worked = moveWest();
                                        if(!worked){
                                            break cmdLoop;
                                        }
                                        break;
                                }
                            }
                            System.out.println("Command Sequence Done.");
                            System.out.println("----------------------------------------------");

                            iterationCount++;



                            //The Holy Grail.
                            if(yPos == yEnd && xPos == xEnd && boardIsClear()){
                                System.out.println("\nSolution Found. \n");
                                break masterLoop;
                            }

                        }
                    }
                }
            }
        }



    //cbme
    static boolean boardIsClear(){
        //immediately return false if there's anything other than 0
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if (a[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean moveEast(){
        //check if out of bounds
        if(xPos+1 > base.length-1){
            return false;
        }
        //if box
        if(a[yPos][xPos+1] == -1){
            if(xPos+2>base.length-1){
                return false;
            }
            int boxTranslate = a[yPos][xPos+1]+a[yPos][xPos+2];
            switch(boxTranslate){
                //check if adjacent box
                case -2:
                    //means box placement is incorrect, break out of current iteration
                    return false;
                case -1:
                    //box moves successfully over to the right
                    a[yPos][xPos+1] = 0;
                    a[yPos][xPos+2] = -1;
                    return true;
                case 0:
                    //box moves over a hole
                    a[yPos][xPos+1] = 0;
                    a[yPos][xPos+2] = 0;
                    return true;
            }

        }
        //if empty space
        if(a[yPos][xPos+1] == 0){
            xPos++;
            return true;
        }
        //if hole
        if(a[yPos][xPos+1] == 1){
            return false;
        }
        return true;
    }

    static boolean moveSouth(){
        //check if out of bounds
        if(yPos+1 > base.length-1){
            return false;
        }
        //if box
        if(a[yPos+1][xPos] == -1){
            if(yPos+2>base.length-1){
                return false;
            }
            int boxTranslate = a[yPos+1][xPos]+a[yPos+2][xPos];
            switch(boxTranslate){
                //check if adjacent box
                case -2:
                    //means box placement is incorrect, break out of current iteration
                    return false;
                case -1:
                    //box moves successfully over to the right
                    a[yPos+1][xPos] = 0;
                    a[yPos+2][xPos] = -1;
                    return true;

                case 0:
                    //box moves over a hole
                    a[yPos+1][xPos] = 0;
                    a[yPos+2][xPos] = 0;
                    return true;
            }

        }
        //if empty space
        if(a[yPos+1][xPos] == 0){
            yPos++;
            return true;
        }
        if(a[yPos+1][xPos] == 1){
            return false;
        }
        return true;
    }
    static boolean moveWest(){
        //check if out of bounds
        if(xPos-1 < 0){
            return false;
        }
        //if box
        if(a[yPos][xPos-1] == -1){
            if(xPos-2<0){
                return false;
            }
            int boxTranslate = a[yPos][xPos-1]+a[yPos][xPos-2];
            switch(boxTranslate){
                //check if adjacent box
                case -2:
                    //means box placement is incorrect, break out of current iteration
                    return false;
                case -1:
                    //box moves successfully over to the right
                    a[yPos][xPos-1] = 0;
                    a[yPos][xPos-2] = -1;
                    return true;
                case 0:
                    //box moves over a hole
                    a[yPos][xPos-1] = 0;
                    a[yPos][xPos-2] = 0;
                    return true;
            }

        }
        //if empty space
        if(a[yPos][xPos-1] == 0){
            xPos--;
            return true;
        }
        if(a[yPos][xPos-1] == 1){
            return false;
        }
        return true;
    }

    static boolean moveNorth(){
        //check if out of bounds
        if(yPos-1 < 0){
            return false;
        }
        //if box
        if(a[yPos-1][xPos] == -1){
            if(yPos-2<0){
                return false;
            }
            int boxTranslate = a[yPos-1][xPos]+a[yPos-2][xPos];
            switch(boxTranslate){
                //check if adjacent box
                case -2:
                    //means box placement is incorrect, break out of current iteration
                    return false;
                case -1:
                    //box moves successfully over to the right
                    a[yPos-1][xPos] = 0;
                    a[yPos-2][xPos] = -1;
                    return true;

                case 0:
                    //box moves over a hole
                    a[yPos-1][xPos] = 0;
                    a[yPos-2][xPos] = 0;
                    return true;
            }

        }
        //if empty space
        if(a[yPos-1][xPos] == 0){
            yPos--;
            return true;
        }
        if(a[yPos-1][xPos] == 1){
            return false;
        }
        return true;
    }




    static void copyArrays(int[][] a, int[][] b){
        for(int i=0; i<a.length; i++)
            for(int j=0; j<a[0].length; j++)
                a[i][j]=b[i][j];
    }

}
