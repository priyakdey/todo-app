package io.todo.api.model.bo;

public class UserDetailsBO {
    private final String username;
    private String password;
    private Boolean isEnabled;
    private String verificationToken;
    private String name;
    private String email;

    private UserDetailsBO(String username) {
        this.username = username;
    }

    public static final class UserDetailsBOBuilder {
        private UserDetailsBO userDetailsBO;

        public UserDetailsBOBuilder(String username) {
            userDetailsBO = new UserDetailsBO(username);
        }

        public static UserDetailsBOBuilder getInstance(String username) {
            return new UserDetailsBOBuilder(username);
        }

        public UserDetailsBOBuilder setPassword(String password) {
            userDetailsBO.password = password;
            return  this;
        }

        public UserDetailsBOBuilder setName(String name) {
            userDetailsBO.name = name;
            return this;
        }

        public UserDetailsBOBuilder setEmail(String email) {
            userDetailsBO.email = email;
            return this;
        }

        public UserDetailsBO build() {
            return userDetailsBO;
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }
}
