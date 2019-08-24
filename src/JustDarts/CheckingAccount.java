/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JustDarts;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.io.*;
/**
 *
 * @author Dennis McCann
 */
public class CheckingAccount extends Account {

    public static final DecimalFormat fmt = new DecimalFormat("$#,###,###,##0.00;($#,###,###,##0.00)");
    private int servChargeID;
    private double totalServiceCharge;
    private boolean under500Flag;
    public static Vector<CheckingAccount> dataStore;
    private ArrayList<Transaction> transList;  // keeps a list of Transaction objects for the account
    private int transCount;   // the count of Transaction objects and used as the ID for each transaction
    static final double checkCharge = 0.15, depCharge = 0.10, bettingCharge = 50.00;
    static final double under500 = 5.00, underZero = 10.00, zeroWarn = 50.00;
    public static final double limitAmt = 500.00, bottomLimit = 0.00;
    public static final Font CAFont = new Font("Courier New", Font.PLAIN, 12);
    public static CheckingAccount acct1; //acctLoadState;
    public static String findAcctStrIn, 
            filename = "default.dat", 
            notFound = "Done searching. Account not found for ", 
            acctFound = "Found Account for ", 
            fileLoadedStr = "File successfully loaded...";
    public static boolean fileFlag = false;
    public static boolean foundFlag = false;
    public static boolean saveFlag = true; //saveFlag checks if file was just loaded, skipFlag states if program should prompt to save
    public static boolean cancelLoadFlag = false;
    public static CheckingAccount savedFileCheck;
    
    public static boolean activeBet = false;
    public int highScore = 0;
    public boolean tutorialState = true;
    public static int highestScore = 0;
    public static String highestScorer = " ";

    public CheckingAccount(String nameIn, double initialBalance)
    {
        super(nameIn, initialBalance);
        totalServiceCharge = 0;
        under500Flag = false;
        transList = new ArrayList<Transaction>();
        transList.clear();
        transCount = 0;
        servChargeID = 3;
    }
    
    public int getServChargeID()
    {
        return servChargeID;
    }
      
    public int getTransCount()  //returns the current value of transCount;
    {
        return transCount++;
    }
    
    public Transaction getTrans(int i) // returns the i-th Transaction object in the list
    {
        return transList.get(i);
    }
    
    public double getBalance()
    {
        return balance;
    }
    
    public void setBalance(double transAmt, int tCode)
    {
        if(tCode == 1) //check transaction
            balance -= transAmt;
        if(tCode == 2) //deposit transaction
            balance += transAmt;
    }
    
    public double getServiceCharge()
    {
        return totalServiceCharge;
    }
    
    public void setServiceCharge(double currentServiceCharge)
    {
        totalServiceCharge += currentServiceCharge;
    }
    
    public boolean getUnder500()
    {
        return under500Flag;
    }
    
    public void setUnder500()
    {
        under500Flag = true;
    }
    
    public static void getInitBal()
    {
        boolean flag = true;
        String name = setAccountName();
        if (!(name == null))
        {
            String balance = setInitialBalance();
            if(!(balance == null))
            {
                acct1 = new CheckingAccount(name, Double.parseDouble(balance));
                flag = false;
            }
        }
        if (flag)
        {
            Main.ta.setText("Account creation cancelled.");
            return;
        }
        
        dataStore.add(acct1);
        Main.ta.setText("Account created.");
        saveFlag = false;
    }

    public static int getTransCode()
    {
        String code;

        code = JOptionPane.showInputDialog(null, "Enter "
                + "transaction code\n'1' for Check\n'2' for Deposit"
                + "\n'0' to end Session", "Input", JOptionPane.PLAIN_MESSAGE);
        
        if(code == null)
            return -1;
        if(code.isEmpty())
        {
            return 4;
        }
        try
        {
            int test = Integer.parseInt(code);
        }
        catch(NumberFormatException nfe)
        {
            return 4;
        }
        
        return Integer.parseInt(code);
    }
    
    public static int getCheckNumber()
    {
        String checkNum = JOptionPane.showInputDialog(null, "Enter check number");
        if(checkNum == null || checkNum.isEmpty())
        {
            System.out.println("\nCheck cancelled\n");
            return -1;
        }
        
        return Integer.parseInt(checkNum);
    }
    
    
    
    
    public static double getTransAmt(int transId)
    {
        String tAmt;
        if (transId == 1)
        {
            do
            {
                tAmt = JOptionPane.showInputDialog(null, "Enter transaction amount: ");
                try
                {
                    double test = Double.parseDouble(tAmt);
                }
                catch(NumberFormatException nfe)
                {
                    tAmt = "Invalid input";
                }
            }while(tAmt == "Invalid input");
            
            return Double.parseDouble(tAmt);
        }
        else
            return 0;
    }
    

    
    
