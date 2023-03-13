package miller;

import java.util.*;

public class Tree
{
    public static String tree_str = "";
    public static ArrayList<TreeNode> tree = new ArrayList<TreeNode>();
    public static Map<String, TreeNode> record = new HashMap<String, TreeNode>();
    public static List<List<String>> genomes = new ArrayList<List<String>>();

    public Tree() { }

    public static void construct(String the_treeStr, List<List<String>> the_genomes)
    {
        tree_str = the_treeStr;
        genomes.addAll(the_genomes);
        tree.add(new TreeNode());
        mkNod(tree.get(0), tree_str.substring(0, tree_str.length() - 1));//拿到根节点和结构
        // 到此为止，父子关系已经明确
        getPrepared(Tree.tree.get(0));
        //getPrepared();
    }

    // 该方法传入的参数分别是 树(网络)的根节点、表示树(网络)的 newick格式的字符串。(习惯将二叉树称为树，而将多分枝且父子关系比较复杂的树称为网络)
    // 我此处用到的是二叉树
    public static void mkNod(TreeNode node, String nodeFlag)
    {
        node.nodeFlag = nodeFlag;
        if (record.containsKey(nodeFlag)) {
            TreeNode newNode = record.get(nodeFlag);
            for (int i = 0; i < newNode.parents.size(); i++) {
                newNode.parents.get(i).children.remove(record.get(nodeFlag));
            }
            newNode.parents.addAll(node.parents);
            for (int i = 0; i < newNode.parents.size(); i++) {
                newNode.parents.get(i).children.add(newNode);
                newNode.parents.get(i).children.remove(node);
            }
            tree.remove(record.get(nodeFlag));
            tree.remove(node);
            tree.add(newNode);
            record.put(nodeFlag, newNode);
        }
        if ((!(record.containsKey(nodeFlag))) && (!(nodeFlag.contains("("))) && (!(nodeFlag.contains(","))) && (!(nodeFlag.contains(")")))) {

            LeafNode newLeafNode = new LeafNode();
            newLeafNode.parents.addAll(node.parents);
            newLeafNode.nodeFlag = nodeFlag;
            for (int i = 0; i < newLeafNode.parents.size(); i++) {
                newLeafNode.parents.get(i).children.add(newLeafNode);
                newLeafNode.parents.get(i).children.remove(node);
            }
            tree.remove(node);
            tree.add(newLeafNode);
        }
        if (nodeFlag.startsWith("(") && nodeFlag.endsWith(")")) {
            Stack<Character> stk = new Stack<Character>();
            ArrayList<Integer> comma = new ArrayList<Integer>();
            for (int i = 0; i < nodeFlag.length(); i++) {
                if (nodeFlag.charAt(i) == '(') {
                    stk.push(nodeFlag.charAt(i));
                }
                if (nodeFlag.charAt(i) == ')') {
                    stk.pop();
                }
                if (nodeFlag.charAt(i) == ',' && stk.size() == 1) {
                    comma.add(i);
                }
            }
            comma.add(0, 0);
            comma.add((nodeFlag.length() - 1));
            for (int j = 0; j < (comma.size() - 1); j++) {
                tree.add(new TreeNode());
                tree.get(tree.size() - 1).nodeFlag = nodeFlag.substring(comma.get(j) + 1, comma.get(j + 1));
                node.children.add(tree.get(tree.size() - 1));
                tree.get(tree.size() - 1).parents.add(node);
                mkNod(tree.get(tree.size() - 1), tree.get(tree.size() - 1).nodeFlag);
            }
        }
        if (nodeFlag.startsWith("(") && nodeFlag.contains(")") && (!(nodeFlag.endsWith(")")))) {
            Stack<Character> stk = new Stack<Character>();
            ArrayList<Integer> comma = new ArrayList<Integer>();
            for (int i = 0; i < nodeFlag.length(); i++) {
                if (nodeFlag.charAt(i) == '(') {
                    stk.push(nodeFlag.charAt(i));
                }
                if (nodeFlag.charAt(i) == ')') {
                    if (stk.size() == 1) {
                        comma.add(i);
                    }
                    stk.pop();
                }
                if (nodeFlag.charAt(i) == ',' && stk.size() == 1) {
                    comma.add(i);
                }
            }
            comma.add(0, 0);

            record.put(nodeFlag.substring(comma.get(comma.size() - 1) + 1, nodeFlag.length()), node);
            for (int j = 0; j < (comma.size() - 1); j++) {
                tree.add(new TreeNode());
                tree.get(tree.size() - 1).nodeFlag = nodeFlag.substring(comma.get(j) + 1, comma.get(j + 1));
                node.children.add(tree.get(tree.size() - 1));
                tree.get(tree.size() - 1).parents.add(node);
                mkNod(tree.get(tree.size() - 1), tree.get(tree.size() - 1).nodeFlag);
            }
            node.nodeFlag = nodeFlag.substring(0, comma.get(comma.size() - 1) + 1);
        }
    }

