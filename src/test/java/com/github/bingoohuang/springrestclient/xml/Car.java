package com.github.bingoohuang.springrestclient.xml;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"brand", "description"})
public class Car {
    @XmlAttribute
    private String registration;
    private String brand;
    private String description;

    public Car() {
    }

    public Car(String registration, String brand, String description) {
        this.registration = registration;
        this.brand = brand;
        this.description = description;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Car car = (Car) o;

        if (registration != null ? !registration.equals(car.registration) : car.registration != null) return false;
        if (brand != null ? !brand.equals(car.brand) : car.brand != null) return false;
        return !(description != null ? !description.equals(car.description) : car.description != null);

    }

    @Override
    public int hashCode() {
        int result = registration != null ? registration.hashCode() : 0;
        result = 31 * result + (brand != null ? brand.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}