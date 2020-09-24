package shortener;
/*
Это будет некий аналог укорачивателя
ссылок Google URL Shortener (https://goo.gl), но мы расширим его функциональность и
сделаем консольным. Он будет сокращать не только ссылки, но и любые строки.
Наш Shortener – это класс, который может для любой строки вернуть некий
уникальный идентификатор и наоборот, по ранее полученному идентификатору
вернуть строку.
*/
class Shortener {
    private Long lastId = 0L;   //Это поле будет отвечать за последнее значение идентификатора, которое было использовано при добавлении новой строки в хранилище.
    private StorageStrategy storageStrategy;    //в котором будет храниться стратегия хранения данных.

    public Shortener(StorageStrategy storageStrategy) {
        this.storageStrategy = storageStrategy;
    }

    //будет возвращать идентификатор id для заданной строки.
    public synchronized Long getId(String string){
        if (storageStrategy.containsValue(string)){
            return storageStrategy.getKey(string);
        }
        lastId++;
        storageStrategy.put(lastId, string);
        return lastId;
    }
    
    //будет возвращать строку для заданного идентификатора или null, если передан неверный идентификатор.
    public synchronized String getString(Long id){
        return storageStrategy.getValue(id);
    }
}
