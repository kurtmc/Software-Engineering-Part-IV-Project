set terminal pngcairo  transparent enhanced font "arial,10" fontscale 1.0 size 500,    350
set output 'method_time.png'
set boxwidth 0.2 absolute
set title "Time spend implementing methods" 
set xlabel "Time in seconds"
set ylabel "Method names"
set xrange[0:9]
set yrange[0:500]

set xtics rotate by 45 right

# Data columns: X Min 1stQuartile Median 3rdQuartile Max BoxWidth Titles

# set bars 4.0
set style fill empty

plot 'method_time.data' using 1:3:2:6:5:7:xticlabels(8) with candlesticks notitle whiskerbars, \
  ''         using 1:4:4:4:4:7 with candlesticks lt -1 notitle
