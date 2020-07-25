import urllib.request
import re
import os

#TODO put status output in code
#TODO put code for if there are no songs in X

#copied over
def findDownloadLnk(URL):
	source = urllib.request.urlopen(URL).read().decode('utf-8')
	rawUrl = re.findall('(?<="file":{).*?(?="},)', source)
	if not rawUrl:
		return False
	rawUrl = rawUrl[0]
	start_url = rawUrl.find(':') + 2
	songUrl = rawUrl[start_url:]
	return songUrl

def downloadHook(count, block_size, total_size):
	ammountDownloaded = int((count * block_size) / 1024)
	percentDone = min(int(count * block_size * 100 / total_size), 100)
	targetSize = int(total_size / 1024)
	print("\r{0}% done. downloaded {1}KB out of {2}KB".format(percentDone, min(ammountDownloaded, targetSize), targetSize), end='', flush=True)

#copied over
def downloadSong(fileURL, fileName):
	try:
		urllib.request.urlretrieve(fileURL, 'songs/{0}'.format(fileName), downloadHook)
		print('')
	except (KeyboardInterrupt, SystemExit):
		print('\nstoping...')
		quit()
	except Exception as e:
		print('')
		print(str(e))
		print('something went wrong. skipping...\n')

def findLinks(URL, itemType, itemProp):
	#copied over
	if itemProp:
		itemProp = ' itemprop="url"'
	else:
		itemProp = ''
	htmlSource = urllib.request.urlopen(URL).read().decode('utf-8')
	trackLinks = re.findall('(?<=<a href="/{0}/).*?(?="{1}>)'.format(itemType, itemProp), htmlSource)#TODO remake this regex
	return trackLinks

def checkIfExsists(URL): #FIXME
	#try:
	#	urllib.request.urlopen(URL)
	#except (HTTPError, URLError) as e:
	#	#raise e
	#	return 0
	#else:
	#	return 1

	#ERROR: AttributeError: 'builtin_function_or_method' object has no attribute 'timeout'
	return 1

def makeDirectory(location, name):
	print('makeing directory {0} in songs/{1}'.format(name, location))
	os.makedirs('songs/{0}/{1}'.format(location, name), exist_ok=True)

def downloadArtist(URL, protocol, baseSite, siteName):#TODO
	trackLinks = findLinks(URL, "track", False)
	print('found {0} track(s) in {1}'.format(len(trackLinks), URL))
	if len(trackLinks) != 0:
		trackNumber = 0
		for track in trackLinks:
			trackNumber += 1
			downloadLink = findDownloadLnk("{0}://{1}/track/{2}".format(protocol, baseSite, track))
			print('\ndownloading {0} from site {1}. ({2}/{3})'.format(track, siteName, trackNumber, len(trackLinks)))
			downloadSong(downloadLink, '{0}/{1}.mp3'.format(siteName, track))
	albumLinks = findLinks(URL, "album", False)
	print('\nfound {0} album(s) in {1}'.format(len(albumLinks), URL))
	if len(albumLinks) != 0:
		albumNumber = 0
		for album in albumLinks:
			print('')
			makeDirectory(siteName, album)
			albumNumber += 1
			trackLinks = findLinks("{0}://{1}/album/{2}".format(protocol, baseSite, album), "track", True)
			if len(trackLinks) != 0:
				print('found {0} track(s) in {1}'.format(len(trackLinks), album))
				trackNumber = 0
				for track in trackLinks:
					trackNumber += 1
					downloadLink = findDownloadLnk("{0}://{1}/track/{2}".format(protocol, baseSite, track))
					print('\ndownloading {0} in {1} from {2}. album({3}/{4}) track({5}/{6})'.format(track, album, siteName, albumNumber, len(albumLinks), trackNumber, len(trackLinks)))
					downloadSong(downloadLink, '{0}/{1}/{2}.mp3'.format(siteName, album, track))

def downloadAlbum(URL, protocol, baseSite, siteName, itemName):
	makeDirectory(siteName, itemName)
	trackLinks = findLinks(URL, "track", True)
	print('\nfound {0} track(s) in {1}'.format(len(trackLinks), URL))
	trackNumber = 0
	for track in trackLinks:
		trackNumber += 1
		downloadLink = findDownloadLnk("{0}://{1}/track/{2}".format(protocol, baseSite, track))
		print('\ndownloading {0} in album {1}. ({2}/{3})'.format(track, itemName, trackNumber, len(trackLinks)))
		downloadSong(downloadLink, '{0}/{1}/{2}.mp3'.format(siteName, itemName, track))


def downloadTrack(URL, protocol, baseSite, siteName, itemName):
	print('\nfound 1 track in {0}'.format(URL))
	downloadLink = findDownloadLnk(URL)
	print('\ndownloading {0}. (1/1)'.format(itemName))
	downloadSong(downloadLink, '{0}/{1}.mp3'.format(siteName, itemName))

def parseInputURL(URL):
	regGroups = re.search('^(http|https):\/\/(.+?)(?:$|\/(track|album|music)|\/)(?:$|\/(.+?$))', URL)
	if regGroups == None: #TMP be more elegant n' shit
		return None, None, None, None, None
	protocol = regGroups.group(1)
	baseSite = regGroups.group(2)
	URLType = regGroups.group(3)
	itemName = regGroups.group(4)
	siteName = re.search('(?:^|\.)(?!music)(?!com)(?!bandcamp)(.+?)(?=$|\.)', baseSite).group(1)
	return protocol, baseSite, URLType, siteName, itemName

def main():
	print('v2.0\nread the "readme.txt" file for instructions and examples.\n')
	listOfURLs = []
	while True:
		userInput = input('> ')
		if userInput == '': break
		listOfURLs.append(userInput)
	for URL in listOfURLs:
		if checkIfExsists(URL) == 0:
			print('')#TODO print error
			continue
		protocol, baseSite, URLType, siteName, itemName = parseInputURL(URL)
		makeDirectory("", siteName)
		if URLType == "music" or URLType == None:
			downloadArtist(URL, protocol, baseSite, siteName)
		elif URLType == "album":
			downloadAlbum(URL, protocol, baseSite, siteName, itemName)
		elif URLType == "track":
			downloadTrack(URL, protocol, baseSite, siteName, itemName)
		else:
			print('{} is not a valid url. skipping...\n'.format(URL))
		print('')



if __name__ == '__main__':
	main()