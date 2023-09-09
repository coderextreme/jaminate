package net.coderextreme.jaminate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author john
 */
public class Motion extends GenericModel implements Serializable {
    private Boolean checked = false;
    private String character = "Character";
    private Double timeStart = 1.0;
    private String motion = "Motion";
    private Integer submove = 1;
    private Double x = 0.0;
    private Double y = 0.0;
    private Double z = 0.0;
    private Double cycleInterval = 1.0;
    private Double xAxis = 0.0;
    private Double yAxis = 0.0;
    private Double zAxis = 0.0;
    private Double degrees = 0.0;
    private String uri = "https://www.web3d.org/Toddler.Motion.txt";
    private static final String CHECKED = "checked";
    private static final String CHARACTER = "character";
    private static final String START = "timeStart";
    private static final String MOTION = "motion";
    private static final String SUBMOTION = "submove";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String Z = "z";
    private static final String INTERVAL = "cycleInterval";
    private static final String X_AXIS = "xAxis";
    private static final String Y_AXIS = "yAxis";
    private static final String Z_AXIS = "zAxis";
    private static final String DEGREES = "degrees";
    private static final String URI = "uri";
    
    private final Column[] fields = new Column[] {
        new Column(CHECKED, "Check all", Boolean.class),
        new Column(CHARACTER, "Character", String.class),
        new Column(START, "Time start [0, 1]", Double.class),
        new Column(MOTION, "Motion", String.class),
        new Column(SUBMOTION, "Submotion", Integer.class),
        new Column(X, "X", Double.class),
        new Column(Y, "Y", Double.class),
        new Column(Z, "Z", Double.class),
        new Column(INTERVAL, "Cycle interval", Double.class),
        new Column(X_AXIS, "X axis", Double.class),
        new Column(Y_AXIS, "Y axis", Double.class),
        new Column(Z_AXIS, "Z axis", Double.class),
        new Column(DEGREES, "Degrees", Double.class),
        new Column(URI, "URL", String.class)
    };
    private final HashMap<String, Integer> htmlToColumn = new HashMap<>(14);
    private static final Integer[] htmlColumnToColumn = new Integer[14];
    {{
            htmlToColumn.put("Check all", 0);
            htmlToColumn.put("Character", 1);
            htmlToColumn.put("Time", 2);
            htmlToColumn.put("Time start [0,1]", 2);
            htmlToColumn.put("Primary move", 3);
            htmlToColumn.put("Motion", 3);
            htmlToColumn.put("Sub move", 4);
            htmlToColumn.put("Submotion", 4);
            htmlToColumn.put("X", 5);
            htmlToColumn.put("Y", 6);
            htmlToColumn.put("Z", 7);
            htmlToColumn.put("Cycle interval", 8);
            htmlToColumn.put("X axis", 9);
            htmlToColumn.put("Y axis", 10);
            htmlToColumn.put("Z axis", 11);
            htmlToColumn.put("Degrees", 12);
            htmlToColumn.put("URL", 13);
    }}
    public Motion() {
    }

    public Motion(GenericTableModel tableModel, int sourceRow) {
       Iterator<Object> i =  tableModel.getModel().getDataVector().get(sourceRow).iterator();
       int col = 0;
       while (i.hasNext()) {
           Object o =  i.next();
           this.setField(col, o.toString());
           col++;
       }
    }


    /**
     * @return the checked
     */
    public Boolean getChecked() {
        return checked;
    }

    /**
     * @param checked the checked to set
     */
    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    /**
     * @return the character
     */
    public String getCharacter() {
        return character;
    }

    /**
     * @param character the character to set
     */
    public void setCharacter(String character) {
        this.character = character;
        if (this.character.equals("Gramps")) {
            setUri(getUri().replace("Toddler", "Adult"));
        }
  
    }

    /**
     * @return the timeStart
     */
    public Double getTimeStart() {
        return timeStart;
    }

    /**
     * @param timeStart the timeStart to set
     */
    public void setTimeStart(Double timeStart) {
        this.timeStart = timeStart;
    }

    /**
     * @return the motion
     */
    public String getMotion() {
        return motion;
    }

    /**
     * @param motion the motion to set
     */
    public void setMotion(String motion) {
        this.setUri(this.getUri());
        this.motion = motion;
    }

