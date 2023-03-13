package ForLiHongDi;
import java.util.*;

public class Functions {
    public static List<Chrosome> getChromosomes(List<String> genome)//     从节点genome(的基因组数据)中提取出节点对应的染色体(chromosomes)
    {
        int size_genome = genome.size();
//        System.out.println(size_genome+" ");
        List<Chrosome> chromosomeSS= new ArrayList<>();
        for(int i=1; i<size_genome; i++)  // 0号单元存的是 ">nodeName"
        {
            String element = genome.get(i);
            int elementLength = element.length();
//            System.out.println(element);
            chromosomeSS.add(new Chrosome(element.substring(0,elementLength-2),element.substring(elementLength-1,elementLength))); // 因为染色体的表示形如 "1 -3 4 $"
        }
//        System.out.println(chromosomeSS);
        return chromosomeSS;
    }
    public static List<AdjTelomere> getAdjs(List<Chrosome> chromosomeList)// 给定一个基因组的染色体list，该函数产生基因组的邻接
    {
        int chromosomes_size = chromosomeList.size();//传入基因组的染色体的数目
        List<AdjTelomere> AdjOfCurGenome=new ArrayList<>();
        System.out.println("执行getAdj(),当前基因组染色体数量："+chromosomes_size);
        for (int i = 0; i < chromosomes_size; i++) // 依次处理每一条染色体
        {
            StringBuilder adjResultStr = new StringBuilder();// StringBuffer 是线程安全的，StringBuilder不是线程安全的，所以性能略高
            List<String> resultList = new ArrayList<String>();
            String currentChromosome = chromosomeList.get(i).getChromosome();        // 取得当前染色体
//            System.out.println("第"+(i+1)+"个染色体："+currentChromosome);
            String[] genesInCurrentChromosome = currentChromosome.split(" "); //将当前染色体上的基因提出来
            int chromosomesGeneNum=genesInCurrentChromosome.length;
            String lastGene=genesInCurrentChromosome[genesInCurrentChromosome.length-1];
            if (chromosomeList.get(i).getType().equals("$"))//判断要转化的是环形染色体还是线性染色体
            {
                if (chromosomesGeneNum==1) //如果染色体的基因是单个基因。
                {
                    if (genesInCurrentChromosome[0].substring(0,1)=="-")
                    {
                        adjResultStr.append("{"+genesInCurrentChromosome[0]+"t"+"}"+"{"+genesInCurrentChromosome[0]+"h"+"}");
                    }
                    else
                    {
                        adjResultStr.append("{"+genesInCurrentChromosome[0]+"h"+"}"+"{"+genesInCurrentChromosome[0]+"t"+"}");
                    }
                }
                else
                {
                    int len = genesInCurrentChromosome.length;            // 当前染色体包含的基因数
                    for (int j = 0; j < len; j++)   //对染色体中基因进行分析处理
                    {
                        String currentGene = genesInCurrentChromosome[j]; // 当前基因
                        if (j == 0) // 当前被处理的是染色体上的第一个基因
                        {
                            if (currentGene.substring(0, 1).equals("-")) // 如果是反向的
                            {
                                String geneName = currentGene.substring(1, currentGene.length());
                                adjResultStr.append("{"+geneName + "t" + "}" + "{" + geneName + "h");
                            } else                                      //如果是正向的
                            {
                                adjResultStr.append("{" + currentGene + "h" + "}" + "{" + currentGene + "t");
                            }
                        } else if (j == len - 1) // 当前处理的是染色体上的最后一个基因
                        {
                            if (currentGene.substring(0, 1).equals("-")) // 如果是反向的
                            {
                                String geneName = currentGene.substring(1, currentGene.length());
                                adjResultStr.append("," + geneName + "t" + "}" + "{" + geneName + "h" +  "}");
                            } else                 //如果是正向的
                            {
                                adjResultStr.append("," + currentGene + "h" + "}" + "{" + currentGene + "t" + "}");
                            }
                        } else // 当前处理的是染色体上处于中间位置的基因
                        {
                            if (currentGene.substring(0, 1).equals("-")) // 如果是反向的
                            {
                                String geneName = currentGene.substring(1, currentGene.length());
                                adjResultStr.append("," + geneName + "t" + "}" + "{" + geneName + "h");
                            } else                 //如果是正向的
                            {
                                adjResultStr.append("," + currentGene + "h" + "}" + "{" + currentGene + "t");
                            }
                        }
                    }
                }

            }
//            if (chromosomeList.get(i).getType().equals(")"))
//            {
//                int len = genesInCurrentChromosome.length;// 当前染色体包含的基因数
//                for (int j = 0; j < len; j++)   //对染色体中基因进行分析处理
//                {
//                    String currentGene = genesInCurrentChromosome[j]; // 当前基因
//                    if (j == 0) // 当前被处理的是染色体上的第一个基因
//                    {
//
//                        if (currentGene.substring(0, 1).equals("-")) // 如果第一个基因是反向的
//                        {
//                            String geneName = currentGene.substring(1, currentGene.length());
//                            if (lastGene.substring(0,1).equals("-"))//第一个基因和最后一个基因都是反向的
//                            {
//                                adjResultStr.append("{"+lastGene.substring(1,2)+"h"+","+geneName+"t"+"}"+"{"+geneName+"h"+",");
//                            }
//                            else //第一个基因反向，最后一个基因正向
//                            {
//                                adjResultStr.append("{"+lastGene+"t"+","+geneName+"t"+"}"+"{"+geneName+"h"+",");
//                            }
//
//                        } else                                      //如果是正向的
//                        {
//                            if(lastGene.substring(0,1).equals("-"))//如果第一个基因是正向的，最后一个基因是反向的
//                            {
//                                adjResultStr.append("{"+lastGene.substring(1,2)+"t"+","+currentGene+"t"+"}"+"{"+currentGene+"t"+",");
//                            }
//                            else //如果第一个基因是正向的，最后一个基因是正向的
//                            {
//                                adjResultStr.append("{"+lastGene+"t"+","+currentGene+"h"+"}"+"{"+currentGene+"t"+",");
//                            }
//                        }
//                    }
//                    else if (j == len - 1) // 当前处理的是染色体上的最后一个基因
//                    {
//                        if (currentGene.substring(0, 1).equals("-")) // 如果是反向的
//                        {
//                            String geneName = currentGene.substring(1);
//                            adjResultStr.append(geneName + "t" + "}");
//                        } else                 //如果是正向的
//                        {
//                            adjResultStr.append(currentGene + "h" + "}");
//                        }
//                    }
//                    else // 当前处理的是染色体上处于中间位置的基因
//                    {
//                        if (currentGene.substring(0, 1).equals("-")) // 如果是反向的
//                        {
//                            String geneName = currentGene.substring(1);
//                            adjResultStr.append("," + geneName + "t" + "}" + "{" + geneName + "h");
//                        } else                 //如果是正向的
//                        {
//                            adjResultStr.append("," + currentGene + "h" + "}" + "{" + currentGene + "t");
//                        }
//                    }
//                }
//            }
            int m = -1, n = -1;
            int len = adjResultStr.length();
//            System.out.println("Is:"+adjResultStr);
            for(int t=0; t<len; t++)  //输出每一个{ }中的内容
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
//            System.out.println(resultList);
            int size = resultList.size();
            for(int k=0; k<size; k++)   // 向adjs 列表中添加邻接
            {
                String currentAdj = resultList.get(k);
                if (!currentAdj.contains(",")) //如果邻接的长度只有1，说明是个端粒邻接，比如1t、2h之类。
                {
                    String left=currentAdj;
                    AdjOfCurGenome.add(new AdjTelomere(left,"N","Telomere",i)); // AdjsOfNode是存放该结点的邻接(端粒)的列表
                }
                else
                {
                    String[] temp = currentAdj.split(",");
                    String left = temp[0];
                    String right = temp[1];
                    AdjOfCurGenome.add(new AdjTelomere(left, right, "adj",i)); // AdjsOfNode是存放该结点的邻接(端粒)的列表,i代表
                }
            }
        }
        return AdjOfCurGenome; // 返回邻接的列表;
    }

