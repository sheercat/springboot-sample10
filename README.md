# springboot-sample10

- はじめての spring boot サンプルの gradle 版

# how to start

1. https://spring.io/tools/sts へいって STS (spring boot plugin いりの eclipse ) を DL します
1. 起動して、help > eclipse marketplace から gradle integration for eclipse を 追加
1. https://projectlombok.org へいって lombok.jar を DL して、実行すると、STS を見つけてくれるので install
1. java 8 を DL してインストール(もしくは一番最初にこれをやると、次の手順いらない)
1. http://qiita.com/ukiuni@github/items/0b2555c7849d2a2647b5 どおりに jre 追加して、1.8 デフォに
1. ひつようなら gradle install
1. ./gradlew bootRun or ./gradlew bootRepackage
1. STS で見るときは import で、Gradle (STS) Project

# Data::Dumper は

    val hoge = new Hoge();
    System.out.println(ToStringBuilder.reflectionToString(hoge));

# heroku のための準備
- Procfile 変更

    heroku config:set GRADLE_TASK="bootRepackage"

を実行

# heroku
- https://fast-sierra-35120.herokuapp.com/customers


