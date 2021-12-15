package DSCoinPackage;

// import DSCoinPackage.Transaction;
// import DSCoinPackage.EmptyQueueException;

public class TransactionQueue {

    public Transaction firstTransaction;
    public Transaction lastTransaction;
    public int numTransactions;

    public void AddTransactions (Transaction transaction) {
        if (numTransactions==0){
            firstTransaction=transaction;
            lastTransaction=transaction;
            numTransactions=1;
        }
        else{
            lastTransaction.next=transaction;
            lastTransaction=transaction;
            numTransactions++;
        }
    }

    public Transaction RemoveTransaction () throws EmptyQueueException {
        Transaction q=new Transaction();
        if(numTransactions!=0){
            if(firstTransaction==lastTransaction){
                q=firstTransaction;
                firstTransaction=null;
                lastTransaction=null;
                numTransactions=0;}
            else {
                q=firstTransaction;
                firstTransaction=firstTransaction.next;
                numTransactions--;
            }
        }
        else {throw new EmptyQueueException();}
        return q;
    }

    public int size() {
        return numTransactions;
    }
}
