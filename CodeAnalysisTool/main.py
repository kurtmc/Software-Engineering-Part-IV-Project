#!/usr/bin/env python

import sys
import os
import zipfile
import shutil
import subprocess
import re

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

    results = list()

    for f in os.listdir(os.getcwd()):
        if f.endswith(".zip"):
            results.append(collectData(f, code_report_tool_path))


    print(results)

    os.chdir(previous_dir)

def run_command(cmd_list):
    return subprocess.Popen(cmd_list, stdout=subprocess.PIPE).communicate()[0].decode("utf-8")

def collectData(filename, code_report_tool_path):

    dir_name = str(filename)[:-4]

    with zipfile.ZipFile(filename, "r") as z:
            z.extractall(os.getcwd() + "/" + dir_name)

    repo_dir = dir_name + "/" + os.listdir(dir_name)[0]

    output = run_command(["java", "-jar", code_report_tool_path, repo_dir])

    shutil.rmtree(dir_name)

    values = dict()
    values["name"] = dir_name

    for item in output.split("\n"):
        if item.startswith("The test lasted"):
            values["test_length"] = [token for token in item.split() if token.isdigit()][0]
        if item.startswith("Characters per minute"):
            values["char_per_minute"] = [token for token in item.split() if token.isdigit()][0]
        if item.startswith("\t"):
            s = re.findall(r"[\w':]+", item)
            values[s[0]] = int(s[1][:-1])
            


    return values
    
    

if __name__ == '__main__':
    main()
