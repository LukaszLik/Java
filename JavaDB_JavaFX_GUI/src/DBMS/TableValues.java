package DBMS;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.swing.text.TableView;
import java.util.ArrayList;
import java.util.List;

public class TableValues {
    private final SimpleObjectProperty val1;
    private final SimpleObjectProperty val2;
    private final SimpleObjectProperty val3;
    private final SimpleObjectProperty val4;
    private final SimpleObjectProperty val5;

    public TableValues(Object id, Object pesel, Object surname, Object name)
    {
        System.out.println("4 args");
        this.val1 = new SimpleObjectProperty(id);
        this.val2 = new SimpleObjectProperty(pesel);
        this.val3 = new SimpleObjectProperty(surname);
        this.val4 = new SimpleObjectProperty(name);
        this.val5 = null;
    }

    public  TableValues(Object id, Object title, Object author, Object releaseYear, Object type )
    {
        System.out.println("5 args");
        this.val1 = new SimpleObjectProperty(id);
        this.val2 = new SimpleObjectProperty(title);
        this.val3 = new SimpleObjectProperty(author);
        this.val4 = new SimpleObjectProperty(releaseYear);
        this.val5 = new SimpleObjectProperty(type);
    }

    public TableValues(List<Object> list){
        if(list.size() == 4){
            System.out.println("List 4 args");
            this.val1 = new SimpleObjectProperty(list.get(0));
            this.val2 = new SimpleObjectProperty(list.get(1));
            this.val3 = new SimpleObjectProperty(list.get(2));
            this.val4 = new SimpleObjectProperty(list.get(3));
            this.val5 = null;
        }

        else if (list.size() == 5){
            System.out.println("List 5 args");
            this.val1 = new SimpleObjectProperty(list.get(0));
            this.val2 = new SimpleObjectProperty(list.get(1));
            this.val3 = new SimpleObjectProperty(list.get(2));
            this.val4 = new SimpleObjectProperty(list.get(3));
            this.val5 = new SimpleObjectProperty(list.get(4));
        }

        else{
            System.out.println("List else");
            this.val1 = null;
            this.val2 = null;
            this.val3 = null;
            this.val4 = null;
            this.val5 = null;
        }
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

    public String getVal5(){
        return val5.get().toString();
    }

    public void setVal5(String name){
        val5.set(name);
    }

    public ArrayList<String> getAllValues(){
        ArrayList<String> list = new ArrayList<>();
        list.add(this.val1.get().toString());
        list.add(this.val2.get().toString());
        list.add(this.val3.get().toString());
        list.add(this.val4.get().toString());

        return list;
    }

    public String getString() {
        if(val5 == null)
            return this.getVal1() + " " + this.getVal2() + " " + this.getVal3() + " " + this.getVal4();

        else
            return this.getVal1() + " " + this.getVal2() + " " + this.getVal3() + " " + this.getVal4() + this.getVal5();


    }

}
