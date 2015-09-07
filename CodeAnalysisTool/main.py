#!/usr/bin/env python

import sys
import os
import zipfile
import shutil

def main():
    if len(sys.argv) < 2:
        print("Must provide a directory containing student tests")
        sys.exit(1)

    tests_dir = sys.argv[1]

    if not os.path.exists(tests_dir):
        print(tests_dir + " is not a valid directory")
        sys.exit(1)

    previous_dir = os.getcwd()

    os.chdir(tests_dir)

    for f in os.listdir(os.getcwd()):
        if f.endswith(".zip"):
            collectData(f)

    

def collectData(filename):
    with zipfile.ZipFile(filename, "r") as z:
            z.extractall(os.getcwd() + "/" + str(filename)[:-4])
    
    
    

if __name__ == '__main__':
    main()
