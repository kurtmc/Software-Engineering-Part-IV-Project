NAME = poster
SUBMIT = Poster_11

all:
	pdflatex $(NAME).tex
	bibtex $(NAME).aux
	pdflatex $(NAME).tex
	pdflatex $(NAME).tex
	mv $(NAME).pdf $(SUBMIT).pdf
