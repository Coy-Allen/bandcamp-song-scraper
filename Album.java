import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

class Album
{
  String rawUrl;
  String rawPage;
  HashMap<String,String> albumInfo;
  List<Song> songs;

  public Album (String rawUrl)
  {
    this.rawUrl = rawUrl;
    rawPage = HelperUtils.getWebpage(rawUrl);
    albumInfo = new HashMap<String,String>();
    songs = new ArrayList<Song>();
    getAlbumInfo();
    getAllSongs();
  }
  private void getAlbumInfo ()
  {
    //TODO: get more info from album page
    albumInfo.put("album name",HelperUtils.findAllMatches("(?<=album_title: \").+?(?=\")",rawPage).get(0));
  }
  private void getAllSongs ()
  {
    //NOTE: for some reason without {1,10} it wont work as it ignores \n for no reason when useing .*?
    for ( String rawSongUrl : HelperUtils.findAllMatches("(?<=<a href=\"/track/).{1,255}(?=\" itemprop=\"url\">)",rawPage) )
    {
      songs.add(new Song(rawUrl.split("album/")[0]+"track/"+rawSongUrl));
    }
  }
  void download (String parentFolder)
  {
    String folder = parentFolder+albumInfo.get("album name")+"/";
    HelperUtils.createDirectory(folder);
    for (Song song : songs)
    {
      song.download(folder);
    }
  }
}