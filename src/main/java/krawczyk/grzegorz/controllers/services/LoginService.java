package krawczyk.grzegorz.controllers.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import krawczyk.grzegorz.EmailManager;
import krawczyk.grzegorz.controllers.EmailLoginResult;
import krawczyk.grzegorz.models.EmailAccount;

import javax.mail.*;

/**
 * Controller responsible for authentication to email provider.
 * <hr></hr>
 * It extends Service class which is used to perform tasks on background Threads.
 * Service class is part of JavaFX and it makes easier to manage multithreading code.
 * <hr></hr>
 * Service will return its parameter - EmailLoginResult.
 */
public class LoginService extends Service<EmailLoginResult> {

    EmailAccount emailAccount;
    EmailManager emailManager;

    public LoginService(EmailAccount emailAccount, EmailManager emailManager) {
        this.emailAccount = emailAccount;
        this.emailManager = emailManager;
    }

    // In LoginWindowController there is loginService.start() method called.
    // start() method calls createTask() in which all background task code is.
    @Override
    protected Task<EmailLoginResult> createTask() {
        return new Task<EmailLoginResult>() {
            @Override
            protected EmailLoginResult call() throws Exception {
                return login();
            }
        };
    }

    /**
     * Method is responsible for connection and authentication to email provider.
     * If successful login it adds email account to EmailTreeView in EmailManager class.
     * @return information if login was successful or if there was error as option from Enum.
     * Returns SUCCESS, FAILED_BY_CREDENTIALS, FAILED_BY_NETWORK or FAILED_BY_UNEXPECTED_ERROR
     */
    private EmailLoginResult login() {
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

            // It adds this emailAccout to TreeView (it will appear in Main window)
            emailManager.addEmailAccount(emailAccount);
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
