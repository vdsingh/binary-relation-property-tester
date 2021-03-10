import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RelationPropertyTester {
  public static void main(String[] args) {

    int[][] data = new int[][] {{1,1},{1,2},{1,3},{2,1},{2,2},{2,3},{3,1},{3,2},{3,3}};
    int[][] required = new int[][] {{1,1}, {2,2}, {3,3}};
    printSummary(data, required);
    
  }
  
  public static void printSummary(int[][] data, int[][] requiredPairs) {
    Relation relation = new Relation(data);
    Map<String, Integer> stats = new HashMap<String, Integer>();
    Set<Relation> subsets = getRelationalSubsets(relation, requiredPairs);
    for(Relation subset: subsets) {
      if(subset.isReflexive()) stats.put("reflexive", stats.getOrDefault("reflexive", 0) + 1);
      if(subset.isSymmetric()) stats.put("symmetric", stats.getOrDefault("symmetric", 0) + 1);
      if(subset.isAntisymmetric()) stats.put("antisymmetric", stats.getOrDefault("antisymmetric", 0) + 1);
      if(subset.isTransitive()) stats.put("transitive", stats.getOrDefault("transitive", 0) + 1);
      if(subset.isReflexive() && subset.isTransitive()) {
        stats.put("reflexiveAndTransitive", stats.getOrDefault("reflexiveAndTransitive", 0) + 1);
        if(subset.isSymmetric()) stats.put("isEquivalence", stats.getOrDefault("isEquivalence", 0) + 1);
        if(subset.isAntisymmetric()) stats.put("isPartial", stats.getOrDefault("isPartial", 0) + 1);
      }
    }
    System.out.println(stats);
  }
  
  //Gets all subsets of a relation.
  public static Set<Relation> getRelationalSubsets(Relation set, int[][] requiredPairs) { 
      int n = set.size(); 
      Set<Relation> subsets = new HashSet<Relation>();      
      int[][] arrForm = new int[n][];
      int counter = 0;
      for(int[] pair: set.relation) {
        arrForm[counter] = pair;
        counter++;
      }
      
      for (int i = 0; i < (1 << n); i++) { 
          Relation subset = new Relation(new int[][] {}, requiredPairs);
          for (int j = 0; j < n; j++) {
              if ((i & (1 << j)) > 0) {
                subset.add(arrForm[j]);
              }
          }
          subsets.add(subset);
      }
     return subsets;
  } 
}

class Relation{
  Set<int[]> relation;
  Set<int[]> requiredPairs;
  
  public Relation() {
    this.relation = new HashSet<int[]>();
    this.requiredPairs = new HashSet<int[]>();
  }
  
  public Relation (int[][] relation) {
    this();
    for(int[] pair: relation) {
      this.relation.add(pair);
    }
  }
  
  public Relation(int[][] relation, int[][] requiredPairs) {
    this(relation);
    for(int[] pair: requiredPairs) {
      this.requiredPairs.add(pair);
    }
  }
  
  public boolean isTransitive () {
    for(int[] pair1: relation){
      for(int[] pair2: relation) {
        if(pair1 == pair2) continue;
        if(pair1[1] == pair2[0]) {//(1,2),(2,3) --> (1,3)
          if(!containsPair(new int[]{pair1[0], pair2[1]})) 
            return false;
        }
      }
    }
    return true;
  }
  
  public boolean isAntisymmetric() {
    for(int[] pair: relation) {
      if(pair[0] != pair[1] && containsPair(new int[] {pair[1], pair[0]})) return false;
    }
    return true;
  }
  
  public  boolean isSymmetric() {
    for(int[] pair: relation) {
      if(pair[0] != pair[1] && !containsPair(new int[] {pair[1], pair[0]})) return false;
    }
    return true;
  }
 
  
  public boolean isReflexive() {
    for(int[] required: requiredPairs) {
      if(!containsPair(required)) return false;
    }
    
    Set<Integer> checked = new HashSet<Integer>();
    for(int[] pair: relation) {
      if(!checked.contains(pair[0])) {
        checked.add(pair[0]);
        if(!containsPair(new int[] {pair[0], pair[0]})) return false;
      }
      
      if(!checked.contains(pair[1])) {
        checked.add(pair[1]);
        if(!containsPair(new int[] {pair[1], pair[1]})) return false;
      }
    }
    return true;
  }
  
  public void printTests() {
    System.out.println("Symmetric: " + isSymmetric());
    System.out.println("Antisymmetric: " + isAntisymmetric());
    System.out.println("Transitive: " + isTransitive());
    System.out.println("reflexive: " + isReflexive());
  }
  
  @Override
  public String toString(){
    if(relation.isEmpty()) return "{}";
    String str = "{";
    for(int[] pair: relation) {
      str += ("(" + pair[0] + "," + pair[1] + "),");
    }
    return str.substring(0, str.length() - 1) + "}";
  }
  
  public int size() {
    return relation.size();
  }
  
  public void add(int[] pair) {
    relation.add(pair);
  }
  
  public boolean containsPair(int[] pair) {
    for(int[] p: relation) {
      if(p[0] == pair[0] && p[1] == pair[1]) return true;
    }
    return false;
  }
}
