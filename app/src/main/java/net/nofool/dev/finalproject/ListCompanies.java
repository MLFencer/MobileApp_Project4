package net.nofool.dev.finalproject;

public class ListCompanies {
    private String name, message;
    private String iD;
    private Category category;

    public enum Category{UpTrend, DownTrend, NoTrend}

    public ListCompanies(String name, String iD, String message,Category category) {
        this.name = name;
        this.iD = iD;
        this.message = message;
        this.category = category;
    }

    public ListCompanies() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getID() {
        return iD;
    }

    public void setID(String noteID) {
        this.iD = iD;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getAssociationDrawable(){
        return categoryToDrawable(category);
    }

    public static int categoryToDrawable(Category noteCategory){
        switch(noteCategory){
            case UpTrend:
                return R.drawable.ic_action_trending_up;
            case DownTrend:
                return R.drawable.ic_trending_down;
            case NoTrend:
                return R.drawable.ic_action_trending_neutral;
            default:
                return R.drawable.ic_action_trending_neutral;
        }
    }
}
