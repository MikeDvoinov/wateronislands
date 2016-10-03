package matrix;
import base.Island;

/**
 * Created by Михаил on 02.10.2016.
 */
public class Mininum {

    public static void main(String[] args) {

        int[][] matrix = {{16,17,18,19}, {20,21,22,23},{15,12,13,14}};
        int min = Island.minimumSearch(matrix);
        System.out.println(min);
    }
}
