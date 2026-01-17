package Entity;

import java.io.Serializable;

public class Patient implements Serializable {
    private String serialNumber;
    private String name;
    private int age;

    public Patient() {
    }

    public Patient(String serialNumber, String name, int age) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.age = age;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
 
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "serialNumber='" + serialNumber + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}