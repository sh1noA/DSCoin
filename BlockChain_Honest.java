package DSCoinPackage;

// import DSCoinPackage.TransactionBlock;

import HelperClasses.CRF;

public class BlockChain_Honest {

    public int tr_count;
    public static final String start_string = "DSCoin";
    public TransactionBlock lastBlock;

    public void InsertBlock_Honest (TransactionBlock newBlock) {
             CRF a=new CRF(64);
             if(lastBlock==null){
                 lastBlock=newBlock;
                 for(int i=1000000001;i<=2147483647;i++){
                     lastBlock.nonce=Integer.toString(i);
                     lastBlock.dgst=a.Fn(start_string+"#"+newBlock.trsummary+"#"+newBlock.nonce);
                     if(lastBlock.dgst.startsWith("0000")){
                         break;
                     }
                 }
             }else{
                 newBlock.previous=lastBlock;
                 lastBlock=newBlock;
                 for (int i = 1000000001; i <= 2147483647; i++) {
                     newBlock.nonce = Integer.toString(i);
                     newBlock.dgst = a.Fn(newBlock.previous.dgst + "#" + newBlock.trsummary + "#" + newBlock.nonce);
                     if (newBlock.dgst.startsWith("0000")) {
                         break;     }
                 }

             }
    }
}

