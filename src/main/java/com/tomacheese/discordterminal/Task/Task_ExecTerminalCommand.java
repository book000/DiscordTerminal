package com.tomacheese.discordterminal.Task;

import com.tomacheese.discordterminal.Main;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.io.*;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class Task_ExecTerminalCommand extends Thread {
    File currentDirectory;
    String command;

    public Task_ExecTerminalCommand(String command) {
        this(new File(new File(".").getAbsoluteFile().getParent()), command);
    }

    public Task_ExecTerminalCommand(File currentDirectory, String command) throws IllegalArgumentException {
        if (!currentDirectory.exists()) {
            // notfound
            throw new IllegalArgumentException("currentDirectory is not found.");
        }
        if (!currentDirectory.isDirectory()) {
            // !dir
            throw new IllegalArgumentException("currentDirectory is not directory.");
        }
        if (command == null || command.trim().isEmpty()) {
            throw new IllegalArgumentException("command is null or blank.");
        }
        this.currentDirectory = currentDirectory;
        this.command = command.trim();
    }

    @Override
    public void run() {
        TextChannel channel = Main.getJDA().getTextChannelById(Main.getChannelID());
        if (channel == null) {
            return;
        }
        Process p = null;
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(command.split(" "));
            pb.directory(currentDirectory);
            pb.redirectErrorStream(true);

            p = pb.start();
            p.waitFor(10, TimeUnit.MINUTES);
        } catch (IOException e) {
            channel.sendMessage("> Command: `" + command + "` [`" + currentDirectory.getAbsolutePath() + "`]\n" +
                    "```exec IOException: " + e.getMessage() + "```").queue();
            return;
        } catch (InterruptedException e) {
            int ret = p.exitValue();
            channel.sendMessage("> Command: `" + command + "`(`" + ret + "`) [`" + currentDirectory.getAbsolutePath() + "`]\n" +
                    "```InterruptedException: " + e.getMessage() + "```").queue();
            return;
        }

        int ret = p.exitValue();
        InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        LinkedList<String> msgs = new LinkedList<>();
        msgs.add("> Command: `" + command + "`(`" + ret + "`)\n" +
                "```");
        try {
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                } else {
                    if ((String.join("\n", msgs) + "\n" + line).length() >= 1900) {
                        // lineを追加すると1900文字を超えるなら一旦送信
                        channel.sendMessage(String.join("\n", msgs) + "```").queue();
                        msgs.clear();
                        msgs.add("```");
                    }
                    msgs.add(line);
                }
            }
            if (msgs.size() > 0) {
                channel.sendMessage(String.join("\n", msgs) + "```").queue();
            }
        } catch (IOException e) {
            channel.sendMessage("> Command: `" + command + "`(`" + ret + "`) [`" + currentDirectory.getAbsolutePath() + "`]\n" +
                    "```IOException: " + e.getMessage() + "```").queue();
        }
    }
}
