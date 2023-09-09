# jaminate.  A high Level Animation Motion Chaining Tool for X3D VRML (Like Excel, but worse)

For JaminateBridge (Java Program)
Load files as .html
Save files as .html
Load propriety format as John.txt

```
$ git clone https://github.com/coderextreme/jaminate
$ cd jaminate/Jaminate/
$ gradle
$ gradle build
$ gradle run
```

Then load the file John.txt, for example.  Save is only to .html now, but you can read .html into the tool.

Have fun Jaminating!

More inforamation on the type of HTML later.  I will create some examples
=======================================================================================
See below for information on taking the proprietary format to generate VRML Event chaining code
In folder: ./Jaminate/app/src/main/javascript

```txt
John.json -- Provide JSON data here.
John.txt -- Input file in proprietary format
takes.John.timers.txt -- Sample time sensors
takes.John.txt -- generated event chaining code
takes.js -- Node.js code that takes John.json 
    and produces takes.John.timer.txt and takes.John.txt.
    Or other characters specified in John.json.
```

Later, I may provide Perl code to produce John.json from John.txt. Not all that information is needed for takes.js anymore, so new code could be written to do the a lesser job. (Global booleans not needed)

so typically, you would run

```
$ node takes.js
```
And pick up VRML in files

takes.*.timers.txt
takes.*.txt

where * becomes one of your characters in John.json
