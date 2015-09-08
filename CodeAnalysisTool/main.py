#!/usr/bin/env python

import sys
import os
import zipfile
import shutil
import subprocess
import re
import numpy
import pprint

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

    os.chdir(previous_dir)

    data = list()

    data.append(process_results(results, "test_length"))

    test_length_data = open("test_length.data", "w")
    test_length_data.write(format_gnuplot(data, ["test_length"]))
    test_length_data.close()

    data = list()

    method_names = ["factorial", "fibonacci", "min", "max", "reverse", "isPalindrome", "shuffle", "rotate"]
    for method in method_names:
        data.append(process_results(results, method))

    method_time_data = open("method_time.data", "w")
    method_time_data.write(format_gnuplot(data, method_names))
    method_time_data.close()


def run_command(cmd_list):
    return subprocess.Popen(cmd_list, stdout=subprocess.PIPE).communicate()[0].decode("utf-8")

def collectData(filename, code_report_tool_path):

    dir_name = str(filename)[:-4]

    with zipfile.ZipFile(filename, "r") as z:
            z.extractall(os.getcwd() + "/" + dir_name)

    repo_dir = dir_name + "/" + os.listdir(dir_name)[0]

    output = run_command(["java", "-jar", code_report_tool_path, repo_dir, "-sourceFile", "Main.java"])

    shutil.rmtree(dir_name)

    values = dict()
    values["name"] = dir_name

    for item in output.split("\n"):
        if item.startswith("The test lasted"):
            values["test_length"] = int([token for token in item.split() if token.isdigit()][0])
        if item.startswith("Characters per minute"):
            values["char_per_minute"] = [token for token in item.split() if token.isdigit()][0]
        if item.startswith("\t"):
            s = re.findall(r"[\w']+", item)
            values[s[0]] = int(s[1][:-1])
            


    return values
    
# Returns a list of [min, 1st quartile, median, 3rd quartile, max]
def process_results(results, attribute):
    time_to_complete = list()

    for student in results:
        if attribute in student.keys():
            time_to_complete.append(student[attribute])
        else:
            time_to_complete.append(0)

    min_value = min(time_to_complete)
    max_value = max(time_to_complete)
    median = numpy.median(numpy.array(time_to_complete))
    fst_q = numpy.percentile(numpy.array(time_to_complete), 25)
    trd_q = numpy.percentile(numpy.array(time_to_complete), 75)

    return [min_value, fst_q, median, trd_q, max_value]

def format_gnuplot(data, names):
    gnuplot = "# Data columns: X Min 1stQuartile Median 3rdQuartile Max BoxWidth Titles\n"
    for i in range(len(data)):
        gnuplot += str(i + 1) + " "
        for value in data[i]:
            gnuplot += str(value) + " "
        gnuplot += "0.3"
        gnuplot += " " + names[i] + "\n"

    return gnuplot
        

    

if __name__ == '__main__':
    main()
