#!/bin/bash
# read susanne file(s) as args or from stdin
# write conll-like pos
echo echo $0 $*'
synopsis: '$0' [FILE1..n] -nocomments
	-nocomments	remove all comments and metadata
reads files from arguments or stdin and produces CONLL-like POS annotations from Susanne-like POS annotations' 1>&2;
COMMENTS=true;
if echo $* | egrep -i '\-nocomment' &>/dev/null; then
	COMMENTS=false;
fi;
if echo $* | egrep '.' &> /dev/null; then
	if [ -e $1 ]; then
		for file in $*; do cat $file 2>/dev/null; done 
	else if [ -e $2 ]; then
		for file in $*; do cat $file 2>/dev/null; done 
		else cat;
		fi;
	fi;
else cat; fi | \
perl -e 'while(<>) {
	#s/^([^\t]*)\t([^\t]*)\t([^\t]*)\t([^\t]*)\t([^\t]*)\t([^\t]*)$/1:\1\t2:\2\t3:\3\t4:\4\t5:\t\5\n/g;
	s/^([^\t]*)\t([^\t]*)\t([^\t]*)\t([^\t]*)\t([^\t]*)\t([^\t]*)$/\4\t\3\t\5\n/g;
	s/^\+/#nospace\n/g;
	s/<hyphen>/-/g;
	s/<mdash>/-/g;
	s/<apos>/'"'"'/g;
	s/<ldquo>/``/g;
	s/<rdquo>/'"''"'/g;
	s/<dollar>/\$/g;
	s/^<docbrk>[^\n]*$/\n\n#docbrk\n\n/g;
	s/^<bmajhd>[^\n]*$/\n\n<h1>/g;
	s/^<emajhd>[^\n]*$/<\/h1>\n\n/g;
	s/^<bminhd>[^\n]*$/\n\n<h2>/g;
	s/^<eminhd>[^\n]*$/<\/h2>\n\n/g;
	s/^<minbrk>[^\n]*$/\n/g;
	print;
}' | egrep -A 1 '^.' | grep -v '^\-\-$' |\
if echo $COMMENTS | grep 'false' &>/dev/null; then
	perl -e 'while(<>) {
		s/^ *#[^\n]*\n//gs;
		s/#[^\n]*//g;
		s/^ *<[^>]*> *\n//gs;
		print; }';
	else cat;
fi | \
egrep -A 1 '.' | egrep -v '^\-\-$'							      