    public static void enterTransaction()
    {
        int loop = 1;
        do
            {
            int code = getTransCode();

            if(code == 4)
            {
                while(code != 1 && code !=2 && code != 0)
                {
                    code = getTransCode();
                }
            }
            else if(code == 1)
            {
                int checkNum = getCheckNumber();
                if(checkNum == -1 || checkNum == 0)
                {
                    return;
                }
                processCheck(checkNum, acct1, getTransAmt(code), code);
            }
            else if(code == 2)
            {
                processDeposit(acct1, code);
            }
            else if(code == 0)
            {
                signOff(acct1);
                loop = 0;
            }
            else
            {
                System.out.println("Invalid code was entered: " + code);
            }
        }while (loop == 1);
    }
    

    
    
    
    public static void processCheck(int checkNum, CheckingAccount acct, double amt, int code)
    {
        acct.setBalance(amt, code);
      
        acct.setServiceCharge(checkCharge);
        
        int assocNum = acct.getTransCount();
        
        acct.addTrans(new Check(code, amt, assocNum, checkNum));
        
        acct.addTrans(new ServiceCharge(acct.getTransCount(), assocNum, acct.getServChargeID(), checkCharge));
        
        String message = acct.getName() + "'s account\n";
        
        message += "Transaction: Check #" + checkNum + " in amount of " + fmt.format(amt) + 
                "\nCurrent Balance: " + fmt.format(acct.getBalance()) +
                "\nService Charge: Check --- charge " + fmt.format(checkCharge); 
        
        if(acct.getBalance() < 500 && acct.getUnder500() == false)
        {
            acct1.setServiceCharge(under500);
            acct.addTrans(new ServiceCharge(acct.getTransCount(), assocNum, 3, under500));
            message += "\nService Charge: Below " + fmt.format(limitAmt) +
                    " --- charge " + fmt.format(under500);
                    
            acct.setUnder500();
        }                  
                
        if(acct.getBalance() < zeroWarn)
        {
            message += "\nWarning: Balance below " + fmt.format(zeroWarn);
        }

        if(acct.getBalance() < bottomLimit)
        {
            acct.setServiceCharge(underZero);
            acct.addTrans(new ServiceCharge(acct.getTransCount(), assocNum, 3, underZero));
            message += "\nService Charge: Below " + fmt.format(bottomLimit) 
                    + " --- charge " + fmt.format(underZero);
        }
        
        message += "\nTotal Service Charge: " + fmt.format(acct.getServiceCharge());
        
        JOptionPane.showMessageDialog(null, message);
        
    }

    
    
    
    
    
    
    public static void processDeposit(CheckingAccount acct, int code)
    {
        double cashIn = 0;
        double checkIn = 0;
        JPanel enterDepPanel = new JPanel();
        enterDepPanel.setLayout(new BoxLayout(enterDepPanel, BoxLayout.Y_AXIS));
        
        JTextField cash = new JTextField();
        JTextField check = new JTextField();
        enterDepPanel.add(new JLabel("Cash:"));
        enterDepPanel.add(cash);
        enterDepPanel.add(new JLabel("Check:"));
        enterDepPanel.add(check);
        cash.requestFocusInWindow();
        
        JOptionPane pane = new JOptionPane(enterDepPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION){
            @Override
            public void selectInitialValue(){
                cash.requestFocusInWindow();
            }
        };
                
        pane.createDialog(null, "Deposit").setVisible(true);
             
        if(cash.getText().isEmpty())
            cashIn = 0;
        else
            cashIn = Double.parseDouble(cash.getText());
        
        if(check.getText().isEmpty())
            checkIn = 0;
        else
            checkIn = Double.parseDouble(check.getText());
        
        if(cash.getText().isEmpty() && check.getText().isEmpty())
        {
            System.out.println("\nDeposit was cancelled\n");
            return;
        }
        
        int assocNum = acct.getTransCount();
        acct.addTrans(new Deposit(code, cashIn + checkIn, assocNum, cashIn, checkIn));

        acct.setBalance((cashIn + checkIn), code);

        acct.setServiceCharge(depCharge);
        acct.addTrans(new ServiceCharge(acct.getTransCount(), assocNum, acct.getServChargeID(), depCharge));
        
        String message = acct.getName() + "'s account\n\n";

        message += "Transaction: Deposit-in amount of : " + 
                fmt.format(cashIn + checkIn) +
                "\nCurrent Balance: " + fmt.format(acct.getBalance()) + 
                "\nService Charge: Deposit --- charge " + fmt.format(depCharge) +
                "\nTotal Service Charge: " + fmt.format(acct.getServiceCharge());
        
        JOptionPane.showMessageDialog(null, message);
    }
    
    
    
    
    
