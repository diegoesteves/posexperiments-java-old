word-level alignment of SUSANNE and PTB POS annotations
=======================================================

conll-wordalign-tsv.sh
	extract columns with word forms, apply diff and use diff information for alignment
	synopsis: ./conll-wordalign-tsv.sh FILE1.tsv FILE2.tsv [COL1] [COL2]
		FILEi.tsv	tab-separated text files, e.g. CoNLL format
		COLi		column number to be used for the alignment, 
					defaults to 0 (first)
	./conll-wordalign-tsv.sh extracts the contents of the specified column, runs diff 
	and integrate the content of FILE1 and FILE2 on that basis
	(similar to sdiff)

ptb2conll.sh FILE.pos [-nocomments]
	convert PTB POS to CoNLL/TreeTagger-like tsv format, first column are word forms
	with -nocomments set, chunks and comments are removed (improves alignment)
	necessary to process ptb pos
	
susa2conll.sh FILE.pos [-nocomments]
	convert SUSANNE tsv format to CoNLL/TreeTagger-like tsv format with word, pos, lemma only,  first column are word forms
	with -nocomments set, comments and structure information are removed
	(optional)
	
Diff2CoNLLAlign.java
	auxiliar routine to conll-wordalign-tsv.sh

example
=======

1. create TSV for PTB POS
$> ./ptb2conll.sh data/ca01.pos.ptb -nocomments > data/ca01.pos.ptb.conll

2. optional: convert SUSANNE TSV 
$> ./susa2conll.sh data/A01.susa -nocomments > data/A01.susa.conll

3. alignment
EITHER
$> ./conll-wordalign-tsv.sh data/ca01.pos.ptb.conll data/A01.susa.conll
OR
$> ./conll-wordalign-tsv.sh data/ca01.pos.ptb.conll data/A01.susa 0 3


Christian Chiarcos
christian.chiarcos@web.de
Goethe-University Frankfurt
Frankfurt, Dec 10th, 2014
http://acoli.cs.uni-frankfurt.de
