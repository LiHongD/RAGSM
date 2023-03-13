package miller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Miller
{
    public static void main(String[] args)
    {
//        String tree_path ="C://Users/li/Desktop/概率/RAGPM/data/All/10_leaves/newick_Tree_for_RAGPM";
//        String leafGenomes_path = "C://Users/li/Desktop/概率/RAGPM/data/All/10_leaves/50_genes/1_chromosome/sample_1/leafGenomes_for_RAGPM";
        String tree_path ="/home/zhaoyujie/data/All/3_leaves/newick_Tree_for_RAGPM";
	String leafGenomes_path = "/home/zhaoyujie/data/All/3_leaves/50_genes/2_chromosome/sample_1/leafGenomes_for_RAGPM";
        String outPath = "/home/lihongdi/RAGPM_files";

        String treeStr = FileUtils.readFile(tree_path).get(0);
        List<List<String>> leafGenomes = new ArrayList<List<String>>();
        List<List<AdjTelomere>> allGenomeAdj=new ArrayList<>();
        double [][]finalPbMatrix=new double[5][5];
        // 1 2 3
        // a b c a,
        getLeafGenomes(leafGenomes,leafGenomes_path);
        getAdj(allGenomeAdj,leafGenomes);
        List<AdjTelomere> unionAdjs=UnionAdjs(allGenomeAdj);
        List<AdjsPair> unionPairs=new ArrayList<>();
        UnionPairs(unionPairs,unionAdjs);
        List<Double[][]> AllAdjpbs =computeEveryAdj(allGenomeAdj, unionPairs);
        finalPb(finalPbMatrix,AllAdjpbs);
        Constant.getP(finalPbMatrix);
//        for(int i=0;i<5;i++)
//        {
//            for(int j=0;j<5;j++)
//            {
//                System.out.print(finalPbMatrix[i][j]+"|");
//            }
//            System.out.println();
//        }
        Tree.construct(treeStr, leafGenomes);
        Tree.showTheTree(outPath);
        for(int i=0; i<Tree.tree.size(); i++)
        {
            TreeNode tn = Tree.tree.get(i);
            if(!(tn instanceof LeafNode))
            {
                SolveBranchNode.construct(tn, outPath, i);
                SolveBranchNode.solve1();
                SolveBranchNode.back();
            }
        }
        Tree.back();
    }

    public static void getLeafGenomes(List<List<String>> leafGenomes, String leafGenomesPath)
    {
        FileReader fileReader;
        BufferedReader bufferedReader;
        String temp;
        try
        {
            fileReader = new FileReader(leafGenomesPath);
            bufferedReader = new BufferedReader(fileReader);
            while((temp=bufferedReader.readLine())!=null)
            {
                if(temp.length()>0 && !temp.equals("\r") && !temp.equals("\n"))
                {
                    if(temp.charAt(0)=='>')
                    {
                        List<String> current = new ArrayList<String>();
                        current.add(temp);
                        leafGenomes.add(current);
                    }
                    else
                    {
                        leafGenomes.get(leafGenomes.size()-1).add(temp);
                    }
                }
            }

            bufferedReader.close();
            fileReader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void getAdj(List<List<AdjTelomere> >allAdjs,List<List<String>> allgenomes)
    {
        for(int i=0;i<allgenomes.size();i++)
        {
            List<AdjTelomere> thisGenome=new ArrayList<>();
            for(int j = 0; j< allgenomes.get(i).size(); j++)
            {
                StringBuilder adjResultStr = new StringBuilder();// StringBuffer 是线程安全的，StringBuilder不是线程安全的，所以性能略高
                List<String> resultList = new ArrayList<String>();
                String currentChromosome = allgenomes.get(i).get(j);        // 取得当前染色体
                String[] genesInCurrentChromosome = currentChromosome.split(" "); //将当前染色体上的基因提出来
                int len = genesInCurrentChromosome.length;            // 当前染色体包含的基因数
                for (int k = 0; k < len-1; k++)   //对染色体中基因进行分析处理,不包括末尾$符号
                {
                   String currentGene = genesInCurrentChromosome[k]; // 当前基因
                   if (k == 0) // 当前被处理的是染色体上的第一个基因
                   {
                       if (currentGene.substring(0, 1).equals("-")) // 如果是反向的
                       {
                          String geneName = currentGene.substring(1);
                          adjResultStr.append("{"+geneName + "t" + "}" + "{" + geneName + "h");
                       }
                       else                                      //如果是正向的
                       {
                          adjResultStr.append("{" + currentGene + "h" + "}" + "{" + currentGene + "t");
                       }
                   }
                   else if (k == len - 2) // 当前处理的是染色体上的最后一个基因
                   {
                        if (currentGene.substring(0, 1).equals("-")) // 如果是反向的
                        {
                            String geneName = currentGene.substring(1);
                            adjResultStr.append("," + geneName + "t" + "}" + "{" + geneName + "h" +  "}");
                        }
                        else                 //如果是正向的
                        {
                            adjResultStr.append("," + currentGene + "h" + "}" + "{" + currentGene + "t" +"}");
                        }
                   }
                   else // 当前处理的是染色体上处于中间位置的基因
                   {
                        if (currentGene.substring(0, 1).equals("-")) // 如果是反向的
                        {
                            String geneName = currentGene.substring(1, currentGene.length());
                            adjResultStr.append("," + geneName + "t" + "}" + "{" + geneName + "h");
                        }
                        else                 //如果是正向的
                        {
                            adjResultStr.append("," + currentGene + "h" + "}" + "{" + currentGene + "t");
                        }
                   }
                }
                int m = -1, n = -1;
                int lens = adjResultStr.length();
                for(int t=0; t<lens; t++)  //输出每一个{ }中的内容
                {
                   if(adjResultStr.charAt(t)=='{')     {  m = t; }
                   if(adjResultStr.charAt(t)=='}')     {  n = t; }
                   if(m!=-1 && n!=-1)
                   {
                       resultList.add(adjResultStr.substring(m+1, n));
                       m = -1;
                       n = -1;
                   }
                }
                for(int k=0; k<resultList.size(); k++)   // 向thisGenome 列表中添加邻接
                {
                   String currentAdj = resultList.get(k);
                   if (!currentAdj.contains(",")) //如果邻接的长度只有1，说明是个端粒邻接，比如1t、2h之类。
                   {
                       String left=currentAdj;
                       String right="N";
                       thisGenome.add(new AdjTelomere(left,right,"Telomere",j)); // AdjsOfNode是存放该结点的邻接(端粒)的列表
                   }
                   else
                   {
                       String[] temp = currentAdj.split(",");
                       String left = temp[0];
                       String right = temp[1];
                       thisGenome.add(new AdjTelomere(left, right,"adj",i)); // AdjsOfNode是存放该结点的邻接(端粒)的列表,i代表
                   }
                }
            }
            allAdjs.add(thisGenome);
        }
    }
    public static List<AdjTelomere> UnionAdjs(List<List<AdjTelomere>> a)
    {
        List<AdjTelomere> unionedAdj=new ArrayList<>();
        unionedAdj.addAll(a.get(0));
        HashSet<AdjTelomere> unionAdjSet=new HashSet<>();
        unionAdjSet.addAll(a.get(0));
        for(int i=1;i<a.size();i++)
        {
            for(AdjTelomere adj:a.get(i))
            {
                if(!unionAdjSet.contains(adj))
                {
                    unionAdjSet.add(adj);
                    unionedAdj.add(adj);
                }
            }
        }
        return unionedAdj;
    }
    public static void UnionPairs(List<AdjsPair> adjsPairList,List<AdjTelomere> curAdj)
    {
        int adjsLength=curAdj.size();
        for (int i = 0; i < adjsLength-1; ++i)
        {
            for (int j=i+1; j<adjsLength ;++j)
            {
                adjsPairList.add(new AdjsPair(curAdj.get(i),curAdj.get(j)));
            }
        }
    }
    public static List<Double[][]> computeEveryAdj(List< List<AdjTelomere>> Genome, List<AdjsPair> unionAdjPair)
    {
        //此函数计算邻接对集合中每一个邻接对的状态转移概率。
//      for(集合中任意邻接对)
//     {
//         for(叶子结点基因组A）
//         {
//             for(其他叶子结点基因组B)
//                 计算邻接对在A中的状态SA、在B中的状态SB
//                 SAtoSB++;
//        }
//     }
//      m:一状态的出现次数
//      n:总次数
//      一转移状态的概率:m/n
        List< Double[][]> AllAdjPb=new ArrayList<>();
        for(AdjsPair pair:unionAdjPair)
        {
            Double [][]pbMatrix=new Double [5][5];
            for(int i=0;i<5;i++)
            {
                for(int j=0;j<5;j++)
                {
                    pbMatrix[i][j]=0.0;
                }
            }

            List<String> stateList=new ArrayList<>();
            for(int i=0;i<Genome.size();i++)
            {
                for(int j=i+1;j< Genome.size();j++)
                {
                    List<Integer> num=new ArrayList<>();
                    List<AdjTelomere> g1=Genome.get(i);
                    List<AdjTelomere> g2=Genome.get(j);
                    HashMap<AdjTelomere, Integer> hmOfG1=createHashMap(g1);// 基因组1的邻接 哈希表，为判断邻接在此基因组的存在状态
                    HashMap<AdjTelomere, Integer> hmOfG2= createHashMap(g2);// 基因组2的邻接 哈希表，为判断邻接在此基因组的存在状态
                    boolean g1HaveLeftAdj = hmOfG1.containsKey(pair.adj1);
                    boolean g1HaveRightAdj = hmOfG1.containsKey(pair.adj2);
                    boolean g2HaveLeftAdj = hmOfG2.containsKey(pair.adj1);
                    boolean g2HaveRightAdj = hmOfG2.containsKey(pair.adj2);
                    boolean isSameBothAdjTypeIng1 = hmOfG1.get(pair.adj1)==hmOfG1.get(pair.adj2);
                    boolean isSameBothAdjTypeIng2 = hmOfG2.get(pair.adj1)==hmOfG2.get(pair.adj2);
                    String stateof1=Functions.transFormBoolean(g1HaveLeftAdj)+Functions.transFormBoolean(g1HaveRightAdj);
                    String stateof2=Functions.transFormBoolean(g2HaveLeftAdj)+Functions.transFormBoolean(g2HaveRightAdj);
                    if(g1HaveLeftAdj && g1HaveRightAdj && !isSameBothAdjTypeIng1)
                    {
                        stateof1=stateof1+"d";
                    }
                    if(g2HaveLeftAdj && g2HaveRightAdj && !isSameBothAdjTypeIng2)
                    {
                        stateof2=stateof2+"d";
                    }
                    String finalString=stateof1+"to"+stateof2;
                    stateList.add(finalString);
                }
            }
            for(String s:stateList)
            {
                switch (s)
                {
                    case "00to00":
                        pbMatrix[0][0]++;
                        break;
                    case "00to01":
                        pbMatrix[0][1]++;
                        pbMatrix[1][0]++;
                        break;
                    case "00to10":
                        pbMatrix[0][2]++;
                        pbMatrix[2][0]++;
                        break;
                    case "00to11":
                        pbMatrix[0][3]++;
                        pbMatrix[3][0]++;
                        break;
                    case "00to11d":
                        pbMatrix[0][4]++;
                        pbMatrix[4][0]++;
                        break;
                    case "01to00":
                        pbMatrix[1][0]++;
                        pbMatrix[0][1]++;
                        break;
                    case "01to01":
                        pbMatrix[1][1]++;
                        break;
                    case "01to10":
                        pbMatrix[1][2]++;
                        pbMatrix[2][1]++;
                        break;
                    case "01to11":
                        pbMatrix[1][3]++;
                        pbMatrix[3][1]++;
                        break;
                    case "01to11d":
                        pbMatrix[1][4]++;
                        pbMatrix[4][1]++;
                        break;
                    case "10to00":
                        pbMatrix[2][0]++;
                        pbMatrix[0][2]++;
                        break;
                    case "10to01":
                        pbMatrix[2][1]++;
                        pbMatrix[1][2]++;
                        break;
                    case "10to10":
                        pbMatrix[2][2]++;
                        break;
                    case "10to11":
                        pbMatrix[2][3]++;
                        pbMatrix[3][2]++;
                        break;
                    case "10to11d":
                        pbMatrix[2][4]++;
                        pbMatrix[4][2]++;
                        break;
                    case "11to00":
                        pbMatrix[3][0]++;
                        pbMatrix[0][3]++;
                        break;
                    case "11to01":
                        pbMatrix[3][1]++;
                        pbMatrix[1][3]++;
                        break;
                    case "11to10":
                        pbMatrix[3][2]++;
                        pbMatrix[2][3]++;
                        break;
                    case "11to11":
                        pbMatrix[3][3]++;
                        break;
                    case "11to11d":
                        pbMatrix[3][4]++;
                        pbMatrix[4][3]++;
                        break;
                    case "11dto00":
                        pbMatrix[4][0]++;
                        pbMatrix[0][4]++;
                        break;
                    case "11dto01":
                        pbMatrix[4][1]++;
                        pbMatrix[1][4]++;
                        break;
                    case "11dto10":
                        pbMatrix[4][2]++;
                        pbMatrix[2][4]++;
                        break;
                    case "11dto11":
                        pbMatrix[4][3]++;
                        pbMatrix[3][4]++;
                        break;
                    case "11dto11d":
                        pbMatrix[4][4]++;
                        break;
                }
            }
            for(int i=0;i<5;i++)
            {
                for(int j=0;j<5;j++)
                {
                    pbMatrix[i][j]=pbMatrix[i][j]/ stateList.size()*2;
//                    System.out.println(pbMatrix[i][j]);
                }
            }
            AllAdjPb.add(pbMatrix);
        }
        return AllAdjPb;
    }
    public static void finalPb(double [][]finalMatrix,List<Double[][]> PairPbs)
    {
        int pairSize=PairPbs.size();
        for(int j=0;j<5;j++)
        {
            for(int k=0;k<5;k++)
            {
                double value=0;
                for(int i=0;i<pairSize;i++)
                {
                    value+=PairPbs.get(i)[j][k];
                }
                finalMatrix[j][k]=value/pairSize;
            }
        }
    }

    public static HashMap<AdjTelomere,Integer> createHashMap(List<AdjTelomere> g)
    {
        HashMap<AdjTelomere, Integer> hmOfG1= new HashMap<>();
        for(AdjTelomere adj1:g)
        {
            hmOfG1.put(adj1,adj1.chrosomePosition);
        }
        return hmOfG1;
    }
}
