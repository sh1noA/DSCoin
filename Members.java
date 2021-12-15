package DSCoinPackage;

import java.lang.reflect.Array;
import java.util.*;
import HelperClasses.CRF;
import HelperClasses.Pair;
import HelperClasses.TreeNode;
// import DSCoinPackage.Transaction;
// import DSCoinPackage.TransactionBlock;
// import DSCoinPackage.DSCoin_Honest;
// import DSCoinPackage.DSCoin_Malicious;
// import DSCoinPackage.MissingTransactionException;

public class Members
{

    public String UID;
    public List<Pair<String, TransactionBlock>> mycoins;
    public Transaction[] in_process_trans=new Transaction[100];
    public ArrayList<Transaction> ipt=new ArrayList<>();

    public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
        Pair<String, TransactionBlock> p=this.mycoins.get(0);
        this.mycoins.remove(0);
        Transaction t=new Transaction();
        t.coinID=p.get_first();
        t.coinsrc_block=p.get_second();
        t.Source=this;
        for(int i=0;i<DSobj.memberlist.length;i++){
            if(DSobj.memberlist[i].UID.equals(destUID)){
                t.Destination=DSobj.memberlist[i];
                break;
            }
        }
        ipt.add(t);
        this.in_process_trans=ipt.toArray(this.in_process_trans);
        DSobj.pendingTransactions.AddTransactions(t);

    }

 public void initiateCoinsend(String destUID, DSCoin_Malicious DSobj) {
        Pair<String, TransactionBlock> p=this.mycoins.get(0);
        this.mycoins.remove(0);
        Transaction t=new Transaction();
        t.coinID=p.get_first();
        t.coinsrc_block=p.get_second();
        t.Source=this;
        for(int i=0;i<DSobj.memberlist.length;i++){
            if(DSobj.memberlist[i].UID.equals(destUID)){
                t.Destination=DSobj.memberlist[i];
                break;
            }
        }
        ipt.add(t);
        this.in_process_trans=ipt.toArray(this.in_process_trans);
        DSobj.pendingTransactions.AddTransactions(t);

    }

    public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
        TransactionBlock tB=DSObj.bChain.lastBlock;
        int j=-1;
        while(tB!=null){
            for(int i=0;i<tB.trarray.length;i++){
                if(tB.trarray[i]==tobj ){
                    j=i;
                    break;
                }
            } if(j!=-1){break;}
            else {
                tB=tB.previous;
            }
        }
        if(tB==null){
            throw new MissingTransactionException();
        }
        else{
            TreeNode curr1=tB.Tree.rootnode;
            int a=0;
            int b=tB.trarray.length-1;
            while(a!=b){int k=(a+b)/2;
                if(j<=k){
                    curr1=curr1.left;
                    b=k;
                }else{
                    curr1=curr1.right;
                    a=k+1;
                }
            }
            ArrayList<Pair<String,String >> q1=new ArrayList<>();
            TreeNode curr2=curr1.parent;
            while(curr2!=null){
                Pair<String,String> l=new Pair<>(curr2.left.val,curr2.right.val);
                q1.add(l);
                curr2=curr2.parent;}
            Pair<String,String> l=new Pair<>(tB.Tree.rootnode.val,null);
            q1.add(l);
            List <Pair<String,String >>  q2=new ArrayList<>();
            if(tB.previous==null){
                Pair<String,String> f1=new Pair<>("DSCoin",null);
                Pair<String ,String> f2=new Pair<>(tB.dgst, "DSCoin"+"#"+tB.trsummary+"#"+tB.nonce);
                q2.add(f1);
                q2.add(f2);
            }
            else{
                Pair<String,String> f3=new Pair<>(tB.previous.dgst,null);
                Pair<String ,String> f4=new Pair<>(tB.dgst, tB.previous.dgst+"#"+tB.trsummary+"#"+tB.nonce);
                q2.add(f3);
                q2.add(f4);
            }
           TransactionBlock t=DSObj.bChain.lastBlock;
            ArrayList<TransactionBlock> store=new ArrayList<>();
            while(t!=tB){
                store.add(t);
                t=t.previous;
            }
            Collections.reverse(store);
            for(int z=0;z<store.size();z++){
                Pair<String,String> v=new Pair<>(store.get(z).dgst,store.get(z).previous.dgst +"#"+store.get(z).trsummary+"#"+store.get(z).nonce);
                q2.add(v);
        }
            Pair<List<Pair<String, String>>, List<Pair<String, String>>> pfinal=new Pair<>(q1,q2);
            ipt.remove(tobj);
            this.in_process_trans=ipt.toArray(this.in_process_trans);
            Pair<String,TransactionBlock> r=new Pair<>(tobj.coinID,tB);
            tobj.Destination.mycoins.add(r);
            Collections.sort(tobj.Destination.mycoins, new Comparator<Pair<String, TransactionBlock>>() {
                @Override
                public int compare(Pair<String, TransactionBlock> o1, Pair<String, TransactionBlock> o2) {
                    if (Integer.parseInt(o1.get_first())>(Integer.parseInt(o2.get_first()))){
                        return 1;
                    }else return -1;
                }
            });

            return pfinal;

        }
    }
     public boolean c(Transaction t,ArrayList<Transaction> a){
         if (a.size()==0) {
             return true;
         }
        for(int i=0;i<a.size();i++){
            if(a.get(i).coinID.equals(t.coinID)){
                return false;
            }
        }
            return true;
     }

    public void MineCoin(DSCoin_Honest DSObj)  {
        ArrayList<Transaction> tarr= new ArrayList<>();
        while(tarr.size()< (DSObj.bChain.tr_count-1)){
            Transaction i= null;
            try {
                i = DSObj.pendingTransactions.RemoveTransaction();
            } catch (EmptyQueueException e) {
                e.printStackTrace();
            }
            if (c(i,tarr) && DSObj.bChain.lastBlock.checkbyminer(i)){
                tarr.add(i);
            }
        }
        Transaction minerRewardTransaction= new Transaction();
        minerRewardTransaction.coinID= Integer.toString(Integer.parseInt(DSObj.latestCoinID)+1);
        minerRewardTransaction.Source=null;
        minerRewardTransaction.Destination=this;
        minerRewardTransaction.coinsrc_block=null;
        tarr.add(minerRewardTransaction);
        Transaction[] transactions= new Transaction[tarr.size()];
        transactions=tarr.toArray(transactions);
        TransactionBlock tB= new TransactionBlock(transactions);
        DSObj.bChain.InsertBlock_Honest(tB);
        Pair<String, TransactionBlock> p= new Pair<>(minerRewardTransaction.coinID,tB);
        DSObj.latestCoinID=minerRewardTransaction.coinID;
        this.mycoins.add(p);
        //might need to be modified to check for valid transactions

    }

    public void MineCoin(DSCoin_Malicious DSObj)  {
        ArrayList<Transaction> tarr= new ArrayList<>();
        while(tarr.size()< (DSObj.bChain.tr_count-1)){
            Transaction i= null;
            try {
                i = DSObj.pendingTransactions.RemoveTransaction();
            } catch (EmptyQueueException e) {
                e.printStackTrace();
            }
            if (c(i,tarr) && DSObj.bChain.FindLongestValidChain().checkbyminer(i)){
                tarr.add(i);
            }
        }
        Transaction minerRewardTransaction= new Transaction();
        minerRewardTransaction.coinID= Integer.toString(Integer.parseInt(DSObj.latestCoinID)+1);
        minerRewardTransaction.Source=null;
        minerRewardTransaction.Destination=this;
        minerRewardTransaction.coinsrc_block=null;
        tarr.add(minerRewardTransaction);
        Transaction[] transactions= new Transaction[tarr.size()];
        transactions=tarr.toArray(transactions);
        TransactionBlock tB= new TransactionBlock(transactions);
        DSObj.bChain.InsertBlock_Malicious(tB);
        Pair<String, TransactionBlock> p= new Pair<>(minerRewardTransaction.coinID,tB);
        DSObj.latestCoinID=minerRewardTransaction.coinID;
        this.mycoins.add(p);
}
}

