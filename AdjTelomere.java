package miller;
public class AdjTelomere {

        public String left;         // 邻接的左半边
        public String right;        // 邻接的右半边
        public String adjType;    // 表示邻接的类型，是普通邻接还是端粒
        public int chrosomePosition;
        public AdjTelomere(String left, String right, String adjType, int chrosomePosition)
        {
            this.left = left;
            this.right = right;
            this.adjType = adjType;
            this.chrosomePosition=chrosomePosition;
        }
        public AdjTelomere(String left, String right, int chrosomePosition)
        {
            this.left = left;
            this.right = right;
            this.chrosomePosition=chrosomePosition;
        }
//        public void setP(double p)
//        {
//            this.p = p;
//        }

        public String toString()
        {
            return this.left + "," + this.right + "," + adjType;
        }
        public String outputAdj(){return this.left+","+this.right;}
        public String toString1()
        {
            return this.left + "," + adjType;
        }
        public boolean EqualAdj(AdjTelomere a)
        {
            if (adjType.equals(a.adjType)&&this.adjType.equals("Telomere"))
            {
                if (left.equals(a.left))
                {
                    return true;
                }
                else
                    return false;
            }
            if (adjType.equals(a.adjType)&&this.adjType.equals("adj"))
            {
                if (left.equals(a.left)&&right.equals(a.right))
                {
                    return true;
                }
                else
                    return false;
            }
            else
                return false;
        }
        //因为后续会将两个基因组产生的邻接进行取并集，用HashSet进行装填，当我们有(1t,2h)和（2t,1h)两个邻接后，我们只存储其中一个，所以需要重写equal()方法和HashCode()方法
        @Override
        public boolean equals(Object o)
        {
            if(this==o) return true;
            if(o==null ||getClass()!=o.getClass()) return false;
            AdjTelomere a=(AdjTelomere) o;
            if(adjType.equals(a.adjType)&&adjType.equals("adj"))
            {
                if((left.equals(a.left)&&right.equals(a.right) )|| (left.equals(a.right)&&right.equals(a.left)) )
                {
                        return true;
                }
                else
                    return false;
            }
            else if(adjType.equals(a.adjType)&&adjType.equals("Telomere"))
            {
                if(left.equals(a.left))
                {
                        return true;
                }
            }
            return false;
        }
    @Override
    public int hashCode()  //返回左键和右键的哈希地址，更唯一
    {
        return left.hashCode()+right.hashCode();
    }
}
//    public boolean contains(AdjOrTelomere adj)
//    {
//        return right.equals(adj.right) && left.equals(adj.left);
//    }
