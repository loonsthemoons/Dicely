package dev.loons.dicely;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

import java.util.Arrays;

public class DicelyController {

    public DicelyController(){
        buildController();
    }

    private void buildController(){
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("dicely").then(ClientCommandManager.argument("sides", IntegerArgumentType.integer()).executes(context ->{
                int noOfSides = IntegerArgumentType.getInteger(context, "sides");
                if(noOfSides<0){
                    context.getSource().sendFeedback(Text.literal("Can't roll a number smaller than 1"));
                    return 1;
                } else {
                    int randomNumber = generateRandomNumber(noOfSides);
                    context.getSource().sendFeedback(Text.literal("Rolled a: " + randomNumber));
                    return 1;
                }
            }).then(ClientCommandManager.argument("count", IntegerArgumentType.integer()).executes(context -> {
                int noOfSides = IntegerArgumentType.getInteger(context, "sides");
                int noOfDice = IntegerArgumentType.getInteger(context, "count");
                if(noOfSides<0 || noOfDice<0){
                    if(noOfSides<0){
                        context.getSource().sendFeedback(Text.literal("Can't roll a number smaller than 1"));
                        return 1;
                    } else {
                        context.getSource().sendFeedback(Text.literal("Cant' roll less then 1 dice"));
                        return 1;
                    }
                } else {
                    int[] randomNumbers= new int[noOfDice];
                    for(int i=0; i<noOfDice; i++){
                        randomNumbers[i] = generateRandomNumber(noOfSides);
                    }
                    context.getSource().sendFeedback(Text.literal("Rolled " + noOfDice + "d" + noOfSides + ": " + Arrays.toString(randomNumbers)));
                    return 1;
                }
            }))).then(ClientCommandManager.literal("copy").executes(context -> {
                context.getSource().sendFeedback(Text.literal("Successfully copied last roll"));
                return 1;
                    })).then(ClientCommandManager.literal("history").executes(context -> {
                        context.getSource().sendFeedback(Text.literal("last rolls were: "));
                        return 1;
                    }))
            );
        });
    }

    private int generateRandomNumber(int noOfSides){
        return (int) ((Math.random() * noOfSides)+1);
    }
}
