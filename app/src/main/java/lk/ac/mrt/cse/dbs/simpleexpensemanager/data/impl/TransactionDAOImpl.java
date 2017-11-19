package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by drox2014 on 11/18/2017.
 */

public class TransactionDAOImpl implements TransactionDAO {

    private DBHelper dbHelper;

    public TransactionDAOImpl(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        dbHelper.logTransaction(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        Cursor res = dbHelper.getAllTransactionLogs();
        while(res.moveToNext()){
            Date date = new Date();
            date.setTime(res.getLong(0));
            ExpenseType expenseType = res.getString(2).equals("EXPENSE") ? ExpenseType.EXPENSE : ExpenseType.INCOME;
            transactions.add(new Transaction(date, res.getString(1), expenseType, res.getDouble(3)));
        }

        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = getAllTransactionLogs();
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }
}
