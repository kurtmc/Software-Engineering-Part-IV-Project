FORMAT = png
all:
	for f in *.dot; do filename="$${f%.*}"; dot -T$(FORMAT) $$f -o $${filename}.$(FORMAT); done
clean:
	rm *.png
