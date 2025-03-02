# -*- coding: utf-8 -*-
import os;
import datetime
googledrive="C:\\googledrive\\"
os.chdir(googledrive+"java\\KattisTool\\src\\kattistool")
print("precompile: aktuell mapp: "+os.getcwd())
now = datetime.datetime.now()

date = now.strftime("%Y-%m-%d")
time = now.strftime("%H:%M")
datetime = now.strftime("%y%m%d-%H%M")
print("date: "+date)
print("time: "+time)
with open('BuildTime.java', 'w') as gris:
    print("fil öppnad")
    gris.write("package kattistool;\n");
    gris.write("public class BuildTime{\n");
    gris.write('    public static final String date="'+date+'";\n');
    gris.write('    public static final String time="'+time+'";\n');
    gris.write("}\n");
os.chdir(googledrive+"java\\KattisTool\\src\\scripts")
with open('buildtime.txt', 'w') as gris:
   print("fil buildtime.txt öppnad")
   gris.write(datetime);
    