    public void addTrans(Transaction newTrans)   // adds a transaction object to the transList
    {
        transList.add(newTrans);
        saveFlag = false;
    }
    
    
    
    public static void listAllTrans(CheckingAccount acctIn)
    {        
        JTextArea text = new JTextArea();
        String message ="Account: " + acctIn.getName()
                + "\nBalance: " + fmt.format(acct1.getBalance()) 
                + "\nService Charge: " + fmt.format(acct1.getServiceCharge())
                + "\n\nList of all transactions:"
                + String.format("\n\n%-10s%-15s%20s\n", "ID", "Type", "Amount");
        
        text.setOpaque(false);
        text.setFont(CAFont);
        text.setBorder(null);
        
        for(int i = 0; i < acctIn.transList.size(); i++)
        {
            message += String.format("%-10d%-15s%20s\n", acctIn.transList.get(i).getTransNumber(), 
                    transIDstring(acctIn.transList.get(i).getTransId()),
                    fmt.format(acctIn.transList.get(i).getTransAmount()));
        }
        
        Main.ta.setText(message);
        transListCheck();
//        JOptionPane.showMessageDialog(null, text);
    }
    
    
    
    
    
    public static void listAllChecks(CheckingAccount acctIn)
    {
        JTextArea text = new JTextArea();
        String message = "Account: " + acctIn.getName()
                + "\nBalance: " + fmt.format(acct1.getBalance()) 
                + "\nService Charge: " + fmt.format(acct1.getServiceCharge())
                + String.format("\n\n%-10s%-15s%20s\n", "ID", "Check", "Amount");
        
        text.setOpaque(false);
        text.setFont(CAFont);
        text.setBorder(null);
        
        for(int i = 0; i < acctIn.transList.size(); i++)
        {
            if(acctIn.transList.get(i).getTransId() == 1)
            {
                Check temp = ((Check)acctIn.transList.get(i));
                message += String.format("%-10d%-15d%20s\n", 
                        acctIn.transList.get(i).getTransNumber(),
                        temp.getCheckNumber(), 
                        fmt.format(acctIn.transList.get(i).getTransAmount()));
            }
        }
        Main.ta.setText(message);
        transListCheck();
    }
    
    
    
    
    
    
    
    

    public static void listAllDeposits(CheckingAccount acctIn)
    {
        String message = "Account: " + acctIn.getName()
                + "\nBalance: " + fmt.format(acct1.getBalance()) 
                + "\nService Charge: " + fmt.format(acct1.getServiceCharge())
                + String.format("\n\n%-5s%-10s%-10s%-10s%10s",
                        "ID", "Type", "Checks", "Cash", "Amount\n");
   
        for(int i = 0; i < acctIn.transList.size(); i++)
        {
            if(acctIn.transList.get(i).getTransId() == 2)
            {
                Deposit temp = (Deposit)acctIn.transList.get(i);
                
                message += String.format("%-5d%-10s%-10s%-10s%10s\n", 
                        acctIn.transList.get(i).getTransNumber(), "Deposit",
                        fmt.format(temp.getCheckAmount()),
                        fmt.format(temp.getCashAmoumnt()),
                        fmt.format(acctIn.transList.get(i).getTransAmount()));
            }
        }
        Main.ta.setText(message);
        
        transListCheck();    }    
    
    public static void listAllServiceCharges(CheckingAccount acctIn)
    {
        JTextArea text = new JTextArea();
        
        String message ="Account: " + acctIn.getName()
                + "\nBalance: " + fmt.format(acct1.getBalance()) 
                + "\nService Charge: " + fmt.format(acct1.getServiceCharge())
                + String.format("\n\n%-15s%-15s", "Trans", "Associated") 
                + String.format("\n%-15s%-15s%15s", "Number", "TransNumber", "Amount");
        
        text.setOpaque(false);
        text.setFont(CAFont);
        text.setBorder(null);
        
        for(int i = 0; i < acctIn.transList.size(); i++)
        {
            if(acctIn.transList.get(i).getTransId() == 3)
            {
                message += String.format("\n%-15s%-15s%15s", acctIn.transList.get(i).getTransNumber(), ((ServiceCharge)acctIn.transList.get(i)).getAssociatedTransNumber(), fmt.format(acctIn.transList.get(i).getTransAmount()));
            }
        }
        Main.ta.setText(message);
        transListCheck();
    }
    
