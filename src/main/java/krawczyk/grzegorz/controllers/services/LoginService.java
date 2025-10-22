package krawczyk.grzegorz.controllers.services;

import krawczyk.grzegorz.EmailManager;
import krawczyk.grzegorz.controllers.EmailLoginResult;
import krawczyk.grzegorz.models.EmailAccount;

import javax.mail.*;

/**
 * Controller responsible for authentication to email provider.
 */
public class LoginService {

    EmailAccount emailAccount;
    EmailManager emailManager;

    public LoginService(EmailAccount emailAccount, EmailManager emailManager) {
        this.emailAccount = emailAccount;
        this.emailManager = emailManager;
    }

    /**
     * Method is responsible for connection and authentication to email provider.
     * @return information if login was successful or if there was error as option from Enum.
     * Returns SUCCESS, FAILED_BY_CREDENTIALS, FAILED_BY_NETWORK or FAILED_BY_UNEXPECTED_ERROR
     */
    public EmailLoginResult login() {
        // It creates new authentication - object which knows how to authenticate on email provider API
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount.getAddress(), emailAccount.getPassword());
            }
        };

        try {
            // It creates new email session with email properties (information about connection from email provider) and authentication.
            Session session = Session.getInstance(this.emailAccount.getProperties(), authenticator);

            // In session Store is created.
            // Store is used to store and retrieve email messages
            Store store = session.getStore("imaps");
            store.connect(this.emailAccount.getProperties().getProperty("incomingHost"),
                    this.emailAccount.getAddress(),
                    this.emailAccount.getPassword());
            emailAccount.setStore(store);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return EmailLoginResult.FAILED_BY_UNEXPECTED_ERROR;
        } catch (AuthenticationFailedException e) {
            e.printStackTrace();
            return EmailLoginResult.FAILED_BY_CREDENTIALS;
        } catch (MessagingException e) {
            e.printStackTrace();
            return EmailLoginResult.FAILED_BY_UNEXPECTED_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            return EmailLoginResult.FAILED_BY_UNEXPECTED_ERROR;
        }

        return EmailLoginResult.SUCCESS;
    }
}
