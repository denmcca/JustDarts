/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JustDarts;
import java.io.*;
/**
 *
 * @author Dennis McCann
 */

public class Transaction implements Serializable
{
  
    private int transNumber; //number of transaction
    private int transId = 0; //check, deposit, service charge
    private double transAmt; //amount transferred for this instance
   
    public Transaction(int number, int id, double amount)
    {
        transNumber = number;
        transId = id;
        transAmt = amount;
    }
   
    public int getTransNumber()
    {
        return transNumber;
    }
   
    public int getTransId()
    {
        return transId;
    }
   
    public double getTransAmount()
    {
        return transAmt;
    }
}
