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
public class Check extends Transaction
{
    private int checkNumber; // check number for each check transaction
 
    public Check(int tId, double tAmt, int tCount, int checkNumberIn) {
        super(tCount, tId, tAmt);
        this.checkNumber = checkNumberIn;
    }
 
    public int getCheckNumber() {
        return checkNumber;
    }
 
    public void setCheckNumber(int checkNumber) {
        this.checkNumber = checkNumber;
    }
}
