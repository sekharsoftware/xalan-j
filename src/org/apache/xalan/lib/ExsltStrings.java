/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Xalan" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 1999, Lotus
 * Development Corporation., http://www.lotus.com.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.xalan.lib;

import org.w3c.dom.*;

import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.NodeSet;
import org.apache.xml.dtm.ref.DTMNodeProxy;

import java.util.StringTokenizer;
import javax.xml.parsers.*;

/**
 * <meta name="usage" content="general"/>
 * This class contains EXSLT strings extension functions.
 *
 * It is accessed by specifying a namespace URI as follows:
 * <pre>
 *    xmlns:math="http://exslt.org/strings"
 * </pre>
 * 
 * The documentation for each function has been copied from the relevant
 * EXSLT Implementer page.
 * 
 * @see <a href="http://www.exslt.org/">EXSLT</a>

 */
public class ExsltStrings
{

  /**
   * The str:align function aligns a string within another string. 
   *
   * The first argument gives the target string to be aligned. The second argument gives 
   * the padding string within which it is to be aligned. 
   *
   * If the target string is shorter than the padding string then a range of characters 
   * in the padding string are repaced with those in the target string. Which characters 
   * are replaced depends on the value of the third argument, which gives the type of 
   * alignment. It can be one of 'left', 'right' or 'center'. If no third argument is 
   * given or if it is not one of these values, then it defaults to left alignment. 
   *
   * With left alignment, the range of characters replaced by the target string begins 
   * with the first character in the padding string. With right alignment, the range of 
   * characters replaced by the target string ends with the last character in the padding 
   * string. With center alignment, the range of characters replaced by the target string 
   * is in the middle of the padding string, such that either the number of unreplaced 
   * characters on either side of the range is the same or there is one less on the left 
   * than there is on the right. 
   *
   * If the target string is longer than the padding string, then it is truncated to be 
   * the same length as the padding string and returned. 
   */
  public static String align(String targetStr, String paddingStr, String type)
  {
    if (targetStr.length() >= paddingStr.length())
      return targetStr.substring(0, paddingStr.length());
    
    StringBuffer sb = new StringBuffer(paddingStr);
    if (type.equals("right"))
    {
      sb.replace(paddingStr.length() - targetStr.length(), paddingStr.length(), targetStr);
    }
    else if (type.equals("center"))
    {
      int startIndex = (paddingStr.length() - targetStr.length()) / 2;
      sb.replace(startIndex, startIndex + targetStr.length(), targetStr);
    }
    // Default is left
    else
    {
      sb.replace(0, targetStr.length(), targetStr);
    }
    
    return sb.toString();
  }

  /**
   * See above
   */
  public static String align(String targetStr, String paddingStr)
  {
    return align(targetStr, paddingStr, "left");
  }
  
  /**
   * The str:concat function takes a node set and returns the concatenation of the 
   * string values of the nodes in that node set. If the node set is empty, it returns 
   * an empty string. 
   */
  public static String concat(NodeList nl)
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < nl.getLength(); i++)
    {
      Node node = nl.item(i);
      String value = null;
      if (node instanceof DTMNodeProxy)
        value = ((DTMNodeProxy)node).getStringValue();
      else
        value = node.getNodeValue();
      
      if (value != null && value.length() > 0)
        sb.append(value);
    }
    
    return sb.toString();
  }
    
  /**
   * The str:padding function creates a padding string of a certain length. 
   * The first argument gives the length of the padding string to be created. 
   * The second argument gives a string to be used to create the padding. This 
   * string is repeated as many times as is necessary to create a string of the 
   * length specified by the first argument; if the string is more than a character 
   * long, it may have to be truncated to produce the required length. If no second 
   * argument is specified, it defaults to a space (' '). If the second argument is 
   * an empty string, str:padding returns an empty string. 
   */
  public static String padding(double length, String pattern)
  {
    if (pattern == null || pattern.length() == 0)
      return "";
    
    StringBuffer sb = new StringBuffer();
    int len = (int)length;
    int numAdded = 0;
    int index = 0;
    while (numAdded < len)
    {
      if (index == pattern.length())
        index = 0;
        
      sb.append(pattern.charAt(index));
      index++;
      numAdded++;
    }
  
    return sb.toString();
  }

  /**
   * See above
   */
  public static String padding(double length)
  {
    return padding(length, " ");
  }
    
  /**
   * The str:split function splits up a string and returns a node set of token 
   * elements, each containing one token from the string. 
   *
   * The first argument is the string to be split. The second argument is a pattern 
   * string. The string given by the first argument is split at any occurrence of 
   * this pattern. For example: 
   *
   * str:split('a, simple, list', ', ')      
   *   Gives the node set consisting of: 
   *
   * <token>a</token>
   * <token>simple</token>
   * <token>list</token>
   *
   * If the second argument is omitted, the default is the string '&#x20;' (i.e. a space).
   */
  public static NodeList split(String str, String pattern)
  {
    Document lDoc = null;
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      lDoc = db.newDocument();
    }
    catch(ParserConfigurationException pce)
    {
      throw new org.apache.xml.utils.WrappedRuntimeException(pce);
    }
    
    NodeSet resultSet = new NodeSet();
    resultSet.setShouldCacheNodes(true);
    
    boolean done = false;
    int fromIndex = 0;
    int matchIndex = 0;
    String token = null;
    
    while (!done && fromIndex < str.length())
    {
      matchIndex = str.indexOf(pattern, fromIndex);
      if (matchIndex >= 0)
      {
	    token = str.substring(fromIndex, matchIndex);
	    fromIndex = matchIndex + pattern.length();
	  }
      else
      {
        done = true;
        token = str.substring(fromIndex);
      }
        
      Element element = lDoc.createElement("token");
      Text text = lDoc.createTextNode(token);
      element.appendChild(text);
      resultSet.addNode(element);      
    }
    
    return resultSet;
  }
  
  /**
   * See above
   */
  public static NodeList split(String str)
  {
    return split(str, " ");
  }

  /**
   * The str:tokenize function splits up a string and returns a node set of token 
   * elements, each containing one token from the string. 
   *
   * The first argument is the string to be tokenized. The second argument is a 
   * string consisting of a number of characters. Each character in this string is 
   * taken as a delimiting character. The string given by the first argument is split 
   * at any occurrence of any of these characters. For example: 
   *
   * str:tokenize('2001-06-03T11:40:23', '-T:')      
   *   Gives the node set consisting of: 
   *
   * <token>2001</token>
   * <token>06</token>
   * <token>03</token>
   * <token>11</token>
   * <token>40</token>
   * <token>23</token>
   * If the second argument is omitted, the default is the string '&#x9;&#xA;&#xD;&#x20;' 
   * (i.e. whitespace characters). 
   *
   * If the second argument is an empty string, the function returns a set of token 
   * elements, each of which holds a single character. 
   */
  public static NodeList tokenize(String toTokenize, String delims)
  {
    Document lDoc;

    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      lDoc = db.newDocument();
    }
    catch(ParserConfigurationException pce)
    {
      throw new org.apache.xml.utils.WrappedRuntimeException(pce);
    }

    StringTokenizer lTokenizer = new StringTokenizer(toTokenize, delims);
    NodeSet resultSet = new NodeSet();

    while (lTokenizer.hasMoreTokens())
    {
      resultSet.addNode(lDoc.createTextNode(lTokenizer.nextToken()));
    }

    return resultSet;
  }

  /**
   * See above
   */
  public static NodeList tokenize(String toTokenize)
  {
    return tokenize(toTokenize, " \t\n\r");
  }
  
}