/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package net.coderextreme.jaminate;

import java.util.Arrays;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author john
 * @param <T>
 */
public class GenericTableModel<T extends GenericModel> extends AbstractTableModel {
    private DefaultTableModel model = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // TODO code application logic here
    }
    public GenericTableModel(DefaultTableModel model) {
       this.model = model;
    }
    public void addRow(T row, TableLoadSave.rowMode mode) {
        if (mode == TableLoadSave.rowMode.DATA) {
            this.getModel().addRow(row.getList(mode).toArray());
        } else if (mode == TableLoadSave.rowMode.HEADER) {
            //this.getModel().setColumnIdentifiers(row.getList(mode).toArray());
            //this.columnIdentifiers = row.getList(mode).toArray();
        } else {
            System.err.println("Adding row in OFF mode.  Is your HTML correct?");
            int reply = JOptionPane.showConfirmDialog(null,
                    "Possible problems parsing HTML?",  "Ok", JOptionPane.ERROR_MESSAGE);
            
        }  
    }

    /**
     * @return the model
     */
    public DefaultTableModel getModel() {
        return model;
    }
    public void reorder(int from, int to) {
        Vector fr = this.model.getDataVector().remove(from);
        this.model.getDataVector().add(to, fr);
        fireTableDataChanged();

    } 
    /**
     * @param model the model to set
     */
    public void setModel(DefaultTableModel model) {
        this.model = model;
    }

    @Override
    public int getRowCount() {
        return this.model.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return this.model.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.model.getValueAt(rowIndex, columnIndex);
    }

    Vector<Vector> getRows() {
        return this.model.getDataVector();
    }
}
