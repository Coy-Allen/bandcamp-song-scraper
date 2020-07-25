this is a bandcamp song extractor that uses python 3.5+ to run.
how to run the main.py file:
https://pythoncentral.io/execute-python-script-file-shell/
python download:
https://www.python.org/downloads/


how to use:
enter one track/album/artist url per line. enter an empty line to start the download.


urls can:
start with http or https (http:// or https://)
have one of three styles (music.foo.com or foo.com or foo.bandcamp.com)
after the main site it can have ether (/track or /album or /music or nothing at all)
if it is /track or /album it needs to have the name of the track/album after it
the url can end with or without a slash

tl;dr: copy and paste the url


url styles that work:
http://music.foo.com/track/bar
https://foo.com/
https://foo.bandcamp.com/music
http://music.foo.com
http://music.foo.com/album/bar/


notes:
	the script does NOT like redirects. if foo.com redirects you to music.foo.com; use music.foo.com
	the script treats http://music.foo.com and http://music.foo.com/ as http://music.foo.com/music so if the first two are pointing to a track or album the script will crash.
	this ONLY works on songs that you can listen to on the preview under the album or song name.(AKA: the big play button) if it does not have a preview it will (most likely) crash.
	also it just downloads the preview

when i feel like it i will:
	un-obfuscated my code
	clean up my code
	comment my code for once in my life
	make a way to handel broken links and songs with no preview