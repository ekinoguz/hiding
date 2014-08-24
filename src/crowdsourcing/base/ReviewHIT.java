package crowdsourcing.base;

public class ReviewHIT {

  public int author;
  public int review;
  public String id;
  
  public ReviewHIT(int author, int review, String id) {
    this.author = author;
    this.review = review;
    this.id = id;
  }
  
  @Override
  public String toString() {
    return author+"_review:"+review+"_id:"+id;
  }
}
