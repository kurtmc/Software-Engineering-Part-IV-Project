Dependencies
============
For Fedora 22 these are the following package dependencies:
- java-1.8.0-openjdk
- git
- python3-numpy
- python3-tkinter
- python3-pillow
- python3-pillow-tk
- gnuplot
- gradle

For the lazy:
```
sudo dnf install java-1.8.0-openjdk git python3-numpy python3-tkinter python3-pillow python3-pillow-tk gnuplot gradle
```


Building
==========
Before you can build this repo you need to initialise the submodules:
```bash
git submodule update --init --recursive
```

Then just run `make` to compile CodeReportTool

Run
===
```
python3 main.py
```

You should point this program to the directory that contains zipped test
submissions. Example: https://github.com/kurtmc/CompletedTests
