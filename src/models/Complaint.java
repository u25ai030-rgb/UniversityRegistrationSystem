package models;

public class Complaint {
    private String description;
    private String status; 

    public Complaint(String description) {
        this.description = description;
        this.status = "Pending";
    }

    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}