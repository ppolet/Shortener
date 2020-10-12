
package shortener;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import shortener.strategy.FileStorageStrategy;
import shortener.strategy.HashMapStorageStrategy;
import shortener.strategy.OurHashMapStorageStrategy;

public class Solution {
    public static Set<Long> getIds(Shortener shortener, Set<String> strings){
        Set<Long> result = new HashSet<>();
        for(String st: strings){
            result.add(shortener.getId(st));
        }
        return result;
    }
    
    public static Set<String> getStrings(Shortener shortener, Set<Long> keys){
        Set<String> result = new HashSet<>();
        for(Long k: keys){
            result.add(shortener.getString(k));
        }
        return result;
    }
    
    public static void testStrategy(StorageStrategy strategy, long elementsNumber){
        //6.2.3.1.	Выводить имя класса стратегии. Имя не должно включать имя пакета.
        Helper.printMessage("Strategy: " + strategy.getClass().getSimpleName());
        
        //6.2.3.2.	Генерировать тестовое множество строк, используя Helper и заданное количество элементов elementsNumber.
        Set<String> testStrings = new HashSet<>();
        for(long i = 0; i < elementsNumber; i++){
            testStrings.add(Helper.generateRandomString());
        }
        
        //6.2.3.3.	Создавать объект типа Shortener, используя переданную стратегию.
        Shortener shortener = new Shortener(strategy);
        
        //6.2.3.4
        long startTimer = new Date().getTime();
        Set<Long> testIds = getIds(shortener, testStrings);
        long endTimer = new Date().getTime();
        Helper.printMessage("Timer for getIds (ms): " + (endTimer - startTimer));
        
        //6.2.3.5. время необходимое для отработки метода getStrings для заданной стратегии и полученного в предыдущем пункте множества идентификаторов.
        startTimer = new Date().getTime();
        Set<String> testGetStrings = getStrings(shortener, testIds);
        endTimer = new Date().getTime();
        Helper.printMessage("Timer for getStrings (ms): " + (endTimer - startTimer));
        
        //6.2.3.6
        if(testStrings.equals(testGetStrings)){
            Helper.printMessage("Тест пройден.");
        } else {
            Helper.printMessage("Тест не пройден.");
        }
    }

    public static void main(String[] args) {
        //6.2.4
        Helper.printMessage("--- HashMapStorageStrategy ---");
        testStrategy(new HashMapStorageStrategy(), 10000);

        System.out.println();
        Helper.printMessage("--- OurHashMapStorageStrategy ---");
        testStrategy(new OurHashMapStorageStrategy(), 10000);

        System.out.println();
        Helper.printMessage("--- FileStorageStrategy ---");
        testStrategy(new FileStorageStrategy(), 1);
    }
}
