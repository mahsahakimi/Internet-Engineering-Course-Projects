package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.database.UserDatabase;
import ir.ac.ut.ece.ie.miobook.model.Role;
import ir.ac.ut.ece.ie.miobook.model.User;

public class AddCreditService {

    public static void addCredit(String username, int credit) {
        if (credit <= 0) {
            throw new IllegalArgumentException("Credit must be greater than zero.");
        }

        if (credit < 100) {
            throw new IllegalArgumentException("Minimum credit is 100 cents (1 dollar).");
        }

        User user = UserDatabase.getInstance().findUserByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        if (user.getRole() != Role.customer) {
            throw new IllegalArgumentException("This command is only for customer users.");
        }

        user.setBalance(user.getBalance() + credit);
    }
}
