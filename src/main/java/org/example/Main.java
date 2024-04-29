package org.example;

import org.example.rozaGOOD.leastCostAndCorner.LeastCostRule;
import org.example.rozaGOOD.rosettacode.CornerAndStone;
import org.example.rozaGOOD.rosettacodeVogel.VogelsApproximationMethod;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    static int[] demand;
    static int[] supply;



        public static void main(String[] args) {
            double[][] costs;

                try (Scanner sc = new Scanner(
                        new File("C:\\morozova\\TransportationProblem5Methods\\src\\main\\java\\org\\example\\input1.txt"))) {
                    int numSources = sc.nextInt();
                    int numDestinations = sc.nextInt();

                    List<Integer> src = new ArrayList<>();
                    List<Integer> dst = new ArrayList<>();


                    for (int i = 0; i < numSources; i++)
                        src.add(sc.nextInt());

                    for (int i = 0; i < numDestinations; i++)
                        dst.add(sc.nextInt());

                    // fix imbalance
//                    int totalSrc = src.stream().mapToInt(i -> i).sum();
//                    int totalDst = dst.stream().mapToInt(i -> i).sum();
//                    if (totalSrc > totalDst)
//                        dst.add(totalSrc - totalDst);
//                    else if (totalDst > totalSrc)
//                        src.add(totalDst - totalSrc);

                    supply = src.stream().mapToInt(i -> i).toArray();
                    demand = dst.stream().mapToInt(i -> i).toArray();

                    costs = new double[supply.length][demand.length];


                    for (int i = 0; i < numSources; i++)
                        for (int j = 0; j < numDestinations; j++)
                            costs[i][j] = sc.nextDouble();


                    System.out.println("costs");
                    Arrays.stream(costs).map(Arrays::toString).forEach(System.out::println);
                    System.out.println("supply");
                    System.out.println(Arrays.toString(supply));
                    System.out.println("demand");
                    System.out.println(Arrays.toString(demand));



                    int[][] intArray = Arrays.stream(costs)
                            .map(row -> Arrays.stream(row)
                                    .mapToInt(d -> (int) d)
                                    .toArray())
                            .toArray(int[][]::new);

//                    System.out.println("costs");
//                    for (int[] row : intArray) {
//                        System.out.println(Arrays.toString(row));
//                    }


                    VogelsApproximationMethod.calculate(demand.clone(), supply.clone(), intArray.clone());
                    LeastCostRule.calculate(demand.clone(), supply.clone(), intArray.clone());
                    CornerAndStone.calculate(demand.clone(), supply.clone(), costs.clone());

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
        }
        }
