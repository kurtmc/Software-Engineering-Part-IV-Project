file_no_ext=report
output_pdf=FinalReportGroup011_kmca733

all:
	cd figures/ast && $(MAKE)
	pdflatex $(file_no_ext).tex
	bibtex $(file_no_ext).aux
	pdflatex $(file_no_ext).tex
	pdflatex $(file_no_ext).tex
	mv $(file_no_ext).pdf $(output_pdf).pdf

clean:
	rm ./*.pdf

spellcheck:
	aspell --personal=./.aspell.en.pws --lang=en_GB --mode=tex check $(file_no_ext).tex
