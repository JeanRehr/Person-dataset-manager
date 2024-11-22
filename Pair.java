package gb;

public class Pair<K, V extends Comparable<V>> implements Comparable<Pair<K, V>> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public int compareTo(Pair<K, V> other) {
        return this.value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return "Key: " + key + ", Value: " + value;
    }
}