    public static void getPrepared()
    {
        int tree_size = tree.size();
        for(int i=0; i<tree_size; i++)
        {
            TreeNode tn = tree.get(i);
            if(!(tn instanceof LeafNode))
            {
                tn.name = tn.nodeFlag;
                if(tn.parents.size()!=0)
                {
                    int start = tree_str.indexOf(tn.nodeFlag) + tn.nodeFlag.length() + 1;
                    StringBuilder sb = new StringBuilder();
                    for(int j=start; j<tree_str.length(); j++)
                    {
                        char temp = tree_str.charAt(j);
                        if(temp==',' || temp==')')
                            break;
                        else
                            sb.append(temp);
                    }
                    tn.branchLength = Double.valueOf(sb.toString());
                }
            }
            else
            {
                tn.name = tn.nodeFlag.substring(0, tn.nodeFlag.indexOf(":"));
                String flag = ">" + tn.name;
                for (List<String> temp : genomes)
                {
                    if (temp.get(0).equals(flag))
                    {
                        tn.genome = new String[temp.size()];
                        temp.toArray(tn.genome);
                        break;
                    }
                }
                tn.getChromosomes();
                tn.getAdjsOfNode();
                tn.getPreAndSuc();
            }
        }
    }

    public static void getPrepared(TreeNode root)
    {
        if (root == null)
            return;

        for (TreeNode child : root.children)
            getPrepared(child);

        if (root.children.size()==0)//   叶子结点
        {
            String[] temp = root.nodeFlag.split(":");
            root.name = temp[0];
            root.branchLength = Double.valueOf(temp[1]);

            String flag = ">" + root.name;
            for (List<String> temp2 : genomes)
            {
                if (temp2.get(0).equals(flag))
                {
                    root.genome = new String[temp2.size()];
                    temp2.toArray(root.genome);
                    break;
                }
            }
            root.getChromosomes();
            root.getAdjsOfNode();
            root.getPreAndSuc();
        }
        else// 进入到分支结点
        {
            if (root.parents.size() == 0) // 根节点
            {
                root.name = root.nodeFlag;
                return;
            }
            String parentFlag = root.parents.get(0).nodeFlag;
            String rootFlag = root.nodeFlag;
            root.name = root.nodeFlag;
            StringBuffer stringBuffer = new StringBuffer();
            int start = parentFlag.indexOf(rootFlag) + rootFlag.length() + 1;
            for (int i = start; i < parentFlag.length(); i++)
            {
                char temp = parentFlag.charAt(i);
                if (temp == ',' || temp == ')')
                    break;
                else
                    stringBuffer.append(temp);
            }
            root.branchLength = Double.valueOf(stringBuffer.toString());
        }
    }

    public static void showTheTree(String path)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("The information of the tree and leaf genomes > " + "\n");
        for(int i=0; i<tree.size(); i++)
        {
            TreeNode tn = tree.get(i);
            boolean leafOrNot = (tn instanceof LeafNode);
            sb.append(i + "  " + tn.name + "  " + (leafOrNot==true?"Leaf":"Branch") + "\n");
            if(leafOrNot)
            {
                for(String s : tn.genome)
                {
                    sb.append(s + "\n");
                }
            }
        }
        if(path.equals(""))
            System.out.println(sb.toString());
        else
        {
            FileUtils.WriteToFile(path + "process", sb.toString(),false);
        }
    }

    // 将静态变量还原
    public static void back()
    {
        tree_str = "";
        tree.clear();
        record.clear();
        genomes.clear();
    }
}