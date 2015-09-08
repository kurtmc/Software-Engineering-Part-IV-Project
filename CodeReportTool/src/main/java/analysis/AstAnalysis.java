package analysis;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.LogVisitor;
import git.AbstractSyntaxTree;
import git.Commit;
import git.Repository;
import git.SourceFile;

import java.util.*;

public class AstAnalysis {

    public static void addTime(HashMap<String, Long> hashMap, String methodName, long time) {
        if (hashMap.containsKey(methodName)) {
            long new_time = hashMap.get(methodName) + time;
            hashMap.put(methodName, new_time);
        } else {
            hashMap.put(methodName, time);
        }
    }

    public static void printChangedNodes(Repository repository) {
        int counter = 0;

        Node previous = null;

        String previousMethod = null;
        Commit previousCommit = null;

        HashMap<String, Long> methodTime = new HashMap<>();

        top:
        for (Commit c : repository.getCommits()) {
            for (SourceFile f : c.getFiles()) {
                AbstractSyntaxTree ast = f.getAST();

                if (ast != null) {
                    CompilationUnit cu = ast.getCompilationUnit();

                    if (cu != null) {
                        if (previous == null) {
                            previous = cu;
                        } else {
                            Node changed = getChangedNode(previous, cu);
                            previous = cu;
                            if (changed != null) {

                                if (previousMethod == null) {
                                    previousMethod = getMethodName(changed);
                                    previousCommit = c;
                                } else {
                                    String currentMethod = getMethodName(changed);

                                    if (previousMethod.equals(currentMethod)) {
                                        addTime(methodTime,
                                                previousMethod,
                                                c.getDate().getTime() - previousCommit.getDate().getTime());

                                    }
                                    previousMethod = getMethodName(changed);
                                    previousCommit = c;
                                }

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

        System.out.println("Time spent in functions:");
        Iterator it = methodTime.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int timeInSeconds = (int) ((long) pair.getValue() / 1000);
            System.out.println("\t" + pair.getKey() + ": " + timeInSeconds + "s");
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    /**
     * Assuming that node a and node b are in order
     * If node a and b have no children and are different b is returned
     * @param a node to compare
     * @param b node to compare
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

    private static String getMethodName(Node node) {
        if (node instanceof MethodDeclaration) {
            return ((MethodDeclaration) node).getName();
        } else {
            return getMethodName(node.getParentNode());
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
