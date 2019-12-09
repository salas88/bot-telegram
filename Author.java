import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.select.Elements;

public class Author {
    private Document document;
    private Document booksDocs;
    private String nameAuthor =  "";

    private int valuesLikesBook;
    private int valuesViewsBooks;
    private int valuesCommentBooks;

    public Author(String name){
        nameAuthor=name;
        connect();
    }

    private void connect() {
        try {
            document = Jsoup.connect("https://www.surgebook.com/"+nameAuthor).get();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getName(){
       Elements namePerson = document.getElementsByClass("author-name bold");
       return namePerson.text();
    }

    public String getBio(){
        Elements  bioPerson = document.getElementsByClass("author-bio");
        return bioPerson.text();
    }

    public String getImage(){
        Elements avatar = document.getElementsByClass("user-avatar");
        String url = avatar.attr("style");
        url = url.replace("background-image: url('", "");
        url = url.replace("');", "");
        return url;
    }

    public String getInfoPerson(){
        String info = "";

        info +="Имя: " +getName() + "\n";
        info +="Статус: " + getBio() + "\n";

        Elements names = document.getElementsByClass("info-stats-name");
        Elements values = document.getElementsByClass("info-stats-num");

        for (int i = 0; i <names.size() ; i++) {
            info += names.get(i).text() + ": "+ values.get(i).text() +"\n";
        }

        info += getBooks();
        return info;
    }

    private String getBooks() {
        try{
            booksDocs = Jsoup.connect("https://www.surgebook.com/"+ nameAuthor +"/books/all").get();
        } catch (IOException e){
            e.printStackTrace();
        }
        String text = "\nСписок книг: \n";
        List<String> textUrlBooks = new ArrayList<>();
        Elements books = booksDocs.getElementsByClass("book_view_mv1v2_title");
        Elements booksUrl = booksDocs.getElementsByClass("book_view_mv1v2_cover");

        for (int i = 0; i <books.size() ; i++) {
            text += books.get(i).text() + "\n";
            textUrlBooks.add(booksUrl.get(i).attr("href"));

        }
        getStatistics(textUrlBooks);
        text += "\nКоличестволайков на книгах: " + valuesLikesBook + "\n";
        text += "Количество просмотров на книгах: " + valuesViewsBooks + "\n";
        text += "Количество коментариев на книгах: " + valuesCommentBooks + "\n";
        return text;
    }

    private String getStatistics(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            try {
                booksDocs = Jsoup.connect(list.get(i)).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements elements = booksDocs.getElementsByClass("font-size-14 color-white ml-5");
            valuesLikesBook += Integer.valueOf(elements.get(0).text());
            valuesCommentBooks += Integer.valueOf(elements.get(1).text());
            valuesViewsBooks += Integer.valueOf(elements.get(2).text());


        }
        return "";
    }
}

