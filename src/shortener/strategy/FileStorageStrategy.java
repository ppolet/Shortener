package shortener.strategy;

//10

import shortener.StorageStrategy;

public class FileStorageStrategy implements StorageStrategy{
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final long DEFAULT_BUCKET_SIZE_LIMIT = 1000L;
    private FileBucket[] table = new FileBucket[DEFAULT_INITIAL_CAPACITY];
    private int size;
    private long bucketSizeLimit = DEFAULT_BUCKET_SIZE_LIMIT;
    private long maxBucketSize = DEFAULT_BUCKET_SIZE_LIMIT;
    
//    public FileStorageStrategy(){
//        for(int i = 0; i < table.length; i++){
//            table[i] = new FileBucket();
//        }
//    }

    public long getBucketSizeLimit() {
        return bucketSizeLimit;
    }

    public void setBucketSizeLimit(long bucketSizeLimit) {
        this.bucketSizeLimit = bucketSizeLimit;
    }

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
        for (Entry e = table[indexFor(hash, table.length)].getEntry(); e != null; e = e.next) {
            Long k;
            if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))){
                return e;
            }
        }
        return null;
    }
            
    //Изменить размер
    public void resize(int newCapacity){
        FileBucket[] newTable = new FileBucket[newCapacity];
        transfer(newTable);

        table = newTable;

        for(FileBucket b: table){
            b.remove();
        }
    }
    
    //Переносит все записи из старой таблицы в новую
    public void transfer(FileBucket[] newTable){
        FileBucket[] src = table;
        int newCapacity = newTable.length;
        
        for (int j = 0; j < src.length; j++) {
            FileBucket oldBucket = src[j];
            Entry e = oldBucket.getEntry();
            if (e != null) {
                src[j] = null;
                do {
                    Entry next = e.next;
                    int i = indexFor(e.hash, newCapacity);
                    FileBucket newTableBucket;
                    
                    if(newTable[i] == null){
                        newTableBucket = new FileBucket();
                        newTable[i] = newTableBucket;
                    } else {
                        newTableBucket = newTable[i];
                    }
                    
                    e.next = newTableBucket.getEntry();
                    newTableBucket.putEntry(e);
                    e = next;
                } while (e != null);
            }
        }
    }
    
    //Добавляет новую запись со специфичным key, value и hash кодом в спец.bucket
    public void addEntry(int hash, Long key, String value, int bucketIndex){
        createEntry(hash, key, value, bucketIndex);
        FileBucket b = table[bucketIndex];

        if (b.getFileSize() > bucketSizeLimit){
            resize(2 * table.length);
        }
    }
    
    /**
     * Like addEntry except that this version is used when creating entries
     * as part of Map construction or "pseudo-construction" (cloning,
     * deserialization).This version needn't worry about resizing the table.  Subclass overrides this to alter the behavior of HashMap(Map),
 clone, and readObject.
     */
    //Создает новую запись
    public void createEntry(int hash, Long key, String value, int bucketIndex){
        FileBucket b = table[bucketIndex];
        if (b == null) {
            b = new FileBucket();
            table[bucketIndex] = b;
        }

        Entry e = b.getEntry();
        Entry newEntry = new Entry(hash, key, value, e);
        b.putEntry(newEntry);
        size++;
    }
    
    @Override
    public boolean containsKey(Long key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(String value) {
        for(int i = 0; i < table.length; i++){
            FileBucket b = table[i];
            
            if(b == null){
                continue;
            }
            
            for(Entry e = b.getEntry(); e != null; e = e.next){
                if(e.getValue().equals(value)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void put(Long key, String value) {
        int hash = hash(key);
        int i = indexFor(hash, table.length);
        FileBucket b = table[i];
        
        if (b == null){
            b = new FileBucket();
            table[i] = b;
        }
        
        for (Entry e = b.getEntry(); e != null; e = e.next) {
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
        for(int i = 0; i < table.length; i++){
            FileBucket b = table[i];
            if(b != null){
                for(Entry e = b.getEntry(); e != null; e = e.next){
                    if (e.getValue().equals(value)) return e.getKey();
                }
            }
        }
        return null;
    }

    @Override
    public String getValue(Long key) {
        return getEntry(key).getValue();
    }
    
}
