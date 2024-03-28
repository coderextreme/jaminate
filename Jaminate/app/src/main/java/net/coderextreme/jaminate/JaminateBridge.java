/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package net.coderextreme.jaminate;

import com.stackoverflow.HtmlSelection;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.xml.sax.InputSource;

class ClipboardItem {

    int row;
    int col;
    Object item;

    ClipboardItem(Object val, int r, int c) {
        this.item = val;
        this.row = r;
        this.col = c;
    }
}

/**
 *
 * @author john
 */
public class JaminateBridge extends javax.swing.JFrame {

    private int[] sourceRows;
    private int sourceColumn;
    private int sourceRow;
    private TableColumn checkboxColumn;
    private GenericTableModel tableModel;
    private int[] sourceColumns;
    private GenericTableModel clipboard;
    private ArrayList<Object> clipboardReversed;
    private Point mousePressPoint;
    private boolean multiselect;
    private ArrayList<int[]> cellSelectClipboard;
    private ArrayList<int[]> cellCopyClipboard;
    private ArrayList<ClipboardItem> cellClipboard;

    /**
     * Creates new form JaminateBridge
     */
    public JaminateBridge() {
        initComponents();
        this.jFileChooser1.setVisible(false);
        //jTable1.setColumnSelectionAllowed(true);
        this.jTable1.setRowSelectionAllowed(true);
        this.jTable1.setColumnSelectionAllowed(true);
        this.jTable1.setVisible(true);
        this.tableModel = new GenericTableModel((DefaultTableModel) jTable1.getModel());
        jScrollPane3.setPreferredSize(new Dimension(1500, 800));
        /*
        setEditableComboBox(2);
        setEditableComboBox(4);
        setEditableComboBox(14);
         */
    }

    public int[] getRowColItem(Point xy, JTable jt) {
        JaminateBridge.this.mousePressPoint = xy;
        int r = jt.rowAtPoint(JaminateBridge.this.mousePressPoint);
        int c = jt.columnAtPoint(JaminateBridge.this.mousePressPoint);
        int[] cell = new int[]{r, c};
        return cell;
    }

    public void copy() {
        if (JaminateBridge.this.cellCopyClipboard == null) {
            JaminateBridge.this.cellCopyClipboard = new ArrayList<>();
        }
        JaminateBridge.this.cellCopyClipboard.clear();
        JaminateBridge.this.cellCopyClipboard.addAll(JaminateBridge.this.cellSelectClipboard);
    }

    public void cut() {
        if (JaminateBridge.this.cellCopyClipboard == null) {
            JaminateBridge.this.cellCopyClipboard = new ArrayList<>();
        }
        for (int[] cell : JaminateBridge.this.cellCopyClipboard) {
            JaminateBridge.this.cellClipboard.add(new ClipboardItem(jTable1.getValueAt(cell[0], cell[1]), cell[0], cell[1]));
            jTable1.setValueAt(null, cell[0], cell[1]);
        }
        JaminateBridge.this.tableModel.fireTableDataChanged();
        repaint();
    }

    public void paste() {
        int[] rc = getRowColItem(JaminateBridge.this.mousePressPoint, jTable1);
        for (ClipboardItem ci : this.cellClipboard) {
            JaminateBridge.this.jTable1.setValueAt(ci.item, ci.row + rc[0], ci.col + rc[1]);
        }
        JaminateBridge.this.tableModel.fireTableDataChanged();
        repaint();
    }

    public void copyRows() {
        JaminateBridge.this.sourceRows = JaminateBridge.this.jTable1.getSelectedRows();
        System.err.println("rows " + Arrays.asList(JaminateBridge.this.sourceRows).toString());
        JaminateBridge.this.clipboard = new GenericTableModel((DefaultTableModel)JaminateBridge.this.jTable1.getModel());
        for (int sr = 0; sr < JaminateBridge.this.sourceRows.length; sr++) {
            Motion motion = new Motion(JaminateBridge.this.tableModel, JaminateBridge.this.sourceRows[sr]);
            JaminateBridge.this.clipboard.addRow(motion, TableLoadSave.rowMode.DATA);
        }
    }

