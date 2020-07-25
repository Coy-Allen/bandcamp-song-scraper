import java.util.HashMap;

class Song
{
  String rawUrl;
  String rawPage;
  HashMap<String,String> songInfo;
  String rawDownloadUrl;

  public Song (String rawUrl)
  {
    this.rawUrl = rawUrl;
    rawPage = HelperUtils.getWebpage(rawUrl);
    songInfo = new HashMap<String,String>();
    getSongInfo();
    findSongFile();
  }
  private void getSongInfo ()
  {
    //TODO: we rely on the set_price atrib to be after the title. we need to find an alternative
    //    var EmbedData looks promising but idk how we would parse it.
    //TODO: get more info from tracks
    songInfo.put("name",HelperUtils.findAllMatches("(?<=\"title\":\").+?(?=\")",rawPage).get(0));
  }
  private void findSongFile ()
  {
    //"mp3-128":"{url}" --> "{url}" --> {url}
    rawDownloadUrl = HelperUtils.findAllMatches("(?<=\"file\":\\{).*?(?=\\})",rawPage).get(0).split(":",2)[1].replaceAll("^\"|\"$", "");
  }
  void download (String parentFolder)
  {
    //TODO: make it auto detect the correct ext
    HelperUtils.downloadSong(parentFolder+songInfo.get("name")+".mp3",rawDownloadUrl);
  }
}