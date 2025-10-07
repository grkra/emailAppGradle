package krawczyk.grzegorz.views;

import krawczyk.grzegorz.EmailManager;

public class ViewFactory {

    private EmailManager emailManager;

    public ViewFactory(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public void showLoginWindow () {
        System.out.println("Showing login window controller.");
    }
}
