import model.Task;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class App {

    public static void main(String[] args) {


        Hashtable<Integer, Double> resultsCache = new Hashtable<Integer, Double>();

        Task t0 = new Task(0, new ArrayList<Double>(List.of(12.3, 12.3)), Task.Type.Add);
        Task t1 = new Task(1, new ArrayList<Double>(List.of(2.0)), Task.Type.Multiply);

        try {
            resultsCache.put(t0.getId(), t0.compute());
            resultsCache.put(t1.getId(), t1.compute());
        }
        catch (ArithmeticException ae){
            System.out.println("[Error] Error executing task: " + ae.getMessage().toString());
        }

        for(Integer key : resultsCache.keySet()){
            System.out.println(key.toString() + " : " + resultsCache.get(key).toString());

        }



        System.out.println("Done!");

    }


}
