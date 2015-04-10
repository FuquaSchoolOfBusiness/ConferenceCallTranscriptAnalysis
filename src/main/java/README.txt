
Conference Call Transcript Analysis

This application is designed to parse transcripts between companies and analysts
for marketing terms and other user-defined dictionaries.  

A dictionary contains word stems or phrases that are matched in a transcript.  
Each stem or phrase can be placed into one category.  The last match in the 
dictionary is considered the best match as each discovered word can only have 
one category. Therefore, it is critical that the dictionaries are constructed 
such that the best match is listed last for proper prioritization of a matched 
word's category.  A dictionary is a text file with a word or phrase and an 
optional comma with a word describing a category (e.g., "data,marketing") on 
one line.  A stem with no category defined will have a category of "NONE".

A transcript has two primary divisions based on the phrase
"QUESTION AND ANSWER SUMMARY" appearing in the transcript.  The "PRE" section
consists of any words found before the Q and A.  The "POST" section consists 
of any words found after the Q and A phrase.  The "POST" section is divided 
into three categories: OPERATOR, POST-COMPANY and POST-ANALYST.  Each sentence
is categorized base on the category of the last speaker.  The OPERATOR 
introduces the ANALYST who is presumed to be the next speaker.  The identity
of the speaker may contain the word ANALYST.

The words are counted for each section.  Two dictionaries are configured and
matched for words or phrases.  There may be a set of exclusion words for each
dictionary which will be excluded in the word counts (but not the phrase counts).
Words between the Matches of different dictionary words are counted and if the
distance is below a user-specified maximum are counted.  The counts between
dictionary words will always be categorized based on the second dictionary-- so
the last dictionary should be the dictionary with all the categorizations. 

