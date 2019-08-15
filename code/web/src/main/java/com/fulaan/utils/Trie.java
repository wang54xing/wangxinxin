package com.fulaan.utils;

/**
 * Created by wang_xinxin on 2015/8/3.
 */
public class Trie {

    final int maxn = 300000;
    int[][] ch;
    int[] v;
    int sz;
    int getIndex(char c){
        return c - '0';
    }
    Trie(){
        v = new int[maxn];
        ch = new int[maxn][];
        for(int i = 0; i < maxn; i++){
            ch[i] = new int[2];
        }
        sz = 1;
    }
    void insert(String str, int id){
        int len = str.length();
        int u = 0;
        for(int i = 0; i < len; i++){
            int c = str.charAt(i) - '0';
            if(ch[u][c] == 0){
                ch[u][c] = sz++;
            }
            u = ch[u][c];
        }
        v[u] = id;
    }
    int find(String str){
        int len = str.length();
        int u = 0;
        for(int i = 0; i < len; i++){
            int c = str.charAt(i) - '0';
            if(ch[u][c] == 0){
                break;
            }
            u = ch[u][c];
        }
        return v[u];
    }
    static public void main(String[] argv){
        // Trie trie = new Trie();
        // do nothing
    }
}
