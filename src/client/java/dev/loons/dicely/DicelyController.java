package dev.loons.dicely;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class DicelyController {
    DicelyStructure structure = new DicelyStructure();

    public DicelyController(){
        buildController();
    }

    private void buildController(){
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("dicely").then(ClientCommandManager.argument("sides", IntegerArgumentType.integer()).executes(context ->{
                int noOfSides = IntegerArgumentType.getInteger(context, "sides");
                context.getSource().sendFeedback(Text.literal(dicelyDeathlyRolls(noOfSides, 1)));
                return 1;
            }).then(ClientCommandManager.argument("count", IntegerArgumentType.integer()).executes(context -> {
                int noOfSides = IntegerArgumentType.getInteger(context, "sides");
                int noOfDice = IntegerArgumentType.getInteger(context, "count");
                context.getSource().sendFeedback(Text.literal(dicelyDeathlyRolls(noOfSides, noOfDice)));
                return 1;
            }))).then(ClientCommandManager.literal("copy").executes(context -> {
                if(!structure.getDicesHistory().isEmpty()){
                    MinecraftClient.getInstance().keyboard.setClipboard(structure.toString());
                    context.getSource().sendFeedback(Text.literal("Successfully copied last roll"));
                } else {
                    context.getSource().sendFeedback(Text.literal("Nothing to copy"));
                }
                        return 1;

                    })).then(ClientCommandManager.literal("history").executes(context -> {
                        context.getSource().sendFeedback(Text.literal("Last rolls were: \n" + structure.getHistoryAsString()));
                        return 1;
                    }))
            );
        });
    }

    private int generateRandomNumber(int noOfSides){
        return (int) ((Math.random() * noOfSides)+1);
    }

    private String dicelyDeathlyRolls(int noOfSides, int noOfDice) {
        if (noOfSides < 0 || noOfDice < 0) {
            if (noOfSides < 0) {
                return "Can't roll a number smaller than 1";
            } else {
                return "Cant' roll less then 1 dice";
            }
        } else {
            int[] randomNumbers = new int[noOfDice];
            for (int i = 0; i < noOfDice; i++) {
                randomNumbers[i] = generateRandomNumber(noOfSides);
            }
            structure.getDicesHistory().add(randomNumbers);
            if(noOfDice>1) return "Rolled " + noOfDice + "d" + noOfSides + " >> " + structure.formatDiceThrows(randomNumbers) + " >> Sum: (" + structure.getSumOfThrow(randomNumbers) + ")";
            return "Rolled " + noOfDice + "d" + noOfSides + " >> " + structure.formatDiceThrows(randomNumbers);
        }
    }
}
