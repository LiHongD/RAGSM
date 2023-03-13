package miller;

import ForLiHongDi.AdjTelomere;
import ForLiHongDi.AdjsPair;
import ForLiHongDi.Functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// 树结点实体类
class  TreeNode implements Cloneable
{
    public String nodeFlag;//>+名字
    public String name;
    public double branchLength;
    public String[] genome;
    public String[] chromosomes;

    public List<AdjOrTelomere> adjsOfNode;
    public List<AdjOrTelomere> pre;
    public List<AdjOrTelomere> suc;
    public List<TreeNode> parents;
    public List<TreeNode> children;

    public TreeNode()
    {
        parents = new ArrayList<TreeNode>();
        children = new ArrayList<TreeNode>();
        adjsOfNode = new ArrayList<AdjOrTelomere>();
        pre = new ArrayList<AdjOrTelomere>();
        suc = new ArrayList<AdjOrTelomere>();
    }

    public void getChromosomes()
    {
        int length_genome = genome.length;
        chromosomes = new String[length_genome-1];
        String element;
        int elementLength;
        for(int i=1; i<length_genome; i++)// 第一个位置存放基因组名字
        {
            element = genome[i];
            elementLength = element.length();
            chromosomes[i-1] = element.substring(0, elementLength-2);
        }
    }

    public void getAdjsOfNode()
    {
        int len;
        String[] genesInCurrentChromosome;
        int count = 0;
        for(String currentChromosome : chromosomes)
        {
            genesInCurrentChromosome = currentChromosome.split(" ");
            len = genesInCurrentChromosome.length;
            adjsOfNode.add(new AdjOrTelomere(0, Integer.valueOf(genesInCurrentChromosome[0]), name+count));
            for (int j = 0; j < len-1; j++)
            {
                adjsOfNode.add(new AdjOrTelomere(Integer.valueOf(genesInCurrentChromosome[j]), Integer.valueOf(genesInCurrentChromosome[j+1]), name+count));
            }
            adjsOfNode.add(new AdjOrTelomere(Integer.valueOf(genesInCurrentChromosome[len-1]), 0, name+count));
            count++;
        }
    }

    public void getPreAndSuc()
    {
        for(AdjOrTelomere current : adjsOfNode)
        {
            if(current.left!=0 && current.right!=0)
            {
                pre.add(current);
                suc.add(current);
            }
            else
            {
                if(current.left!=0)
                    suc.add(current);
                if(current.right!=0)
                    pre.add(current);
            }
        }
    }


}
