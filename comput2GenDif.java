package miller;

import ForLiHongDi.AdjTelomere;
import ForLiHongDi.Chrosome;
import ForLiHongDi.Functions;

import java.util.HashSet;
import java.util.List;

public class comput2GenDif {
    public static void main(String a[])
    {
        // 输入两个文件，一个是原始文件，一个是模拟的基因组文件。
        String path1="C://Users/li/Desktop/newdistance/src/qqq/root_chromosomes.txt";
        String path2="C://Users/li/Desktop/newdistance/relelatefile/genralGenome";
//        String path1="C://Users/li/Desktop/newdistance/relelatefile/test1";
//        String path2="C://Users/li/Desktop/newdistance/relelatefile/test2";
        List<String> genome1=FileUtils.readFile(path1);
        List<String> genome2=FileUtils.readFile(path2);
        List<Chrosome> genome1ch=Functions.getChromosomes(genome1);
        List<Chrosome> genome2ch=Functions.getChromosomes(genome2);
        List<AdjTelomere> g1Adj=Functions.getAdjs(genome1ch);
        List<AdjTelomere> g2Adj=Functions.getAdjs(genome2ch);
        computedif(g1Adj,g2Adj);
    }
    public static void computedif(List<AdjTelomere> adj1,List<AdjTelomere> adj2)
    {
        HashSet<AdjTelomere> g1=new HashSet<                                                                                                                                                                                                                                                                                                                                >();
        HashSet<AdjTelomere> g2=new HashSet<>();
        HashSet<AdjTelomere> unions=new HashSet<>();
        int numOfUnion=0,numOfInter=0;
        g1.addAll(adj1);
        g1.retainAll(adj2);
        g2.addAll(adj1);
        g2.addAll(adj2);
        System.out.println(g1.size()+" "+g2.size());
        // 1 2 3  1t,2h
        // 1,2
        double value=(double)g1.size()/g2.size();
        System.out.println("test accuarcy is:"+value);
    }

}
