package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by drox2014 on 11/17/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "150528X";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE account(account_no TEXT PRIMARY KEY, bank_name TEXT, account_holder_name TEXT, balance DOUBLE)");
        db.execSQL("CREATE TABLE transactions(transaction_date INT, account_no TEXT, expense_type TEXT, amount DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS account");
        db.execSQL("DROP TABLE IF EXISTS transactions");
        onCreate(db);
    }


    public Cursor getAccountNumbersList() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT account_no FROM account", null);
    }

    public Cursor getAccountsList() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM account", null);
    }

    public Cursor getAccount(String accountNo) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM account WHERE account_no = ?", new String[]{accountNo});
    }

    public boolean addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_no", account.getAccountNo());
        contentValues.put("bank_name", account.getBankName());
        contentValues.put("account_holder_name", account.getAccountHolderName());
        contentValues.put("balance", account.getBalance());
        return db.insert("account", null, contentValues) != -1;
    }

    public boolean removeAccount(String accountNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("account", "account_no = ?", new String[]{accountNo}) > 0;
    }

    public boolean updateBalance(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_no", account.getAccountNo());
        contentValues.put("bank_name", account.getBankName());
        contentValues.put("account_holder_name", account.getAccountHolderName());
        contentValues.put("balance", account.getBalance());
        return db.update("account", contentValues, "account_no = ?", new String[]{account.getAccountNo()}) > 0;
    }

    public void logTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("transaction_date", transaction.getDate().getTime());
        contentValues.put("account_no", transaction.getAccountNo());
        contentValues.put("expense_type", transaction.getExpenseType().toString());
        contentValues.put("amount", transaction.getAmount());
        db.insert("transactions", null, contentValues);
    }

    public Cursor getAllTransactionLogs() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM transactions", null);
    }
}
