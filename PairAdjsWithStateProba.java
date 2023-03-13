package miller;

public class PairAdjsWithStateProba
{
    public AdjOrTelomere adj_1;
    public AdjOrTelomere adj_2;

    public double p_00;
    public double p_01;
    public double p_10;
    public double p_11;
    public double p_11d;  

    public PairAdjsWithStateProba(AdjOrTelomere adj_1, AdjOrTelomere adj_2)
    {
        this.adj_1 = adj_1;
        this.adj_2 = adj_2;
        p_00 = 0;
        p_01 = 0;
        p_10 = 0;
        p_11 = 0;
        p_11d = 0;
    }
}
