package blog_post;

import java.net.ContentHandler;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BlogPost {
  String authorName;
  String title;
  String text;
  //  LocalDate publicationDate;
  String publicationDate;

  public BlogPost() {
  }

  public BlogPost(String authorName, String title, String text, String publicationDate) {
    this.authorName = authorName;
    this.title = title;
    this.text = text;
    this.publicationDate = publicationDate;
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
//    this.publicationDate = LocalDate.parse(publicationDate, formatter);
  }

  public void getContent() {
    System.out.println("Author's name: " + this.authorName);
    System.out.println("Title: " + this.authorName);
    System.out.println("Text: " + this.text);
    System.out.println("Date of publication: " + this.publicationDate);
  }
}
