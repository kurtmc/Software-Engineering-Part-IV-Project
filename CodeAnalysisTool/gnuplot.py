import utils
import os

class gnuplot_script:
    def __init__(self, output_file, output_type = "png", script = None):
        self.output_file = output_file
        self.script = script
        self.output_type = output_type
        self.data_files = list()

    def plot_box_and_whiskers(self, title, xlabel, ylabel, data, xrange, yrange, xtic_rotation=0):
        data_file = self.data_to_file(data)

        plot_script = ""
        if self.output_type == "svg":
            plot_script += "set terminal svg enhanced font \"arial,10\" size 500, 350"
        else: # defaults to png
            plot_script += "set terminal pngcairo enhanced background rgb 'white' font \"arial,10\" fontscale 1.0 size 500, 350\n"

        plot_script += "set output '" + self.output_file + "." + self.output_type + "'\n"
        plot_script += "set boxwidth 0.2 absolute\n"
        plot_script += "set title \"" + title + "\"\n"
        plot_script += "set xrange[" + str(xrange[0]) + ":" + str(xrange[1]) + "]\n"
        plot_script += "set yrange[" + str(yrange[0]) + ":" + str(yrange[1]) + "]\n"

        if xtic_rotation != 0:
            plot_script += "set xtics rotate by " + str(xtic_rotation) + " right\n"

        if xlabel is not None:
            plot_script += "set xlabel \"" + xlabel + "\"\n"
        if ylabel is not None:
            plot_script += "set ylabel \"" + ylabel + "\"\n"

        plot_script += "set style fill empty\n"
        plot_script += "plot '" + data_file + "' using 1:3:2:6:5:7:xticlabels(8) with candlesticks notitle whiskerbars, '' using 1:4:4:4:4:7 with candlesticks lt -1 notitle"
        self.script = plot_script

    def add_student_line(self, data, student_name):
        data_file = self.data_to_file(data)
        self.script += ", \\\n"
        self.script += "'" + data_file + "' using 1:2 w line title \"" + student_name + "\""

    def render(self):
        filename = "temp_" + self.output_file + ".gnuplot"
        f = open(filename, "w")
        f.write(self.script)
        f.close()

        utils.run_command(["gnuplot", filename])
        os.remove(filename)

    def cleanup_data_files(self):
        for f in self.data_files:
            os.remove(f)

    def data_to_file(self, data):
        filename = str(id(data)) + ".data"
        contents = ""
        for value in data:
            value_as_string = [str(i) for i in value]
            contents += " ".join(value_as_string) + "\n"

        data_file = open(filename, "w")
        data_file.write(contents)
        data_file.close()

        self.data_files.append(filename)

        return filename

