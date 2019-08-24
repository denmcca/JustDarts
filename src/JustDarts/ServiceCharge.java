/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JustDarts;

/**
 *
 * @author Dennis
 */
public class ServiceCharge extends Transaction 
{
    private int associatedTransNum;
    
    public ServiceCharge(int numberIn, int assocNumIn, int IdIn, double amountIn)
    {
        super(numberIn, IdIn, amountIn);
        associatedTransNum = assocNumIn;
    }
    
    public int getAssociatedTransNumber()
    {
        return associatedTransNum;
    }
}
