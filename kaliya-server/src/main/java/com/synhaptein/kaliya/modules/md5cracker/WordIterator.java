package com.synhaptein.kaliya.modules.md5cracker;

/**
 * The wordIterator generate word block for the clients.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class WordIterator {
    private String list = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private byte[] charlist = new byte[list.length()];
    private int max = 8;
    private int[] word;
    private int incr = 1;
    private int num = 1;
    
    /**
     * Construct a new word iterator to create list of word to test
     * @param incr the increment to the create next word
     * @param num the number of thread getting words
     */
    public WordIterator(int incr,int num){
        this.incr = incr;
        this.num = num;
        init();
    }
    
    /**
     * Contruct a new word iterator with defaut setting incr = 1 and num = 1
     */
    public WordIterator(){
        init();
    }
    
    /**
     * Return the next word to next
     * @return a word
     */
    public String next(){
        incr(0);
        return getString();
    }
    
    /**
     * Transform a number to a string
     * @return a word
     */
    private String getString(){
        StringBuilder s = new StringBuilder();
        for(int i=0;i<word.length;++i){
            if(word[i] == -1)
                break;
            else
                s.append(list.charAt(word[i]));
        }
        return s.toString();
    }
    
    /**
     * Add the incr to the byte array
     * @param index in the byte array
     */
    private void incr(int index){
        int tmp = word[index];
        if(tmp+incr >= charlist.length){
            if(!(index >= word.length)){
                incr(index+1);
                if(index == 0)
                    word[index] = num-1;
                else
                    word[index] = 0;
            }
        } else {
            word[index] += incr;
        }
    }

    /**
     * Initialized the word iterator
     */
    private void init(){
        word = new int[max];
        for(int i = 0;i<list.length();++i){
            charlist[i] = (""+list.charAt(i)).getBytes()[0];
        }
        for(int i=0;i<word.length;++i){
            word[i] = -1;
        }
        word[0] = num - incr - 1;
        for(int i = 0;i<list.length();++i){
            int j = (int)(Math.random()*list.length());
            int k = (int)(Math.random()*list.length());
            swap(j,k);
        }
    }
    
    /**
     * Swap to char in an array
     * @param a first char
     * @param b second char
     */
    private void swap(int a, int b){
        byte tmp = charlist[a];
        charlist[a] = charlist[b];
        charlist[b] = tmp;
    }

    private String getNextBlock() {
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i = 0; i < 999; ++i) {
            result.append("\"");
            //result.append(this.m_wordIterator.next());
            result.append("\",");
        }
        result.append("\"");
        //result.append(this.m_wordIterator.next());
        result.append("\"]");
        return result.toString();
    }
}
