package com.github.bingoohuang.springrestclient.boot.domain;

public class Car {
    private String brand;
    private int age;

    public Car() {
    }

    public Car(String brand, int age) {
        this.brand = brand;
        this.age = age;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Car car = (Car) o;

        if (age != car.age) return false;
        return !(brand != null ? !brand.equals(car.brand) : car.brand != null);

    }

    @Override
    public int hashCode() {
        int result = brand != null ? brand.hashCode() : 0;
        result = 31 * result + age;
        return result;
    }
}
