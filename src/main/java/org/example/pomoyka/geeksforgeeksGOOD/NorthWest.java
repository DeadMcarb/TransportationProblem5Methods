package org.example.pomoyka.geeksforgeeksGOOD;

public class NorthWest {
    public static void main(String[] args)
    {
        int[][] grid = new int[][] {
                { 5, 1, 2, 4 },
                { 6, 5, 7, 6 },
                { 6, 4, 6, 8 },
                { 7, 5, 5, 7 },
        }; // table

        int[] supply = new int[] { 70, 50, 70, 70 }; // supply
        int[] demand = new int[] { 110, 35, 25, 100 }; // demand

        int startR = 0; // start row
        int startC = 0; // start col
        int ans = 0;

        // Loop runs until it reaches the bottom right
        // corner
        while (startR != grid.length
                && startC != grid[0].length) {

            // If demand is greater than supply
            if (supply[startR] <= demand[startC]) {
                ans += supply[startR]
                        * grid[startR][startC];

                // Subtract the value of supply from the
                // demand
                demand[startC] -= supply[startR];
                startR++;
            }

            // If supply is greater than demand
            else {
                ans += demand[startC]
                        * grid[startR][startC];

                // Subtract the value of demand from the
                // supply
                supply[startR] -= demand[startC];
                startC++;
            }
        }

        System.out.println(
                "The initial feasible basic solution is "
                        + ans);
    }
}
