/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package net.coderextreme.jaminate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author john
 */
public abstract class GenericModel {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // TODO code application logic here
    }
    public abstract Column[] getFields();
    public Object getField(Integer htmlColumn, String setToValue)  {
        Integer col = getLocalColumnFromHTMLColumn(htmlColumn);
        if (col != null) {
            Column c = this.getFields()[col];
            Class valueClass = c.type;
            if (valueClass == Boolean.class) {
                return Boolean.valueOf(setToValue);
            } else if (valueClass == Double.class) {
                return Double.valueOf(setToValue);
            } else if (valueClass ==  Float.class) {
                return Float.valueOf(setToValue);
            } else if (valueClass ==  Integer.class) {
                return Integer.valueOf(setToValue);
            } else if (valueClass == Long.class) {
                return Long.valueOf(setToValue);
            } else {
                return setToValue;
            }
        } else {
            System.err.println("col is null, html is "+htmlColumn+" value is "+setToValue);
            return setToValue;
        }
    }
    private Column getColumn(Integer localColumn) {
        Column [] column = this.getFields();
        Column col = column[localColumn];
        return col;
    }
    public abstract Integer getLocalColumnFromHTMLHeader(String header);
    // Integer localColumn = htmlToColumn.get(setToValue);
    public abstract void putLocalColumnInHTMLColumn(Integer HTMLColumn, Integer localColumn);
    // Motion.htmlColumnToColumn[HTMLColumn] = localColumn;
    public abstract Integer getLocalColumnFromHTMLColumn(Integer HTMLColumn);
    // return  Motion.htmlColumnToColumn[HTMLColumn];
    public void setField(Integer HTMLColumn, TableLoadSave.rowMode mode, String setToValue) throws IllegalAccessException, NoSuchFieldException {
        if (mode == TableLoadSave.rowMode.HEADER) {
            Integer localColumn = getLocalColumnFromHTMLHeader(setToValue);
            putLocalColumnInHTMLColumn(HTMLColumn, localColumn);
        } else if (mode == TableLoadSave.rowMode.DATA) {
            Integer localColumn = getLocalColumnFromHTMLColumn(HTMLColumn);
            setField(localColumn, setToValue);
            /*if (localColumn != null) {
                Column col = this.getColumn(localColumn);
                Field myField = Motion.class.getDeclaredField(col.fieldName);
                myField.setAccessible(true);
                Object value = this.getField(localColumn, setToValue);
                String colstr = col.type.getName(); // for debugging
                myField.set(this, col.type.cast(value));
            } else {
                System.err.println("localColumn is null in Motion.setField, column="+HTMLColumn+" localColumn="+localColumn);
            }*/
        }
    }
    public List getList(TableLoadSave.rowMode mode) {
       List row = new ArrayList();
       if (mode == TableLoadSave.rowMode.DATA) {
            for (Integer column = 0; column < this.getFields().length; column++) {
                try {
                    Column c = this.getColumn(column);
                    Field myField = Motion.class.getDeclaredField(c.fieldName);
                    myField.setAccessible(true);
                    Object o = myField.get(this);
                    row.add(o);
                } catch (IllegalAccessException | NoSuchFieldException e){
                    e.printStackTrace(System.err);
                }
            }
        } else if (mode == TableLoadSave.rowMode.HEADER) {
            for (Integer column = 0; column < this.getFields().length; column++) {
                Column c = this.getColumn(column);
                row.add(c.header);
            }
        }
        
        return row;
    }
    public void setField(Integer localColumn, String setToValue) {
        if (localColumn != null) {
            try {
                Column col = this.getColumn(localColumn);
                Field myField = Motion.class.getDeclaredField(col.fieldName);
                myField.setAccessible(true);
                Object value = this.getField(localColumn, setToValue);
                String colstr = col.type.getName(); // for debugging
                myField.set(this, col.type.cast(value));
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException iae) {
                iae.printStackTrace(System.err);
            }
        } else {
            System.err.println("localColumn is null in Motion.setField, localColumn=" + localColumn);
        }
    }
}
