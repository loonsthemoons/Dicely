package dev.loons.dicely;

import java.util.ArrayList;
import java.util.Arrays;

public class DicelyStructure {
    private ArrayList<int[]> dicesHistory = new ArrayList<>();

    public DicelyStructure(){
        // Constructor
    }

    public String formatDiceThrows(int[] diceThrow){
        if(diceThrow==null || diceThrow.length==0){
            return "";
        }
        String[] randomNumbersAsString = new String[diceThrow.length];
        for (int i = 0; i<diceThrow.length; i++){
            randomNumbersAsString[i] = String.valueOf(diceThrow[i]);
        }
        return String.join(", ", randomNumbersAsString);
    }

    public ArrayList<int[]> getDicesHistory() {
        return dicesHistory;
    }

    public int[] getLast(){
        return dicesHistory.get(dicesHistory.size()-1);
    }

    @Override
    public String toString() {
        if(dicesHistory.isEmpty()){
            return "";
        }
        return formatDiceThrows(getLast());
    }

    public String getHistoryAsString(){
        if(dicesHistory.isEmpty()){
            return "No rolls were made";
        }
        StringBuilder historyString = new StringBuilder();
        int startIndex = Math.max(0, dicesHistory.size() - 10);
        for (int i = startIndex; i < dicesHistory.size(); i++){
            if(i==dicesHistory.size()-1){
                historyString.append((formatDiceThrows(dicesHistory.get(i))));
            } else {
                historyString.append(formatDiceThrows(dicesHistory.get(i))).append("\n");
            }
        }
        return historyString.toString();
    }

    public int getSumOfThrow(int[] diceThrow){
        return Arrays.stream(diceThrow).sum();
    }
}
