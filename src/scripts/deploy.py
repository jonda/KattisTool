import os, shutil
import zipfile

print("deploy.py")
def zipdir(path, ziph):
    #is zipfile handle
    for root, dirs, files in os.walk(path):
        for file in files:
            ziph.write(os.path.join(root, file))

googledrive="C:/googledrive/"
sketchbook = googledrive+"/procsketchbook/"
#sketchbook = "G:/Mina dokument/Processing/"
basetool=sketchbook+"tools/KattisTool/";
dest = basetool+"tool/"
print("dest:", dest)
shutil.copy("dist/KattisTool.jar", dest)
os.chdir(basetool+"/..")
print(os.getcwd())

zipf = zipfile.ZipFile('KattisTool.zip', 'w', zipfile.ZIP_DEFLATED)
zipdir("KattisTool", zipf)
zipf.close()