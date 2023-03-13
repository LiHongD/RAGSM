package miller;
public class AdjsPair {
    public AdjTelomere adj1;
    public AdjTelomere adj2;
//    public  String AdjsPairState;
//    AdjsPairState 表示邻接对的状态，一共有五种类型，分别是（1,0）、（1,1）、（0,0）、（0,1）、（1,1）d、
    public AdjsPair(AdjTelomere adj1, AdjTelomere adj2)
    {
         this.adj1=adj1;
         this.adj2=adj2;
//         this.AdjsPairState=AdjsPairState;
    }
    public String toshow()
    {
        String s="{"+adj1.outputAdj()+"}"+","+"{"+adj2.outputAdj()+"}";
        return s;
    }

}
