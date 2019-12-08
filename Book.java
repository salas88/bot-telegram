import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Book {
    private Document document;

    public Book(){
        connect();
    }

    private void connect() {
        try {
            document = Jsoup.connect("https://www.surgebook.com/A_Achell/book/uvertyura").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTitle(){
        return document.title();
    }

    public String getLikes(){
        Element element = document.getElementById("likes");
        return element.text();
    }

    public String getDescription(){
        Element element = document.getElementById("description");
        return element.text();
    }

    public String getGeners(){
        Elements element = document.getElementsByClass("genres d-block");
        return element.text();
    }

    public String getAuthorName(){
        Elements element = document.getElementsByClass("text-decoration-none column-author-name bold max-w-140 text-overflow-ellipsis");
        return element.text();
    }

    public String getImage(){
        Elements elements = document.getElementsByClass("cover-book");
        String url = elements.attr("style");
        url = url.replace("background-image: url('", "");
        url = url.replace("');", "");

        return url;
    }
}
