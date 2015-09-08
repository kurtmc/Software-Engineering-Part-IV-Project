set terminal pngcairo  transparent enhanced font "arial,10" fontscale 1.0 size 500,    350
set output 'test_length.png'
set boxwidth 0.2 absolute
set title "Length of time taken to complete test" 
set xrange[0:2]
set yrange[0:45]

set ylabel "Time taken in minutes"

# Data columns: X Min 1stQuartile Median 3rdQuartile Max BoxWidth Titles

# set bars 4.0
set style fill empty
plot 'test_length.data' using 1:3:2:6:5:7:xticlabels(8) with candlesticks notitle whiskerbars, \
  ''         using 1:4:4:4:4:7 with candlesticks lt -1 notitle
