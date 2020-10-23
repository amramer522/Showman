
package showman0.amoor.showman.data.model;

public class Product
{
    public Product() {
    }

    private String companyName;
    private String productSize;
    private String productCode;
    private String productName;
    private double packageSizeInMeter;
    private String tonC;
    private long productQuantity;
    private String productQuality;
    private String productType;

    public Product(String companyName, String productSize, String productCode, String productName, double packageSizeInMeter, String tonC, Long productQuantity, String productQuality, String productType) {
        this.companyName = companyName;
        this.productSize = productSize;
        this.productCode = productCode;
        this.productName = productName;
        this.packageSizeInMeter = packageSizeInMeter;
        this.tonC = tonC;
        this.productQuantity = productQuantity;
        this.productQuality = productQuality;
        this.productType = productType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getProductSize() {
        return productSize;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public double getPackageSizeInMeter() {
        return packageSizeInMeter;
    }

    public String getTonC() {
        return tonC;
    }

    public long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductQuality() {
        return productQuality;
    }

    public String getProductType() {
        return productType;
    }

}
