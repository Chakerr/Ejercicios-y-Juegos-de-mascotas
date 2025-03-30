package co.edu.unipiloto.petapp.model;

public class LoginResponse {

    private boolean success;
    private int userId;
    public void setRole(String role) {
        this.role = role;
    }

    private String role;

    public LoginResponse(boolean success, int userId, String role) {
        this.success = success;
        this.userId = userId;
        this.role = role;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
