package com.company;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;


public class Main {

    public static HashMap<String, ArrayList<String>> dict = new HashMap<>();
    public static ArrayList<String> productiveNonTerminals = new ArrayList<>();
    public static String input = "text.txt";

    public static boolean isIn(String s, ArrayList<String> list){
        return list.indexOf(s) != -1;
    }

    public static void addValToHashMap(HashMap<String, ArrayList<String>> dict, String key, String value ){
        ArrayList<String> arr = dict.get(key);
        arr.add(value);
        dict.put(key, arr);
    }

    public static void addRuleDict(String line, HashMap<String, ArrayList<String>> dict){
            if (dict.get(String.valueOf(line.charAt(0))) == null){
                dict.put(String.valueOf(line.charAt(0)), new ArrayList<>());
                addValToHashMap(dict, String.valueOf(line.charAt(0)), line.substring(3, line.length()));
            }
            else {
                addValToHashMap(dict, String.valueOf(line.charAt(0)), line.substring(3, line.length()));
            }

    }



    public static ArrayList<String> findProductive(String currNode, HashMap<String, ArrayList<String>> dict,
                                                   ArrayList<String> prevNodes){

        if(dict.get(currNode) == null || isIn(currNode, productiveNonTerminals)) return productiveNonTerminals;
        ArrayList<String> rules = dict.get(currNode);

        for(String rule: rules){
            boolean flag = true;
            for(int i = 0; i < rule.length(); i++){
                if(Character.isLowerCase(rule.charAt(i)) ||
                        isIn(String.valueOf(rule.charAt(i)), productiveNonTerminals)) continue;
                if(isIn(String.valueOf(rule.charAt(i)), prevNodes)){
                    flag = false;
                    break;
                }
                prevNodes.add(String.valueOf(rule.charAt(i)));
                findProductive(String.valueOf(rule.charAt(i)), dict, prevNodes);
                if(isIn(String.valueOf(rule.charAt(i)), productiveNonTerminals)) continue;
                else {
                    flag = false;
                    break;
                }
            }
            if (flag){
                productiveNonTerminals.add(currNode);
                return productiveNonTerminals;
            }
        }

        return productiveNonTerminals;
    }

    public static void main(String[] args) {
        try{
            File file = new File(input);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);

            String line = "";
            do{
                line = reader.readLine();
                if(line != null){
                    addRuleDict(line, dict);
                }
            }while(line != null);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        //-------------------------

        ArrayList<HashMap.Entry> entries = new ArrayList<>(dict.entrySet());
        for (HashMap.Entry entry : entries) {
            if(isIn(entry.toString(), productiveNonTerminals)) continue;
            ArrayList<String> previous = new ArrayList<>();
            previous.add(entry.getKey().toString());
            productiveNonTerminals = findProductive(entry.getKey().toString(), dict, previous);
        }

        System.out.print("Непродуктивні нетермінали: ");
        for(HashMap.Entry entry: entries){
            if(!isIn(entry.getKey().toString(), productiveNonTerminals)){
                System.out.print(entry.getKey().toString() + " ");
            }
        }

        System.out.println("\nНепродуктивні правила: ");

        for(HashMap.Entry entry: entries){
            ArrayList<String> arr = new ArrayList<>();
            arr = dict.get(entry.getKey().toString());
            for(String rule: arr){
                for(int i = 0; i < rule.length(); i++){
                    if(!Character.isLowerCase(rule.charAt(i)) &&
                       !isIn(String.valueOf(rule.charAt(i)), productiveNonTerminals)){
                        System.out.println(entry.getKey() + "->" + rule);
                        break;
                    }
                }
            }
        }

    }
}
