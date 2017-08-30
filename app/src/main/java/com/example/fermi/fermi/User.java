package com.example.fermi.fermi;

/**
 * Created by system1 on 30-08-2017.
 */

public class User {
    String name = "";
    boolean isHelper = false, maths = false, physics = false, chemistry = false, biology = false, arts = false, health = false, fitness = false;

    User() {
        //default constructor required
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHelper() {
        return isHelper;
    }

    public void setHelper(boolean helper) {
        isHelper = helper;
    }

    public boolean isMaths() {
        return maths;
    }

    public void setMaths(boolean maths) {
        this.maths = maths;
    }

    public boolean isPhysics() {
        return physics;
    }

    public void setPhysics(boolean physics) {
        this.physics = physics;
    }

    public boolean isChemistry() {
        return chemistry;
    }

    public void setChemistry(boolean chemistry) {
        this.chemistry = chemistry;
    }

    public boolean isBiology() {
        return biology;
    }

    public void setBiology(boolean biology) {
        this.biology = biology;
    }

    public boolean isArts() {
        return arts;
    }

    public void setArts(boolean arts) {
        this.arts = arts;
    }

    public boolean isHealth() {
        return health;
    }

    public void setHealth(boolean health) {
        this.health = health;
    }

    public boolean isFitness() {
        return fitness;
    }

    public void setFitness(boolean fitness) {
        this.fitness = fitness;
    }
}
