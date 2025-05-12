package dev.gracco.inventorium.utils;

public class Pair<L, R> {
    public final L left;
    public final R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" :: { left: %s, right: %s }", this.left, this.right);
    }
}
