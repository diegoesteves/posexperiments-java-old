echo "starting..."
sh ptb2conll.sh data/ca01.pos.ptb -nocomments > data/ca01.pos.ptb.conll
echo "step 1, done!"
sh susa2conll.sh $1 -nocomments > data/A01.susa.conll
echo "step 2, done!"
sh conll-wordalign-tsv.sh data/ca01.pos.ptb.conll data/A01.susa.conll
echo "step 3, done!"

