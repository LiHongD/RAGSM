package miller;

class AdjOrTelomere
{
    public int left;
    public int right;
    public String fromWhere;
    public int num;// 给邻接编号

    public  AdjOrTelomere(int left, int right, String fromWhere)
    {
        this.left = left;
        this.right = right;
        this.fromWhere = fromWhere;
    }

    public String toString()
    {
        return this.left + " " + this.right;
    }
}