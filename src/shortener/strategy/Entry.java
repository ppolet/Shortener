
package shortener.strategy;

import java.io.Serializable;
import java.util.Objects;

//7
public class Entry implements Serializable{
    Long key;
    String value;
    Entry next;
    int hash;

    public Entry(int hash, Long key, String value, Entry next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public Long getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = getKey().hashCode();;
        hash = 23 * hash + getValue().hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Entry other = (Entry) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        return true;
    }



    @Override
    public String toString() {
        return "key=" + key + ", value=" + value;
    }
    
    
}
