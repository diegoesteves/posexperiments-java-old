#!/bin/bash
# read PTB pos file(s) as args or from stdin
# write conll-like pos
echo echo $0 $*'
synopsis: '$0' [FILE1..n] -nocomments
	-nocomments	remove all comments and metadata
reads files from arguments or stdin and produces CONLL-like POS annotations from PTB-like POS annotations' 1>&2;
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
	s/^\*/#\*/g;
	s/^==/#==/g;
	s/\[/\n<nc>\n/g;
	s/\]/\n<\/nc>\n/g;
	s/([^ <][^ <]*)\/([^ ]*)/\n\1\t\2\n/g;
	print;
}' | \
egrep -v '^ *$' | \
sed -e s/'^ *#==* *$'//g | \
if echo $COMMENTS | grep 'false' &>/dev/null; then
	perl -e 'while(<>) {
		s/^ *#[^\n]*\n//gs;
		s/#[^\n]*//g;
		s/^ *<[^>]*> *\n//gs;
		print; }';
	else cat;
fi | \
egrep -A 1 '.' | egrep -v '^\-\-$'
