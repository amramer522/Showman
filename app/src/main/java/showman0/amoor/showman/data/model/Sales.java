package showman0.amoor.showman.data.model;

import com.google.firebase.database.DatabaseReference;

public class Sales
{
   private String companyName;
   private String productType;
   private String saleId;
   private String salesManName;
   private String time;
   private String product_code;
   private String product_name;
   private String date;
   private long Quantity;
   private String user_place;
   private String customer_name;
   private String out_number;

   public String getCompanyName() {
      return companyName;
   }

   public void setCompanyName(String companyName) {
      this.companyName = companyName;
   }

   public String getProductType() {
      return productType;
   }

   public void setProductType(String productType) {
      this.productType = productType;
   }

   public Sales()
   {

   }





   public String getSaleId() {
      return saleId;
   }

   public void setSaleId(String saleId) {
      this.saleId = saleId;
   }

   public String getSalesManName() {
      return salesManName;
   }

   public void setSalesManName(String salesManName) {
      this.salesManName = salesManName;
   }

   public String getTime() {
      return time;
   }

   public void setTime(String time) {
      this.time = time;
   }

   public String getProduct_code() {
      return product_code;
   }

   public void setProduct_code(String product_code) {
      this.product_code = product_code;
   }

   public String getProduct_name() {
      return product_name;
   }

   public void setProduct_name(String product_name) {
      this.product_name = product_name;
   }

   public String getDate() {
      return date;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public long getQuantity() {
      return Quantity;
   }

   public void setQuantity(long quantity) {
      Quantity = quantity;
   }

   public String getUser_place() {
      return user_place;
   }

   public void setUser_place(String user_place) {
      this.user_place = user_place;
   }

   public String getCustomer_name() {
      return customer_name;
   }

   public void setCustomer_name(String customer_name) {
      this.customer_name = customer_name;
   }

   public String getOut_number() {
      return out_number;
   }

   public void setOut_number(String out_number) {
      this.out_number = out_number;
   }

   public Sales(String saleId, String salesManName, String time, String product_code, String product_name, String date, long quantity, String user_place, String customer_name, String out_number, String productType, String companyName)
   {
      this.saleId = saleId;
      this.salesManName = salesManName;
      this.time = time;
      this.product_code = product_code;
      this.product_name = product_name;
      this.date = date;
      Quantity = quantity;
      this.user_place = user_place;
      this.out_number = out_number;
      this.customer_name = customer_name;
      this.productType= productType;
      this.companyName=companyName;

   }


}