    public static void showAdjs(List<AdjTelomere> currAdjs)// 输出所有的邻接
    {
        for(AdjTelomere adjOrTelomere : currAdjs)
        {
            System.out.print("{"+adjOrTelomere.outputAdj()+"}");
        }
    }
    public static List<AdjsPair> adjsPair(List<AdjTelomere> curAdj)
    {
        List<AdjsPair> adjsPairList=new ArrayList<>();
        int adjsLength=curAdj.size();
        System.out.println("邻接的数量"+adjsLength);
        for (int i = 0; i < adjsLength-1; ++i)
        {
            for (int j=i+1; j<adjsLength ;++j)
            {
                if (curAdj.get(i).chrosomePosition==curAdj.get(j).chrosomePosition)
                {
                    adjsPairList.add(new AdjsPair(curAdj.get(i),curAdj.get(j)));
                }
                else if (curAdj.get(i).chrosomePosition!=curAdj.get(j).chrosomePosition)
                {
                    adjsPairList.add(new AdjsPair(curAdj.get(i),curAdj.get(j)));
                }
            }
        }
        for(AdjsPair a:adjsPairList)
        {
            System.out.println(a.toshow());
        }
        System.out.println("邻接对的数量"+adjsPairList.size());
        return adjsPairList;
    }
    public static void showAdjsPair(List<AdjsPair> adjsPairList)
    {
        int i=1;
        for(AdjsPair adjsPair1 : adjsPairList)
        {
            System.out.println("第"+i+"对："+adjsPair1.toshow());
            i++;
        }
    }
    public static boolean judgeAdjPairIsEqual(AdjsPair adjsPair1,AdjsPair adjsPair2)
    {
        if ((adjsPair1.adj1.EqualAdj(adjsPair2.adj1)||adjsPair1.adj1.EqualAdj(adjsPair2.adj2))&&(adjsPair1.adj2.EqualAdj(adjsPair2.adj1)||adjsPair1.adj2.EqualAdj(adjsPair2.adj2)))
        {
            return true;
        }
        else
            return false;
    }
    public static List<AdjTelomere> UnionBothAdjSets(List<AdjTelomere> list1,List<AdjTelomere> list2)
    {
       List<AdjTelomere> newlist=new ArrayList<>();
       HashSet<AdjTelomere> check=new HashSet<>();
       int i=0;
       check.addAll(list1);
       check.addAll(list2);
       newlist.addAll(check);
       return newlist;
    }
    public static String transFormBoolean(Boolean b)
    {
        if(b)
            return "1";
        else
            return "0";
    }
    public static String reverAdj(String adj)
    {
        String adjs[]=adj.split(",");
        String newString;
        newString=adjs[1]+adjs[0];
        return newString;
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
                }
            }
        }
        return unionedAdj;
    }