    public void pasteRows() {
        int to = JaminateBridge.this.jTable1.rowAtPoint(JaminateBridge.this.mousePressPoint);
        int targetColumn = JaminateBridge.this.jTable1.getSelectedColumn();
        Vector<Vector> rows = JaminateBridge.this.clipboard.getRows();
        int tr = 0;
        if (rows.size() > 0) {
            for (Vector row : rows) {
                if (row.size() > 0) {
                    for (Object obj : row) {
                        JaminateBridge.this.jTable1.setValueAt(obj,
                                tr,
                                targetColumn);
                    }
                }
                tr++;
            }
        }
        jTextArea1.setText("Cut rows");
        JaminateBridge.this.tableModel.fireTableDataChanged();
        repaint();
    }

    public void cutRows() {
        try {
            for (int sr = JaminateBridge.this.sourceRows.length - 1; sr >= 0; sr--) {
                JaminateBridge.this.tableModel.getModel().removeRow(JaminateBridge.this.sourceRows[sr]);
            }
            jTextArea1.setText("Cut rows");
            JaminateBridge.this.tableModel.fireTableDataChanged();
            repaint();
        } catch (NullPointerException e) {
            JaminateBridge.this.tableModel = new GenericTableModel((DefaultTableModel) jTable1.getModel());
            int reply = JOptionPane.showConfirmDialog(null,
                    "Creating a table model for you!", "Ok", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /*
    private void setEditableComboBox(Integer column) {
         java.awt.EventQueue.invokeLater(() -> {            
            TableCellEditor editor = jTable1.getColumnModel().getColumn(column).getCellEditor();
            JComboBox box = (JComboBox)editor.getTableCellEditorComponent(jTable1, JaminateBridge.this, rootPaneCheckingEnabled, 0, column);
            jTable1.getColumnModel().getColumn(column).setCellEditor(editor);
            box.setEditable(true);
            box.addActionListener((ActionEvent ae) -> {
                boolean found = false;
                for (int opt = 0; opt < box.getItemCount(); opt++) {
                    Object item = box.getItemAt(opt);
                    if (item != null && item.equals(box.getSelectedItem())) {
                        found = true;
                    }
                }
                if (!found) {
                    Object o = box.getSelectedItem();
                    if (o != null && !o.toString().trim().equals("")) {
                        box.addItem(box.getSelectedItem());
                    }
                } else {
                    // System.err.println("Found "+box.getSelectedItem());
                }
            });
        });             
    }
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        Cut = new javax.swing.JMenuItem();
        Copy = new javax.swing.JMenuItem();
        Paste = new javax.swing.JMenuItem();
        CopyRows = new javax.swing.JMenuItem();
        CutRows = new javax.swing.JMenuItem();
        PasteRows = new javax.swing.JMenuItem();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jFileChooser1 = new javax.swing.JFileChooser();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane(jTable1);
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        Open = new javax.swing.JMenuItem();
        Save = new javax.swing.JMenuItem();
        Import = new javax.swing.JMenuItem();
        Export = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();

        Cut.setText("Cut");
        Cut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CutActionPerformed(evt);
            }
        });
        jPopupMenu1.add(Cut);

        Copy.setText("Copy");
        Copy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CopyActionPerformed(evt);
            }
        });
        jPopupMenu1.add(Copy);

        Paste.setText("Paste");
        Paste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PasteActionPerformed(evt);
            }
        });
        jPopupMenu1.add(Paste);

        CopyRows.setText("Copy rows");
        CopyRows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CopyRowsActionPerformed(evt);
            }
        });
        CopyRows.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CopyRowsKeyPressed(evt);
            }
        });
        jPopupMenu1.add(CopyRows);

        CutRows.setText("Cut rows");
        CutRows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CutRowsActionPerformed(evt);
            }
        });
        jPopupMenu1.add(CutRows);

        PasteRows.setLabel("Paste rows");
        PasteRows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PasteRowsActionPerformed(evt);
            }
        });
        jPopupMenu1.add(PasteRows);

        jDesktopPane1.setLayer(jFileChooser1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jSplitPane1.setRightComponent(jScrollPane2);

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setMinimumSize(new java.awt.Dimension(1500, 20));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(1500, 800));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new String [] {
                "Check all", "Id", "Character", "Time start [0,1]", "Motion", "Submotion", "X", "Y", "Z", "Cycle interval", "X axis", "Y axis", "Z axis", "Degrees", "URL"
            }, 4000
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        jTable1.setColumnSelectionAllowed(true);
        jTable1.setMaximumSize(new java.awt.Dimension(2147483647, 1080));
        jTable1.setMinimumSize(new java.awt.Dimension(1920, 1080));
        jTable1.setPreferredSize(new java.awt.Dimension(960, 540));
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable1MousePressed(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jTable1);
        jTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(1).setMinWidth(10);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(10);
            jTable1.getColumnModel().getColumn(2).setMinWidth(50);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(3).setMinWidth(40);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(4).setMinWidth(40);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(5).setMinWidth(15);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(15);
            jTable1.getColumnModel().getColumn(6).setMinWidth(40);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(7).setMinWidth(40);
            jTable1.getColumnModel().getColumn(7).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(8).setMinWidth(40);
            jTable1.getColumnModel().getColumn(8).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(9).setMinWidth(20);
            jTable1.getColumnModel().getColumn(9).setPreferredWidth(20);
            jTable1.getColumnModel().getColumn(10).setMinWidth(40);
            jTable1.getColumnModel().getColumn(10).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(11).setMinWidth(40);
            jTable1.getColumnModel().getColumn(11).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(12).setMinWidth(40);
            jTable1.getColumnModel().getColumn(12).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(13).setMinWidth(40);
            jTable1.getColumnModel().getColumn(13).setPreferredWidth(40);
            jTable1.getColumnModel().getColumn(14).setMinWidth(250);
            jTable1.getColumnModel().getColumn(14).setPreferredWidth(250);
        }

        jSplitPane1.setLeftComponent(jScrollPane3);

        fileMenu.setText("File");

        Open.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        Open.setText("Open File...");
        Open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenActionPerformed(evt);
            }
        });
        fileMenu.add(Open);

        Save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        Save.setText("Save As...");
        Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveActionPerformed(evt);
            }
        });
        fileMenu.add(Save);

        Import.setText("Import from clipboard");
        Import.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImportActionPerformed(evt);
            }
        });
        fileMenu.add(Import);

        Export.setText("Export to Clipboard");
        Export.setActionCommand("Export to clipboard");
        Export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportActionPerformed(evt);
            }
        });
        fileMenu.add(Export);

        jMenuBar1.add(fileMenu);

        editMenu.setText("Edit");
        jMenuBar1.add(editMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
        java.awt.EventQueue.invokeLater(() -> {
            JaminateBridge.this.mousePressPoint = evt.getPoint();
            if ((evt.getModifiersEx() & evt.BUTTON3_DOWN_MASK) == evt.BUTTON3_DOWN_MASK) {
                JaminateBridge.this.jPopupMenu1.show(JaminateBridge.this.jTable1, evt.getX(), evt.getY());
            } else if ((evt.getModifiersEx() & evt.BUTTON1_DOWN_MASK) == evt.BUTTON1_DOWN_MASK) {
                if (JaminateBridge.this.cellSelectClipboard == null) {
                    JaminateBridge.this.cellSelectClipboard = new ArrayList<>();
                }
                if (!JaminateBridge.this.multiselect) {
                    JaminateBridge.this.cellSelectClipboard.clear();
                }
                JaminateBridge.this.cellSelectClipboard.add(JaminateBridge.this.getRowColItem(JaminateBridge.this.mousePressPoint, jTable1));
            }
        });
    }//GEN-LAST:event_jTable1MousePressed

    private void CopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CopyActionPerformed
        java.awt.EventQueue.invokeLater(() -> {
            copy();
        });
    }//GEN-LAST:event_CopyActionPerformed

    private void CutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CutActionPerformed
        java.awt.EventQueue.invokeLater(() -> {
            copy();
            cut();
        });
    }//GEN-LAST:event_CutActionPerformed

    private void PasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PasteActionPerformed
        java.awt.EventQueue.invokeLater(() -> {
            try {
                paste();
            } catch (NullPointerException nepe) {
                if (JaminateBridge.this.cellClipboard == null) {
                    int reply2 = JOptionPane.showConfirmDialog(JaminateBridge.this,
                            "You need to copy or cut something first!", "Ok", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException iae) {
                System.err.println(iae.getMessage());

                int reply = JOptionPane.showConfirmDialog(JaminateBridge.this,
                        "Can't paste that here, try somewhere else!", "Ok", JOptionPane.ERROR_MESSAGE);
            }
        });
    }//GEN-LAST:event_PasteActionPerformed

    private void CutRowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CutRowsActionPerformed
        java.awt.EventQueue.invokeLater(() -> {
            copyRows();
            cutRows();
        });
    }//GEN-LAST:event_CutRowsActionPerformed

    private void CopyRowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CopyRowsActionPerformed
        java.awt.EventQueue.invokeLater(() -> {
            copyRows();
        });   // Collections.reverse(JaminateBridge.this.clipboard);
    }//GEN-LAST:event_CopyRowsActionPerformed

    private void SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveActionPerformed
        java.awt.EventQueue.invokeLater(() -> {
            jFileChooser1.setCurrentDirectory(currentFolder);
            jFileChooser1.setVisible(true);
            java.util.Iterator<Frame> frames = (java.util.Iterator<Frame>) java.util.Arrays.asList(JaminateBridge.getFrames()).iterator();
            while (frames.hasNext()) {
                frames.next().setTitle("Please select a file or filename to save");
            }
            
            // we can save HTML or JSON.
            jFileChooser1.setAcceptAllFileFilterUsed(false);
            jFileChooser1.removeChoosableFileFilter(jFileChooser1.getFileFilter());
            jFileChooser1.setFileFilter(new FileNameExtensionFilter(
                    "Files ending in .json, .html",
                    "json", 
                    "html"));
            int select = jFileChooser1.showSaveDialog(null);
            if (JFileChooser.APPROVE_OPTION == select) {
                currentFolder = jFileChooser1.getCurrentDirectory();
                File selectedFile = jFileChooser1.getSelectedFile();
                System.out.println("Got here");
                if (selectedFile != null) {
                    TableLoadSave save = new TableLoadSave();
                    System.out.println(selectedFile);
                    //JaminateBridge.this.tableModel = new GenericTableModel((DefaultTableModel) jTable1.getModel());
                    String s = save.saveTableModel(JaminateBridge.this.tableModel, selectedFile);
                    jTable1.repaint();  // refresh
                    jTextArea1.setText(s);
                } else {
                    System.err.println("Did not select a file to open");
                }
            } else {
                System.err.println("Did not approve");
            }
            frames = (java.util.Iterator<Frame>) java.util.Arrays.asList(JaminateBridge.getFrames()).iterator();
            while (frames.hasNext()) {
                frames.next().setTitle("JAminate Application");
            }
        });
    }//GEN-LAST:event_SaveActionPerformed

    private void OpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenActionPerformed
        java.awt.EventQueue.invokeLater(() -> {
            jFileChooser1.setCurrentDirectory(currentFolder);
            jFileChooser1.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jFileChooser1.setAcceptAllFileFilterUsed(false);
            jFileChooser1.removeChoosableFileFilter(jFileChooser1.getFileFilter());
            jFileChooser1.setFileFilter(new FileNameExtensionFilter(
                    "Files ending in .txt (chaining), .html (dopesheet), .js (scene graph)",
                    "txt",
                    "html",
                    "js"));
     
            jFileChooser1.setVisible(true);
            java.util.Iterator<Frame> frames = (java.util.Iterator<Frame>) java.util.Arrays.asList(JaminateBridge.getFrames()).iterator();
            while (frames.hasNext()) {
                frames.next().setTitle("Please select a file to open");
            }
            int select = jFileChooser1.showOpenDialog(null);
            System.out.println("Got here A");
            if (JFileChooser.APPROVE_OPTION == select) {
                currentFolder = jFileChooser1.getCurrentDirectory();
                File selectedFile = jFileChooser1.getSelectedFile();
                // System.out.println("Got here");
                if (selectedFile != null) {
                    TableLoadSave load = new TableLoadSave();
                    System.out.println(selectedFile);
                    JaminateBridge.this.tableModel.getModel().setRowCount(0);  // clear table
                    load.loadTableModel(JaminateBridge.this.tableModel, selectedFile);
		    jScrollPane3.setPreferredSize(new Dimension(JaminateBridge.this.jTable1.getPreferredSize().width,6000));
		    // JaminateBridge.this.tableModel.fireTableRowsInserted(0, JaminateBridge.this.tableModel.getRowCount()-1);
		    /*
                    Integer rowCount = JaminateBridge.this.tableModel.getRowCount();
                    //System.err.println("Number of rows " + rowCount);
                    Integer colCount = JaminateBridge.this.tableModel.getColumnCount();
                    //System.err.println("Number of columns " + colCount);
                    for (int r = 0; r < rowCount; r++) {
                        for (int c = 0; c < colCount; c++) {
                            try {
                                //jTable1.getColumn(c).
                                Object value = JaminateBridge.this.jTable1.getValueAt(r, c);
                                //System.err.println("row " + r + " col " + c + " obj " + value.getClass().getName() + " " + value.toString());
                                TableCellRenderer renderer = JaminateBridge.this.jTable1.getCellRenderer(r, c);
                                JaminateBridge.this.jTable1.getDefaultRenderer(value.getClass());
                                // System.err.println(renderer.getClass());
                                try {
                                    // if JaminateBridge.this fails, we have the wrong type somewhere
                                    Component component1 = renderer.getTableCellRendererComponent(JaminateBridge.this.jTable1, value, false, false, r, c);
                                } catch (IllegalArgumentException | ClassCastException e) {
                                    System.err.println(e.getMessage()+" row "+r+" column "+c);
                                }
                            } catch (NullPointerException npe) {
                                System.err.println("Row " + r + " Column " + c + ": " + npe.getMessage());

                            }
                            //Format   formatter = Format.getInstance();
                            //System.err.println(formatter.format(value));
                        }
                    }
		    */
                    try {
                        //System.err.println("Firing table change");
                        java.awt.EventQueue.invokeLater(() -> {
			    JaminateBridge.this.tableModel.fireTableDataChanged();
        		    jScrollPane3.setViewportView(JaminateBridge.this.jTable1);
			    // JaminateBridge.this.jScrollPane3.repaint();
        		    //JaminateBridge.this.jSplitPane1.setLeftComponent(JaminateBridge.this.jScrollPane3);
            		    JaminateBridge.this.jTable1.repaint();
            		    //JaminateBridge.this.jScrollPane3.repaint();
                        });
                        // JaminateBridge.this.jTable1.repaint();
                    } catch (IllegalArgumentException | ClassCastException e) {
                        System.err.println(e.getMessage());
                    }
                } else {
                    System.err.println("Did not select a file to open");
                }
            } else {
                System.err.println("Did not approve");
            }
            frames = (java.util.Iterator<Frame>) java.util.Arrays.asList(JaminateBridge.getFrames()).iterator();
            while (frames.hasNext()) {
                frames.next().setTitle("JAminate Application");
            }
        });
    }//GEN-LAST:event_OpenActionPerformed

    private void PasteRowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PasteRowsActionPerformed
        //Point tablePoint = SwingUtilities.convertPoint(jPopupMenu1, JaminateBridge.this.mousePressPoint, jTable1);
        java.awt.EventQueue.invokeLater(() -> {
            pasteRows();
        });

    }//GEN-LAST:event_PasteRowsActionPerformed

    private void CopyRowsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CopyRowsKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_CopyRowsKeyPressed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        if (evt.isControlDown()) {
            JaminateBridge.this.multiselect = true;
        }
    }//GEN-LAST:event_jTable1KeyPressed

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
        if (!evt.isControlDown()) {
            JaminateBridge.this.multiselect = false;
        }
    }//GEN-LAST:event_jTable1KeyReleased

    private void ImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImportActionPerformed
        java.awt.EventQueue.invokeLater(() -> {
            try {
                String html = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.allHtmlFlavor); 
                TableLoadSave load = new TableLoadSave();
                html = /*"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+*/html.substring(html.indexOf("<"));
                jTextArea1.setText(html);
                load.loadHTMLSource(JaminateBridge.this.tableModel, new InputSource(new StringReader(html)));
            } catch (IOException | UnsupportedFlavorException e) {
                e.printStackTrace(System.err);
            }
        });
    }//GEN-LAST:event_ImportActionPerformed

    private void ExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportActionPerformed
        java.awt.EventQueue.invokeLater(() -> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            TableLoadSave save = new TableLoadSave();
            String sb = save.saveTableToHTMLString(JaminateBridge.this.tableModel);
            clipboard.setContents(new HtmlSelection(sb), null);
        });
    }//GEN-LAST:event_ExportActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    //break;
                }
                System.err.println(info.getClassName());
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace(System.err);
        }
        java.awt.EventQueue.invokeLater(() -> {
            JaminateBridge bridge = new JaminateBridge();
            bridge.setVisible(true);
        });
    }
    private File currentFolder = new File("src/main/javascript/");
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Copy;
    private javax.swing.JMenuItem CopyRows;
    private javax.swing.JMenuItem Cut;
    private javax.swing.JMenuItem CutRows;
    private javax.swing.JMenuItem Export;
    private javax.swing.JMenuItem Import;
    private javax.swing.JMenuItem Open;
    private javax.swing.JMenuItem Paste;
    private javax.swing.JMenuItem PasteRows;
    private javax.swing.JMenuItem Save;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

}
