package ir.ac.ut.ece.ie.miobook.service;

import ir.ac.ut.ece.ie.miobook.exceptions.*;

import ir.ac.ut.ece.ie.miobook.model.Wallet;
import ir.ac.ut.ece.ie.miobook.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public void deposit(String username, int amount) {
        Wallet wallet = walletRepository.findByUsername(username)
                .orElseThrow(() -> new WalletNotFoundException(username));

        wallet.deposit(amount);
        // todo
        walletRepository.save(wallet);
    }

    public int getBalance(String username) {
        return walletRepository.findByUsername(username)
                .map(Wallet::getBalance)
                .orElseThrow(() -> new WalletNotFoundException(username));
    }
}