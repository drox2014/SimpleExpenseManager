package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by drox2014 on 11/6/2017.
 */

public class AccountDAOImpl implements AccountDAO {

    private DBHelper dbHelper;

    public AccountDAOImpl(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> numbersList = new ArrayList<>();
        Cursor res = dbHelper.getAccountNumbersList();
        while (res.moveToNext()) {
            numbersList.add(res.getString(0));
        }
        return numbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        ArrayList<Account> accountList = new ArrayList<>();
        Cursor res = dbHelper.getAccountsList();
        while (res.moveToNext()) {
            Account account = new Account(res.getString(0), res.getString(1), res.getString(2), res.getDouble(3));
            accountList.add(account);
        }
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor res = dbHelper.getAccount(accountNo);
        if (res.moveToNext()) {
            return new Account(res.getString(0), res.getString(1), res.getString(2), res.getDouble(3));
        } else {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void addAccount(Account account) {
        dbHelper.addAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        boolean res = dbHelper.removeAccount(accountNo);
        if (!res) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        if (account != null) {
            switch (expenseType) {
                case EXPENSE:
                    account.setBalance(account.getBalance() - amount);
                    break;
                case INCOME:
                    account.setBalance(account.getBalance() + amount);
                    break;
            }
            boolean res = dbHelper.updateBalance(account);
            if (!res) {
                String msg = "Unable to update the balance";
                throw new InvalidAccountException(msg);
            }
        } else {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

    }
}
