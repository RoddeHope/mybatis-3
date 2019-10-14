/**
 *    Copyright 2009-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.parsing;

/**
 * 通用的token解析器
 *
 * @author Clinton Begin
 */
public class GenericTokenParser {

  /**
   * 开始的token字符串
   */
  private final String openToken;

  /**
   * 结束的token字符串
   */
  private final String closeToken;
  private final TokenHandler handler;

  public GenericTokenParser(String openToken, String closeToken, TokenHandler handler) {
    this.openToken = openToken;
    this.closeToken = closeToken;
    this.handler = handler;
  }

  public String parse(String text) {
    if (text == null || text.isEmpty()) {
      return "";
    }
    // search open token
    // 寻找开始的open token的位置
    int start = text.indexOf(openToken);
    if (start == -1) {
      // 找不到直接返回
      return text;
    }
    char[] src = text.toCharArray();
    // 起始查找位置
    int offset = 0;
    // 结果
    final StringBuilder builder = new StringBuilder();
    // 匹配结果，openToken和closeToken之间的表达式
    StringBuilder expression = null;
    while (start > -1) {
      if (start > 0 && src[start - 1] == '\\') {
        // this open token is escaped. remove the backslash and continue.
        // 忽略\转义字符
        builder.append(src, offset, start - offset - 1).append(openToken);
        offset = start + openToken.length();
      } else {
        // found open token. let's search close token.
        // 创建/重置结果
        if (expression == null) {
          expression = new StringBuilder();
        } else {
          expression.setLength(0);
        }
        // 添加offset到openToken之间的内容到结果中
        builder.append(src, offset, start - offset);
        // 修改offset
        offset = start + openToken.length();
        // 寻找closeToken的位置
        int end = text.indexOf(closeToken, offset);
        while (end > -1) {
          if (end > offset && src[end - 1] == '\\') {
            // this close token is escaped. remove the backslash and continue.
            // 如果closeToken前面的字符是\转义字符，则忽略
            expression.append(src, offset, end - offset - 1).append(closeToken);
            // 修改offset
            offset = end + closeToken.length();
            // 继续寻找closeToken的位置
            end = text.indexOf(closeToken, offset);
          } else {
            // 找到了closeToken的位置，添加到结果中
            expression.append(src, offset, end - offset);
            break;
          }
        }
        if (end == -1) {
          // close token was not found.
          // 如果找不到closeToken，就直接从openToken开始到结尾拼接到结果
          builder.append(src, start, src.length - start);
          offset = src.length;
        } else {
          // 如果找到了closeToken，将expression交给handler处理，并将结果添加到builder中
          builder.append(handler.handleToken(expression.toString()));
          // 修改offset
          offset = end + closeToken.length();
        }
      }
      // 继续寻找openToken的位置
      start = text.indexOf(openToken, offset);
    }
    // 拼接剩余的部分
    if (offset < src.length) {
      builder.append(src, offset, src.length - offset);
    }
    return builder.toString();
  }
}
