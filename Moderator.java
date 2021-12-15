package DSCoinPackage;
import HelperClasses.Pair;


public class Moderator
{

  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
        for(int i=0;i<coinCount;i++){
            Transaction transaction=new Transaction();
            transaction.coinID=Integer.toString(100000+i);
            transaction.Destination=DSObj.memberlist[i%(DSObj.memberlist.length)];
            Members moderator=new Members();
            moderator.UID="Moderator";
            transaction.Source=moderator;
            transaction.coinsrc_block=null;
            DSObj.pendingTransactions.AddTransactions(transaction);
        }
        int n=coinCount/DSObj.bChain.tr_count;
        for(int k=0;k<n;k++){
            Transaction[] transaaray=new Transaction[DSObj.bChain.tr_count];
            for(int i=0;i<transaaray.length;i++){
                try {
                    transaaray[i]=DSObj.pendingTransactions.RemoveTransaction();
                } catch (EmptyQueueException e) {
                }
            }
            TransactionBlock tb=new TransactionBlock(transaaray);
            DSObj.bChain.InsertBlock_Honest(tb);
            for(int i=0;i<tb.trarray.length;i++){
                Pair <String,TransactionBlock> p=new Pair<>(tb.trarray[i].coinID,tb);
                tb.trarray[i].Destination.mycoins.add(p);
            }
        }DSObj.latestCoinID=Integer.toString(100000+coinCount-1);
    }

    public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
        for(int i=0;i<coinCount;i++){
            Transaction transaction=new Transaction();
            transaction.coinID=Integer.toString(100000+i);
            transaction.Destination=DSObj.memberlist[i%(DSObj.memberlist.length)];
            Members moderator=new Members();
            moderator.UID="Moderator";
            transaction.Source=moderator;
            transaction.coinsrc_block=null;
            DSObj.pendingTransactions.AddTransactions(transaction);
        }
        int n=coinCount/DSObj.bChain.tr_count;
        for(int k=0;k<n;k++){
            Transaction[] transaaray=new Transaction[DSObj.bChain.tr_count];
            for(int i=0;i<transaaray.length;i++){
                try {
                    transaaray[i]=DSObj.pendingTransactions.RemoveTransaction();
                } catch (EmptyQueueException e) {
                }
            }
            TransactionBlock tb=new TransactionBlock(transaaray);
            DSObj.bChain.InsertBlock_Malicious(tb);
            for(int i=0;i<tb.trarray.length;i++){
                Pair <String,TransactionBlock> p=new Pair<>(tb.trarray[i].coinID,tb);
                tb.trarray[i].Destination.mycoins.add(p);
            }
        }DSObj.latestCoinID=Integer.toString(100000+coinCount-1);
    }
}
