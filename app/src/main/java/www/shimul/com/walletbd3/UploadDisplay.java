package www.shimul.com.walletbd3;

public class UploadDisplay {


    private String code;
    //private String title;
    private String price;

    private String image_ONE;
    //private String image_TWO;


    public UploadDisplay(String image_ONE, String code, String price) {
        this.image_ONE = image_ONE;
        this.code = code;
        this.price = price;
    }

    public String getImage_ONE() {
        return image_ONE;
    }

    public void setImage_ONE(String image_ONE) {
        this.image_ONE = image_ONE;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
