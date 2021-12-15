package DSCoinPackage;

import HelperClasses.CRF;
import HelperClasses.MerkleTree;

import java.util.ArrayList;

public class BlockChain_Malicious {

    public int tr_count;
    public static final String start_string = "DSCoin";
    public TransactionBlock[] lastBlocksList=new TransactionBlock[100];
    public ArrayList<TransactionBlock> tal=new ArrayList<>(0);
    //tal is the list form of lastblocklists.

    public static boolean checkTransactionBlock(TransactionBlock tB) {
        CRF a = new CRF(64);
        boolean b = false;
        if (tB.previous == null) {
            String c = a.Fn(start_string + "#" + tB.trsummary + "#" + tB.nonce);
            if (!(tB.dgst.startsWith("0000") && tB.dgst.equals(c))) {
                return false;
            }
        }
        if (tB.previous != null) {
            String str = a.Fn(tB.previous.dgst + "#" + tB.trsummary + "#" + tB.nonce);
            if (!(tB.dgst.startsWith("0000") && tB.dgst.equals(str))) {
                return false;
            }
        }
        MerkleTree m = new MerkleTree();
        String str1 = m.Build(tB.trarray);
        if (!tB.trsummary.equals(str1)) {
            return false;
        }
        for (int i = 0; i < tB.trarray.length; i++) {
            if (!tB.checkTransaction(tB.trarray[i])) {
                return false;
            }
        }
        return true;
    }

    public TransactionBlock FindLongestValidChain() {
         int c=0;
         TransactionBlock e=null;
         for(int i=0;i< tal.size();i++){
             int a=0;
             TransactionBlock t=tal.get(i);
             TransactionBlock d=tal.get(i);
             while(t!=null){
                 if(checkTransactionBlock(t)){
                     a++;
                 }
                 else if(!checkTransactionBlock(t)){
                     a=0;
                     d=t.previous;
                 }
                 t=t.previous;
             }if(a>c){
                 c=a;
                 e=d;
             }
         }
        return e;
    }

    public void InsertBlock_Malicious(TransactionBlock newBlock) {
        CRF a = new CRF(64);
        if (this.tal.size() == 0) {
            tal.add(newBlock);
            lastBlocksList=tal.toArray(lastBlocksList);
            for (int i = 1000000001; i <= 2147483647; i++) {
                newBlock.nonce = Integer.toString(i);
                newBlock.dgst = a.Fn(start_string + "#" + newBlock.trsummary + "#" + newBlock.nonce);
                if (newBlock.dgst.startsWith("0000")) {
                    break;
                }
            }
        } else {
            TransactionBlock tb = FindLongestValidChain();
            newBlock.previous = tb;
            boolean b = false;
            for (int i = 0; i < tal.size(); i++) {
                if (tal.get(i) == tb) {
                    tal.set(i,newBlock);
                    lastBlocksList=tal.toArray(lastBlocksList);
                    b = true;
                    break;
                }
            }
            if (!b) {
                tal.add(newBlock);
                lastBlocksList=tal.toArray(lastBlocksList);
            }
            for (int i = 1000000001; i <= 2147483647; i++) {
                newBlock.nonce = Integer.toString(i);
                newBlock.dgst = a.Fn(newBlock.previous.dgst + "#" + newBlock.trsummary + "#" + newBlock.nonce);
                if (newBlock.dgst.startsWith("0000")) {
                    break;
                }
            }
        }
    }


}