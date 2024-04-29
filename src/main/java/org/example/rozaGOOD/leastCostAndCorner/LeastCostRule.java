/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.rozaGOOD.leastCostAndCorner;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;


public class LeastCostRule {
    
        
    double []required;
    double []stock;
    double [][]cost;
    LinkedList<Variable> feasible = new LinkedList<Variable>();
    
    int stockSize;    
    int requiredSize;

    public static void calculate(int[] demand, int[] supply, int[][] costs) throws IOException {

//        int[][] costs = new int[][] {
//                { 5, 1, 2, 4 },
//                { 6, 5, 7, 6 },
//                { 6, 4, 6, 8 },
//                { 7, 5, 5, 7 }
//        }; // table
//
//        int[] supply = new int[] { 70, 50, 70, 70 }; // supply
//        int[] demand = new int[] { 110, 35, 25, 100 }; // demand


//        Scanner scanner = new Scanner(System.in);

//        System.out.println("Please enter the problem size:");

        double x;
        LeastCostRule test = new LeastCostRule(4, 4);
        System.out.println("LeastCostRule:");
        System.out.println(" ");


//        System.out.println("lease enter the supply:");
        for (int i = 0; i < test.stockSize; i++){
//            x = scanner.nextDouble();
//            test.setStock(x, i);

            test.setStock(supply[i], i);
        }

//        System.out.println("Please enter the requirements:");
        for (int i = 0; i < test.requiredSize; i++){
//            x = scanner.nextDouble();
//            test.setRequired(x, i);

            test.setRequired(demand[i], i);
        }

//        System.out.println("Please enter the transportation costs:");
        for (int i = 0; i < test.stockSize; i++)
            for (int j = 0; j < test.requiredSize; j++) {
//                x = scanner.nextDouble();
//                test.setCost(x, i, j);

                test.setCost(costs[i][j], i, j);
            }




        //test.northWestCorner();
        test.leastCostRule();
        //////////////////////////////////////////////////////
        int[][] resultMatrix = new int[][] {
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 }
        };

        for(Variable t: test.feasible){
            resultMatrix[t.getStock()][t.getRequired()] = (int) t.getValue();
//            System.out.println(t);
        }
//        System.out.println(Arrays.deepToString(resultMatrix));

        Arrays.stream(resultMatrix).map(Arrays::toString).forEach(System.out::println);

//        public String toString() {
//            Formatter f = new Formatter();
//            f.format("x[%d,%d]=%f", this.stock+1, this.required+1, this.value);
//            return f.toString();
//        }
        //////////////////////////////////////////////////////

        System.out.println("Target function: " + test.getSolution());

    }




    public LeastCostRule(int stockSize, int requiredSize ){
        this.stockSize = stockSize;
        this.requiredSize = requiredSize;
        
        stock = new double[stockSize];
        required = new double[requiredSize];
        cost = new double[stockSize][requiredSize];
        
        for(int i=0; i < (requiredSize + stockSize -1); i++)
            feasible.add(new Variable());

    }
    
    public void setStock(double value, int index){
        stock[index] = value;        
    }
    
    public void setRequired(double value, int index){
        required[index] = value;        
    }
    
    
    public void setCost(double value, int stock, int required){
        cost[stock][required] = value;
    }
    
    /**
     * initializes the feasible solution list using the North-West Corner
     * @return time elapsed
     */
    
    public double northWestCorner() {
        long start = System.nanoTime();
        
        double min;        
        int k = 0; //feasible solutions counter
        
        //isSet is responsible for annotating cells that have been allocated
        boolean [][]isSet = new boolean[stockSize][requiredSize];        
        for (int j = 0; j < requiredSize; j++)
            for (int i = 0;  i < stockSize; i++)
                    isSet[i][j] = false;
        
        //the for loop is responsible for iterating in the 'north-west' manner
        for (int j = 0; j < requiredSize; j++)
            for (int i = 0;  i < stockSize; i++)
                if(!isSet[i][j]){
                    
                    //allocating stock in the proper manner
                    min = Math.min(required[j], stock[i]);
                    
                    feasible.get(k).setRequired(j);
                    feasible.get(k).setStock(i);
                    feasible.get(k).setValue(min);
                    k++;

                    required[j] -= min;
                    stock[i] -= min;
                    
                    //allocating null values in the removed row/column
                    if(stock[i] == 0)
                        for(int l = 0; l < requiredSize; l++)
                            isSet[i][l] = true;                    
                    else
                        for(int l = 0; l < stockSize; l++)
                            isSet[l][j] = true;
                }
        return (System.nanoTime() - start) * 1.0e-9;
    }
    
    /**
     * initializes the feasible solution list using the Least Cost Rule
     * 
     * it differs from the North-West Corner rule by the order of candidate cells
     * which is determined by the corresponding cost
     * 
     * @return double: time elapsed
     */
    
    public double leastCostRule() {
        long start = System.nanoTime();

        double min;        
        int k = 0; //feasible solutions counter
        
        //isSet is responsible for annotating cells that have been allocated
        boolean [][]isSet = new boolean[stockSize][requiredSize];        
        for (int j = 0; j < requiredSize; j++)
            for (int i = 0;  i < stockSize; i++)
                    isSet[i][j] = false;
        
        int i = 0, j = 0;
        Variable minCost = new Variable();
        
        //this will loop is responsible for candidating cells by their least cost
        while(k < (stockSize + requiredSize - 1)){
            
            minCost.setValue(Double.MAX_VALUE);            
            //picking up the least cost cell          
            for (int m = 0;  m < stockSize; m++)
                for (int n = 0; n < requiredSize; n++)
                    if(!isSet[m][n])
                        if(cost[m][n] < minCost.getValue()){
                            minCost.setStock(m);
                            minCost.setRequired(n);
                            minCost.setValue(cost[m][n]);
                        }            
            
            i = minCost.getStock();
            j = minCost.getRequired();            
            
            //allocating stock in the proper manner
            min = Math.min(required[j], stock[i]);

            feasible.get(k).setRequired(j);
            feasible.get(k).setStock(i);
            feasible.get(k).setValue(min);
            k++;

            required[j] -= min;
            stock[i] -= min;
            
            //allocating null values in the removed row/column
            if(stock[i] == 0)
                for(int l = 0; l < requiredSize; l++)
                    isSet[i][l] = true;                    
            else
                for(int l = 0; l < stockSize; l++)
                    isSet[l][j] = true;

        }
        
        return (System.nanoTime() - start) * 1.0e-9;    
            
    }
    
    public double getSolution(){
        double result = 0;
        for(Variable x: feasible){
            result += x.getValue() * cost[x.getStock()][x.getRequired()];
        }
        
        return result;
    
    }
    



    

}
