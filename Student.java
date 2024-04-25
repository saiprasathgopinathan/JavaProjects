package application;

import javafx.beans.property.*;

public class Student 
{
    public final IntegerProperty id;
    public final StringProperty name;
    public final DoubleProperty fees;

    public Student(int id, String name, double fees) 
    {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.fees = new SimpleDoubleProperty(fees);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getFees() {
        return fees.get();
    }

    public void setFees(double fees) {
        this.fees.set(fees);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public DoubleProperty feesProperty() {
        return fees;
    }

}
