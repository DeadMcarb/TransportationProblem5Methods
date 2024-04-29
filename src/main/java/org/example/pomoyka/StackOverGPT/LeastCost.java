package org.example.pomoyka.StackOverGPT;

import java.util.Scanner;

public class LeastCost {

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            int[][] c;
            int[] dem, sup, rf, cf;

            System.out.print("\nNumber of Rows: ");
            int m = scanner.nextInt();
            System.out.print("\nNumber of Columns: ");
            int n = scanner.nextInt();

            c = new int[m][n];
            dem = new int[n];
            sup = new int[m];
            rf = new int[m];
            cf = new int[n];
            int sum = 0;

            System.out.println("\nCost: "); // Matrix
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    System.out.print("Cost Matrix : " + (i + 1) + (j + 1) + ": ");
                    c[i][j] = scanner.nextInt();
                }
            }

            System.out.println("\nDemand: ");
            for (int i = 0; i < n; i++) {
                System.out.print("Demand [" + (i + 1) + "]: ");
                dem[i] = scanner.nextInt();
            }

            System.out.println("\nSupply: ");
            for (int i = 0; i < m; i++) {
                System.out.print("Supply [" + (i + 1) + "]: ");
                sup[i] = scanner.nextInt();
            }

            System.out.println("\nMatrix:");
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++)
                    System.out.print(c[i][j] + " | ");
                System.out.print(sup[i]);
                System.out.println();
            }
            for (int j = 0; j < n; j++)
                System.out.print(dem[j] + " ");

            int b = m;
            int d = n;

            while (b > 0 && d > 0) {
                int min = 1000;
                int p = 0, q = 0;

                for (int i = 0; i < m; i++) {
                    if (rf[i] != 1) {
                        for (int j = 0; j < n; j++) {
                            if (cf[j] != 1) {
                                if (min > c[i][j]) {
                                    min = c[i][j];
                                    p = i;
                                    q = j;
                                }
                            }
                        }
                    }
                }
                int c1;

                if (sup[p] < dem[q])
                    c1 = sup[p];
                else
                    c1 = dem[q];

                int c2;

                for (int i = 0; i < m; i++) {
                    if (rf[i] != 1) {
                        for (int j = 0; j < n; j++) {
                            if (cf[j] != 1) {
                                if (min == c[i][j]) {
                                    if (sup[i] < dem[j])
                                        c2 = sup[i];
                                    else
                                        c2 = dem[j];

                                    if (c2 > c1) {
                                        c1 = c2;
                                        p = i;
                                        q = j;
                                    }
                                }
                            }
                        }
                    }
                }

                if (sup[p] < dem[q]) {
                    sum += c[p][q] * sup[p];
                    dem[q] -= sup[p];
                    rf[p] = 1;
                    b--;
                } else if (sup[p] > dem[q]) {
                    sum += c[p][q] * dem[q];
                    sup[p] -= dem[q];
                    cf[q] = 1;
                    d--;
                } else if (sup[p] == dem[q]) {
                    sum += c[p][q] * sup[p];
                    rf[p] = 1;
                    cf[q] = 1;
                    b--;
                    d--;
                }
            }
            System.out.printf("\n\nTotal cost: %d\n\n", sum);
            scanner.close();
        }

}
