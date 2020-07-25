import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class HelperUtils
{
  static List<String> findAllMatches (String regexStr, String text)
  {
    List<String> allMatches = new ArrayList<String>();
    Matcher m = Pattern.compile(regexStr).matcher(text);
    while(m.find()) allMatches.add(m.group());
    return allMatches;
  }
  static String getWebpage (String url)
  {
    URL targetUrl;
    String inputLine;
    String webPage = "";
    try
    {
      targetUrl = new URL(url);
      BufferedReader in = new BufferedReader(new InputStreamReader(targetUrl.openStream()));
      while((inputLine = in.readLine()) != null) webPage += inputLine;
    }
    catch(MalformedURLException ex)
    {
      //DOTO: error catching
      return "";
    }
    catch(IOException ex)
    {
      //DOTO: error catching
      return "";
    }
    return webPage;
  }
  static void createDirectory(String relDir)
  {
    try
    {
      Files.createDirectories(Paths.get(relDir));
    }
    catch(IOException ex)
    {
      //DOTO: error catching
      return;
    }
  }
  static void downloadSong(String file, String url)
  {
    try
    {
      ReadableByteChannel rbc = Channels.newChannel((new URL(url)).openStream());
      FileOutputStream fos = new FileOutputStream(file);
      fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
      fos.close();
      rbc.close();
    }
    catch(IOException ex)
    {
      //DOTO: error catching
      return;
    }
  }
}