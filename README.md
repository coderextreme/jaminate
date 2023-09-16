# jaminate.  A high Level Animation Motion Chaining Tool for X3D VRML (Like Excel, but worse)
=======================================================
Capabilities:
Copy/Paste HTML between apps through Import/Export in File menu (copy or paste spreadsheet or table).
Note that you must copy from LibreOffice each time you want to import into Jaminate.
Open/Load Proprietary format (txt..don't load takes\*.txt) and HTML.
Open/Load/Save in HTML.
Save in JSON.

===========================================================

```txt
For JaminateBridge (Java Program), 
Load files as .html or proprietary .txt file (see John.txt)
Load propriety textual format such as John.txt.
Save files as .html or .json (for input into takes.js, but the Java runs takes.js)

Import/Export from Spreadsheet (LibreOffice works, want to try Excel). Uses HTML in clipboard.
```

You'll need a JDK, OpenJDK, or GraalVM.  I use GraalVM.
You'll need git to download the code.  I use Git for Windows.
You'll need gradle to build and run the code.  I use version 8.2.1.
You'll need nodejs to run takes.js code.  I use version 20.6.1
```bash
$ git clone https://github.com/coderextreme/jaminate
$ cd jaminate/Jaminate/
$ gradle
$ gradle clean
$ gradle build 
$ gradle install
$ gradle run
```
Then load the file John.txt or John.html, for example.

When you save as .json, the chained animation code will show up in the GUI text area

File ... Import/Export to cut/copy/paste clipboard

The context menu doesn't work yet!

Have fun Jaminating!

=======================================================

See below for information on taking a JSON file and
generating VRML Event chaining code from it.  This is also done by the java GUI now.

In folder: ./Jaminate/app/src/main/javascript:

```txt
John.json -- Provide JSON data here (Now generated from Java tool)
John.txt -- Input file in proprietary format (open/load into Java tool)
takes.John.timers.txt -- Sample time sensors, generated
takes.John.txt -- generated event chaining code, needs uncommented timers
takes.js -- Node.js code that takes John.json 
    and produces takes.John.timer.txt and takes.John.txt.
    Or other characters specified in John.json.
```

The below is now integrated into the Java GUI.

```bash
$ node takes.js < your_json_file.json
```
```txt
And pick up VRML in files

takes.\*.timers.txt
takes.\*.txt

And on standard output.

where \* becomes one of your characters in your *.json file
```
