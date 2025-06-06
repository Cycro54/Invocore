package invoker54.invocore.client.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommonUtil {
    public static <U> List<U> pickRandomObjectsFromList(int count, List<U> objectList){
        List<U> copyList = new ArrayList<>(objectList);
        Random random = new Random();
        List<U> chosenList = new ArrayList<>();

         for (int a = 0; a < count; a++){
             U object = copyList.get(random.nextInt(copyList.size()));
             chosenList.add(object);
             copyList.remove(object);
         }

         return chosenList;
    }
}
