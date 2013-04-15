package nrgscoutserver;

public class Team {
    
    private int number = 0;
    private String name = "";
    private String[] data;
    
    public Team(int number, String name) {
        this.number = number;
        this.name = name;
    }
    
    public void setData(String[] data) {
        this.data = data;
    }
    
    public String[] getData() {
        return data;
    }
    
    public int getNumber() {
        return number;
    }
    
    public String getName() {
        return name;
    }
}
