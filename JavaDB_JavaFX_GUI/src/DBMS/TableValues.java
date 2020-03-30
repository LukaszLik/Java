package DBMS;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.swing.text.TableView;
import java.util.List;

public class TableValues {
    private final SimpleObjectProperty val1;
    private final SimpleObjectProperty val2;
    private final SimpleObjectProperty val3;
    private final SimpleObjectProperty val4;

    public TableValues(Object id, Object pesel, Object surname, Object name)
    {
        this.val1 = new SimpleObjectProperty(id);
        this.val2 = new SimpleObjectProperty(pesel);
        this.val3 = new SimpleObjectProperty(surname);
        this.val4 = new SimpleObjectProperty(name);
    }

    public TableValues(List<Object> list){
            this(list.get(0), list.get(1), list.get(2), list.get(3));
    }

    public String getVal1(){
        return val1.get().toString();
    }

    public void setVal1(String id){
        val1.set(id);
    }

    public String getVal2(){
        return val2.get().toString();
    }

    public void setVal2(String pesel){
        val2.set(pesel);
    }

    public String getVal3(){
        return val3.get().toString();
    }

    public void setVal3(String surname){
        val3.set(surname);
    }

    public String getVal4(){
        return val4.get().toString();
    }

    public void setVal4(String name){
        val4.set(name);
    }

}
