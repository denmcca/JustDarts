/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JustDarts;

/**
 *
 * @author dennis_mccann
 */
public class Deposit extends Transaction
{ //derived from transaction class
    private double cashAmount;
    private double checkAmount;
    //each deposit type shall be inputted from different input screens/prompts?
    
    public Deposit(int tId, double tAmt, int tCount, double cashIn, double checkIn)
    {
        super(tCount, tId, tAmt);
        cashAmount = cashIn;
        checkAmount = checkIn;
    }
    
    public double getCashAmoumnt(){
        return cashAmount;
    }
    
    public double getCheckAmount(){
        return checkAmount;
    }
    
    public double getTotalAmount(){
        return cashAmount + checkAmount;
    }
}
