package www.shimul.com.walletbd3;

import android.net.Uri;

public class Upload {

    private String image_ONE;
    private String image_TWO;

    private String code;
    private String title;
    private String price;
    private String description;

    public Upload(){

    }

    public Upload(String image_ONE, String code, String price) {
        this.image_ONE = image_ONE;
        this.code = code;
        this.price = price;
    }

    public Upload(String image_ONE, String image_TWO, String code, String title, String price, String description) {
        this.image_ONE = image_ONE;
        this.image_TWO = image_TWO;
        this.code = code;
        this.title = title;
        this.price = price;
        this.description = description;
    }

    public String getImage_ONE() {
        return image_ONE;
    }

    public void setImage_ONE(String image_ONE) {
        this.image_ONE = image_ONE;
    }

    public String getImage_TWO() {
        return image_TWO;
    }

    public void setImage_TWO(String image_TWO) {
        this.image_TWO = image_TWO;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
