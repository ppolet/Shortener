package shortener.strategy;

//9

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import shortener.ExceptionHandler;

public class FileBucket {
    Path path;
    
    public FileBucket(){
        try {
            path = Files.createTempFile(null, null);
            //9.3.2. Создавать новый файл, используя path. Если такой файл уже есть, то заменять его.
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException ex) {
            ExceptionHandler.log(ex);
        }
        
        File file = path.toFile();
        file.deleteOnExit();
    }
    
    public long getFileSize(){
        try {
            return Files.size(path);
        } catch (IOException ex) {
            ExceptionHandler.log(ex);
        }
        return -1;
    }
    
    //должен сериализовывать переданный entry в файл. Учти, каждый entry может содержать еще один entry.
    public void putEntry(Entry entry){
        try (ObjectOutputStream ob = new ObjectOutputStream(Files.newOutputStream(path))){
            ob.writeObject(entry);
        } catch (IOException ex) {
            ExceptionHandler.log(ex);
        }
    }
    
    //должен забирать entry из файла. Если файл имеет нулевой размер, вернуть null.
    public Entry getEntry(){
        Entry entry = null;
        
        try (ObjectInputStream ob = new ObjectInputStream(Files.newInputStream(path))){
            if (getFileSize()>0){
                entry = (Entry)ob.readObject();
            }
        } catch (Exception ex) {
            System.out.print("---- ");
            ExceptionHandler.log(ex);
        }
        
        return entry;
        
    }
        
    //удалять файл на который указывает path
    public void remove(){
        try {
            Files.delete(path);
        } catch (IOException ex) {
            ExceptionHandler.log(ex);
        }
    }
}
