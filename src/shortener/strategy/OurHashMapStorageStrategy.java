
package shortener.strategy;

//8

import shortener.StorageStrategy;

public class OurHashMapStorageStrategy implements StorageStrategy{
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Entry[] table = new Entry[DEFAULT_INITIAL_CAPACITY];
    private int size;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private float loadFactor = DEFAULT_LOAD_FACTOR;    

    public int hash(Long k){
        return k.hashCode();
    }
    
    //позиция в массиве, куда будет помещен элемент.
    public int indexFor(int hash, int length){
        return hash & (length - 1);
    }
    
    //возвращает entry в соответствии с key
    public Entry getEntry(Long key){
        int hash = (key == null) ? 0 : hash(key);
        for (Entry e = table[indexFor(hash, table.length)];
             e != null;
             e = e.next) {
            Object k;
            if (e.hash == hash &&
                ((k = e.key) == key || (key != null && key.equals(k))))
                return e;
        }
        return null;
    }
            
    //Изменить размер
    public void resize(int newCapacity){
        Entry[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == (1 << 30)) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        Entry[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int)(newCapacity * loadFactor);
    }
    
    //Переносит все записи из старой таблицы в новую
    public void transfer(Entry[] newTable){
        int newCapacity = newTable.length;
        for (int j = 0; j < table.length; j++) {
            Entry e = table[j];
            if (e != null) {
                table[j] = null;
                do {
                    Entry next = e.next;
                    int i = indexFor(e.hash, newCapacity);
                    e.next = newTable[i];
                    newTable[i] = e;
                    e = next;
                } while (e != null);
            }
        }
    }
    
    //Добавляет новую запись со специфичным key, value и hash кодом в спец.bucket
    public void addEntry(int hash, Long key, String value, int bucketIndex){
        Entry e = table[bucketIndex];
        table[bucketIndex] = new Entry(hash, key, value, e);
        if (size++ >= threshold){
            resize(2 * table.length);
        }
    }
    
    /**
     * Like addEntry except that this version is used when creating entries
     * as part of Map construction or "pseudo-construction" (cloning,
     * deserialization).  This version needn't worry about resizing the table.
     *
     * Subclass overrides this to alter the behavior of HashMap(Map),
     * clone, and readObject.
     */
    //Создает новую запись
    public void createEntry(int hash, Long key, String value, int bucketIndex){
        Entry e = table[bucketIndex];
        table[bucketIndex] = new Entry(hash, key, value, e);
        size++;
    }
    
    //содержит ключ?
    @Override
    public boolean containsKey(Long key) {
        return getEntry(key) != null;
    }

    //содержит значение?
    @Override
    public boolean containsValue(String value) {
        Entry t[] = table;
        for(int i = 0; i < t.length; i++){
            for(Entry e = t[i]; e != null; e = e.next){
                if (value.equals(e.value)) return true;
            }
        }
        return false;
    }

    @Override
    public void put(Long key, String value) {
        int hash = hash(key);
        int i = indexFor(hash, table.length);

        for (Entry e = table[i]; e != null; e = e.next) {
            Long k;

            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                e.value = value;
                return;
            }
        }

        addEntry(hash, key, value, i);
    }

    @Override
    public Long getKey(String value) {
        Entry t[] = table;
        for(int i = 0; i < t.length; i++){
            for(Entry e = t[i]; e != null; e = e.next){
                if (e.getValue().equals(value)) return e.getKey();
            }
        }
        return null;
    }

    @Override
    public String getValue(Long key) {
        return getEntry(key).value;
    }
    
}
