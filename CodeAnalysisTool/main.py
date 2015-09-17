#!/usr/bin/env python

import sys
import os
import zipfile
import shutil
import subprocess
import re
import numpy
import pprint
import json
import gnuplot
import utils
import glob
import tkinter
from tkinter import filedialog
from PIL import Image, ImageTk
import time

class Application(tkinter.Frame):
    def __init__(self, master, directory):
        tkinter.Frame.__init__(self, master)
        self.directory = directory
        self.pack()
        self.createWidgets()

    def createWidgets(self):
        self.run = tkinter.Button(self)
        self.run["text"] = "Run Analysis"
        self.run["command"] = self.run_analysis
        self.run.pack(side="top")

        self.path_label = tkinter.Label(self)
        self.path_label["text"] = "Path: " + str(self.directory)
        self.path_label.pack(side="top")

        self.choose_path = tkinter.Button(self)
        self.choose_path["text"] = "Choose path"
        self.choose_path["command"] = self.load_file
        self.choose_path.pack(side="top")

        self.student_name_label = tkinter.Label(self, text = "Student name:")
        self.student_name_label.pack(side="top")

        self.student_name = tkinter.Entry(self)
        self.student_name.pack(side="top")

        self.image = None
        self.image_canvas = tkinter.Canvas(self, width=500, height=500)
        self.image_canvas.pack(side="top")


    def load_file(self):
        self.directory = filedialog.askdirectory()
        self.path_label["text"] = "Path: " + str(self.directory)

    def run_analysis(self):
        student_name = self.student_name.get()
        if not student_name:
            main(self.directory)
        else:
            main(self.directory, student_name)
        im = Image.open("graphs/method_time.png")
        self.image = ImageTk.PhotoImage(im)
        self.image_canvas.create_image(250, 250, image=self.image)
        #self.image_canvas.pack(side="top")


def main(directory, student_name=None):
    tests_dir = directory

    if not os.path.exists(tests_dir):
        print(tests_dir + " is not a valid directory")
        return

    previous_dir = os.getcwd()

    code_report_tool_path = os.path.abspath("bin/CodeReportTool.jar")

    os.chdir(tests_dir)

    results = get_result_data(os.getcwd(), code_report_tool_path)
    os.chdir(previous_dir)

    data = list()
    method_names = ["factorial", "fibonacci", "min", "max", "reverse", "isPalindrome", "shuffle", "rotate"]
    for method in method_names:
        data.append(process_results(results, method))

    student_result = None
    for v in results:
        if v["name"] == student_name:
            student_result = v
            break


    gnuplots = list()

    plot = gnuplot.gnuplot_script("method_time", "png")
    plot.plot_box_and_whiskers("Time spend implementing methods",
        "Method names",
        "Time in seconds",
        format_gnuplot(data, method_names),
        [0, 9],
        [0, 500],
        45)
    if student_name is not None and student_result is not None:
        plot.add_student_line(format_individual_student(student_result, method_names), student_name)
    gnuplots.append(plot)

    plot = gnuplot.gnuplot_script("test_length", "png")
    plot.plot_box_and_whiskers("Length of time taken to complete test",
            None,
            "Time taken in minutes",
            format_gnuplot([process_results(results, "test_length")], ["test_length"]),
            [0, 2],
            [0, 45])
    gnuplots.append(plot)

    plot = gnuplot.gnuplot_script("char_per_minute", "png")
    plot.plot_box_and_whiskers("Characters per minute",
            None,
            "Characters per minute",
            format_gnuplot([process_results(results, "char_per_minute")], ["char_per_minute"]),
            [0, 2],
            [0, 200])
    gnuplots.append(plot)

    for p in gnuplots:
        p.render()

    for p in gnuplots:
        p.cleanup_data_files()

    #utils.run_command("convert -flatten char_per_minute.png char_per_minute_white.png".split())
    #utils.run_command("convert -flatten method_time.png method_time_white.png".split())
    #utils.run_command("convert -flatten test_length.png test_length_white.png".split())

    utils.run_command(["mkdir", "-p", "graphs"])
    mv_images = ["mv"] + glob.glob("*.png") + ["graphs/"]
    utils.run_command(mv_images)



def get_result_data(path, code_report_tool_path):

    # get stored results
    try:
        with open(".results.cache") as results_file:
            results = json.load(results_file)
    except FileNotFoundError:
        results = list()

    processed_names = list()
    for student in results:
        processed_names.append(student["name"])

    for f in os.listdir(path):
        if f.endswith(".zip") and f[:-4] not in processed_names:
            results.append(collectData(f, code_report_tool_path))

    # store new results
    with open(".results.cache", "w") as results_file:
        json.dump(results, results_file)

    return results


def collectData(filename, code_report_tool_path):

    dir_name = str(filename)[:-4]

    with zipfile.ZipFile(filename, "r") as z:
            z.extractall(os.getcwd() + "/" + dir_name)

    repo_dir = dir_name + "/" + os.listdir(dir_name)[0]

    output = utils.run_command(["java", "-jar", code_report_tool_path, repo_dir, "-sourceFile", "Main.java"])

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
            time_to_complete.append(int(student[attribute]))
        else:
            time_to_complete.append(0)

    min_value = min(time_to_complete)
    max_value = max(time_to_complete)
    median = numpy.median(numpy.array(time_to_complete))
    fst_q = numpy.percentile(numpy.array(time_to_complete), 25)
    trd_q = numpy.percentile(numpy.array(time_to_complete), 75)

    return [min_value, fst_q, median, trd_q, max_value]

def format_individual_student(data, names):
    gnuplot = list()
    i = 1
    for value in names:
        row = list()
        row.append(i)
        row.append(data[value])
        i += 1
        gnuplot.append(row)

    return gnuplot


def format_gnuplot(data, names):
    gnuplot = list()
    for i in range(len(names)):
        row = list()
        row.append(i + 1)
        for value in data[i]:
            row.append(value)
        row.append(0.3)
        row.append('"' + names[i].replace("_"," ") + '"')

        gnuplot.append(row)

    return gnuplot
    
if __name__ == '__main__':
    path = None
    if len(sys.argv) > 1:
        path = sys.argv[1]
        main(path)
    root = tkinter.Tk()
    app = Application(root, path)
    app.mainloop()
