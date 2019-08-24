/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JustDarts;
import java.io.*;

/**
 *
 * @author dennis_mccann
 */
public class Account implements Serializable
{
    protected String name; // The person who owns the account
    protected double balance;// do not define this in CheckingAccount class
 
    public Account(String acctName, double initBalance){
             balance = initBalance;
             name = acctName;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public double getBalance() {
        return balance;
    }
 
    public void setBalance(double balance) {
        this.balance = balance;
    }
}