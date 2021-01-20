package requester;

public class RequestFactory {

    public static Request scrapeRequest(){
        return new Request().format("csv").paramQuery("bundle", "confezione_farmaco");
    }

}
