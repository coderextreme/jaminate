#!/bin/bash
# README:
# run gradle to build the grammar
# antlr4.exe -vistor src/main/antlr4/CPPONGrammar.g4
# java -Xss1g -Xmx8g -cp "C:/Users/john/Downloads/antlr-4.13.1-complete.jar;src/main/java" org.antlr.v4.Tool -Dlanguage=Java -visitor src/main/antlr4/CPPONGrammar.g4 -package net.coderextreme.cppon -o src/main/java/net/coderextreme/cppon
gradle clean build install run
java -Xss1g -Xmx8g -cp "build/install/app/lib/antlr4-runtime-4.13.1.jar;src/main/java;build/classes/java/main" net.coderextreme.cppon.CPPONGrammarDOMVisitor src/main/cpp/*.h
