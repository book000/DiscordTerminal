# DiscordTerminal

![Java CI with Maven](https://github.com/book000/DiscordTerminal/workflows/Java%20CI%20with%20Maven/badge.svg)
[![License](https://img.shields.io/github/license/jaoafa/CheckRegionIntersects)](https://github.com/jaoafa/CheckRegionIntersects/blob/master/LICENSE)

[Click here for English README](https://github.com/book000/DiscordTerminal/blob/master/README.md)

Discord上からLinux端末のターミナルを使うかのようにコマンドを実行できるJavaアプリケーションです。  
Linux系にのみ対応しております。Windowsには対応しておりません。

## 注意

このアプリケーションを動作させると、設定によっては全てのユーザーが**Discord上からアプリケーションが実行されているユーザーが実行できる同等のコマンドを実行できてしまいます。**  
例えば、root権限でこのアプリケーションを動作させた場合、該当するチャンネル・ロール・ユーザーではrootと同等の権限が持てることになり、`shutdown`コマンドなどを自由に使えてしまいます。  
権限が制限されているユーザーで動作させるなど、十分注意してご利用ください。

## 特徴

- Discord上からLinux端末に対してコマンドを実行させられます。
- 使用を許可するチャンネル・ロール・ユーザーを設定できます。
- コマンドのエイリアスを設定できます。

## 使い方

1. [Release](https://github.com/book000/DiscordTerminal/releases)から最新の`DiscordTerminal.jar`をダウンロードし配置します。
2. [#設定](#設定)通りに`config.json`を作ります。
3. `java -jar DiscordTerminal.jar`などで実行し起動してください。
4. 必要に応じてSystemdへの登録等をして常時起動するようにしておきましょう。

## 設定

カレントディレクトリの`config.json`で設定します。

例:

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

Discord Botのトークンを指定します。必須です。

例: `XXXXXXX0XXXXXXX0XXXXXXXX.XXXXXX.XXXXXXXXX0XXXX0XXXXXXX0X0XX`

### channelid

ターミナルとするチャンネルのIDを指定します。必須です。

例: `597419057251090443`

### userid

このアプリケーションを使用できるユーザーのIDを指定します。必須ではありません。

例: `221991565567066112`

### roleid

このアプリケーションを使用できるロール(役職)のIDを指定します。必須ではありません。

例: `597405190156714004`

### alias

コマンドのエイリアスを指定します。配列で指定してください。必須ではありません。
