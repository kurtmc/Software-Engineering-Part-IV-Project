set terminal svg enhanced font "arial,10" size 500, 350
set output 'method_time.svg'
set boxwidth 0.2 absolute
set title "Time spend implementing methods"
set xrange[0:9]
set yrange[0:500]
set xtics rotate by 45 right
set xlabel "Method names"
set ylabel "Time in seconds"
set style fill empty
plot 'method_time.data' using 1:3:2:6:5:7:xticlabels(8) with candlesticks notitle whiskerbars, \
'' using 1:4:4:4:4:7 with candlesticks lt -1 notitle, \
'method_time_student1.data' using 1:2 w line title "tests"
