file_no_ext=report
output_pdf=2015_Final_Report_Proj011_kmca733

all:
	pdflatex $(file_no_ext).tex
	bibtex $(file_no_ext).aux
	pdflatex $(file_no_ext).tex
	pdflatex $(file_no_ext).tex
	mv $(file_no_ext).pdf $(output_pdf).pdf

clean:
	rm ./*.pdf

spellcheck:
	aspell --lang=en_GB --mode=tex check $(file_no_ext).tex
