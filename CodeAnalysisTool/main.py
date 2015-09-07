#!/usr/bin/env python

import sys
import os
import zipfile
import shutil
import subprocess

def main():
    if len(sys.argv) < 2:
        print("Must provide a directory containing student tests")
        sys.exit(1)

    tests_dir = sys.argv[1]

    if not os.path.exists(tests_dir):
        print(tests_dir + " is not a valid directory")
        sys.exit(1)

    previous_dir = os.getcwd()

    code_report_tool_path = os.path.abspath("bin/CodeReportTool.jar")

    os.chdir(tests_dir)

    for f in os.listdir(os.getcwd()):
        if f.endswith(".zip"):
            collectData(f, code_report_tool_path)

    os.chdir(previous_dir)

    

def collectData(filename, code_report_tool_path):

    dir_name = str(filename)[:-4]

    with zipfile.ZipFile(filename, "r") as z:
            z.extractall(os.getcwd() + "/" + dir_name)

    repo_dir = dir_name + "/" + os.listdir(dir_name)[0]

    output = subprocess.Popen(["java", "-jar", code_report_tool_path, repo_dir], stdout=subprocess.PIPE).communicate()[0]

    print("Output:")
    print(output)

    shutil.rmtree(dir_name)
    
    

if __name__ == '__main__':
    main()