    public static void listAllAcct()
    {
        acctSort();
        String message = "Accounts on file:\n";
        for(CheckingAccount nameOut : dataStore)
            message += nameOut.name + "\n";
        message += "end.\n";
        
        Main.ta.setText(message);
    }
    
    public static void acctSort()
    {
        CheckingAccount item;
        for(int mainIndex = 0; mainIndex < dataStore.size(); mainIndex++)
        {

            if(mainIndex < (dataStore.size() - 1))
            {
                if(0 > dataStore.get(mainIndex + 1).name.compareTo(dataStore.get(mainIndex).name))
                {
                    item = dataStore.get(mainIndex + 1);
                    dataStore.remove(mainIndex + 1);
                    for(int backStep = mainIndex; 
                            backStep > -1;
                            backStep--)///place item into proper location
                    {
                        if(backStep > 0)
                        {
                            if(0 > item.name.compareTo(dataStore.get(backStep).name) && (0 <= item.name.compareTo(dataStore.get(backStep - 1).name)))
                            {
                                dataStore.add(backStep, item);
                                break;
                            }
                        }
                        else
                        {
                            dataStore.add(backStep, item);
                        }
                    }            
                }
            }
            else
            {
                //reserved
            }
        }        
    }
    
    
    
    public static void signOff(CheckingAccount acct)
    {
        String message = "Transaction: End" + "\nAccount name: " + acct.getName()
                + "\nCurrent Balance: " + fmt.format(acct.getBalance())
                + "\nTotal Service Charge: " + fmt.format(acct.getServiceCharge())
                + "\nFinal Balance: " + fmt.format((acct.getBalance() - acct.getServiceCharge()));
        
        JOptionPane.showMessageDialog(null, message);
    }
    
    public static String transIDstring(int transIDin){
        if(transIDin == 1)
            return "Check";
        if(transIDin == 2)
            return "Deposit";
        if(transIDin == 3)
            return "Service Charge";
        return "error";
    }
    
    public static void readFile()
    {
        chooseFile(1);
        if(!(cancelLoadFlag))
        {
            try
            {
                FileInputStream fis = new FileInputStream(filename);
                ObjectInputStream in = new ObjectInputStream(fis);
                dataStore = (Vector)in.readObject();
                acct1 = dataStore.get(0);
                saveFlag = true;
                in.close();
            }
            catch(ClassNotFoundException err)
            {
                System.out.println(err);
                Main.ta.setText("Error while loading file!\n");
                return;
            }
            catch(IOException err)
            {
                System.out.println(err);
                Main.ta.setText("Error while loading file!\n");
                return;
            }
            if(acct1 == dataStore.get(0))
            {
                Main.ta.setText("File loaded successfully!");
            }
            
            cancelLoadFlag = false;
        }
        
        CheckingAccount.topScoreAll();
    }
    
    public static void saveFile()
    {
        chooseFile(2);

        try
        {
            FileOutputStream fout = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(dataStore);
            saveFlag = true;
        }
        catch(IOException err)
        {
                System.out.println(err);
                Main.ta.setText("Error while saving file!\n" + err);
                saveFlag = false;
        }
    
    }

   public static void chooseFile(int ioOption) 
   {
        int status, confirm;

        String  message = "Would you like to use the current default file: \n"
                + filename;
        confirm = JOptionPane.showConfirmDialog (null, message);
        if (confirm == JOptionPane.YES_OPTION)
        {
            return;
        }
        else if (confirm == JOptionPane.CANCEL_OPTION)
        {
            Main.frame.setVisible(true);
            return;
        }
        else
        {
            JFileChooser chooser = new JFileChooser();
            
            if (ioOption == 1)
            {
                status = chooser.showOpenDialog (null);
            }
            else
            {
                status = chooser.showSaveDialog (null);
            }

            if (status == JFileChooser.APPROVE_OPTION)
            {
                File file = chooser.getSelectedFile();
                filename = file.getPath();
                Main.ta.setText(filename + " selected.");
            }
            else
            {
                chooseFile(ioOption);
                cancelLoadFlag = true;
            }
        }

   }
    
    
    
    
    
    

    
    
  
    public static String setAccountName()
    {
        String name;
        do
        {
            name = JOptionPane.showInputDialog(null, "Enter the account name:",
                    "Input", JOptionPane.QUESTION_MESSAGE);
            if (name == null)
            {
                return null;
            }
        }while (name.isEmpty());
        return name;
    }
    
