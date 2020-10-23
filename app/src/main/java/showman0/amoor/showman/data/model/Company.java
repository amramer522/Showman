package showman0.amoor.showman.data.model;

public class Company
{
    private String company_name,company_stands;

    public Company() {
    }

    public Company(String company_name, String company_stands) {
        this.company_name = company_name;
        this.company_stands = company_stands;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_stands() {
        return company_stands;
    }

    public void setCompany_stands(String company_stands) {
        this.company_stands = company_stands;
    }
}
