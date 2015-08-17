package analysis;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.LogVisitor;
import git.AbstractSyntaxTree;
import git.Commit;
import git.Repository;
import git.SourceFile;

import java.util.List;

/**
 * Created by Kurt McAlpine on 18/08/15.
 */
public class AstAnalysis {

    public static void printChangedNodes(Repository repository) {
        int counter = 0;

        Node previous = null;

        top:
        for (Commit c : repository.getCommits()) {
            for (SourceFile f : c.getFiles()) {
                AbstractSyntaxTree ast = f.getAST();

                if (ast != null) {
                    LogVisitor v = new LogVisitor();
                    CompilationUnit cu = ast.getCompilationUnit();

                    if (cu != null) {
                        if (previous == null) {
                            previous = cu;
                        } else {
                            Node changed = getChangedNode(previous, cu);
                            previous = cu;
                            if (changed != null) {
                                System.out.println("Changed: " + changed);
                                //System.out.println("Type: " + changed.getClass());
                                //System.out.println(f.getContents());
                            }
                        }

                        counter++;
                    }

                }

                if (counter > 20000) {
                    break top;
                }
            }
        }
    }

    private static boolean isMemberOf(Node n, List<Node> list) {
        for (Node x : list) {
            if (n.hashCode() == x.hashCode()) {
                return true;
            }
        }
        return false;
    }

    private static void removeFromList(Node n, List<Node> list) {
        Node toRemove = null;
        if (isMemberOf(n, list)) {
            for (Node x : list) {
                if (x.hashCode() == n.hashCode()) {
                    toRemove = x;
                }
            }

            list.remove(toRemove);
        }
    }

    /**
     * listB - listA
     */
    private static void subtractLists(List<Node> listA, List<Node> listB) {
        for (Node a : listA) {
            while (isMemberOf(a, listB)) {
                removeFromList(a, listB);
            }
        }
    }

    /**
     * Assuming that node a and node b are in order
     * If node a and b have no children and are different b is returned
     * @param a
     * @param b
     */
    public static Node getChangedNode(Node a, Node b) {

        // Same hashcode, return null
        if (a.hashCode() == b.hashCode())
            return null;

        List<Node> aNodes = a.getChildrenNodes();
        List<Node> bNodes = b.getChildrenNodes();

        // Both nodes have no children, return b
        if (aNodes.size() == 0 && bNodes.size() == 0) {
            return b;
        }

        if (aNodes.size() > bNodes.size()) {
            int i = 0;
            while (i < bNodes.size()) {
                if (aNodes.get(i).hashCode() != bNodes.get(i).hashCode()) {
                    return getChangedNode(aNodes.get(i), bNodes.get(i));
                }
                i++;
            }
            return aNodes.get(i);
        } else if (aNodes.size() < bNodes.size()) {
            int i = 0;
            while (i < aNodes.size()) {
                if (aNodes.get(i).hashCode() != bNodes.get(i).hashCode()) {
                    return getChangedNode(aNodes.get(i), bNodes.get(i));
                }
                i++;
            }
            return bNodes.get(i);
        } else {
            int i = 0;
            while (i < aNodes.size()) {
                if (aNodes.get(i).hashCode() != bNodes.get(i).hashCode()) {
                    return getChangedNode(aNodes.get(i), bNodes.get(i));
                }
                i++;
            }
            return null;
        }
    }

    public static void printAstLogFile(Repository repository) {
        int counter = 0;

        for (Commit c : repository.getCommits()) {
            for (SourceFile f : c.getFiles()) {
                AbstractSyntaxTree ast = f.getAST();

                if (ast != null) {
                    LogVisitor v = new LogVisitor();
                    CompilationUnit cu = ast.getCompilationUnit();

                    if (cu != null) {
                        cu.accept(v, null);
                        System.out.println("AST_snapshot_version:" + counter);
                        System.out.println("Timestamp:" + c.getDate().getTime());
                        System.out.println(v.getSource());
                        counter++;

                    }

                }
            }
        }
    }

}
