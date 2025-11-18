package krawczyk.grzegorz.models;

/**
 * Class is used to display size of a message.
 * It overrides toString() method.
 * It implements Comparable interface.
 */
public class SizeInteger implements Comparable<SizeInteger> {
    private int size;

    /**
     * Constructor of the class SizeInteger.
     * @param size - int containing size of a message.
     */
    public SizeInteger(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        if (size <= 0) {
            return "0";
        } else if (size < 1024) {
            return size + " B";
        } else if (size < 1048576) {
            return size / 1024 + " kB";
        } else {
            return size / 1048576 + " MB";
        }
    }

    @Override
    public int compareTo(SizeInteger o) {
        if (size > o.size) {
            return 1;
        } else if (size > o.size) {
            return -1;
        } else {
            return 0;
        }
    }
}
