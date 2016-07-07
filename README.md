This is a demo application to show how to get room list and listen to real time messages(danmaku). 

To run this demo, firstly you need to build the engine.io-client-java-engine.io-client-0.7.0 manually, since this is a home made version of socket.io client. go into engine.io-client-java-engine.io-client-0.7.0 folder, and run maven command ( make sure you have maven 3.x configured in your environment)

mvn clean install -Dmaven.test.skip=true

Then you can run GetRoomListApp to fetch the room list, and run MomoDanmakuListener to get real time messages from Momo room.

Forgot to mention, in MomoDanmakuListener replace the fullPathOfKeyStore to your own keystore file. and replace liveId with the room you want to listen.

To create your own keystore you can use keytool provided by java, and import momo certificate. Momo certificated can be exporeted from firefox browser. 