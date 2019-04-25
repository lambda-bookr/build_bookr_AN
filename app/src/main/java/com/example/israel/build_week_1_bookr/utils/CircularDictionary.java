package com.example.israel.build_week_1_bookr.utils;

public class CircularDictionary<K, E> {
    private E[] data;
    private K[] keys;
    private int position, size;

    public CircularDictionary(int size) {
        data = (E[]) new Object[size];
        keys = (K[]) new Object[size];
        this.size = size;
        position = 0;
    }

    public void put(K key, E item) {
        keys[position] = key;
        data[position] = item;
        incrementPosition();
    }

    public E get() {
        E element = data[position];
        incrementPosition();
        return element;
    }

    public E get(K key) {
        int index = -1;
        for (int i = 0; i < keys.length; ++i) {
            if (key.equals(keys[i])) {
                index = i;
                break;
            }
        }
        return index == -1 ? null : data[index];
    }

    private void incrementPosition() {
        ++position;
        if (position >= data.length) {
            position = 0;
        }
    }

    public int size() {
        return size;
    }
}