# DiscordTerminal

![Java CI with Maven](https://github.com/book000/DiscordTerminal/workflows/Java%20CI%20with%20Maven/badge.svg)
[![License](https://img.shields.io/github/license/jaoafa/CheckRegionIntersects)](https://github.com/jaoafa/CheckRegionIntersects/blob/master/LICENSE)

[日本語の README はこちらから](https://github.com/book000/DiscordTerminal/blob/master/README-ja.md)

It is a Java application that can execute commands from Discord as if using a Linux terminal.  
It only support Linux. It does not support Windows.

## Warning

When this application is run, depending on the settings, all users will be able to execute the same command from **Discord that can be executed by the user who is running the application.**  
For example, if you run this application with root privileges, the corresponding channel, role and user will have the same privileges as root, and you will be able to use the `shutdown` command freely.  
Please use with caution such as operating as a user with limited permissions.

## Feature

- Can execute commands from Discord to a Linux terminal.
- Can set which channel, role and user are allowed to use it.
- Can set command aliases.

## How to use

1. Download the latest `DiscordTerminal.jar` from [Release](https://github.com/book000/DiscordTerminal/releases) and place it.
2. Create `config.json` according to [#Settings](#Settings).
3. Execute it with `java -jar DiscordTerminal.jar` and start it.
4. If necessary, register with Systemd so that it always starts.

## Settings

Set in `config.json` in the current directory.

Example:

```json
{
    "token": "XXXXXXX0XXXXXXX0XXXXXXXX.XXXXXX.XXXXXXXXX0XXXX0XXXXXXX0X0XX",
    "channelid": "597419057251090443",
    "userid": "221991565567066112",
    "roleid": "597405190156714004",
    "alias": {
        "ping": "echo pong!"
    }
}
```

### token

Specifies the Discord Bot token. Required.

Example: `XXXXXXX0XXXXXXX0XXXXXXXX.XXXXXX.XXXXXXXXX0XXXX0XXXXXXX0X0XX`

### channelid

Specify the ID of the terminal channel. Required.

Example: `597419057251090443`

### userid

Specifies the ID of the user who can use this application. Not required.

Example: `221991565567066112`

### roleid

Specify the ID of the role that can use this application. Not required.

Example: `597405190156714004`

### alias

Specify an alias for the command. Please specify as an array. Not required.

## License

The license for this project is [MIT License](https://github.com/book000/DiscordTerminal/blob/master/LICENSE).