    /**
     * @return the x
     */
    public Double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(Double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public Double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(Double y) {
        this.y = y;
    }

    /**
     * @return the z
     */
    public Double getZ() {
        return z;
    }

    /**
     * @param z the z to set
     */
    public void setZ(Double z) {
        this.z = z;
    }

    /**
     * @return the cycleInterval
     */
    public Double getCycleInterval() {
        return cycleInterval;
    }

    /**
     * @param cycleInterval the cycleInterval to set
     */
    public void setCycleInterval(Double cycleInterval) {
        this.cycleInterval = cycleInterval;
    }

    /**
     * @return the xAxis
     */
    public Double getxAxis() {
        return xAxis;
    }

    /**
     * @param xAxis the xAxis to set
     */
    public void setxAxis(Double xAxis) {
        this.xAxis = xAxis;
    }

    /**
     * @return the yAxis
     */
    public Double getyAxis() {
        return yAxis;
    }

    /**
     * @param yAxis the yAxis to set
     */
    public void setyAxis(Double yAxis) {
        this.yAxis = yAxis;
    }

    /**
     * @return the zAxis
     */
    public Double getzAxis() {
        return zAxis;
    }

    /**
     * @param zAxis the zAxis to set
     */
    public void setzAxis(Double zAxis) {
        this.zAxis = zAxis;
    }

    /**
     * @return the degrees
     */
    public Double getDegrees() {
        return degrees;
    }

    /**
     * @param degrees the degrees to set
     */
    public void setDegrees(Double degrees) {
        this.degrees = degrees;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        this.uri = this.uri.replaceAll("Motion", this.motion);
        if (getCharacter().equals("Gramps")) {
            this.uri = this.uri.replace("Toddler", "Adult");
        }
        return this.uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri.replaceAll("Motion", this.motion);
    }
        /**
     * @return the submove
     */
    public Integer getSubmove() {
        return submove;
    }

    /**
     * @param submove the submove to set
     */
    public void setSubmove(Integer submove) {
        this.submove = submove;
    }

    /**
     * @return the fields
     */
    @Override
    public Column[] getFields() {
        return this.fields;
    }
    
    @Override
    public Object getField(Integer col, String setToValue)  {
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
            System.err.println("col is null, value is "+setToValue);
            return setToValue;
        }
    }
    private Column getColumn(Integer localColumn) {
        Column [] column = this.getFields();
        Column col = column[localColumn];
        return col;
    }
    @Override
    public void setField(Integer HTMLColumn, TableLoadSave.rowMode mode, String setToValue) throws IllegalAccessException, NoSuchFieldException {
        if (mode == TableLoadSave.rowMode.HEADER) {
            Integer localColumn = htmlToColumn.get(setToValue);
            Motion.htmlColumnToColumn[HTMLColumn] = localColumn;
        } else if (mode == TableLoadSave.rowMode.DATA) {
            Integer localColumn = Motion.htmlColumnToColumn[HTMLColumn];
            setField(localColumn, setToValue);

        }
    }
    @Override
    public List getList(TableLoadSave.rowMode mode) {
       List row = new ArrayList();
       if (mode == TableLoadSave.rowMode.DATA) {
            for (Integer column = 0; column < this.getFields().length; column++) {
                try {
                    Column c = this.getColumn(column);
                    Field myField = Motion.class.getDeclaredField(c.fieldName);
                    myField.setAccessible(true);
                    Object o = myField.get(this);
                    if (o != null) {
                        System.err.println("column "+c.fieldName+" classname "+o.getClass().getName()+" "+o.toString());
                        if (c.fieldName.equals(URI)) { // URI is a template, replace "Motion" and "Toddler"
                            o = this.getUri();
                        }
                    }
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

    @Override
    public Integer getLocalColumnFromHTMLHeader(String header) {
        return htmlToColumn.get(header);
    }

    @Override
    public void putLocalColumnInHTMLColumn(Integer HTMLColumn, Integer localColumn) {
        Motion.htmlColumnToColumn[HTMLColumn] = localColumn;
    }

    @Override
    public Integer getLocalColumnFromHTMLColumn(Integer HTMLColumn) {
        return  Motion.htmlColumnToColumn[HTMLColumn];
    }
}
