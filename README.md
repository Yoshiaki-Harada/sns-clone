## SNSのCloneアプリ

#### Databaseの準備 (Dockerでpostgresを起動)
1 environments/db/build.sh を実行する
2 environments/db/run.sh　を実行する

#### テーブル設計
![テーブル設計](/table.png) 

#### ドメインの説明
+ User: Messageやコメントを行う主体
+ Message: ツイートと同じ概念
+ Comment: Messageに対するコメント
+ Tag: MessageにはTagをつけることができる

#### 現状できること
+ Userの作成と更新と一覧
+ Messageの作成と更新と閲覧
+ Commentの作成と更新と閲覧
#### 設計の特徴
+ ReadとWriteの型を分けている
    + Writeの型は`valueobject`package以下にある
+ トランザクションはRepositoryで貼っている
