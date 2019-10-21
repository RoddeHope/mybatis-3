package org.apache.ibatis.reflection.property;

import org.junit.jupiter.api.Test;

class PropertyTokenizerTest {

  @Test
  void testPropertyTokenizer() {
    String express = "order[0].item[0].name";
    PropertyTokenizer propertyTokenizer = new PropertyTokenizer(express);
    boolean hasNext = propertyTokenizer.hasNext();
    String name = propertyTokenizer.getName();
    String index = propertyTokenizer.getIndex();
    String indexedName = propertyTokenizer.getIndexedName();
    String children = propertyTokenizer.getChildren();
    System.out.println("==== first ====");
    System.out.println("hasNext = " + hasNext);
    System.out.println("name = " + name);
    System.out.println("index = " + index);
    System.out.println("indexedName = " + indexedName);
    System.out.println("children = " + children);

    PropertyTokenizer next = propertyTokenizer.next();
    hasNext = next.hasNext();
    name = next.getName();
    index = next.getIndex();
    indexedName = next.getIndexedName();
    children = next.getChildren();
    System.out.println("==== second ====");
    System.out.println("hasNext = " + hasNext);
    System.out.println("name = " + name);
    System.out.println("index = " + index);
    System.out.println("indexedName = " + indexedName);
    System.out.println("children = " + children);

  }

}
