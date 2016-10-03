package base;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Михаил on 02.10.2016.
 */
public class Island {

    static int rowEdge, columnEdge;

    static ArrayList<int[][]> markedList= new ArrayList<int[][]>();

    public static void main(String[] args) {
        ArrayList<int[][]> listOfMatrix = inputData();
        processingList(listOfMatrix, markedList);

    }

    public static int[][] nextRain(int[][] wetIsland, int[][] marked, int min){
        int numberOfRows = wetIsland.length;
        int numberOfColumns = wetIsland[0].length;

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                if (wetIsland[i][j] < min && marked[i][j]!=1) wetIsland[i][j]=min;
            }
        }
        return wetIsland;
    }

    public static void markFullOfRain(int[][] wetIsland, int[][] marked, int min){
        int numberOfRows = wetIsland.length;
        int numberOfColumns = wetIsland[0].length;

        for (int i = 0; i < numberOfColumns; i++) {//проверяем верхний ряд с углами
            if (wetIsland[0][i]==min) marked[0][i]=1;
        }
        for (int i = 0; i < numberOfColumns; i++) {// проверяем нижний ряд с углами
            if (wetIsland[numberOfRows-1][i]==min) marked[numberOfRows-1][i]=1;
        }
        for (int i = 1; i < numberOfRows-1; i++) { //проверяем первую колонку без углов
            if (wetIsland[i][0]==min) marked[i][0]=1;
        }
        for (int i = 1; i < numberOfRows-1; i++) { // проверяем последнюю колонку без углов
            if (wetIsland[i][numberOfColumns-1]==min) marked[i][numberOfColumns-1]=1;
        }

        for (int check = 0; check < numberOfRows; check++) {
            for (int i = 1; i < numberOfRows-1; i++) {
                for (int j = 1; j < numberOfColumns-1; j++) {
                    if (marked[i][j]!=1 && wetIsland[i][j]==min){
                        if (marked[i-1][j]==1 || marked[i+1][j]==1 || marked[i][j-1]==1 || marked[i][j+1]==1){
                            marked[i][j]=1;
                        }
                    }
                }
            }
        }
    }

    public static boolean allMarked(int[][] marked){
        int numberOfRows = marked.length;
        int numberOfColumns = marked[0].length;

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                if (marked[i][j]!=1){
                    return false;
                }
            }
        }

        return true;
    }

    public static int sumOfMatrixElement(int[][] matrix){

        int difOfSumms=0;

        int numberOfRows = matrix.length;
        int numberOfColumns = matrix[0].length;

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                difOfSumms=difOfSumms+matrix[i][j];
            }
        }

        return difOfSumms;
    }

    private static void copy_Matrix(int origin_Matrix[][] , int copy_Matrix[][]) //взял со stackoverflow
    {
    /* assert origin_Matrix.length == copy_Matrix.length */
        for ( int i = 0 ; i < origin_Matrix.length; i++)
        {
        /* assert origin_Matrix[i].length == copy_Matrix[i].length */
            System.arraycopy(origin_Matrix[i], 0, copy_Matrix[i], 0, origin_Matrix[i].length);
        }
    }



    public static void processingIsland(int[][] island, int[][] marked){

        int numberOfRows = island.length;
        int numberOfColumns = island[0].length;

        boolean stop = false;

        //int markedMax = numberOfRows*numberOfColumns;
        //int markedMin = 0;
        if (numberOfRows<=2 || numberOfColumns<=2){
            System.out.println(0);
        }
        else{
            //int[][] wetIsland = island.clone();
            int[][] wetIsland = new int[island.length][island[0].length];
            copy_Matrix(island,wetIsland);
            //System.arraycopy(island, 0, wetIsland, 0, island.length);

            //int min = 2;
            int min =minimumSearch(wetIsland);
            while (!stop){// будем пошагово заливать остров водой начиная с минимума относительно края, пока не достигнем максимума
                wetIsland = nextRain(wetIsland, marked, min);//заливаем уровнем, равным min, значения меньше min
                markFullOfRain(wetIsland, marked, min); // маркируем все, что граничит с краями острова или уже маркированными клетками
                if (allMarked(marked)) stop=true;// если все маркировано, выходим из цикла, больше вода на острове не задерживается.
                else min++; //увеличиваем высоту проверяемых клеток
            }
            int water = sumOfMatrixElement(wetIsland)-sumOfMatrixElement(island);// считаем разницу сумм всех значений в двумерных матрицах
            System.out.println(water); // выводим на печать разницу
        }
    }

    public static void setStartMinimumEdge (int row, int column){
        rowEdge=row;
        columnEdge=column;
    }

    public static int minimumSearch(int[][] wetIsland){

        int minimumEdge = wetIsland[0][1];
        int startRow = 0, startColumn = 1;
        int lastRow = wetIsland.length-1;
        int lastColumn = wetIsland[0].length-1;

        for (int i = 0; i < wetIsland[0].length; i++) {
            if (wetIsland[0][i] < minimumEdge){
                minimumEdge=wetIsland[0][i];
                startRow = 0;
                startColumn = i;
            }
            if (wetIsland[lastRow][i] < minimumEdge) {
                minimumEdge=wetIsland[lastRow][i];
                startRow = lastRow;
                startColumn = i;
            }
        }
        for (int i = 0; i < wetIsland.length; i++) {
            if (wetIsland[i][0] < minimumEdge){
                minimumEdge=wetIsland[i][0];
                startRow = i;
                startColumn = 0;
            }
            if (wetIsland[i][lastColumn] < minimumEdge) {
                minimumEdge = wetIsland[i][lastColumn];
                startRow = i;
                startColumn = lastColumn;
            }
        }

        setStartMinimumEdge(startRow,startColumn);
        return minimumEdge;
    }


    public static void processingList(ArrayList<int[][]> list, ArrayList<int[][]> markedList){
        for (int i = 0; i < list.size(); i++) {
            processingIsland(list.get(i), markedList.get(i));
        }
    }
    
    public static void printIslands(ArrayList<int[][]> list){

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).length; j++) {
                for (int k = 0; k < list.get(i)[j].length; k++) {
                    System.out.print(list.get(i)[j][k] + " ");
                }
                System.out.println();
            }
        }
    }




    public static ArrayList<int[][]> inputData(){

        ArrayList<int[][]> listOfMatrix= new ArrayList<int[][]>();
        boolean inputCorrect=true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try{
            String stringNumberOfMatrix = reader.readLine();
            if (!stringNumberOfMatrix.equals("") || !stringNumberOfMatrix.equals("0") ){
                int numberOfMatrix = Integer.parseInt(stringNumberOfMatrix);

                for (int i = 0; i < numberOfMatrix; i++) {

                    String str = reader.readLine();
                    String[] strings = str.split(" ");
                    int numberOfRows = Integer.parseInt(strings[0]);
                    int numberOfColumns = Integer.parseInt(strings[1]);

                    int[][] matrix = new int[numberOfRows][numberOfColumns];
                    for (int j = 0; j < numberOfRows; j++) {
                        String matrixRow = reader.readLine();
                        String[] rowData = matrixRow.split(" ");
                        if (rowData.length==numberOfColumns){
                            for (int k = 0; k < rowData.length; k++) {
                                matrix[j][k] = Integer.parseInt(rowData[k]);
                            }
                        }
                        else {
                            System.out.println("Ошибка при вводе данных. Обработка этого острова прекращена");
                            inputCorrect=false;
                            break;
                        }
                    }

                    if (inputCorrect){
                        listOfMatrix.add(matrix);
                        markedList.add(new int[numberOfRows][numberOfColumns]);
                    }
                }
            }
            else{
                System.out.println("Ввод исходных данных завершен");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return listOfMatrix;
    }
}
