//TODO: get these classes working: Artist

class Main{
	public static void main(String[] args)
  {
    //TODO: get user input
    //System.out.println(HelperUtils.findAllMatches("(?<=\"title\":\").+?(?=\")",HelperUtils.getWebpage("https://soundoftheaviators.bandcamp.com/track/godhunter")).get(0));
		Album godhunter = new Album("https://soundoftheaviators.bandcamp.com/album/masks-ep");
    godhunter.download("songs/");
	}
}