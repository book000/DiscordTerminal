package com.tomacheese.DiscordTerminal.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import com.tomacheese.DiscordTerminal.Main;

public class Task_ExecTerminalCommand extends Thread {
	File currentdir;
	String command;

	public Task_ExecTerminalCommand(String command) {
		this(new File(new File(".").getAbsoluteFile().getParent()), command);
	}

	public Task_ExecTerminalCommand(File currentdir, String command) throws IllegalArgumentException {
		if (!currentdir.exists()) {
			// notfound
			throw new IllegalArgumentException("currentdir is not found.");
		}
		if (!currentdir.isDirectory()) {
			// !dir
			throw new IllegalArgumentException("currentdir is not directory.");
		}
		if (command == null || command.trim().isEmpty()) {
			throw new IllegalArgumentException("command is null or blank.");
		}
		this.currentdir = currentdir;
		this.command = command.trim();
	}

	@Override
	public void run() {
		Process p = null;
		try {
			ProcessBuilder pb = new ProcessBuilder();
			pb.command("sh", "-c", command);
			pb.directory(currentdir);
			pb.redirectErrorStream(true);

			p = pb.start();
			p.waitFor(10, TimeUnit.MINUTES);
		} catch (IOException e) {
			Main.getJDA().getTextChannelById(Main.getChannelID())
					.sendMessage("> Command: `" + command + "` [`" + currentdir.getAbsolutePath()
							+ "`]\n```exec IOException: " + e.getMessage() + "```")
					.queue();
			return;
		} catch (InterruptedException e) {
			int ret = p.exitValue();
			Main.getJDA().getTextChannelById(Main.getChannelID())
					.sendMessage("> Command: `" + command + "`(`" + ret + "`) [`"
							+ currentdir.getAbsolutePath() + "`]\n```InterruptedException: " + e.getMessage() + "```")
					.queue();
			return;
		}

		int ret = p.exitValue();
		InputStream is = p.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		LinkedList<String> msgs = new LinkedList<>();
		msgs.add("> Command: `" + command + "`(`" + ret + "`)\n```");
		try {
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				} else {
					if ((String.join("\n", msgs) + "\n" + line).length() >= 1900) {
						// lineを追加すると1900文字を超えるなら一旦送信
						Main.getJDA().getTextChannelById(Main.getChannelID())
								.sendMessage(String.join("\n", msgs) + "```").queue();
						msgs.clear();
						msgs.add("```");
					}
					msgs.add(line);
				}
			}
			if (msgs.size() > 0) {
				Main.getJDA().getTextChannelById(Main.getChannelID()).sendMessage(String.join("\n", msgs) + "```")
						.queue();
			}
			return;
		} catch (IOException e) {
			Main.getJDA().getTextChannelById(Main.getChannelID())
					.sendMessage("> Command: `" + command + "`(`" + ret + "`) [`"
							+ currentdir.getAbsolutePath() + "`]\n```IOException: " + e.getMessage() + "```")
					.queue();
			return;
		}
	}

}
