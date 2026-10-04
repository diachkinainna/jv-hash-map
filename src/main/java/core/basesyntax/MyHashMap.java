package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_SIZE = 0;
    private static final int GROWTH_FACTOR = 2;
    private int size;
    private int capacity;
    private Node<K, V>[] table;

    public MyHashMap() {
        size = INITIAL_SIZE;
        capacity = INITIAL_CAPACITY;
        table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {

        Node<K, V> newNode = new Node<>(key, value);
        newNode.hash = getHash(key);
        int bucketIndex = getBucketIndex(newNode.hash);

        Node<K, V> currentNode = table[bucketIndex];

        while (currentNode != null) {
            if (compareKey(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }

            currentNode = currentNode.next;
        }

        if (!checkThreshold()) {
            resizeTable();
        }

        currentNode = table[bucketIndex];

        if (currentNode == null) {
            table[bucketIndex] = newNode;
            size++;
            return;
        }

        while (currentNode != null) {
            if (currentNode.next == null) {
                break;
            }
            currentNode = currentNode.next;
        }

        currentNode.next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(getHash(key));
        Node<K, V> currentNode = table[bucketIndex];

        while (currentNode != null) {
            if (compareKey(key, currentNode.key)) {
                return currentNode.value;
            }

            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean compareKey(K key1, K key2) {
        return key1 == null && key2 == null || key1 != null && key1.equals(key2);
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int getBucketIndex(int hash) {
        return hash % capacity;
    }

    private boolean checkThreshold() {
        return size < capacity * LOAD_FACTOR;
    }

    private void resizeTable() {
        int newCapacity = capacity * GROWTH_FACTOR;
        capacity = newCapacity;
        size = INITIAL_SIZE;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[newCapacity];

        for (Node<K, V> node: oldTable) {
            move(node);
        }
    }

    private void move(Node<K, V> node) {
        while (node != null) {
            Node<K, V> next = node.next;
            node.next = null;
            put(node.key, node.value);

            node = next;
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
