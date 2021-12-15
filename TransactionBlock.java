package DSCoinPackage;

// import DSCoinPackage.Transaction;
import HelperClasses.MerkleTree;
import HelperClasses.CRF;

public class TransactionBlock {

    public Transaction[] trarray;
    public TransactionBlock previous;
    public MerkleTree Tree;
    public String trsummary;
    public String nonce;
    public String dgst;

    TransactionBlock(Transaction[] t) {
       Transaction[] ta=new Transaction[t.length];
       for(int i=0;i<t.length;i++){
           ta[i]=t[i];
       }
       this.trarray=ta;
       this.previous=null;
       MerkleTree m=new MerkleTree();
       m.Build(ta);
       this.Tree=m;
       this.trsummary=m.Build(t);
       this.dgst=null;
    }

    public boolean checkTransaction (Transaction t) {
        //might need to be modified.coinsrc equal to null needs to be thought.
        boolean a=false;
        if(t.coinsrc_block!=null){
            for(int i=0;i<t.coinsrc_block.trarray.length;i++){
                if(t.coinsrc_block.trarray[i].coinID.equals(t.coinID) && t.Source==t.coinsrc_block.trarray[i].Destination){
                    a=true;
                    break;
                }
            }
        }
        else if(t.coinsrc_block==null){
            return true;
        }

        TransactionBlock tb=this.previous;
        while(tb!=t.coinsrc_block){
            for(int j=0;j<tb.trarray.length;j++){
                if(tb.trarray[j].coinID.equals(t.coinID)){
                    a=false;
                    break;
                }
            }if(a==false){
                break;
            }
            else {
                tb=tb.previous;
            }
        }


        return a;
    }
    public boolean checkbyminer(Transaction t){
        boolean a=false;
        if(t.coinsrc_block!=null){
            for(int i=0;i<t.coinsrc_block.trarray.length;i++){
                if(t.coinsrc_block.trarray[i].coinID.equals(t.coinID) && t.Source==t.coinsrc_block.trarray[i].Destination){
                    a=true;
                    break;
                }
            }
        }
        else if(t.coinsrc_block==null ){
            return true;
        }

        TransactionBlock tb=this;
        while(tb!=t.coinsrc_block){
            for(int j=0;j<tb.trarray.length;j++){
                if(tb.trarray[j].coinID.equals(t.coinID)){
                    a=false;
                    break;
                }
            }if(a==false){
                break;
            }
            else {
                tb=tb.previous;
            }
        }


        return a;
    }

}

