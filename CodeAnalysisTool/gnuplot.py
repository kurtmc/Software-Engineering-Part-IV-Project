import utils
import os

class gnuplot_script:
    def __init__(self, output_file, script = None):
        self.output_file = output_file
        self.script = script

    def plot_box_and_whiskers(self, title, xlabel, ylabel, data_file, xrange, yrange, xtic_rotation=0):
        plot_script = "set terminal pngcairo transparent enhanced font \"arial,10\" fontscale 1.0 size 500, 350\n"
        plot_script += "set output '" + self.output_file + "'\n"
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

    def render(self):
        filename = "temp_" + self.output_file + ".gnuplot"
        f = open(filename, "w")
        f.write(self.script)
        f.close()

        utils.run_command(["gnuplot", filename])
        os.remove(filename)
