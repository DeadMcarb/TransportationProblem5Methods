package org.example.rozaGOOD.rosettacodeVogel;


import java.util.Arrays;
import static java.util.Arrays.stream;
import java.util.concurrent.*;

public class VogelsApproximationMethod {

    static int[] demand = { 0, 0, 0, 0 };
    static int[] supply =  { 0, 0, 0, 0 };
    static int[][] costs = {
            { 0, 0, 0, 0 },
            { 0, 0, 0, 0 },
            { 0, 0, 0, 0 },
            { 0, 0, 0, 0 }
    };

    final static int nRows = supply.length;
    final static int nCols = demand.length;

    static boolean[] rowDone = new boolean[nRows];
    static boolean[] colDone = new boolean[nCols];
    static int[][] result = new int[nRows][nCols];

    static ExecutorService es = Executors.newFixedThreadPool(2);

    public static void calculate(int[] mainDemand, int[] mainSupply, int[][] mainCosts) throws Exception {
//        demand = new int[]{110, 35, 25, 100};
//        supply = new int[]{70, 50, 70, 70};
//        costs = new int[][]{
//                {5, 1, 2, 4},
//                {6, 5, 7, 6},
//                {6, 4, 6, 8},
//                {7, 5, 5, 7}
//        };

        demand = mainDemand;
        supply = mainSupply;
        costs = mainCosts;


        int supplyLeft = stream(supply).sum();
        int totalCost = 0;

        while (supplyLeft > 0) {
            int[] cell = nextCell();
            int r = cell[0];
            int c = cell[1];

            int quantity = Math.min(demand[c], supply[r]);
            demand[c] -= quantity;
            if (demand[c] == 0)
                colDone[c] = true;

            supply[r] -= quantity;
            if (supply[r] == 0)
                rowDone[r] = true;

            result[r][c] = quantity;
            supplyLeft -= quantity;

            totalCost += quantity * costs[r][c];
        }

        System.out.println("VogelsApproximationMethod \n");
        stream(result).forEach(a -> System.out.println(Arrays.toString(a)));
        System.out.println("\nTotal cost: " + totalCost);

        es.shutdown();
    }

    static int[] nextCell() throws Exception {
        Future<int[]> f1 = es.submit(() -> maxPenalty(nRows, nCols, true));
        Future<int[]> f2 = es.submit(() -> maxPenalty(nCols, nRows, false));

        int[] res1 = f1.get();
        int[] res2 = f2.get();

        if (res1[3] == res2[3])
            return res1[2] < res2[2] ? res1 : res2;

        return (res1[3] > res2[3]) ? res2 : res1;
    }

    static int[] diff(int j, int len, boolean isRow) {
        int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
        int minP = -1;
        for (int i = 0; i < len; i++) {
            if (isRow ? colDone[i] : rowDone[i])
                continue;
            int c = isRow ? costs[j][i] : costs[i][j];
            if (c < min1) {
                min2 = min1;
                min1 = c;
                minP = i;
            } else if (c < min2)
                min2 = c;
        }
        return new int[]{min2 - min1, min1, minP};
    }

    static int[] maxPenalty(int len1, int len2, boolean isRow) {
        int md = Integer.MIN_VALUE;
        int pc = -1, pm = -1, mc = -1;
        for (int i = 0; i < len1; i++) {
            if (isRow ? rowDone[i] : colDone[i])
                continue;
            int[] res = diff(i, len2, isRow);
            if (res[0] > md) {
                md = res[0];  // max diff
                pm = i;       // pos of max diff
                mc = res[1];  // min cost
                pc = res[2];  // pos of min cost
            }
        }
        return isRow ? new int[]{pm, pc, mc, md} : new int[]{pc, pm, mc, md};
    }
}

//[0, 0, 50, 0, 0]
//[30, 0, 20, 0, 10]
//[0, 20, 0, 30, 0]
//[0, 0, 0, 0, 50]
//Total cost: 3100

