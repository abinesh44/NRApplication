package com.abinesh.app.message;

/**
 * Created by abine on 12/10/2017.
 */
public class DedupeStatus {
    int number;
    boolean unique;

    public DedupeStatus(int number, boolean unique) {
        this.number = number;
        this.unique = unique;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    @Override
    public String toString() {
        return "DedupeStatus{" +
                "number=" + number +
                ", unique=" + unique +
                '}';
    }
}
