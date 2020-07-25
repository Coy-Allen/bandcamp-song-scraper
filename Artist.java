import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

class Artist
{
  String rawUrl;
  String rawPage;
  HashMap<String,String> artistInfo;
  List<Song> songs;
  List<Album> albums;

  public Artist (String rawUrl)
  {
    this.rawUrl = rawUrl;
    rawPage = HelperUtils.getWebpage(rawUrl);
    artistInfo = new HashMap<String,String>();
    songs = new ArrayList<Song>();
    albums = new ArrayList<Album>();
    getArtistInfo();
    getAllSongs();
    getAllAlbums();
  }
  private void getArtistInfo ()
  {
    //TODO: get info from artist
    //<meta property="og:title" content="{name}">
  }
  private void getAllSongs ()
  {
    for (String rawSongUrl : HelperUtils.findAllMatches("(?<=<a href=\"/track/).*?(?=\">)",rawPage))
    {
        songs.add(new Song(rawUrl+"track/"+rawSongUrl));
    }
  }
  private void getAllAlbums ()
  {
    for (String rawAlbumUrl : HelperUtils.findAllMatches("(?<=<a href=\"/album/).*?(?=\">)",rawPage))
    {
        albums.add(new Album(rawUrl+"album/"+rawAlbumUrl));
    }
  }
  void download (String parentFolder)
  {
    String folder = parentFolder+artistInfo.get("artist name")+"/";//FIXME: wont work without geting the info in getArtistInfo 
    HelperUtils.createDirectory(folder);
    for (Song song : songs)
    {
      song.download(folder);
    }
    for (Album album : albums)
    {
      album.download(folder);
    }
  }
}