//    public static List<Integer> computeProbality(List<AdjTelomere> a1,List<AdjTelomere> a2,List<AdjsPair> unionPair) {
//        float NumOf11dtoitSelf = 0;
//        float NumOfOthersToItself = 0;
//        float NumOfOtherToOther = 0;
//        float NumOfOtherTo11d = 0;
//        float probability_1 = 0;
//        float probability_2 = 0;
//        float probability_3 = 0;
//        float probability_4 = 0;
//
//        List<Integer> num=new ArrayList<>();
//        HashMap<AdjTelomere, Integer> adjHashMapOfGenome1= new HashMap<>();
//        HashMap<AdjTelomere, Integer> adjHashMapOfGenome2= new HashMap<>();
//        List<String> stateOfG1Adj=new ArrayList<>();
//        List<String> stateOfG2Adj=new ArrayList<>();
//        List<String> stateList=new ArrayList<>();
//        for(AdjTelomere adj1:a1)
//        {
//            adjHashMapOfGenome1.put(adj1,adj1.chrosomePosition);
//        }
//        for(AdjTelomere adj2:a2)
//        {
//            adjHashMapOfGenome2.put(adj2,adj2.chrosomePosition);
//        }
//        for(AdjsPair aa:unionPair)
//        {
//            String stateof1,stateof2;
//            boolean g1HaveLeftAdj = adjHashMapOfGenome1.containsKey(aa.adj1);
//            boolean g1HaveRightAdj = adjHashMapOfGenome1.containsKey(aa.adj2);
//            boolean g2HaveLeftAdj = adjHashMapOfGenome2.containsKey(aa.adj1);
//            boolean g2HaveRightAdj = adjHashMapOfGenome2.containsKey(aa.adj2);
//            boolean isSameBothAdjTypeIng1 = adjHashMapOfGenome1.get(aa.adj1)==adjHashMapOfGenome1.get(aa.adj2);
//            boolean isSameBothAdjTypeIng2 = adjHashMapOfGenome2.get(aa.adj1)==adjHashMapOfGenome2.get(aa.adj2);
////            System.out.println("curradjPair:"+a.toshow()+g1HaveLeftAdj+","+g1HaveRightAdj+","+g2HaveLeftAdj+","+g2HaveRightAdj);
//            stateof1=Functions.transFormBoolean(g1HaveLeftAdj)+Functions.transFormBoolean(g1HaveRightAdj);
//            stateof2=Functions.transFormBoolean(g2HaveLeftAdj)+Functions.transFormBoolean(g2HaveRightAdj);
//            if(g1HaveLeftAdj && g1HaveRightAdj && !isSameBothAdjTypeIng1)
//            {
//                stateof1=stateof1+"d";
//            }
//            if(g2HaveLeftAdj && g2HaveRightAdj && !isSameBothAdjTypeIng2)
//            {
//                stateof2=stateof2+"d";
//            }
//            String finalString=stateof1+"to"+stateof2;
//            stateList.add(finalString);
//            stateOfG1Adj.add(stateof1);
//            stateOfG2Adj.add(stateof2);
//        }
//        int sum=stateList.size();
//        for(int i=0;i<sum;i++)
//        {
//            System.out.println("第"+(i+1)+"个："+finalAdjPair.get(i).toshow()+",state is:"+stateList.get(i));
//        }
//        for(String s:stateList)
//        {
//            if(s.equals("11dto11d"))
//            {
//                NumOf11dtoitSelf++;
//            }
//            else if (s.equals("00to11")||s.equals("01to10")||s.equals("01to11")||s.equals("10to11")||s.equals("10to01")||s.equals("11to10")||s.equals("11to00")||s.equals("11to01"))
//            {
//                NumOfOtherToOther++;
//            }
//            else if(s.equals("11dto11")||s.equals("11dto10")||s.equals("11dto01")||s.equals("11dto00")||s.equals("00to11d")||s.equals("01to11d")||s.equals("10to11d")||s.equals("11to11d"))
//            {
//                NumOfOtherTo11d++;
//            }
//            else if(s.equals("11to11"))
//            {
//                NumOfOthersToItself++;
//            }
//        }
//        return  num;
//    }
    public static List<Double[][]> computeEveryAdj(List< List<AdjTelomere>> Genome,List<AdjsPair> unionAdjPair)
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
                        break;
                    case "00to10": pbMatrix[0][2]++; break;
                    case "00to11": pbMatrix[0][3]++; break;
                    case "00to11d": pbMatrix[0][4]++; break;
                    case "01to00": pbMatrix[1][0]++; break;
                    case "01to01": pbMatrix[1][1]++; break;
                    case "01to10": pbMatrix[1][2]++; break;
                    case "01to11": pbMatrix[1][3]++; break;
                    case "01to11d": pbMatrix[1][4]++; break;
                    case "10to00": pbMatrix[2][0]++; break;
                    case "10to01": pbMatrix[2][1]++; break;
                    case "10to10": pbMatrix[2][2]++; break;
                    case "10to11": pbMatrix[2][3]++; break;
                    case "10to11d": pbMatrix[2][4]++; break;
                    case "11to00":

                        System.out.println(pbMatrix[3][0]);
                        pbMatrix[3][0]++;
                        break;
                    case "11to01": pbMatrix[3][1]++; break;
                    case "11to10": pbMatrix[3][2]++; break;
                    case "11to11": pbMatrix[3][3]++; break;
                    case "11to11d": pbMatrix[3][4]++; break;
                    case "11dto00": pbMatrix[4][0]++; break;
                    case "11dto01": pbMatrix[4][1]++; break;
                    case "11dto10": pbMatrix[4][2]++; break;
                    case "11dto11": pbMatrix[4][3]++; break;
                    case "11dto11d": pbMatrix[4][4]++; break;
                }
            }
            for(int i=0;i<5;i++)
            {
                for(int j=0;j<5;j++)
                {
                    pbMatrix[i][j]=pbMatrix[i][j]/ stateList.size();
                }
            }
            AllAdjPb.add(pbMatrix);
        }
        return AllAdjPb;
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

