package ir.ac.ut.ece.ie.miobook;

import ir.ac.ut.ece.ie.miobook.command.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        String[] parts = line.split(" ", 2);
        String command = parts[0];
        String inputJson = parts.length > 1 ? parts[1] : null;

        if (command.equals("add_user")) {
            AddUserCommand.execute(inputJson);
        } else if (command.equals("add_author")) {
            AddAuthorCommand.execute(inputJson);
        } else if (command.equals("add_book")) {
            AddBookCommand.execute(inputJson);
        } else if (command.equals("add_cart")) {
            AddCartCommand.execute(inputJson);
        }else if (command.equals("add_credit")) {
            AddCreditCommand.execute(inputJson);
        } else if (command.equals("show_user_details")) {
            ShowUserDetailsCommand.execute(inputJson);
        } else {
            System.out.println("Invalid command.");
        }
    }
}
