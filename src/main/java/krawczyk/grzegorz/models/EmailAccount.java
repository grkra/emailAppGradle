package krawczyk.grzegorz.models;

import javax.mail.Store;
import java.util.Properties;

/**
 * Class holds information about email account and connection to it.
 */
public class EmailAccount {
    private String address;
    private String password;

    // email configuration
    private Properties properties;

    // for storing and retrieving messages
    private Store store;

    /**
     * EmailAccount constructor.
     * <hr></hr>
     * Saves passed values to properties.
     * Initializes email properties.
     * @param address - String containing email address.
     * @param password - String containing password to email account.
     */
    public EmailAccount(String address, String password) {
        this.address = address;
        this.password = password;
        properties = new Properties();

        // host for sending emails
        properties.put("incomingHost", "imap.wp.pl");
        properties.put("mail.store.protocol", "imaps");

        // host for getting emails
        properties.put("mail.transport.protocol", "smtps");
        properties.put("mail.smtps.host", "smtp.wp.pl");
        properties.put("mail.smtps.auth", "true");
        properties.put("outgoingHost", "smtp.wp.pl");
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
