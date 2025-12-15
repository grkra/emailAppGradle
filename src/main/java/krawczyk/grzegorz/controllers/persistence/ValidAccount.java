package krawczyk.grzegorz.controllers.persistence;

import java.io.Serializable;

/**
 * Class holds valid account (email address and password of email account to which user logged in in the application).
 * This data is saved locally to log in automatically after closing the app.
 * It's separate from EmailAccount because it does need only this data.
 */
public class ValidAccount implements Serializable {

    private String emailAddress;
    private String password;

    /**
     * Constructor of class ValidAccount
     * @param emailAddress - String with email address used to log in
     * @param password - String with password used to log in
     */
    public ValidAccount(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
    }

    /**
     * Method returns email address used to log in
     * @return - String with email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Method sets email address used to log in
     * @param emailAddress - String with email address
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Method returns password used to log in
     * @return - String with password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Method sets password used to log in
     * @param password - String with password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
