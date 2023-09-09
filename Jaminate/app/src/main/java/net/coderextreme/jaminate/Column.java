/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package net.coderextreme.jaminate;

/**
 *
 * @author john
 */
public class Column {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // TODO code application logic here
    }
    public Column(String fieldName, String header, Class type) {
            this.fieldName = fieldName;
            this.header = header;
            this.type = type;
    }
    protected String fieldName = "";
    protected String header = "";
    protected Class type = Object.class;
}