    public static String setInitialBalance()
    {
        String bal;
        
        do
        {
            bal = JOptionPane.showInputDialog(null, "Enter initial balance:", "Input", JOptionPane.QUESTION_MESSAGE);
            if(bal == null)
            {
                return null;
            }
            try
            {
                double d = Double.parseDouble(bal);
            }
            catch(NumberFormatException nfe)
            {
                bal = "Invalid entry";
                System.out.println(nfe);                
            }

        }while(bal == "Invalid entry");
        return bal;
    }
    
    public int getTransListSize()
    {
        return transList.size();
    }
    
    public static boolean equals(CheckingAccount in)
    {
        boolean results = false;

        if (acct1.getName().equals(in.getName()))
        {
            if(acct1.getBalance() == in.getBalance())
            {
                if(acct1.getTransListSize() == in.getTransListSize())
                {
                    results = true;
                }
            }
        }
        return results;
    }
    
    public static void findAccount()
    {               
        findAcctStrIn = JOptionPane.showInputDialog(null, "Enter Account Name (First Last)");
        if(null != findAcctStrIn)
        {
            acctSort();                
            findAcctBinaryRecurs(0, dataStore.size() - 1);
        }
        else
        {
            Main.ta.setText("Search cancelled.");
        }
    }
    
    public static void findAcctBinaryRecurs(int front, int back)//back = dataStore.size() - 1
    {
        if(front == back)
        {
            if(0 == findAcctStrIn.compareTo(dataStore.get(front).name))
            {
                acct1 = dataStore.get(front);
                foundFlag = true;                
            }
        }
        else
        {
            if(0 > findAcctStrIn.compareTo(dataStore.get(front + (back - front)/2 + 1).name))
            {
                System.out.println("1");
                findAcctBinaryRecurs(front, front + (back - front)/2);
                back = front;
                System.out.println("2");
                return;
            }
            else if(0 < findAcctStrIn.compareTo(dataStore.get(front + (back - front)/2 + 1).name))
            {
                findAcctBinaryRecurs(front + (back - front)/2 + 1, back);
                back = front;
                return;
            }
            else
            {
                if(foundFlag == false)
                {
                    acct1 = dataStore.get(front + (back - front)/2 + 1);
                    foundFlag = true;
                                System.out.println("3");

                }
            }
        }
        if(foundFlag == false)
        {
            System.out.println("4");
            Main.ta.setText(notFound + findAcctStrIn + ".");
        }
        else
        {
            System.out.println("5");
            Main.ta.setText(acctFound + findAcctStrIn + ".");
            foundFlag = false;
        }
        System.out.println("6");
    }
    
    public static void transListCheck()
    {
        if (acct1.getTransListSize() == 0)
        {
            Main.ta.append("No transactions have been made yet!");
        }
    }
    
    public static void startGame() {
        int option;
        
        option = JOptionPane.showConfirmDialog(null, "$50.00 to win back your amount in points plus 50%!", "Would you like to make a bet?", JOptionPane.YES_NO_OPTION);
        if(option == 1)
        {
            activeBet = false;
            JOptionPane.showMessageDialog(null, "No transactions will be made", null, JOptionPane.NO_OPTION);
        }
        else
        {
            activeBet = true;
        }
        
        DartsLauncher.dartsMain();
    }
    
    public static double calculateWin(double pointsIn)
    {
        double amount;
        
        amount = pointsIn * 1.5;
        
        return amount;
    }
    
    public static void depositWin()
    {        
        //CheckingAccount.acct1.addTrans(new Deposit(1, CheckingAccount.calculateWin(DartsPanel.getPlayerScore()), CheckingAccount.acct1.getTransCount(), 0, DartsPanel.getPlayerScore()));
        //CheckingAccount.acct1.setBalance(CheckingAccount.calculateWin(DartsPanel.getPlayerScore()), 2);
    }
    
//    public static void bettingCharge()
//    {
//        CheckingAccount.acct1.addTrans(new Deposit(3, bettingCharge, CheckingAccount.acct1.getTransCount(), 0, bettingCharge));
//        CheckingAccount.acct1.setBalance(bettingCharge, 1);
//    }
    
    public int getHighScore()
    {
        return highScore;
    }
    
    public static void topScoreAll()
    {
        for (CheckingAccount temp : dataStore)
        {
            if(highestScore < temp.getHighScore())
            {
                highestScorer = temp.getName();
                highestScore = temp.getHighScore();
            }
        
            System.out.println("Top Score: " + highestScore + " | Top Scorer: " + highestScorer);
            
        }
    }
    
    public static void checkTopScore()
    {
        if(DartsPanel.getPlayerScore() > highestScore)
        {
            highestScore = (int)DartsPanel.getPlayerScore();
            highestScorer = "player";
            
        }
    }
}

